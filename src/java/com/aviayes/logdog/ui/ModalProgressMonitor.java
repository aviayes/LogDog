/*
 * Copyright (c) 2016 Alexander Bondar
 *
 */

package com.aviayes.logdog.ui;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Component;

/**
 * ModalProgressMonitor
 * It was needed to create custom monitor because javax.swing.ProgressMonitor does not support modal mode
 *
 * @author <a href="mailto:alex.aviayes@gmail.com">Alexander Bondar</a>
 */

public class ModalProgressMonitor {
    private final JProgressBar dpb;
    private final JLabel progressNote;
    private final int max;
    private final JDialog dlg;

    public ModalProgressMonitor(Component parentComponent, String title, int min, int max) {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(parentComponent);
        dlg = new JDialog(topFrame, title, true);
        this.max = max;
        dpb = new JProgressBar(min, max);
        dlg.add(BorderLayout.CENTER, dpb);
        progressNote = new JLabel("Progress...");
        dlg.add(BorderLayout.NORTH, progressNote);
        dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dlg.setSize(400, 100);
        dlg.setLocationRelativeTo(topFrame);
    }

    public void setProgress(int n, String note) {
        if (n > max) {
            n = max;
        }
        dpb.setValue(n);
        progressNote.setText(note);
        if (n == max) {
            dlg.setVisible(false);
            dlg.dispose();
        }
    }

    public void setProgressError(String note) {
        dpb.setValue(max);
        dlg.setTitle("Downloading failed");
        progressNote.setText(note);
    }

    public void show() {
        Thread t = new Thread(new Runnable() {
            public void run() {
                dlg.setVisible(true);
            }
        });
        t.start();
    }
}
