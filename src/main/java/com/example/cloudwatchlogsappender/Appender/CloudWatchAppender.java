package com.example.cloudwatchlogsappender.Appender;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import java.util.Collections;
import java.util.Optional;
import lombok.Setter;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;
import software.amazon.awssdk.services.cloudwatchlogs.model.CloudWatchLogsException;
import software.amazon.awssdk.services.cloudwatchlogs.model.CreateLogStreamRequest;
import software.amazon.awssdk.services.cloudwatchlogs.model.DescribeLogStreamsRequest;
import software.amazon.awssdk.services.cloudwatchlogs.model.DescribeLogStreamsResponse;
import software.amazon.awssdk.services.cloudwatchlogs.model.InputLogEvent;
import software.amazon.awssdk.services.cloudwatchlogs.model.LogStream;
import software.amazon.awssdk.services.cloudwatchlogs.model.PutLogEventsRequest;

/**
 * CloudWatchAppender.
 */
public class CloudWatchAppender extends AppenderBase<ILoggingEvent> {

  @Setter
  private String region = "ap-northeast-1";
  @Setter
  private String logGroupName = "logGroupName";
  @Setter
  private String logStreamPrefix = "logStreamPrefix";
  @Setter
  PatternLayoutEncoder encoder;

  private CloudWatchLogsClient cloudWatchLogsClient;
  private String logStreamName;

  @Override
  public void start() {
    encoder.start();
    this.cloudWatchLogsClient = CloudWatchLogsClient.builder()
        .region(Region.of(this.region))
        .build();
    this.logStreamName = String.format("%s-%04d%02d%02d",
        this.logStreamPrefix,
        java.time.LocalDate.now().getYear(),
        java.time.LocalDate.now().getMonthValue(),
        java.time.LocalDate.now().getDayOfMonth());
    super.start();
  }

  @Override
  public void append(ILoggingEvent event) {
    try {
      LogStream logStream = getLogStream();

      String sequenceToken = logStream.uploadSequenceToken();

      InputLogEvent inputLogEvent = InputLogEvent.builder()
          .message(encoder.getLayout().doLayout(event))
          .timestamp(System.currentTimeMillis())
          .build();

      PutLogEventsRequest putLogEventsRequest = PutLogEventsRequest.builder()
          .logEvents(Collections.singletonList(inputLogEvent))
          .logGroupName(this.logGroupName)
          .logStreamName(this.logStreamName)
          .sequenceToken(sequenceToken)
          .build();

      this.cloudWatchLogsClient.putLogEvents(putLogEventsRequest);

    } catch (CloudWatchLogsException e) {
      addError(e.awsErrorDetails().errorMessage());
    }
  }

  private LogStream getLogStream() {
    DescribeLogStreamsRequest logStreamRequest = DescribeLogStreamsRequest.builder()
        .logGroupName(this.logGroupName)
        .logStreamNamePrefix(this.logStreamName)
        .build();
    DescribeLogStreamsResponse describeLogStreamsResponse =
        this.cloudWatchLogsClient.describeLogStreams(logStreamRequest);

    Optional<LogStream> logStream = describeLogStreamsResponse.logStreams().stream()
        .filter(stream -> stream.logStreamName().equals(this.logStreamName))
        .findFirst();

    if (logStream.isPresent()) {
      return logStream.get();
    } else {
      CreateLogStreamRequest createRequest = CreateLogStreamRequest.builder()
          .logGroupName(this.logGroupName)
          .logStreamName(this.logStreamName)
          .build();

      this.cloudWatchLogsClient.createLogStream(createRequest);

      return LogStream.builder()
          .logStreamName(this.logStreamName).build();
    }
  }

  @Override
  public void stop() {
    try {
      super.stop();
      this.cloudWatchLogsClient.close();
    } catch (Exception e) {
      addError("Error while stopping CloudWatchAppender.", e);
    }
  }
}