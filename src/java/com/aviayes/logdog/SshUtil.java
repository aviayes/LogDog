/*
 * Copyright (c) 2016 Alexander Bondar
 *
 */

package com.aviayes.logdog;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * CommandBuilder
 *
 * @author <a href="mailto:alex.aviayes@gmail.com">Alexander Bondar</a>
 */

public class SshUtil {
    private static final Logger logger = Util.getLogger(SshUtil.class);

    public static Session openSession(ConnectionSettings cSettings) {
        logger.info("openSession open to host=" + cSettings.getHost());
        JSch jsch = new JSch();
        Session session = null;
        String result;
        try {
            session = jsch.getSession(cSettings.getUsername(), cSettings.getHost());
            session.setPassword(cSettings.getPassword());
            session.setConfig("StrictHostKeyChecking", "no");//it is unsecured but in case it's ok
            session.connect();
        } catch (Exception e) {
            logger.error("openSession error", e);
            //todo logs
            result = "LogDog error: " + e.toString();
            session = null;
            //todo close session?
        }
        logger.info("openSession result=" + session);
        return session;
    }

    public static void closeSession(Session session) {
        try {
            if (session != null) {
                session.disconnect();
            }
        } catch (Exception e) {
            logger.error("closeSession error, session=" + session, e);
        }
    }

    public static void download(Session session, String sourcePath, File destFile) {
        try {
            logger.info("download. sourcePath=" + sourcePath + ", destFile=" + destFile);
            ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();
            logger.info("sftp channel created.");
            try (InputStream out = sftpChannel.get(sourcePath)) {
                Files.copy(out, destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            sftpChannel.disconnect();
        } catch (Exception e) {
            logger.error("download sftp error", e);
        }
    }

    public static String execCommand(Session session, String command) {
        StringBuilder outputBuffer = new StringBuilder();
        try {
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            InputStream commandOutput = channel.getInputStream();
            channel.connect();
            int readByte = commandOutput.read();

            //limits
            while (readByte != 0xffffffff) {
                outputBuffer.append((char) readByte);
                readByte = commandOutput.read();
            }

            channel.disconnect();
        } catch (Exception e) {
            logger.error("execCommand error", e);
            outputBuffer.append(ERROR_TITLE + e.getMessage());
        }
        return outputBuffer.toString();
    }

    private static final String ERROR_TITLE = "execCommand error:";

    public static boolean execCommand(Session session, String command, ResultChecker resultChecker) {
        String result = execCommand(session, command);
        return !result.contains(ERROR_TITLE) && resultChecker.checkResult(result);
    }

    public interface ResultChecker {
        boolean checkResult(String result);
    }
}