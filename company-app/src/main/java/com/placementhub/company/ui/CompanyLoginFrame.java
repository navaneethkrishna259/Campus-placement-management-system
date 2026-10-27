package com.placementhub.company.ui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.placementhub.company.util.ApiClient;
import com.placementhub.company.util.CredentialStorage;
import com.placementhub.company.util.UIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Company Login and Registration Frame.
 * Light-mode UI with consistent blue buttons.
 * Existing application workflows are preserved.
 */
public class CompanyLoginFrame extends JFrame {

    // UI colors only
    private static final Color BG_COLOR = new Color(245, 247, 251);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color PRIMARY_BLUE = new Color(43, 91, 171);
    private static final Color HOVER_BLUE = new Color(32, 72, 143);
    private static final Color TEXT_COLOR = new Color(35, 48, 67);
    private static final Color MUTED_COLOR = new Color(105, 119, 139);
    private static final Color BORDER_COLOR = new Color(222, 228, 237);

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
    private JTextField regWebsiteField;
    private JTextField regAddressField;
    private JTextArea regDescriptionArea;
    private JButton registerButton;
    private JButton backToLoginButton;

    public CompanyLoginFrame() {
        setTitle("Placement Hub - Company Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 640);
        setLocationRelativeTo(null);
        setResizable(false);

        initUI();
    }

    private void initUI() {
        containerPanel.setBackground(BG_COLOR);

        containerPanel.add(buildLoginPanel(), "LOGIN");
        containerPanel.add(buildRegisterPanel(), "REGISTER");

        add(containerPanel);
    }

    // ==================== LOGIN PANEL ====================

    private JPanel buildLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);
        panel.setBorder(new EmptyBorder(35, 40, 35, 40));

        // Header
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        headerPanel.setBackground(BG_COLOR);

        JLabel titleLabel = new JLabel(
                "Company Portal",
                SwingConstants.CENTER
        );
        titleLabel.setFont(UIUtils.FONT_TITLE);
        titleLabel.setForeground(PRIMARY_BLUE);

        JLabel subtitleLabel = new JLabel(
                "Log in to manage jobs & applicants",
                SwingConstants.CENTER
        );
        subtitleLabel.setFont(UIUtils.FONT_REGULAR);
        subtitleLabel.setForeground(MUTED_COLOR);

        headerPanel.add(titleLabel);
        headerPanel.add(subtitleLabel);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Login card
        JPanel formCard = UIUtils.createCardPanel();
        formCard.setBackground(CARD_COLOR);
        formCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));
        formCard.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.weightx = 1.0;

        int row = 0;

        // Email
        gbc.gridx = 0;
        gbc.gridy = row++;

        JLabel emailLbl = new JLabel("Company Email");
        emailLbl.setFont(UIUtils.FONT_BOLD);
        emailLbl.setForeground(TEXT_COLOR);
        formCard.add(emailLbl, gbc);

        gbc.gridy = row++;

        loginEmailField = UIUtils.createTextField();

        String savedEmail = CredentialStorage.getSavedEmail();

        if (!savedEmail.isEmpty()) {
            loginEmailField.setText(savedEmail);
        }

        styleTextField(loginEmailField);
        formCard.add(loginEmailField, gbc);

        // Password
        gbc.gridy = row++;

        JLabel passLbl = new JLabel("Password");
        passLbl.setFont(UIUtils.FONT_BOLD);
        passLbl.setForeground(TEXT_COLOR);
        formCard.add(passLbl, gbc);

        gbc.gridy = row++;

        loginPasswordField = UIUtils.createPasswordField();
        stylePasswordField(loginPasswordField);
        formCard.add(loginPasswordField, gbc);

        // Remember Email
        gbc.gridy = row++;

        rememberEmailCheckBox = new JCheckBox("Remember Email");
        rememberEmailCheckBox.setBackground(CARD_COLOR);
        rememberEmailCheckBox.setForeground(TEXT_COLOR);
        rememberEmailCheckBox.setFont(UIUtils.FONT_REGULAR);
        rememberEmailCheckBox.setFocusPainted(false);
        rememberEmailCheckBox.setSelected(!savedEmail.isEmpty());

        formCard.add(rememberEmailCheckBox, gbc);

        // Login button
        gbc.gridy = row++;
        gbc.insets = new Insets(18, 5, 8, 5);

        loginButton = createBlueButton("Login");
        loginButton.addActionListener(e -> handleLogin());

        formCard.add(loginButton, gbc);

        // Register New Company button
        gbc.gridy = row++;
        gbc.insets = new Insets(4, 5, 4, 5);

        showRegisterButton = createBlueButton("Register New Company");
        showRegisterButton.addActionListener(
                e -> cardLayout.show(containerPanel, "REGISTER")
        );

        formCard.add(showRegisterButton, gbc);

        panel.add(formCard, BorderLayout.CENTER);

        return panel;
    }

    // ==================== REGISTRATION PANEL ====================

    private JPanel buildRegisterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);
        panel.setBorder(new EmptyBorder(15, 30, 15, 30));

        JLabel titleLabel = new JLabel(
                "Register Company",
                SwingConstants.CENTER
        );
        titleLabel.setFont(UIUtils.FONT_TITLE);
        titleLabel.setForeground(PRIMARY_BLUE);

        panel.add(titleLabel, BorderLayout.NORTH);

        // Registration card
        JPanel formCard = UIUtils.createCardPanel();
        formCard.setBackground(CARD_COLOR);
        formCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(12, 18, 12, 18)
        ));
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));

        // Input fields
        regNameField = UIUtils.createTextField();
        regEmailField = UIUtils.createTextField();
        regPasswordField = UIUtils.createPasswordField();
        regConfirmPasswordField = UIUtils.createPasswordField();
        regPhoneField = UIUtils.createTextField();
        regWebsiteField = UIUtils.createTextField();
        regAddressField = UIUtils.createTextField();

        regDescriptionArea = new JTextArea(3, 20);
        regDescriptionArea.setLineWrap(true);
        regDescriptionArea.setWrapStyleWord(true);
        regDescriptionArea.setFont(UIUtils.FONT_REGULAR);
        regDescriptionArea.setForeground(TEXT_COLOR);
        regDescriptionArea.setBackground(CARD_COLOR);
        regDescriptionArea.setBorder(new EmptyBorder(8, 8, 8, 8));

        // Apply consistent input styling
        styleTextField(regNameField);
        styleTextField(regEmailField);
        stylePasswordField(regPasswordField);
        stylePasswordField(regConfirmPasswordField);
        styleTextField(regPhoneField);
        styleTextField(regWebsiteField);
        styleTextField(regAddressField);

        // Registration fields
        formCard.add(createFieldBlock(
                "Company Name *", regNameField
        ));

        formCard.add(createFieldBlock(
                "Company Email *", regEmailField
        ));

        formCard.add(createFieldBlock(
                "Password *", regPasswordField
        ));

        formCard.add(createFieldBlock(
                "Confirm Password *", regConfirmPasswordField
        ));

        formCard.add(createFieldBlock(
                "Phone Number", regPhoneField
        ));

        formCard.add(createFieldBlock(
                "Website", regWebsiteField
        ));

        formCard.add(createFieldBlock(
                "Address", regAddressField
        ));

        formCard.add(createFieldBlock(
                "Description", new JScrollPane(regDescriptionArea)
        ));

        // Buttons
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        btnPanel.setBackground(CARD_COLOR);
        btnPanel.setBorder(new EmptyBorder(12, 0, 5, 0));

        registerButton = createBlueButton("Register");
        registerButton.addActionListener(e -> handleRegister());

        backToLoginButton = createBlueButton("Back to Login");
        backToLoginButton.addActionListener(
                e -> cardLayout.show(containerPanel, "LOGIN")
        );

        btnPanel.add(registerButton);
        btnPanel.add(backToLoginButton);

        formCard.add(btnPanel);

        JScrollPane scrollPane = new JScrollPane(formCard);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BG_COLOR);
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // ==================== UI STYLING ====================

    /**
     * Creates a consistent blue button.
     * This method changes appearance only.
     */
    private JButton createBlueButton(String text) {
        JButton button = new JButton(text);

        button.setFont(UIUtils.FONT_BOLD);
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_BLUE);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.setBorder(new EmptyBorder(12, 16, 12, 16));
        button.setPreferredSize(new Dimension(150, 42));

        // Visual hover effect only
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(HOVER_BLUE);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(PRIMARY_BLUE);
            }
        });

        return button;
    }

    private void styleTextField(JTextField field) {
        field.setFont(UIUtils.FONT_REGULAR);
        field.setForeground(TEXT_COLOR);
        field.setBackground(CARD_COLOR);
        field.setCaretColor(PRIMARY_BLUE);
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(7, 9, 7, 9)
        ));
    }

    private void stylePasswordField(JPasswordField field) {
        field.setFont(UIUtils.FONT_REGULAR);
        field.setForeground(TEXT_COLOR);
        field.setBackground(CARD_COLOR);
        field.setCaretColor(PRIMARY_BLUE);
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(7, 9, 7, 9)
        ));
    }

    private JPanel createFieldBlock(String label, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setBackground(CARD_COLOR);
        p.setBorder(new EmptyBorder(4, 0, 6, 0));

        JLabel lbl = new JLabel(label);
        lbl.setFont(UIUtils.FONT_BOLD);
        lbl.setForeground(TEXT_COLOR);

        p.add(lbl, BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);

        return p;
    }

    // ==================== LOGIN WORKFLOW ====================

    private void handleLogin() {
        String email = loginEmailField.getText().trim();
        String password =
                new String(loginPasswordField.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter both email and password.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        loginButton.setEnabled(false);
        loginButton.setText("Logging in...");

        SwingWorker<Map<String, Object>, Void> worker =
                new SwingWorker<>() {

            @Override
            protected Map<String, Object> doInBackground()
                    throws Exception {

                Map<String, String> body = new HashMap<>();
                body.put("email", email);
                body.put("password", password);

                return ApiClient.post(
                        "/api/companies/login",
                        body,
                        new TypeReference<Map<String, Object>>() {}
                );
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
                        Map<String, Object> data =
                                (Map<String, Object>) resp.get("data");

                        String token = (String) data.get("token");
                        ApiClient.setAuthToken(token);

                        if (rememberEmailCheckBox.isSelected()) {
                            CredentialStorage.saveEmail(email);
                        } else {
                            CredentialStorage.clearEmail();
                        }

                        // Open Dashboard
                        dispose();

                        SwingUtilities.invokeLater(
                                () -> new CompanyDashboardFrame()
                                        .setVisible(true)
                        );

                    } else {
                        String msg = (String) resp.get("message");

                        JOptionPane.showMessageDialog(
                                CompanyLoginFrame.this,
                                msg != null ? msg : "Login failed.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            CompanyLoginFrame.this,
                            ex.getMessage(),
                            "Connection Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };

        worker.execute();
    }

    // ==================== REGISTRATION WORKFLOW ====================

    private void handleRegister() {
        String name = regNameField.getText().trim();
        String email = regEmailField.getText().trim();
        String password =
                new String(regPasswordField.getPassword()).trim();
        String confirm =
                new String(regConfirmPasswordField.getPassword()).trim();
        String phone = regPhoneField.getText().trim();
        String website = regWebsiteField.getText().trim();
        String address = regAddressField.getText().trim();
        String desc = regDescriptionArea.getText().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Company Name, Email, and Password are required.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid email address.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Passwords do not match.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        registerButton.setEnabled(false);
        registerButton.setText("Registering...");

        SwingWorker<Map<String, Object>, Void> worker =
                new SwingWorker<>() {

            @Override
            protected Map<String, Object> doInBackground()
                    throws Exception {

                Map<String, String> body = new HashMap<>();
                body.put("companyName", name);
                body.put("email", email);
                body.put("password", password);
                body.put("phone", phone);
                body.put("website", website);
                body.put("address", address);
                body.put("description", desc);

                return ApiClient.post(
                        "/api/companies/register",
                        body,
                        new TypeReference<Map<String, Object>>() {}
                );
            }

            @Override
            protected void done() {
                registerButton.setEnabled(true);
                registerButton.setText("Register");

                try {
                    Map<String, Object> resp = get();
                    Boolean success = (Boolean) resp.get("success");

                    if (Boolean.TRUE.equals(success)) {
                        JOptionPane.showMessageDialog(
                                CompanyLoginFrame.this,
                                "Company registered successfully! Please log in.",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                        loginEmailField.setText(email);
                        loginPasswordField.setText("");

                        cardLayout.show(containerPanel, "LOGIN");

                    } else {
                        String msg = (String) resp.get("message");

                        JOptionPane.showMessageDialog(
                                CompanyLoginFrame.this,
                                msg != null ? msg : "Registration failed.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            CompanyLoginFrame.this,
                            ex.getMessage(),
                            "Registration Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };

        worker.execute();
    }
}