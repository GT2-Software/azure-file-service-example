package dev.gt2software.main.services.v2;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.OnStartupTriggeringPolicy;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.xml.XmlConfigurationFactory;
import org.apache.logging.log4j.core.config.builder.api.PropertyComponentBuilder;
import org.apache.logging.log4j.core.layout.PatternLayout;

public class LogService {

    private static Logger logger = null;
    private static LoggerContext context = new LoggerContext("LogContext");

    private LogService() {
    }

    public static Logger getInstance() {
        if (logger == null) {
            try {
                SizeBasedTriggeringPolicy sizePolicy = SizeBasedTriggeringPolicy.createPolicy("5 KB");
                TimeBasedTriggeringPolicy timePolicy = TimeBasedTriggeringPolicy.newBuilder()
                        .withInterval(1)
                        .withModulate(true)
                        .build();
                PatternLayout layout = PatternLayout
                        .newBuilder()
                        .withPattern("%C %-5level - %d{yyyy-MM-dd HH:mm}{GMT-6} - %p - %m%n")
                        .withConfiguration(context.getConfiguration()).build();
                ConfigurationFactory factory = XmlConfigurationFactory.getInstance();
                Configuration configuration = factory.getInstance().newConfigurationBuilder().build();
                ConsoleAppender consoleAppender = ConsoleAppender
                        .createDefaultAppenderForLayout(layout);
                configuration.addAppender(consoleAppender);
                RollingFileAppender fileAppender = RollingFileAppender.newBuilder()
                        .withFileName(String.format("%s/%s/%s/%s",
                                System.getenv("LOG_ROOT_PATH"),
                                System.getenv("COUNTRY"),
                                System.getenv("COMPUTERNAME"),
                                "tlbo_be_current.log"))
                        .withAppend(true)
                        .setName("LogToRollingFile")
                        .setConfiguration(configuration)
                        .withLocking(false)
                        .withCreateOnDemand(true)
                        // .withFileGroup("root")
                        // .withFileOwner("root")
                        .withFilePermissions("rw-rw-rw-")
                        .withFilePattern(String.format("%s/%s/%s/%s",
                                System.getenv("LOG_ROOT_PATH"),
                                System.getenv("COUNTRY"),
                                System.getenv("COMPUTERNAME"),
                                "%d{YYY-MM-dd}-%i.log"))
                        .setLayout(layout)
                        .withPolicy(sizePolicy)
                        .withPolicy(timePolicy)
                        .build();
                configuration.addAppender(fileAppender);

                LoggerConfig loggerConfig = new LoggerConfig("LogToRollingFile", Level.ALL, false);
                loggerConfig.addAppender(consoleAppender, Level.DEBUG, null);
                loggerConfig.addAppender(fileAppender, Level.ALL, null);
                configuration.addLogger("LogToRollingFile", loggerConfig);
                context = new LoggerContext("LogContext");
                context.start(configuration);
                timePolicy.initialize();
                timePolicy.start();
                sizePolicy.initialize();
                sizePolicy.start();
                logger = context.getLogger("LogToRollingFile");
                logger.info("Logging started");
            } catch (Exception e) {
                error("Error en el logger: \n" + e.getMessage());
                e.printStackTrace();
            }
        }

        return logger;
    }

    public static void error(String Message) {
        getInstance();
        if (System.getenv("LOG_ERROR").equals("1")) {
            logger.error(Message);
        }
    }

    public static void info(String Message) {
        getInstance();
        if (System.getenv("LOG_INFO").equals("1")) {
            logger.info(Message);
        }
    }

    public static void warn(String Message) {
        getInstance();
        if (System.getenv("LOG_WARN").equals("1")) {
            logger.warn(Message);
        }
    }

    public static void debug(String Message) {
        getInstance();
        if (System.getenv("LOG_DEBUG").equals("1")) {
            logger.debug(Message);
        }
    }
}
