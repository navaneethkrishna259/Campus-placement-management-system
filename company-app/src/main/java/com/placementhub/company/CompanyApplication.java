package com.placementhub.company;

import com.placementhub.company.ui.CompanyLoginFrame;

import javax.swing.*;

public class CompanyApplication {

    public static void main(String[] args) {
        // Set modern native look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            CompanyLoginFrame loginFrame = new CompanyLoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
