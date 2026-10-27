package com.placementhub.student;

import com.placementhub.student.ui.StudentLoginFrame;

import javax.swing.*;

public class StudentApplication {

    public static void main(String[] args) {
        // Set modern native look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            StudentLoginFrame loginFrame = new StudentLoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
