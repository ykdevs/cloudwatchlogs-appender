# CloudWatch Logs Logback Appender

## Overview

CloudWatch Logs の Logback Appender の作成。

## Step 1 logback.xmlでログファイルに出力

`logback.xml` に以下の設定を追加する。

```xml
<configuration>
    <property name="LOG_DIR" value="logs" />
    <property name="LOG_FILE" value="${LOG_DIR}/app.log" />

    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_FILE}</file>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <fileNamePattern>${LOG_DIR}/app-%d{yyyy-MM-dd}.%i.log</fileNamePattern>
            <maxFileSize>10MB</maxFileSize>
            <maxHistory>30</maxHistory> <!-- 3日分のログファイルを保持 -->
        </rollingPolicy>
    </appender>

    <root level="INFO">
        <appender-ref ref="FILE"/>
    </root>

</configuration>
```

logs/app.log にログが出力される。

```text
2024-05-19 19:54:42.278 [main] INFO  c.e.c.CloudwatchlogsAppenderApplication - Starting CloudwatchlogsAppenderApplication using Java 17.0.5 with PID 6018 (/Users/yuzuru/work/aws/cloudwatchlogs-appender/build/classes/java/main started by yuzuru in /Users/yuzuru/work/aws/cloudwatchlogs-appender)
2024-05-19 19:54:42.280 [main] INFO  c.e.c.CloudwatchlogsAppenderApplication - No active profile set, falling back to 1 default profile: "default"
2024-05-19 19:54:42.849 [main] INFO  c.e.c.CloudwatchlogsAppenderApplication - Started CloudwatchlogsAppenderApplication in 0.959 seconds (process running for 1.512)
2024-05-19 19:54:42.851 [main] INFO  c.e.c.batch.BatchMain - Cloud Watch Logs Appender Application Start
```



## References

- [Java standalone app for sending logs to Amazon CloudWatch Logs with Slf4j/Logback](https://kishida58.medium.com/java-standalone-app-for-sending-logs-to-amazon-cloudwatch-logs-with-slf4j-logback-2543e35ce19a)
- [SpringBootログ出力まとめ（slf4j + logback）](https://qiita.com/Masahiro_Uemura1234/items/61a25ce4aa815a9922d6)
- [Javaのログ出力について](https://qiita.com/kero3/items/0033adca07a585623768)
- [Spring BootでSLF4J+logback+Lombokを使いログ出力を行う](https://www.aruse.net/entry/2022/07/09/220510)
- [SDK for Java 2.x でのログ記録](https://docs.aws.amazon.com/ja_jp/sdk-for-java/latest/developer-guide/logging-slf4j.html)
- [logback機能,設定まとめ](https://qiita.com/rubytomato@github/items/93770f827e46cc7e684f)
- [Chapter 4: Logback Appenders](https://logback.qos.ch/manual/appenders.html)
- [Chapter 5: Encoders](https://logback.qos.ch/manual/encoders.html)
- [Logback log appender for AWS CloudWatch](https://github.com/j256/cloudwatch-logback-appender)
- [cloudwatch-logback-appender](https://github.com/graingert/cloudwatch-logback-appender)
- [AWS SDKs を使用した CloudWatch ログのコード例](https://docs.aws.amazon.com/ja_jp/AmazonCloudWatch/latest/logs/service_code_examples.html)
- [AWS SDK での CloudWatch ログの使用](https://docs.aws.amazon.com/ja_jp/AmazonCloudWatch/latest/logs/sdk-general-information-section.html)
- [PutLogEvents.java](https://github.com/awsdocs/aws-doc-sdk-examples/blob/main/javav2/example_code/cloudwatch/src/main/java/com/example/cloudwatch/PutLogEvents.java)
