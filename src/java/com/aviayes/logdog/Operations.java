/*
 * Copyright (c) 2016 Alexander Bondar
 *
 */

package com.aviayes.logdog;

import org.apache.log4j.Logger;
import com.aviayes.logdog.ui.ModalProgressMonitor;
import com.jcraft.jsch.Session;

import java.io.File;

/**
 * CommandBuilder
 *
 * @author <a href="mailto:alex.aviayes@gmail.com">Alexander Bondar</a>
 */

public class Operations {

    private static final Logger logger = Util.getLogger(Operations.class);

    private static final String PREFIX = "logdog.";

    public static void download(final ModalProgressMonitor monitor, final String url, final File downloadDir) {
        final Runnable runnable = new Runnable() {
            public void run() {
                logger.info("download: " + url);
                ConnectionSettings connectionSettings = ConnectionSettings.createConnectionSettings(url);
                monitor.setProgress(0, "Connecting to " + connectionSettings.getHost() + "...");
                Session session = SshUtil.openSession(connectionSettings);
                if (session == null) {
                    String msg = "Connection failed";
                    logger.info("download: " + msg);
                    monitor.setProgressError(msg);
                    return;
                }

                try {
                    boolean result = SSHOperations.fileExists(session, connectionSettings.getPath());
                    if (!result) {
                        String msg = "Source file does not exists: " + connectionSettings.getPath();
                        logger.info("download: " + msg);
                        monitor.setProgressError(msg);
                        return;
                    }
                    monitor.setProgress(1, "Packing files...");
                    File fileToZip = new File(connectionSettings.getPath());
                    final String filenameWithoutPrefix = connectionSettings.getHost() + "." + fileToZip.getName() + ".zip";
                    final String filenameWithPrefix = PREFIX + filenameWithoutPrefix;
                    String parentDir = (File.separatorChar == '\\') ? toUnixPath(fileToZip.getParent()) :
                            fileToZip.getParent();
                    final String destZipPath = parentDir + "/" + filenameWithPrefix;
                    result = SSHOperations.zip(session, connectionSettings.getPath(), destZipPath);
                    if (!result) {
                        monitor.setProgressError("Archivation error on remote host");
                        return;
                    }
                    result = SSHOperations.fileExists(session, destZipPath);
                    if (!result) {
                        monitor.setProgressError("Archive file does not exist: " + destZipPath);
                        return;
                    }
                    logger.info("download: downloading: " + destZipPath);
                    monitor.setProgress(2, "Downloading files...");
                    File destFile = new File(downloadDir, filenameWithoutPrefix);
                    SshUtil.download(session, destZipPath, destFile);
                    result = SSHOperations.rm(session, destZipPath);
                    //todo
                } catch (Exception e) {
                    logger.info("download: general error", e);
                } finally {
                    SshUtil.closeSession(session);
                }
                logger.info("download: completed");
                monitor.setProgress(4, "Downloading completed");
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }


    private static String toUnixPath(String path) {
        return path.replace('\\', '/');
    }
}
