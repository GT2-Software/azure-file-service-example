package dev.gt2software.main.services.v2;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.Configurator;

public class LogService {
        private static Logger _logger = null;

        /**
         * Private constructor to prevent instantiation of this class.
         */
        private LogService() {
        }

        /**
         * Get the instance of the logger for the application.
         * 
         * @return
         */
        public static Logger getAppEventInstance() {
                if (_logger != null) {
                        return _logger;
                }
                ConfigurationBuilder<BuiltConfiguration> configBuilder = ConfigurationBuilderFactory
                                .newConfigurationBuilder();
                Configuration configuration = configBuilder
                                .add(
                                                configBuilder
                                                                .newAppender("Stdout", "CONSOLE")
                                                                .add(
                                                                                configBuilder.newLayout(
                                                                                                "PatternLayout")
                                                                                                .addAttribute("pattern",
                                                                                                                "%C %-5level - %d{yyyy-MM-dd HH:mm}{GMT-6} - %m%n")))
                                .add(
                                                configBuilder
                                                                .newAppender("RollingFile",
                                                                                "RollingRandomAccessFile")
                                                                .add(
                                                                                configBuilder.newLayout(
                                                                                                "PatternLayout")
                                                                                                .addAttribute("pattern",
                                                                                                                "%C %-5level - %d{yyyy-MM-dd HH:mm}{GMT-6} T %nano - %m %n")
                                                                                                .addAttribute("header",
                                                                                                                "Rolling in file\n")
                                                                                                .addAttribute("footer",
                                                                                                                "\nRolling out file"))
                                                                .addAttribute("fileName",
                                                                                String.format("%s/%s/%s/%s",
                                                                                                System.getenv("LOG_ROOT_PATH"),
                                                                                                System.getenv("COUNTRY"),
                                                                                                System.getenv("COMPUTERNAME"),
                                                                                                "app-events.log"))
                                                                .addAttribute("filePattern",
                                                                                String.format("%s/%s/%s/%s",
                                                                                                System.getenv("LOG_ROOT_PATH"),
                                                                                                System.getenv("COUNTRY"),
                                                                                                System.getenv("COMPUTERNAME"),
                                                                                                "app-events-%d{yyyy-MM-dd HH}-%i.log"))
                                                                .addAttribute("append", true)
                                                                .addAttribute("createOnDemand", true)
                                                                .addComponent(
                                                                                configBuilder.newComponent(
                                                                                                "TimeBasedTriggeringPolicy")
                                                                                                .addAttribute("interval",
                                                                                                                1)
                                                                                                .addAttribute("modulate",
                                                                                                                true))
                                                                .addComponent(
                                                                                configBuilder.newComponent(
                                                                                                "SizeBasedTriggeringPolicy")
                                                                                                .addAttribute("size",
                                                                                                                "5 KB")))
                                .add(
                                                configBuilder
                                                                .newRootLogger(Level.TRACE))
                                .add(
                                                configBuilder.newLogger("AppEventLogger", Level.TRACE,
                                                                true)
                                                                .addComponent(
                                                                                configBuilder.newAppenderRef("Stdout"))
                                                                .addComponent(
                                                                                configBuilder.newAppenderRef(
                                                                                                "RollingFile")))
                                .build(false);
                LoggerContext loggerContext = Configurator.initialize(configuration);
                return loggerContext.getLogger("AppEventLogger");
        }
}
