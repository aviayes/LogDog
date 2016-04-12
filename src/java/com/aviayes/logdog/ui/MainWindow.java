/*
 * Copyright (c) 2016 Alexander Bondar
 *
 */

package com.aviayes.logdog.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;

/**
 * MainWindow
 *
 * @author <a href="mailto:alex.aviayes@gmail.com">Alexander Bondar</a>
 */

public class MainWindow extends JFrame {
    static final String VERSION = "0.1";
    public MainWindow(File rootDir) {
        super("LogDog ver " + VERSION);
        JPanel cp = new JPanel(new BorderLayout());
        setContentPane(cp);
        cp.add(new MainPanel(rootDir), BorderLayout.CENTER);
        cp.setBackground(Color.green);
        setSize(830, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        File rootDir = new File(args[0], "..");
        new MainWindow(rootDir).setVisible(true);
    }
}
