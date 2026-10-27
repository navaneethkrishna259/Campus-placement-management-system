package com.placementhub.student.util;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Modern, clean Swing styling utility for PLACEMENT HUB.
 * Solid color palette strictly complying with specification:
 * - Primary navy blue: #173B78
 * - Secondary blue: #2457A7
 * - Action blue: #246BFD
 * - Background: #F4F7FB
 * - White: #FFFFFF
 * - Border gray: #DCE4EF
 * - Main text: #17243A
 * - Secondary text: #64748B
 * - Success green: #168A55
 * - Error red: #D9363E
 */
public class UIUtils {
    public static final Color COLOR_PRIMARY = new Color(0x17, 0x3B, 0x78);
    public static final Color COLOR_SECONDARY = new Color(0x24, 0x57, 0xA7);
    public static final Color COLOR_ACTION = new Color(0x24, 0x6B, 0xFD);
    public static final Color COLOR_BG = new Color(0xF4, 0xF7, 0xFB);
    public static final Color COLOR_WHITE = Color.WHITE;
    public static final Color COLOR_BORDER = new Color(0xDC, 0xE4, 0xEF);
    public static final Color COLOR_TEXT_MAIN = new Color(0x17, 0x24, 0x3A);
    public static final Color COLOR_TEXT_MUTED = new Color(0x64, 0x74, 0x8B);
    public static final Color COLOR_SUCCESS = new Color(0x16, 0x8A, 0x55);
    public static final Color COLOR_ERROR = new Color(0xD9, 0x36, 0x3E);
    public static final Color COLOR_PURPLE = new Color(0x6F, 0x42, 0xC1);

    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);

    public static JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BOLD);
        btn.setBackground(COLOR_PRIMARY);
        btn.setForeground(COLOR_WHITE);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static JButton createActionButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BOLD);
        btn.setBackground(COLOR_ACTION);
        btn.setForeground(COLOR_WHITE);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static JButton createSecondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BOLD);
        btn.setBackground(new Color(0xEE, 0xF2, 0xF8)); // Soft filled slate tint
        btn.setForeground(COLOR_PRIMARY);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(7, 14, 7, 14)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

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

    public static JButton createSuccessButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BOLD);
        btn.setBackground(COLOR_SUCCESS);
        btn.setForeground(COLOR_WHITE);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static JTextField createTextField() {
        JTextField tf = new JTextField();
        tf.setFont(FONT_REGULAR);
        tf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDER, 1),
                new EmptyBorder(6, 8, 6, 8)
        ));
        return tf;
    }

    public static JPasswordField createPasswordField() {
        JPasswordField pf = new JPasswordField();
        pf.setFont(FONT_REGULAR);
        pf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDER, 1),
                new EmptyBorder(6, 8, 6, 8)
        ));
        return pf;
    }

    public static JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(COLOR_WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDER, 1),
                new EmptyBorder(12, 12, 12, 12)
        ));
        return panel;
    }
}
