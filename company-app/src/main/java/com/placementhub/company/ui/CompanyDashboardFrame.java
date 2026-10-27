package com.placementhub.company.ui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.placementhub.company.model.ApplicationItem;
import com.placementhub.company.model.CompanyProfile;
import com.placementhub.company.model.JobItem;
import com.placementhub.company.util.ApiClient;
import com.placementhub.company.util.UIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Placement Hub Company Dashboard.
 * Light-mode UI styling only.
 * Existing navigation, API calls, and business logic are preserved.
 */
public class CompanyDashboardFrame extends JFrame {

    // Light-mode color palette
    private static final Color LIGHT_BG = new Color(245, 247, 251);
    private static final Color WHITE = Color.WHITE;
    private static final Color NAVY = new Color(28, 43, 68);
    private static final Color ACCENT_BLUE = new Color(43, 91, 171);
    private static final Color MAIN_TEXT = new Color(35, 48, 67);
    private static final Color MUTED_TEXT = new Color(105, 119, 139);
    private static final Color BORDER_COLOR = new Color(222, 228, 237);
    private static final Color SUCCESS_GREEN = new Color(35, 125, 89);
    private static final Color ERROR_RED = new Color(190, 62, 70);

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);

    // Sidebar navigation buttons
    private JButton btnNavHome;
    private JButton btnNavPostJob;
    private JButton btnNavMyJobs;
    private JButton btnNavApplications;
    private JButton btnNavAccount;

    // Home components
    private JLabel homeWelcomeLabel;
    private JLabel homeSubLabel;

    // Post Job components
    private JTextField postTitleField;
    private JTextArea postDescArea;
    private JTextField postSkillsField;
    private JTextField postSalaryField;
    private JTextField postLocationField;
    private JComboBox<String> postTypeCombo;
    private JTextField postDeadlineField;

    // My Jobs components
    private JTable myJobsTable;
    private DefaultTableModel myJobsTableModel;
    private List<JobItem> currentCompanyJobs;

    // Applications components
    private JTable applicationsTable;
    private DefaultTableModel applicationsTableModel;
    private List<ApplicationItem> currentApplications;

    // Account components
    private JTextField accNameField;
    private JTextField accEmailField;
    private JTextField accPhoneField;
    private JTextField accWebsiteField;
    private JTextField accAddressField;
    private JTextArea accDescArea;

    private CompanyProfile currentProfile;

    public CompanyDashboardFrame() {
        setTitle("Placement Hub - Company Management Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1080, 700);
        setLocationRelativeTo(null);

        initUI();
        loadProfileAndInit();
    }

    private void initUI() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(LIGHT_BG);

        JPanel sidebar = buildSidebar();
        getContentPane().add(sidebar, BorderLayout.WEST);

        contentPanel.setBackground(LIGHT_BG);
        contentPanel.add(buildHomePanel(), "HOME");
        contentPanel.add(buildPostJobPanel(), "POST_JOB");
        contentPanel.add(buildMyJobsPanel(), "MY_JOBS");
        contentPanel.add(buildApplicationsPanel(), "APPLICATIONS");
        contentPanel.add(buildAccountPanel(), "ACCOUNT");

        getContentPane().add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(NAVY);
        sidebar.setLayout(new BorderLayout());

        JPanel brandPanel = new JPanel(new GridLayout(2, 1));
        brandPanel.setBackground(NAVY);
        brandPanel.setBorder(new EmptyBorder(25, 20, 20, 20));

        JLabel logoLbl = new JLabel("PLACEMENT HUB");
        logoLbl.setFont(UIUtils.FONT_TITLE);
        logoLbl.setForeground(WHITE);

        JLabel subLbl = new JLabel("Company Console");
        subLbl.setFont(UIUtils.FONT_SMALL);
        subLbl.setForeground(new Color(176, 196, 222));

        brandPanel.add(logoLbl);
        brandPanel.add(subLbl);
        sidebar.add(brandPanel, BorderLayout.NORTH);

        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(NAVY);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        btnNavHome = createSidebarNavButton("1. Home");
        btnNavPostJob = createSidebarNavButton("2. Post Job");
        btnNavMyJobs = createSidebarNavButton("3. My Jobs");
        btnNavApplications = createSidebarNavButton("4. Applications");
        btnNavAccount = createSidebarNavButton("5. Account");

        JButton btnLogout = createSidebarNavButton("6. Logout");

        btnNavHome.addActionListener(
                e -> switchTab("HOME", btnNavHome)
        );

        btnNavPostJob.addActionListener(
                e -> switchTab("POST_JOB", btnNavPostJob)
        );

        btnNavMyJobs.addActionListener(e -> {
            switchTab("MY_JOBS", btnNavMyJobs);
            fetchCompanyJobs();
        });

        btnNavApplications.addActionListener(e -> {
            switchTab("APPLICATIONS", btnNavApplications);
            fetchApplications();
        });

        btnNavAccount.addActionListener(e -> {
            switchTab("ACCOUNT", btnNavAccount);
            populateAccountForm();
        });

        btnLogout.addActionListener(e -> handleLogout());

        menuPanel.add(btnNavHome);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(btnNavPostJob);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(btnNavMyJobs);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(btnNavApplications);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(btnNavAccount);
        currentActiveNavButton = btnNavHome;
        btnNavHome.setForeground(WHITE);
        btnNavHome.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 3, 0, 0, ACCENT_BLUE),
                new EmptyBorder(10, 15, 10, 15)
        ));

        sidebar.add(menuPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(NAVY);
        bottomPanel.setBorder(new EmptyBorder(10, 10, 20, 10));
        bottomPanel.add(btnLogout, BorderLayout.CENTER);
        sidebar.add(bottomPanel, BorderLayout.SOUTH);

        return sidebar;
    }

    private JButton currentActiveNavButton;

    private JButton createSidebarNavButton(String title) {
        JButton btn = new JButton(title);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btn.setFont(UIUtils.FONT_BOLD);
        btn.setForeground(new Color(208, 224, 245));
        btn.setBackground(NAVY);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 3, 0, 0, NAVY),
                new EmptyBorder(10, 15, 10, 15)
        ));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (btn != currentActiveNavButton) {
                    btn.setForeground(WHITE);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (btn != currentActiveNavButton) {
                    btn.setForeground(new Color(208, 224, 245));
                }
            }
        });

        return btn;
    }

    private void switchTab(String cardName, JButton activeButton) {
        cardLayout.show(contentPanel, cardName);
        currentActiveNavButton = activeButton;

        JButton[] buttons = {
                btnNavHome,
                btnNavPostJob,
                btnNavMyJobs,
                btnNavApplications,
                btnNavAccount
        };

        for (JButton b : buttons) {
            b.setOpaque(false);
            b.setContentAreaFilled(false);
            b.setForeground(new Color(208, 224, 245));
            b.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 3, 0, 0, NAVY),
                    new EmptyBorder(10, 15, 10, 15)
            ));
        }

        activeButton.setOpaque(false);
        activeButton.setContentAreaFilled(false);
        activeButton.setForeground(WHITE);
        activeButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 3, 0, 0, ACCENT_BLUE),
                new EmptyBorder(10, 15, 10, 15)
        ));
    }

    private JPanel buildHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_BG);
        panel.setBorder(new EmptyBorder(30, 35, 30, 35));

 JPanel card = UIUtils.createCardPanel();
card.setBackground(LIGHT_BG);
card.setOpaque(true);
card.setBorder(new EmptyBorder(15, 15, 15, 15));
card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        homeWelcomeLabel = new JLabel("Welcome to Placement Hub");
        homeWelcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        homeWelcomeLabel.setForeground(NAVY);

        homeSubLabel = new JLabel(
                "Manage your recruitment process smoothly from this unified console."
        );
        homeSubLabel.setFont(UIUtils.FONT_REGULAR);
        homeSubLabel.setForeground(MUTED_TEXT);

        card.add(homeWelcomeLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(homeSubLabel);
        card.add(Box.createVerticalStrut(25));

        JLabel info = new JLabel(
                "<html><b>Quick Actions:</b><br/>" +
                "• <b>Post Job:</b> Create and publish new career opportunities for students.<br/>" +
                "• <b>My Jobs:</b> View posted vacancies and cancel inactive job positions.<br/>" +
                "• <b>Applications:</b> Review candidates, shortlist, reject, or schedule interviews.<br/>" +
                "• <b>Account:</b> Keep your corporate contact information up to date.</html>"
        );

        info.setFont(UIUtils.FONT_REGULAR);
        info.setForeground(MAIN_TEXT);
        card.add(info);

        panel.add(card, BorderLayout.NORTH);
        return panel;
    }

    private JPanel buildPostJobPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_BG);
        panel.setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel titleLbl = new JLabel("Post New Job Opportunity");
        titleLbl.setFont(UIUtils.FONT_TITLE);
        titleLbl.setForeground(NAVY);
        panel.add(titleLbl, BorderLayout.NORTH);

        JPanel card = UIUtils.createCardPanel();
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        postTitleField = UIUtils.createTextField();

        postDescArea = new JTextArea(4, 20);
        postDescArea.setLineWrap(true);
        postDescArea.setWrapStyleWord(true);
        postDescArea.setFont(UIUtils.FONT_REGULAR);
        postDescArea.setForeground(MAIN_TEXT);
        postDescArea.setBackground(WHITE);
        postDescArea.setBorder(new EmptyBorder(8, 8, 8, 8));

        postSkillsField = UIUtils.createTextField();
        postSalaryField = UIUtils.createTextField();
        postLocationField = UIUtils.createTextField();

        postTypeCombo = new JComboBox<>(
                new String[]{"Full Time", "Part Time", "Internship", "Contract"}
        );
        postTypeCombo.setFont(UIUtils.FONT_REGULAR);
        postTypeCombo.setForeground(MAIN_TEXT);
        postTypeCombo.setBackground(WHITE);

        postDeadlineField = UIUtils.createTextField();

        card.add(createFormRow("Job Title *", postTitleField));
        card.add(createFormRow(
                "Job Description *",
                new JScrollPane(postDescArea)
        ));
        card.add(createFormRow(
                "Required Skills * (e.g., Java, Spring Boot, MySQL)",
                postSkillsField
        ));
        card.add(createFormRow(
                "Salary / Package (e.g., $80,000 / yr or 6-8 LPA)",
                postSalaryField
        ));
        card.add(createFormRow(
                "Location (e.g., Remote / New York, NY / Bangalore)",
                postLocationField
        ));
        card.add(createFormRow("Job Type", postTypeCombo));
        card.add(createFormRow(
                "Application Deadline (e.g., 2026-12-31)",
                postDeadlineField
        ));

        JPanel btnPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 10)
        );
        btnPanel.setBackground(WHITE);

        JButton postBtn = UIUtils.createPrimaryButton("Post Job");
        postBtn.addActionListener(e -> handlePostJob());

        JButton clearBtn = UIUtils.createSecondaryButton("Clear Form");
        clearBtn.addActionListener(e -> clearPostJobForm());

        btnPanel.add(postBtn);
        btnPanel.add(clearBtn);
        card.add(btnPanel);

        JScrollPane scroll = new JScrollPane(card);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(LIGHT_BG);
        scroll.getVerticalScrollBar().setUnitIncrement(12);

        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFormRow(String label, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setBackground(LIGHT_BG);
        p.setBorder(new EmptyBorder(8, 5, 8, 5));

        JLabel lbl = new JLabel(label);
        lbl.setFont(UIUtils.FONT_BOLD);
        lbl.setForeground(MAIN_TEXT);

        p.add(lbl, BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);

        return p;
    }

    private void clearPostJobForm() {
        postTitleField.setText("");
        postDescArea.setText("");
        postSkillsField.setText("");
        postSalaryField.setText("");
        postLocationField.setText("");
        postDeadlineField.setText("");
    }
        private void handlePostJob() {
        String title = postTitleField.getText().trim();
        String desc = postDescArea.getText().trim();
        String skills = postSkillsField.getText().trim();
        String salary = postSalaryField.getText().trim();
        String location = postLocationField.getText().trim();
        String type = (String) postTypeCombo.getSelectedItem();
        String deadline = postDeadlineField.getText().trim();

        if (title.isEmpty() || desc.isEmpty() || skills.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Title, Description, and Required Skills are required.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Map<String, String> body = new HashMap<>();
        body.put("jobTitle", title);
        body.put("jobDescription", desc);
        body.put("requiredSkills", skills);
        body.put("salary", salary);
        body.put("jobLocation", location);
        body.put("jobType", type);
        body.put("applicationDeadline", deadline);

        SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, Object> doInBackground() throws Exception {
                return ApiClient.post(
                        "/api/companies/jobs",
                        body,
                        new TypeReference<Map<String, Object>>() {}
                );
            }

            @Override
            protected void done() {
                try {
                    Map<String, Object> resp = get();
                    Boolean success = (Boolean) resp.get("success");

                    if (Boolean.TRUE.equals(success)) {
                        JOptionPane.showMessageDialog(
                                CompanyDashboardFrame.this,
                                "Job posted successfully!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                        clearPostJobForm();
                        switchTab("MY_JOBS", btnNavMyJobs);
                        fetchCompanyJobs();
                    } else {
                        JOptionPane.showMessageDialog(
                                CompanyDashboardFrame.this,
                                (String) resp.get("message"),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            CompanyDashboardFrame.this,
                            ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };

        worker.execute();
    }

    private JPanel buildMyJobsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(LIGHT_BG);
        panel.setBorder(new EmptyBorder(25, 30, 25, 30));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(LIGHT_BG);

        JLabel titleLbl = new JLabel("My Posted Jobs");
        titleLbl.setFont(UIUtils.FONT_TITLE);
        titleLbl.setForeground(NAVY);
        topBar.add(titleLbl, BorderLayout.WEST);

        JPanel actionBtns = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 10, 0)
        );
        actionBtns.setBackground(LIGHT_BG);

        JButton refreshBtn = UIUtils.createSecondaryButton("Refresh");
        refreshBtn.addActionListener(e -> fetchCompanyJobs());

        JButton cancelJobBtn = UIUtils.createDangerButton("Cancel Job");
        cancelJobBtn.addActionListener(e -> handleCancelJob());

        actionBtns.add(refreshBtn);
        actionBtns.add(cancelJobBtn);
        topBar.add(actionBtns, BorderLayout.EAST);

        panel.add(topBar, BorderLayout.NORTH);

        String[] columns = {
                "ID", "Job Title", "Skills", "Salary",
                "Location", "Type", "Deadline", "Status"
        };

        myJobsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        myJobsTable = new JTable(myJobsTableModel);
        myJobsTable.setFont(UIUtils.FONT_REGULAR);
        myJobsTable.setForeground(MAIN_TEXT);
        myJobsTable.setBackground(WHITE);
        myJobsTable.setGridColor(BORDER_COLOR);
        myJobsTable.setRowHeight(34);
        myJobsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        myJobsTable.setSelectionBackground(new Color(225, 236, 253));
        myJobsTable.setSelectionForeground(NAVY);
        myJobsTable.setShowVerticalLines(false);
        myJobsTable.setIntercellSpacing(new Dimension(0, 1));

        myJobsTable.getTableHeader().setFont(UIUtils.FONT_BOLD);
        myJobsTable.getTableHeader().setBackground(new Color(237, 242, 249));
        myJobsTable.getTableHeader().setForeground(NAVY);
        myJobsTable.getTableHeader().setBorder(
                new LineBorder(BORDER_COLOR, 1)
        );
        myJobsTable.getTableHeader().setReorderingAllowed(false);

        myJobsTable.setDefaultRenderer(Object.class,
                new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(
                            JTable table,
                            Object value,
                            boolean isSelected,
                            boolean hasFocus,
                            int row,
                            int col) {

                        Component c = super.getTableCellRendererComponent(
                                table, value, isSelected, hasFocus, row, col
                        );

                        if (!isSelected) {
                            c.setBackground(WHITE);
                            c.setForeground(MAIN_TEXT);

                            Object statusValue =
                                    table.getModel().getValueAt(row, 7);

                            if (statusValue != null
                                    && "CANCELLED".equalsIgnoreCase(
                                            statusValue.toString())) {
                                c.setForeground(ERROR_RED);
                            }
                        } else {
                            c.setBackground(new Color(225, 236, 253));
                            c.setForeground(NAVY);
                        }

                        if (c instanceof JLabel) {
                            ((JLabel) c).setBorder(
                                    new EmptyBorder(0, 8, 0, 8)
                            );
                        }

                        return c;
                    }
                });

        JScrollPane scroll = new JScrollPane(myJobsTable);
        scroll.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        scroll.getViewport().setBackground(WHITE);
        scroll.getVerticalScrollBar().setUnitIncrement(12);

        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private void fetchCompanyJobs() {
        SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, Object> doInBackground() throws Exception {
                return ApiClient.get(
                        "/api/companies/jobs",
                        new TypeReference<Map<String, Object>>() {}
                );
            }

            @Override
            protected void done() {
                try {
                    Map<String, Object> resp = get();
                    Boolean success = (Boolean) resp.get("success");

                    if (Boolean.TRUE.equals(success)) {
                        @SuppressWarnings("unchecked")
                        List<Map<String, Object>> data =
                                (List<Map<String, Object>>) resp.get("data");

                        myJobsTableModel.setRowCount(0);

                        if (data != null) {
                            for (Map<String, Object> j : data) {
                                boolean isActive =
                                        Boolean.TRUE.equals(j.get("isActive"));

                                myJobsTableModel.addRow(new Object[]{
                                        j.get("jobId"),
                                        j.get("jobTitle"),
                                        j.get("requiredSkills"),
                                        j.get("salary"),
                                        j.get("jobLocation"),
                                        j.get("jobType"),
                                        j.get("applicationDeadline"),
                                        isActive ? "ACTIVE" : "CANCELLED"
                                });
                            }
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            CompanyDashboardFrame.this,
                            ex.getMessage(),
                            "Error Fetching Jobs",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };

        worker.execute();
    }

    private void handleCancelJob() {
        int selectedRow = myJobsTable.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a job from the table to cancel.",
                    "Notice",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        Long jobId = Long.valueOf(
                myJobsTableModel.getValueAt(selectedRow, 0).toString()
        );

        String currentStatus =
                (String) myJobsTableModel.getValueAt(selectedRow, 7);

        if ("CANCELLED".equalsIgnoreCase(currentStatus)) {
            JOptionPane.showMessageDialog(
                    this,
                    "This job is already cancelled.",
                    "Notice",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to cancel this job?\n"
                        + "Students will no longer see or apply for it. "
                        + "Existing applicants are preserved.",
                "Confirm Job Cancellation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            SwingWorker<Map<String, Object>, Void> worker =
                    new SwingWorker<>() {

                @Override
                protected Map<String, Object> doInBackground()
                        throws Exception {
                    return ApiClient.patch(
                            "/api/companies/jobs/" + jobId + "/cancel",
                            null,
                            new TypeReference<Map<String, Object>>() {}
                    );
                }

                @Override
                protected void done() {
                    try {
                        Map<String, Object> resp = get();

                        if (Boolean.TRUE.equals(resp.get("success"))) {
                            JOptionPane.showMessageDialog(
                                    CompanyDashboardFrame.this,
                                    "Job cancelled successfully.",
                                    "Success",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                            fetchCompanyJobs();
                        } else {
                            JOptionPane.showMessageDialog(
                                    CompanyDashboardFrame.this,
                                    (String) resp.get("message"),
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(
                                CompanyDashboardFrame.this,
                                ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            };

            worker.execute();
        }
    }
        private JPanel buildApplicationsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(LIGHT_BG);
        panel.setBorder(new EmptyBorder(25, 30, 25, 30));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(LIGHT_BG);

        JLabel titleLbl = new JLabel("Student Applications");
        titleLbl.setFont(UIUtils.FONT_TITLE);
        titleLbl.setForeground(NAVY);
        topBar.add(titleLbl, BorderLayout.WEST);

        JPanel actionBtns = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 8, 0)
        );
        actionBtns.setBackground(LIGHT_BG);

        JButton refreshBtn = UIUtils.createSecondaryButton("Refresh");
        refreshBtn.addActionListener(e -> fetchApplications());

        JButton viewBtn = UIUtils.createSecondaryButton("View Details");
        viewBtn.addActionListener(e -> handleViewApplicationDetails());

        JButton acceptBtn = UIUtils.createActionButton("Accept / Shortlist");
        acceptBtn.addActionListener(e -> updateSelectedStatus("SHORTLISTED"));

        JButton rejectBtn = UIUtils.createDangerButton("Reject");
        rejectBtn.addActionListener(e -> handleRejectApplication());

        JButton interviewBtn =
                UIUtils.createPrimaryButton("Schedule Interview");
        interviewBtn.addActionListener(
                e -> handleScheduleInterviewDialog()
        );

        actionBtns.add(refreshBtn);
        actionBtns.add(viewBtn);
        actionBtns.add(acceptBtn);
        actionBtns.add(rejectBtn);
        actionBtns.add(interviewBtn);

        topBar.add(actionBtns, BorderLayout.EAST);
        panel.add(topBar, BorderLayout.NORTH);

        String[] columns = {
                "App ID", "Student Name", "Email", "Phone",
                "Job Title", "Applied Date", "Status"
        };

        applicationsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        applicationsTable = new JTable(applicationsTableModel);
        applicationsTable.setFont(UIUtils.FONT_REGULAR);
        applicationsTable.setForeground(MAIN_TEXT);
        applicationsTable.setBackground(WHITE);
        applicationsTable.setGridColor(BORDER_COLOR);
        applicationsTable.setRowHeight(34);
        applicationsTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );
        applicationsTable.setSelectionBackground(
                new Color(225, 236, 253)
        );
        applicationsTable.setSelectionForeground(NAVY);
        applicationsTable.setShowVerticalLines(false);
        applicationsTable.setIntercellSpacing(new Dimension(0, 1));

        applicationsTable.getTableHeader().setFont(UIUtils.FONT_BOLD);
        applicationsTable.getTableHeader().setBackground(
                new Color(237, 242, 249)
        );
        applicationsTable.getTableHeader().setForeground(NAVY);
        applicationsTable.getTableHeader().setBorder(
                new LineBorder(BORDER_COLOR, 1)
        );
        applicationsTable.getTableHeader().setReorderingAllowed(false);

        applicationsTable.setDefaultRenderer(
                Object.class,
                new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(
                            JTable table,
                            Object value,
                            boolean isSelected,
                            boolean hasFocus,
                            int row,
                            int col) {

                        Component c =
                                super.getTableCellRendererComponent(
                                        table, value, isSelected,
                                        hasFocus, row, col
                                );

                        if (isSelected) {
                            c.setBackground(new Color(225, 236, 253));
                            c.setForeground(NAVY);
                        } else {
                            c.setBackground(WHITE);
                            c.setForeground(MAIN_TEXT);

                            if (col == 6 && value != null) {
                                String status = value.toString();

                                if ("SHORTLISTED".equalsIgnoreCase(status)) {
                                    c.setForeground(SUCCESS_GREEN);
                                } else if ("REJECTED".equalsIgnoreCase(status)) {
                                    c.setForeground(ERROR_RED);
                                } else if (
                                        "INTERVIEW_SCHEDULED"
                                                .equalsIgnoreCase(status)) {
                                    c.setForeground(ACCENT_BLUE);
                                }
                            }
                        }

                        if (c instanceof JLabel) {
                            ((JLabel) c).setBorder(
                                    new EmptyBorder(0, 8, 0, 8)
                            );
                        }

                        return c;
                    }
                }
        );

        JScrollPane scroll = new JScrollPane(applicationsTable);
        scroll.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        scroll.getViewport().setBackground(WHITE);
        scroll.getVerticalScrollBar().setUnitIncrement(12);

        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private void fetchApplications() {
        SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, Object> doInBackground() throws Exception {
                return ApiClient.get(
                        "/api/companies/applications",
                        new TypeReference<Map<String, Object>>() {}
                );
            }

            @Override
            protected void done() {
                try {
                    Map<String, Object> resp = get();

                    if (Boolean.TRUE.equals(resp.get("success"))) {
                        @SuppressWarnings("unchecked")
                        List<Map<String, Object>> data =
                                (List<Map<String, Object>>) resp.get("data");

                        applicationsTableModel.setRowCount(0);

                        if (data != null) {
                            for (Map<String, Object> app : data) {
                                applicationsTableModel.addRow(new Object[]{
                                        app.get("applicationId"),
                                        app.get("studentName"),
                                        app.get("studentEmail"),
                                        app.get("studentPhone"),
                                        app.get("jobTitle"),
                                        app.get("appliedAt"),
                                        app.get("status")
                                });
                            }
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            CompanyDashboardFrame.this,
                            ex.getMessage(),
                            "Error Fetching Applications",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };

        worker.execute();
    }

    private void handleViewApplicationDetails() {
        int row = applicationsTable.getSelectedRow();

        if (row < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Select an application to view details.",
                    "Notice",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        Long appId = Long.valueOf(
                applicationsTableModel.getValueAt(row, 0).toString()
        );

        SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, Object> doInBackground() throws Exception {
                return ApiClient.get(
                        "/api/applications/" + appId,
                        new TypeReference<Map<String, Object>>() {}
                );
            }

            @Override
            protected void done() {
                try {
                    Map<String, Object> resp = get();

                    @SuppressWarnings("unchecked")
                    Map<String, Object> app =
                            (Map<String, Object>) resp.get("data");

                    String msg = String.format(
                            "Student: %s\nEmail: %s\nPhone: %s\n"
                                    + "Department: %s\nGraduation Year: %s\n\n"
                                    + "Job: %s\nStatus: %s\n\n"
                                    + "Interview Date: %s\nInterview Time: %s\n"
                                    + "Interview Mode: %s\nLocation / Link: %s\n"
                                    + "Feedback: %s",
                            app.get("studentName"),
                            app.get("studentEmail"),
                            app.get("studentPhone"),
                            app.get("department"),
                            app.get("graduationYear"),
                            app.get("jobTitle"),
                            app.get("status"),
                            app.get("interviewDate") != null
                                    ? app.get("interviewDate") : "N/A",
                            app.get("interviewTime") != null
                                    ? app.get("interviewTime") : "N/A",
                            app.get("interviewMode") != null
                                    ? app.get("interviewMode") : "N/A",
                            app.get("interviewLocationOrLink") != null
                                    ? app.get("interviewLocationOrLink") : "N/A",
                            app.get("companyFeedback") != null
                                    ? app.get("companyFeedback") : "N/A"
                    );

                    JOptionPane.showMessageDialog(
                            CompanyDashboardFrame.this,
                            msg,
                            "Application Details",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            CompanyDashboardFrame.this,
                            ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };

        worker.execute();
    }

    private void updateSelectedStatus(String status) {
        int row = applicationsTable.getSelectedRow();

        if (row < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Select an application first.",
                    "Notice",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        Long appId = Long.valueOf(
                applicationsTableModel.getValueAt(row, 0).toString()
        );

        Map<String, String> body = Map.of("status", status);

        SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, Object> doInBackground() throws Exception {
                return ApiClient.patch(
                        "/api/companies/applications/" + appId + "/status",
                        body,
                        new TypeReference<Map<String, Object>>() {}
                );
            }

            @Override
            protected void done() {
                try {
                    Map<String, Object> resp = get();

                    if (Boolean.TRUE.equals(resp.get("success"))) {
                        JOptionPane.showMessageDialog(
                                CompanyDashboardFrame.this,
                                "Status updated to " + status,
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                        fetchApplications();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            CompanyDashboardFrame.this,
                            ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };

        worker.execute();
    }

    private void handleRejectApplication() {
        int row = applicationsTable.getSelectedRow();

        if (row < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Select an application first.",
                    "Notice",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to reject this applicant?",
                "Confirm Rejection",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            updateSelectedStatus("REJECTED");
        }
    }

    private void handleScheduleInterviewDialog() {
        int row = applicationsTable.getSelectedRow();

        if (row < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Select an application first.",
                    "Notice",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        Long appId = Long.valueOf(
                applicationsTableModel.getValueAt(row, 0).toString()
        );

        JDialog dialog = new JDialog(this, "Schedule Interview", true);
        dialog.setSize(440, 420);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBackground(LIGHT_BG);
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JTextField dateField = UIUtils.createTextField();
        JTextField timeField = UIUtils.createTextField();

        JComboBox<String> modeCombo =
                new JComboBox<>(new String[]{"Online", "Offline"});

        JTextField linkField = UIUtils.createTextField();
        JTextField feedbackField = UIUtils.createTextField();

        panel.add(createMiniRow(
                "Interview Date (YYYY-MM-DD) *", dateField
        ));
        panel.add(createMiniRow(
                "Interview Time (e.g., 10:30 AM) *", timeField
        ));
        panel.add(createMiniRow("Interview Mode *", modeCombo));
        panel.add(createMiniRow(
                "Meeting Link / Room Location *", linkField
        ));
        panel.add(createMiniRow(
                "Instructions / Feedback", feedbackField
        ));

        JPanel btnPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT)
        );
        btnPanel.setBackground(LIGHT_BG);

        JButton submitBtn =
                UIUtils.createPrimaryButton("Confirm Interview");

        submitBtn.addActionListener(e -> {
            String d = dateField.getText().trim();
            String t = timeField.getText().trim();
            String m = (String) modeCombo.getSelectedItem();
            String l = linkField.getText().trim();
            String fb = feedbackField.getText().trim();

            if (d.isEmpty() || t.isEmpty() || l.isEmpty()) {
                JOptionPane.showMessageDialog(
                        dialog,
                        "Date, Time, and Location/Link are required.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            Map<String, String> body = new HashMap<>();
            body.put("interviewDate", d);
            body.put("interviewTime", t);
            body.put("interviewMode", m);
            body.put("interviewLocationOrLink", l);
            body.put("companyFeedback", fb);

            SwingWorker<Map<String, Object>, Void> worker =
                    new SwingWorker<>() {
                @Override
                protected Map<String, Object> doInBackground()
                        throws Exception {
                    return ApiClient.post(
                            "/api/companies/applications/" + appId
                                    + "/interview",
                            body,
                            new TypeReference<Map<String, Object>>() {}
                    );
                }

                @Override
                protected void done() {
                    try {
                        Map<String, Object> resp = get();

                        if (Boolean.TRUE.equals(resp.get("success"))) {
                            dialog.dispose();

                            JOptionPane.showMessageDialog(
                                    CompanyDashboardFrame.this,
                                    "Interview scheduled successfully!",
                                    "Success",
                                    JOptionPane.INFORMATION_MESSAGE
                            );

                            fetchApplications();
                        } else {
                            JOptionPane.showMessageDialog(
                                    dialog,
                                    (String) resp.get("message"),
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(
                                dialog,
                                ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            };

            worker.execute();
        });

        JButton cancelBtn = UIUtils.createSecondaryButton("Cancel");
        cancelBtn.addActionListener(e -> dialog.dispose());

        btnPanel.add(submitBtn);
        btnPanel.add(cancelBtn);
        panel.add(btnPanel);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private JPanel createMiniRow(String label, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout(0, 2));
        p.setBackground(LIGHT_BG);

        JLabel lbl = new JLabel(label);
        lbl.setFont(UIUtils.FONT_BOLD);
        lbl.setForeground(MAIN_TEXT);

        p.add(lbl, BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);

        return p;
    }
        private JPanel buildAccountPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_BG);
        panel.setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel titleLbl = new JLabel("Company Profile");
        titleLbl.setFont(UIUtils.FONT_TITLE);
        titleLbl.setForeground(NAVY);
        panel.add(titleLbl, BorderLayout.NORTH);

        JPanel card = UIUtils.createCardPanel();
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        accNameField = UIUtils.createTextField();
        accEmailField = UIUtils.createTextField();
        accEmailField.setEditable(false);

        accPhoneField = UIUtils.createTextField();
        accWebsiteField = UIUtils.createTextField();
        accAddressField = UIUtils.createTextField();

        accDescArea = new JTextArea(3, 20);
        accDescArea.setFont(UIUtils.FONT_REGULAR);
        accDescArea.setForeground(MAIN_TEXT);
        accDescArea.setBackground(WHITE);
        accDescArea.setLineWrap(true);
        accDescArea.setWrapStyleWord(true);
        accDescArea.setBorder(new EmptyBorder(8, 8, 8, 8));

        card.add(createFormRow("Company Name", accNameField));
        card.add(createFormRow(
                "Registered Email (Cannot be changed)",
                accEmailField
        ));
        card.add(createFormRow("Phone", accPhoneField));
        card.add(createFormRow("Website", accWebsiteField));
        card.add(createFormRow("Address", accAddressField));
        card.add(createFormRow(
                "Description",
                new JScrollPane(accDescArea)
        ));

        JPanel btnPnl = new JPanel(
                new FlowLayout(FlowLayout.LEFT)
        );
        btnPnl.setBackground(WHITE);

       btnPnl.setBackground(LIGHT_BG);

JButton saveBtn = new JButton("Save Changes");
saveBtn.setFont(UIUtils.FONT_BOLD);
saveBtn.setForeground(Color.WHITE);
saveBtn.setBackground(ACCENT_BLUE);
saveBtn.setOpaque(true);
saveBtn.setBorderPainted(false);
saveBtn.setFocusPainted(false);
saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
saveBtn.setBorder(new EmptyBorder(10, 18, 10, 18));
saveBtn.addActionListener(e -> handleUpdateProfile());
        btnPnl.add(saveBtn);
        card.add(btnPnl);

        JScrollPane scroll = new JScrollPane(card);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(LIGHT_BG);
        scroll.getVerticalScrollBar().setUnitIncrement(12);

        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private void loadProfileAndInit() {
        SwingWorker<Map<String, Object>, Void> worker =
                new SwingWorker<>() {

            @Override
            protected Map<String, Object> doInBackground()
                    throws Exception {
                return ApiClient.get(
                        "/api/companies/me",
                        new TypeReference<Map<String, Object>>() {}
                );
            }

            @Override
            protected void done() {
                try {
                    Map<String, Object> resp = get();

                    if (Boolean.TRUE.equals(resp.get("success"))) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> d =
                                (Map<String, Object>) resp.get("data");

                        homeWelcomeLabel.setText(
                                "Welcome back, " + d.get("companyName") + "!"
                        );
                        homeSubLabel.setText(
                                "Email: " + d.get("email")
                        );

                        currentProfile = new CompanyProfile();
                        currentProfile.setId(
                                Long.valueOf(d.get("id").toString())
                        );
                        currentProfile.setCompanyName(
                                (String) d.get("companyName")
                        );
                        currentProfile.setEmail(
                                (String) d.get("email")
                        );
                        currentProfile.setPhone(
                                (String) d.get("phone")
                        );
                        currentProfile.setWebsite(
                                (String) d.get("website")
                        );
                        currentProfile.setAddress(
                                (String) d.get("address")
                        );
                        currentProfile.setDescription(
                                (String) d.get("description")
                        );
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            CompanyDashboardFrame.this,
                            ex.getMessage(),
                            "Error Loading Profile",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };

        worker.execute();
    }

    private void populateAccountForm() {
        if (currentProfile != null) {
            accNameField.setText(currentProfile.getCompanyName());
            accEmailField.setText(currentProfile.getEmail());
            accPhoneField.setText(currentProfile.getPhone());
            accWebsiteField.setText(currentProfile.getWebsite());
            accAddressField.setText(currentProfile.getAddress());
            accDescArea.setText(currentProfile.getDescription());
        }
    }

    private void handleUpdateProfile() {
        String name = accNameField.getText().trim();
        String phone = accPhoneField.getText().trim();
        String web = accWebsiteField.getText().trim();
        String addr = accAddressField.getText().trim();
        String desc = accDescArea.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Company name cannot be blank.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Map<String, String> body = new HashMap<>();
        body.put("companyName", name);
        body.put("phone", phone);
        body.put("website", web);
        body.put("address", addr);
        body.put("description", desc);

        SwingWorker<Map<String, Object>, Void> worker =
                new SwingWorker<>() {

            @Override
            protected Map<String, Object> doInBackground()
                    throws Exception {
                return ApiClient.put(
                        "/api/companies/me",
                        body,
                        new TypeReference<Map<String, Object>>() {}
                );
            }

            @Override
            protected void done() {
                try {
                    Map<String, Object> resp = get();

                    if (Boolean.TRUE.equals(resp.get("success"))) {
                        JOptionPane.showMessageDialog(
                                CompanyDashboardFrame.this,
                                "Profile updated successfully!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                        loadProfileAndInit();
                    } else {
                        JOptionPane.showMessageDialog(
                                CompanyDashboardFrame.this,
                                (String) resp.get("message"),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            CompanyDashboardFrame.this,
                            ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };

        worker.execute();
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to log out?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            SwingWorker<Void, Void> worker = new SwingWorker<>() {

                @Override
                protected Void doInBackground() throws Exception {
                    ApiClient.post(
                            "/api/auth/logout",
                            null,
                            new TypeReference<Map<String, Object>>() {}
                    );

                    ApiClient.clearAuthToken();
                    return null;
                }

                @Override
                protected void done() {
                    dispose();

                    SwingUtilities.invokeLater(
                            () -> new CompanyLoginFrame().setVisible(true)
                    );
                }
            };

            worker.execute();
        }
    }
}