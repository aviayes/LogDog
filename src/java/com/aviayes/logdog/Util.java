/*
 * Copyright (c) 2016 Alexander Bondar
 *
 */

package com.aviayes.logdog;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.util.Properties;

/**
 * Util
 *
 * @author <a href="mailto:alex.aviayes@gmail.com">Alexander Bondar</a>
 */

public class Util {

    public static Logger getLogger(Class clazz) {
        return Logger.getLogger(clazz.getName());
    }

    public static void configureLog4j(String logFile) {
        Properties props = new Properties();
        props.put("log4j.rootLogger", "DEBUG, FILE");
        props.put("log4j.appender.FILE", "org.apache.log4j.FileAppender");
        props.put("log4j.appender.FILE.file", logFile);
        props.put("log4j.appender.FILE.Append", "false");
        props.put("log4j.appender.FILE.layout", "org.apache.log4j.PatternLayout");
        props.put("log4j.appender.FILE.layout.conversionPattern", "%d{ISO8601} %-5p %-18c{1} : %m%n");
        LogManager.resetConfiguration();
        PropertyConfigurator.configure(props);
    }
}
