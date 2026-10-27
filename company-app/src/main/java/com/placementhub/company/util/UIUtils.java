
package com.placementhub.company.util;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Shared light-mode styling utility for PLACEMENT HUB.
 * UI styling only. No business logic or workflow changes.
 */
public class UIUtils {

    // Light-mode color palette
    public static final Color COLOR_PRIMARY =
            new Color(23, 59, 120);

    public static final Color COLOR_SECONDARY =
            new Color(36, 87, 167);

    public static final Color COLOR_ACTION =
            new Color(43, 91, 171);

    public static final Color COLOR_BG =
            new Color(245, 247, 251);

    public static final Color COLOR_WHITE =
            Color.WHITE;

    public static final Color COLOR_BORDER =
            new Color(222, 228, 237);

    public static final Color COLOR_TEXT_MAIN =
            new Color(23, 36, 58);

    public static final Color COLOR_TEXT_MUTED =
            new Color(100, 116, 139);

    public static final Color COLOR_SUCCESS =
            new Color(22, 138, 85);

    public static final Color COLOR_ERROR =
            new Color(217, 54, 62);

    public static final Color COLOR_PURPLE =
            new Color(111, 66, 193);

    // Typography
    public static final Font FONT_TITLE =
            new Font("Segoe UI", Font.BOLD, 20);

    public static final Font FONT_SUBTITLE =
            new Font("Segoe UI", Font.BOLD, 15);

    public static final Font FONT_BOLD =
            new Font("Segoe UI", Font.BOLD, 13);

    public static final Font FONT_REGULAR =
            new Font("Segoe UI", Font.PLAIN, 13);

    public static final Font FONT_SMALL =
            new Font("Segoe UI", Font.PLAIN, 11);

    // Primary blue button
    public static JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text);

        btn.setFont(FONT_BOLD);
        btn.setBackground(COLOR_ACTION);
        btn.setForeground(COLOR_WHITE);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBorder(new EmptyBorder(9, 16, 9, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return btn;
    }

    // Blue action button
    public static JButton createActionButton(String text) {
        JButton btn = new JButton(text);

        btn.setFont(FONT_BOLD);
        btn.setBackground(COLOR_ACTION);
        btn.setForeground(COLOR_WHITE);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBorder(new EmptyBorder(8, 14, 8, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return btn;
    }

    // Light secondary button
    public static JButton createSecondaryButton(String text) {
        JButton btn = new JButton(text);

        btn.setFont(FONT_BOLD);
        btn.setBackground(COLOR_WHITE);
        btn.setForeground(COLOR_PRIMARY);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setFocusPainted(false);
        btn.setBorderPainted(true);
        btn.setBorder(new LineBorder(COLOR_BORDER, 1, true));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return btn;
    }

    // Red danger button
    public static JButton createDangerButton(String text) {
        JButton btn = new JButton(text);

        btn.setFont(FONT_BOLD);
        btn.setBackground(COLOR_ERROR);
        btn.setForeground(COLOR_WHITE);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBorder(new EmptyBorder(8, 14, 8, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return btn;
    }

    // White text field with subtle border
    public static JTextField createTextField() {
        JTextField tf = new JTextField();

        tf.setFont(FONT_REGULAR);
        tf.setForeground(COLOR_TEXT_MAIN);
        tf.setBackground(COLOR_WHITE);
        tf.setCaretColor(COLOR_PRIMARY);
        tf.setOpaque(true);
        tf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(6, 8, 6, 8)
        ));

        return tf;
    }

    // White password field with subtle border
    public static JPasswordField createPasswordField() {
        JPasswordField pf = new JPasswordField();

        pf.setFont(FONT_REGULAR);
        pf.setForeground(COLOR_TEXT_MAIN);
        pf.setBackground(COLOR_WHITE);
        pf.setCaretColor(COLOR_PRIMARY);
        pf.setOpaque(true);
        pf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(6, 8, 6, 8)
        ));

        return pf;
    }

    // Transparent-style card: background matches the light workspace.
    // Use a separate white panel explicitly where white cards are needed.
    public static JPanel createCardPanel() {
        JPanel panel = new JPanel();

        panel.setBackground(COLOR_BG);
        panel.setOpaque(true);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        return panel;
    }
}
