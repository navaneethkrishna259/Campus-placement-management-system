package com.placementhub.student.ui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.placementhub.student.util.ApiClient;
import com.placementhub.student.util.CredentialStorage;
import com.placementhub.student.util.UIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Student Login and Registration Frame.
 */
public class StudentLoginFrame extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel containerPanel = new JPanel(cardLayout);

    // Login Form Fields
    private JTextField loginEmailField;
    private JPasswordField loginPasswordField;
    private JCheckBox rememberEmailCheckBox;
    private JButton loginButton;
    private JButton showRegisterButton;

    // Register Form Fields
    private JTextField regNameField;
    private JTextField regEmailField;
    private JPasswordField regPasswordField;
    private JPasswordField regConfirmPasswordField;
    private JTextField regPhoneField;
    private JTextField regDeptField;
    private JTextField regGradYearField;
    private JButton registerButton;
    private JButton backToLoginButton;

    public StudentLoginFrame() {
        setTitle("Placement Hub - Student Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 620);
        setLocationRelativeTo(null);
        setResizable(false);

        initUI();
    }

    private void initUI() {
        containerPanel.add(buildLoginPanel(), "LOGIN");
        containerPanel.add(buildRegisterPanel(), "REGISTER");
        add(containerPanel);
    }

    private JPanel buildLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIUtils.COLOR_BG);
        panel.setBorder(new EmptyBorder(35, 40, 35, 40));

        // Header
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        headerPanel.setBackground(UIUtils.COLOR_BG);

        JLabel titleLabel = new JLabel("Student Portal", SwingConstants.CENTER);
        titleLabel.setFont(UIUtils.FONT_TITLE);
        titleLabel.setForeground(UIUtils.COLOR_PRIMARY);

        JLabel subtitleLabel = new JLabel("Log in to discover opportunities & apply", SwingConstants.CENTER);
        subtitleLabel.setFont(UIUtils.FONT_REGULAR);
        subtitleLabel.setForeground(UIUtils.COLOR_TEXT_MUTED);

        headerPanel.add(titleLabel);
        headerPanel.add(subtitleLabel);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Form
        JPanel formCard = UIUtils.createCardPanel();
        formCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.weightx = 1.0;

        int row = 0;

        // Email
        gbc.gridx = 0; gbc.gridy = row++;
        JLabel emailLbl = new JLabel("Student Email");
        emailLbl.setFont(UIUtils.FONT_BOLD);
        formCard.add(emailLbl, gbc);

        gbc.gridy = row++;
        loginEmailField = UIUtils.createTextField();
        String savedEmail = CredentialStorage.getSavedEmail();
        if (!savedEmail.isEmpty()) {
            loginEmailField.setText(savedEmail);
        }
        formCard.add(loginEmailField, gbc);

        // Password
        gbc.gridy = row++;
        JLabel passLbl = new JLabel("Password");
        passLbl.setFont(UIUtils.FONT_BOLD);
        formCard.add(passLbl, gbc);

        gbc.gridy = row++;
        loginPasswordField = UIUtils.createPasswordField();
        formCard.add(loginPasswordField, gbc);

        // Remember Email
        gbc.gridy = row++;
        rememberEmailCheckBox = new JCheckBox("Remember Email");
        rememberEmailCheckBox.setBackground(UIUtils.COLOR_WHITE);
        rememberEmailCheckBox.setFont(UIUtils.FONT_REGULAR);
        rememberEmailCheckBox.setSelected(!savedEmail.isEmpty());
        formCard.add(rememberEmailCheckBox, gbc);

        // Action Buttons
        gbc.gridy = row++;
        gbc.insets = new Insets(18, 5, 8, 5);
        loginButton = UIUtils.createActionButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setPreferredSize(new Dimension(0, 42));
        loginButton.addActionListener(e -> handleLogin());
        formCard.add(loginButton, gbc);

        gbc.gridy = row++;
        gbc.insets = new Insets(4, 5, 4, 5);
        showRegisterButton = UIUtils.createSecondaryButton("Register New Student");
        showRegisterButton.addActionListener(e -> cardLayout.show(containerPanel, "REGISTER"));
        formCard.add(showRegisterButton, gbc);

        panel.add(formCard, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildRegisterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIUtils.COLOR_BG);
        panel.setBorder(new EmptyBorder(15, 30, 15, 30));

        JLabel titleLabel = new JLabel("Register Student", SwingConstants.CENTER);
        titleLabel.setFont(UIUtils.FONT_TITLE);
        titleLabel.setForeground(UIUtils.COLOR_PRIMARY);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel formCard = UIUtils.createCardPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));

        regNameField = UIUtils.createTextField();
        regEmailField = UIUtils.createTextField();
        regPasswordField = UIUtils.createPasswordField();
        regConfirmPasswordField = UIUtils.createPasswordField();
        regPhoneField = UIUtils.createTextField();
        regDeptField = UIUtils.createTextField();
        regGradYearField = UIUtils.createTextField();

        formCard.add(createFieldBlock("Full Name *", regNameField));
        formCard.add(createFieldBlock("Student Email *", regEmailField));
        formCard.add(createFieldBlock("Password *", regPasswordField));
        formCard.add(createFieldBlock("Confirm Password *", regConfirmPasswordField));
        formCard.add(createFieldBlock("Phone Number", regPhoneField));
        formCard.add(createFieldBlock("Department (e.g., Computer Science)", regDeptField));
        formCard.add(createFieldBlock("Graduation Year (e.g., 2026)", regGradYearField));

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        btnPanel.setBackground(UIUtils.COLOR_WHITE);
        btnPanel.setBorder(new EmptyBorder(10, 0, 5, 0));

        registerButton = UIUtils.createActionButton("Register");
        registerButton.setPreferredSize(new Dimension(0, 40));
        registerButton.addActionListener(e -> handleRegister());

        backToLoginButton = UIUtils.createSecondaryButton("Back to Login");
        backToLoginButton.addActionListener(e -> cardLayout.show(containerPanel, "LOGIN"));

        btnPanel.add(registerButton);
        btnPanel.add(backToLoginButton);
        formCard.add(btnPanel);

        JScrollPane scrollPane = new JScrollPane(formCard);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFieldBlock(String label, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout(0, 3));
        p.setBackground(UIUtils.COLOR_WHITE);
        p.setBorder(new EmptyBorder(3, 0, 5, 0));
        JLabel lbl = new JLabel(label);
        lbl.setFont(UIUtils.FONT_BOLD);
        p.add(lbl, BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);
        return p;
    }

    private void handleLogin() {
        String email = loginEmailField.getText().trim();
        String password = new String(loginPasswordField.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both email and password.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        loginButton.setEnabled(false);
        loginButton.setText("Logging in...");

        SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, Object> doInBackground() throws Exception {
                Map<String, String> body = new HashMap<>();
                body.put("email", email);
                body.put("password", password);

                return ApiClient.post("/api/students/login", body, new TypeReference<Map<String, Object>>() {});
            }

            @Override
            protected void done() {
                loginButton.setEnabled(true);
                loginButton.setText("Login");
                try {
                    Map<String, Object> resp = get();
                    Boolean success = (Boolean) resp.get("success");
                    if (Boolean.TRUE.equals(success)) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> data = (Map<String, Object>) resp.get("data");
                        String token = (String) data.get("token");
                        ApiClient.setAuthToken(token);

                        if (rememberEmailCheckBox.isSelected()) {
                            CredentialStorage.saveEmail(email);
                        } else {
                            CredentialStorage.clearEmail();
                        }

                        // Open Dashboard
                        dispose();
                        SwingUtilities.invokeLater(() -> new StudentDashboardFrame().setVisible(true));
                    } else {
                        String msg = (String) resp.get("message");
                        JOptionPane.showMessageDialog(StudentLoginFrame.this, msg != null ? msg : "Login failed.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(StudentLoginFrame.this, ex.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void handleRegister() {
        String name = regNameField.getText().trim();
        String email = regEmailField.getText().trim();
        String password = new String(regPasswordField.getPassword()).trim();
        String confirm = new String(regConfirmPasswordField.getPassword()).trim();
        String phone = regPhoneField.getText().trim();
        String dept = regDeptField.getText().trim();
        String gradYearStr = regGradYearField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Full Name, Email, and Password are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer gradYear = null;
        if (!gradYearStr.isEmpty()) {
            try {
                gradYear = Integer.parseInt(gradYearStr);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Graduation Year must be a valid number.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        final Integer finalGradYear = gradYear;
        registerButton.setEnabled(false);
        registerButton.setText("Registering...");

        SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, Object> doInBackground() throws Exception {
                Map<String, Object> body = new HashMap<>();
                body.put("fullName", name);
                body.put("email", email);
                body.put("password", password);
                body.put("phone", phone);
                body.put("department", dept);
                body.put("graduationYear", finalGradYear);

                return ApiClient.post("/api/students/register", body, new TypeReference<Map<String, Object>>() {});
            }

            @Override
            protected void done() {
                registerButton.setEnabled(true);
                registerButton.setText("Register");
                try {
                    Map<String, Object> resp = get();
                    Boolean success = (Boolean) resp.get("success");
                    if (Boolean.TRUE.equals(success)) {
                        JOptionPane.showMessageDialog(StudentLoginFrame.this, "Student registered successfully! Please log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        loginEmailField.setText(email);
                        loginPasswordField.setText("");
                        cardLayout.show(containerPanel, "LOGIN");
                    } else {
                        String msg = (String) resp.get("message");
                        JOptionPane.showMessageDialog(StudentLoginFrame.this, msg != null ? msg : "Registration failed.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(StudentLoginFrame.this, ex.getMessage(), "Registration Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }
}
