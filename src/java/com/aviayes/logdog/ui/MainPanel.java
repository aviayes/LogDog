/*
 * Copyright (c) 2016 Alexander Bondar
 *
 */

package com.aviayes.logdog.ui;

import com.aviayes.logdog.ConnectionSettings;
import com.aviayes.logdog.Operations;
import com.aviayes.logdog.Util;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * MainPanel
 *
 * @author <a href="mailto:alex.aviayes@gmail.com">Alexander Bondar</a>
 */

public class MainPanel extends JPanel {
    private File downloadsDir;

    public MainPanel(final File rootDir) {
        downloadsDir = new File(rootDir, "downloads");
        if (!downloadsDir.exists()) {
            //todo
            downloadsDir.mkdir();
        }

        Util.configureLog4j(new File(new File(rootDir, "bin"), "logdog.log").getAbsolutePath());
        Util.getLogger(getClass()).info("Started. Version: " + MainWindow.VERSION);
        JPanel addressPanel = new JPanel();
        Border border = addressPanel.getBorder();
        Border margin = new EmptyBorder(10, 10, 10, 10);
        addressPanel.setBorder(new CompoundBorder(border, margin));

        GridBagLayout panelGridBagLayout = new GridBagLayout();
        panelGridBagLayout.columnWidths = new int[]{86, 86, 0};
        panelGridBagLayout.rowHeights = new int[]{20, 20, 20, 20, 20, 0};
        panelGridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        panelGridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        addressPanel.setLayout(panelGridBagLayout);

        final JTextField urlText = addLabelAndTextField("SSH URL:", 0, addressPanel);
        urlText.setToolTipText(ConnectionSettings.URL_EXAMPLE);
        //addLabelAndTextField("Pattern:", 1, addressPanel);

        JButton downloadBtn = new JButton("Download");
        downloadBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //progress monitor can not be a modal
              /*  final ProgressMonitor monitor = new ProgressMonitor(MainPanel.this, "Downloading",
                        "Downloading", 0, 4);*/
                ModalProgressMonitor monitor = new ModalProgressMonitor(MainPanel.this, "Downloading", 0, 4);
                monitor.show();
                Operations.download(monitor, urlText.getText(), downloadsDir);
            }
        });
        addComp(3, 0, downloadBtn, addressPanel);

        JButton openBtn = new JButton();
        openBtn.setIcon(UIManager.getIcon("FileView.directoryIcon"));
        openBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().open(downloadsDir);
                } catch (IOException e1) {
                    //todo
                }
            }
        });
        addComp(4, 0, openBtn, addressPanel);

        add(addressPanel);
    }

    private JTextField addLabelAndTextField(String labelText, int yPos, Container addressPanel) {
        JTextField textField = new JTextField(50);
        addComp(0, yPos, new JLabel(labelText), addressPanel);
        addComp(1, yPos, textField, addressPanel);
        return textField;
    }

    private void addComp(int xPos, int yPos, Component comp, Container addressPanel) {
        GridBagConstraints gridBagConstraintForLabel = new GridBagConstraints();
        gridBagConstraintForLabel.fill = GridBagConstraints.BOTH;
        gridBagConstraintForLabel.insets = new Insets(0, 0, 5, 5);
        gridBagConstraintForLabel.gridx = xPos;
        gridBagConstraintForLabel.gridy = yPos;
        addressPanel.add(comp, gridBagConstraintForLabel);
    }
}
