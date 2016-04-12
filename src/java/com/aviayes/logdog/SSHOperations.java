/*
 * Copyright (c) 2016 Alexander Bondar
 *
 */

package com.aviayes.logdog;

import com.jcraft.jsch.Session;
import org.apache.log4j.Logger;

/**
 * CommandBuilder
 *
 * @author <a href="mailto:alex.aviayes@gmail.com">Alexander Bondar</a>
 */

public class SSHOperations {
    private static final Logger logger = Util.getLogger(SSHOperations.class);

    public static boolean zip(Session session, String sourcePath, String destPath) {
        String commandStr = " zip -j -q " + destPath + " " + sourcePath;
        logger.info("zip commandStr=" + commandStr);
        return SshUtil.execCommand(session, commandStr, new SshUtil.ResultChecker() {
            @Override
            public boolean checkResult(String result) {
                logger.info("zip result=" + result);
                return result.trim().isEmpty();
            }
        });
    }

    public static boolean fileExists(Session session, String path) {
        final String notExistsStr = "not_exists";
        String commandStr = "[ -f " + path + " ] && echo \"exists\" || echo \"" + notExistsStr + "\"";
        logger.info("fileExists commandStr=" + commandStr);
        return SshUtil.execCommand(session, commandStr, new SshUtil.ResultChecker() {
            @Override
            public boolean checkResult(String result) {
                logger.info("fileExists result=" + result);
                return !result.contains(notExistsStr);
            }
        });
    }

    public static boolean rm(Session session, String destPath) {
        String commandStr = " rm -f " + destPath;
        logger.info("rm commandStr=" + commandStr);
        return SshUtil.execCommand(session, commandStr, new SshUtil.ResultChecker() {
            @Override
            public boolean checkResult(String result) {
                logger.info("rm result=" + result);
                return result.trim().isEmpty();
            }
        });
    }
}
