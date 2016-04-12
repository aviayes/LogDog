/*
 * Copyright (c) 2016 Alexander Bondar
 *
 */

package com.aviayes.logdog;

import org.apache.log4j.Logger;

/**
 * CommandBuilder
 *
 * @author <a href="mailto:alex.aviayes@gmail.com">Alexander Bondar</a>
 */

public class ConnectionSettings {
    private static final Logger logger = Util.getLogger(ConnectionSettings.class);

    public static final String URL_EXAMPLE = "ssh://username:password@host:port/somedir1/log/system1.log";

    private String host;
    private int port;
    private String username;
    private String password;
    private String path;


    public static ConnectionSettings createConnectionSettings(String sshUrl) {
        sshUrl = sshUrl.trim();
        //todo rewrite it with pattern. Support url without password
        if (!sshUrl.startsWith("ssh://") || !sshUrl.contains("@")) {
            String msg = "Incorrect ssh url (1). Must be: " + URL_EXAMPLE;
            logger.info(msg);
            throw new IllegalArgumentException();
        }
        sshUrl = sshUrl.substring("ssh://".length());
        String[] one = sshUrl.split("@");
        if (one.length != 2) {
            String msg = "Incorrect ssh url (2). Must be: " + URL_EXAMPLE;
            logger.info(msg);
        }
        String[] usernamePass = one[0].split(":");
        String[] hostAndPortPath = one[1].split(":");
        int firstSlashIndex = hostAndPortPath[1].indexOf("/");
        String portStr = hostAndPortPath[1].substring(0, firstSlashIndex);
        String path = hostAndPortPath[1].substring(firstSlashIndex);

        ConnectionSettings result = new ConnectionSettings(hostAndPortPath[0], Integer.parseInt(portStr),
                usernamePass[0], usernamePass[1], path);
        return result;
    }

    public ConnectionSettings(String host, String username, String password) {
        this(host, 22, username, password, null);
    }

    public ConnectionSettings(String host, int port, String username, String password, String path) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.path = path;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
