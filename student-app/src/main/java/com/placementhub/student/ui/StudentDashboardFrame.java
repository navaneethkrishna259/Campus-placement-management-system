package com.placementhub.student.ui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.placementhub.student.model.StudentApplicationItem;
import com.placementhub.student.model.StudentJobItem;
import com.placementhub.student.model.StudentProfile;
import com.placementhub.student.model.StudentSavedJobItem;
import com.placementhub.student.util.ApiClient;
import com.placementhub.student.util.UIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.List;

/**
 * Modern Student Dashboard Frame.
 * Navigation:
 * 1. Home (Browse Opportunities, Save Job, Apply)
 * 2. Saved Jobs (View, Remove, Apply)
 * 3. My Applications (Status tracking, Feedback)
 * 4. Interview Schedule (Monthly Swing Calendar)
 * 5. Account (Profile management)
 * 6. Logout
 */
public class StudentDashboardFrame extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);

    // Sidebar navigation buttons
    private JButton btnNavHome;
    private JButton btnNavSavedJobs;
    private JButton btnNavApplications;
    private JButton btnNavInterviews;
    private JButton btnNavAccount;

    // Home Section (Job Opportunities)
    private JPanel jobsContainerPanel;

    // Saved Jobs Section
    private JTable savedJobsTable;
    private DefaultTableModel savedJobsTableModel;
    private List<StudentSavedJobItem> currentSavedJobs = new ArrayList<>();

    // Applications Section
    private JTable applicationsTable;
    private DefaultTableModel applicationsTableModel;
    private List<StudentApplicationItem> currentApplications = new ArrayList<>();

    // Interview Schedule Calendar Section
    private JLabel calendarMonthYearLabel;
    private JPanel calendarGridPanel;
    private JTextArea interviewDetailsArea;
    private YearMonth currentCalMonth = YearMonth.now();
    private final Map<LocalDate, List<StudentApplicationItem>> interviewsByDate = new HashMap<>();

    // Account Section
    private JTextField accNameField;
    private JTextField accEmailField;
    private JTextField accPhoneField;
    private JTextField accDeptField;
    private JTextField accGradYearField;
    private StudentProfile currentProfile;

    public StudentDashboardFrame() {
        setTitle("Placement Hub - Student Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 720);
        setLocationRelativeTo(null);

        initUI();
        loadProfileAndInit();
        fetchActiveJobs();
    }

    private void initUI() {
        getContentPane().setLayout(new BorderLayout());

        JPanel sidebar = buildSidebar();
        getContentPane().add(sidebar, BorderLayout.WEST);

        contentPanel.setBackground(UIUtils.COLOR_BG);
        contentPanel.add(buildHomePanel(), "HOME");
        contentPanel.add(buildSavedJobsPanel(), "SAVED_JOBS");
        contentPanel.add(buildApplicationsPanel(), "APPLICATIONS");
        contentPanel.add(buildInterviewSchedulePanel(), "INTERVIEWS");
        contentPanel.add(buildAccountPanel(), "ACCOUNT");

        getContentPane().add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(UIUtils.COLOR_PRIMARY);
        sidebar.setLayout(new BorderLayout());

        // Header
        JPanel brandPanel = new JPanel(new GridLayout(2, 1));
        brandPanel.setBackground(UIUtils.COLOR_PRIMARY);
        brandPanel.setBorder(new EmptyBorder(25, 20, 20, 20));

        JLabel logoLbl = new JLabel("PLACEMENT HUB");
        logoLbl.setFont(UIUtils.FONT_TITLE);
        logoLbl.setForeground(UIUtils.COLOR_WHITE);

        JLabel subLbl = new JLabel("Student Portal");
        subLbl.setFont(UIUtils.FONT_SMALL);
        subLbl.setForeground(new Color(0xB0, 0xC4, 0xDE));

        brandPanel.add(logoLbl);
        brandPanel.add(subLbl);
        sidebar.add(brandPanel, BorderLayout.NORTH);

        // Menu
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(UIUtils.COLOR_PRIMARY);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        btnNavHome = createSidebarNavButton("1. Home (Jobs)");
        btnNavSavedJobs = createSidebarNavButton("2. Saved Jobs");
        btnNavApplications = createSidebarNavButton("3. My Applications");
        btnNavInterviews = createSidebarNavButton("4. Interview Schedule");
        btnNavAccount = createSidebarNavButton("5. Account");
        JButton btnLogout = createSidebarNavButton("6. Logout");

        btnNavHome.addActionListener(e -> {
            switchTab("HOME", btnNavHome);
            fetchActiveJobs();
        });
        btnNavSavedJobs.addActionListener(e -> {
            switchTab("SAVED_JOBS", btnNavSavedJobs);
            fetchSavedJobs();
        });
        btnNavApplications.addActionListener(e -> {
            switchTab("APPLICATIONS", btnNavApplications);
            fetchStudentApplications();
        });
        btnNavInterviews.addActionListener(e -> {
            switchTab("INTERVIEWS", btnNavInterviews);
            fetchInterviews();
        });
        btnNavAccount.addActionListener(e -> {
            switchTab("ACCOUNT", btnNavAccount);
            populateAccountForm();
        });
        btnLogout.addActionListener(e -> handleLogout());

        menuPanel.add(btnNavHome);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(btnNavSavedJobs);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(btnNavApplications);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(btnNavInterviews);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(btnNavAccount);

        currentActiveNavButton = btnNavHome;
        btnNavHome.setForeground(UIUtils.COLOR_WHITE);
        btnNavHome.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 3, 0, 0, UIUtils.COLOR_ACTION),
                new EmptyBorder(10, 15, 10, 15)
        ));

        sidebar.add(menuPanel, BorderLayout.CENTER);

        // Logout
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(UIUtils.COLOR_PRIMARY);
        bottomPanel.setBorder(new EmptyBorder(10, 10, 20, 10));
        bottomPanel.add(btnLogout, BorderLayout.CENTER);
        sidebar.add(bottomPanel, BorderLayout.SOUTH);

        return sidebar;
    }

    private JButton createSidebarNavButton(String title) {
        JButton btn = new JButton(title);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btn.setFont(UIUtils.FONT_BOLD);
        btn.setForeground(new Color(0xD0, 0xE0, 0xF5));
        btn.setBackground(UIUtils.COLOR_PRIMARY);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 3, 0, 0, UIUtils.COLOR_PRIMARY),
                new EmptyBorder(10, 15, 10, 15)
        ));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (btn != currentActiveNavButton) {
                    btn.setForeground(UIUtils.COLOR_WHITE);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (btn != currentActiveNavButton) {
                    btn.setForeground(new Color(0xD0, 0xE0, 0xF5));
                }
            }
        });

        return btn;
    }

    private JButton currentActiveNavButton;

    private void switchTab(String cardName, JButton activeButton) {
        cardLayout.show(contentPanel, cardName);
        currentActiveNavButton = activeButton;

        JButton[] buttons = {btnNavHome, btnNavSavedJobs, btnNavApplications, btnNavInterviews, btnNavAccount};
        for (JButton b : buttons) {
            b.setOpaque(false);
            b.setContentAreaFilled(false);
            b.setForeground(new Color(0xD0, 0xE0, 0xF5));
            b.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 3, 0, 0, UIUtils.COLOR_PRIMARY),
                    new EmptyBorder(10, 15, 10, 15)
            ));
        }

        activeButton.setOpaque(false);
        activeButton.setContentAreaFilled(false);
        activeButton.setForeground(UIUtils.COLOR_WHITE);
        activeButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 3, 0, 0, UIUtils.COLOR_ACTION),
                new EmptyBorder(10, 15, 10, 15)
        ));
    }

    // ==========================================
    // 1. HOME: JOB OPPORTUNITIES
    // ==========================================
    private JPanel buildHomePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(UIUtils.COLOR_BG);
        panel.setBorder(new EmptyBorder(25, 30, 25, 30));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UIUtils.COLOR_BG);

        JLabel titleLbl = new JLabel("Explore Job Opportunities");
        titleLbl.setFont(UIUtils.FONT_TITLE);
        titleLbl.setForeground(UIUtils.COLOR_PRIMARY);
        topBar.add(titleLbl, BorderLayout.WEST);

        JButton refreshBtn = UIUtils.createSecondaryButton("Refresh Jobs");
        refreshBtn.addActionListener(e -> fetchActiveJobs());
        topBar.add(refreshBtn, BorderLayout.EAST);

        panel.add(topBar, BorderLayout.NORTH);

        jobsContainerPanel = new JPanel();
        jobsContainerPanel.setLayout(new BoxLayout(jobsContainerPanel, BoxLayout.Y_AXIS));
        jobsContainerPanel.setBackground(UIUtils.COLOR_BG);

        JScrollPane scroll = new JScrollPane(jobsContainerPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private void fetchActiveJobs() {
        SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, Object> doInBackground() throws Exception {
                return ApiClient.get("/api/jobs", new TypeReference<Map<String, Object>>() {});
            }

            @Override
            protected void done() {
                try {
                    Map<String, Object> resp = get();
                    if (Boolean.TRUE.equals(resp.get("success"))) {
                        @SuppressWarnings("unchecked")
                        List<Map<String, Object>> list = (List<Map<String, Object>>) resp.get("data");
                        renderJobCards(list);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(StudentDashboardFrame.this, ex.getMessage(), "Error Fetching Jobs", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void renderJobCards(List<Map<String, Object>> jobs) {
        jobsContainerPanel.removeAll();

        if (jobs == null || jobs.isEmpty()) {
            JLabel emptyLbl = new JLabel("No active job postings available right now. Check back soon!", SwingConstants.CENTER);
            emptyLbl.setFont(UIUtils.FONT_SUBTITLE);
            emptyLbl.setForeground(UIUtils.COLOR_TEXT_MUTED);
            emptyLbl.setBorder(new EmptyBorder(50, 0, 0, 0));
            jobsContainerPanel.add(emptyLbl);
        } else {
            for (Map<String, Object> j : jobs) {
                Long jobId = Long.valueOf(j.get("jobId").toString());
                String title = (String) j.get("jobTitle");
                String company = (String) j.get("companyName");
                String desc = (String) j.get("jobDescription");
                String skills = (String) j.get("requiredSkills");
                String salary = (String) j.get("salary");
                String location = (String) j.get("jobLocation");
                String type = (String) j.get("jobType");
                String deadline = (String) j.get("applicationDeadline");
                boolean isSaved = Boolean.TRUE.equals(j.get("isSaved"));

                JPanel card = UIUtils.createCardPanel();
                card.setLayout(new BorderLayout(15, 10));
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

                // Left & Center
                JPanel content = new JPanel();
                content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
                content.setBackground(UIUtils.COLOR_WHITE);

                JLabel titleLabel = new JLabel(title + "  —  " + (company != null ? company : "Company"));
                titleLabel.setFont(UIUtils.FONT_SUBTITLE);
                titleLabel.setForeground(UIUtils.COLOR_PRIMARY);

                JLabel metaLabel = new JLabel(String.format("Location: %s  |  Type: %s  |  Salary: %s  |  Deadline: %s",
                        location != null ? location : "N/A",
                        type != null ? type : "Full Time",
                        salary != null ? salary : "Not specified",
                        deadline != null ? deadline : "Open"));
                metaLabel.setFont(UIUtils.FONT_SMALL);
                metaLabel.setForeground(UIUtils.COLOR_TEXT_MUTED);

                JLabel descLabel = new JLabel("<html>" + (desc != null && desc.length() > 140 ? desc.substring(0, 140) + "..." : desc) + "</html>");
                descLabel.setFont(UIUtils.FONT_REGULAR);
                descLabel.setForeground(UIUtils.COLOR_TEXT_MAIN);

                JLabel skillsLabel = new JLabel("Skills: " + (skills != null ? skills : "N/A"));
                skillsLabel.setFont(UIUtils.FONT_BOLD);
                skillsLabel.setForeground(UIUtils.COLOR_SECONDARY);

                content.add(titleLabel);
                content.add(Box.createVerticalStrut(4));
                content.add(metaLabel);
                content.add(Box.createVerticalStrut(6));
                content.add(descLabel);
                content.add(Box.createVerticalStrut(4));
                content.add(skillsLabel);

                card.add(content, BorderLayout.CENTER);

                // Right action buttons
                JPanel actions = new JPanel(new GridLayout(2, 1, 0, 8));
                actions.setBackground(UIUtils.COLOR_WHITE);

                JButton saveBtn = UIUtils.createSecondaryButton(isSaved ? "Saved" : "Save Job");
                if (isSaved) {
                    saveBtn.setForeground(UIUtils.COLOR_SUCCESS);
                }
                saveBtn.addActionListener(e -> handleToggleSaveJob(jobId, isSaved));

                JButton applyBtn = UIUtils.createSuccessButton("Apply Now");
                applyBtn.addActionListener(e -> handleApplyJob(jobId));

                actions.add(applyBtn);
                actions.add(saveBtn);
                card.add(actions, BorderLayout.EAST);

                jobsContainerPanel.add(card);
                jobsContainerPanel.add(Box.createVerticalStrut(12));
            }
        }

        jobsContainerPanel.revalidate();
        jobsContainerPanel.repaint();
    }

    private void handleToggleSaveJob(Long jobId, boolean currentlySaved) {
        SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, Object> doInBackground() throws Exception {
                if (currentlySaved) {
                    return ApiClient.delete("/api/students/saved-jobs/" + jobId, new TypeReference<Map<String, Object>>() {});
                } else {
                    return ApiClient.post("/api/students/saved-jobs/" + jobId, null, new TypeReference<Map<String, Object>>() {});
                }
            }

            @Override
            protected void done() {
                try {
                    Map<String, Object> resp = get();
                    if (Boolean.TRUE.equals(resp.get("success"))) {
                        fetchActiveJobs();
                    } else {
                        JOptionPane.showMessageDialog(StudentDashboardFrame.this, (String) resp.get("message"), "Notice", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(StudentDashboardFrame.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void handleApplyJob(Long jobId) {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to apply for this job?", "Confirm Application", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, Object> doInBackground() throws Exception {
                return ApiClient.post("/api/jobs/" + jobId + "/applications", null, new TypeReference<Map<String, Object>>() {});
            }

            @Override
            protected void done() {
                try {
                    Map<String, Object> resp = get();
                    if (Boolean.TRUE.equals(resp.get("success"))) {
                        JOptionPane.showMessageDialog(StudentDashboardFrame.this, "Application submitted successfully!", "Application Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(StudentDashboardFrame.this, (String) resp.get("message"), "Notice", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(StudentDashboardFrame.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    // ==========================================
    // 2. SAVED JOBS
    // ==========================================
    private JPanel buildSavedJobsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(UIUtils.COLOR_BG);
        panel.setBorder(new EmptyBorder(25, 30, 25, 30));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UIUtils.COLOR_BG);

        JLabel titleLbl = new JLabel("Saved Jobs");
        titleLbl.setFont(UIUtils.FONT_TITLE);
        titleLbl.setForeground(UIUtils.COLOR_PRIMARY);
        topBar.add(titleLbl, BorderLayout.WEST);

        JPanel actionBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actionBtns.setBackground(UIUtils.COLOR_BG);

        JButton refreshBtn = UIUtils.createSecondaryButton("Refresh");
        refreshBtn.addActionListener(e -> fetchSavedJobs());

        JButton viewBtn = UIUtils.createSecondaryButton("View Details");
        viewBtn.addActionListener(e -> handleViewSavedJobDetails());

        JButton removeBtn = UIUtils.createDangerButton("Remove");
        removeBtn.addActionListener(e -> handleRemoveSavedJob());

        JButton applyBtn = UIUtils.createSuccessButton("Apply Now");
        applyBtn.addActionListener(e -> handleApplyFromSaved());

        actionBtns.add(refreshBtn);
        actionBtns.add(viewBtn);
        actionBtns.add(removeBtn);
        actionBtns.add(applyBtn);
        topBar.add(actionBtns, BorderLayout.EAST);

        panel.add(topBar, BorderLayout.NORTH);

        String[] columns = {"Job ID", "Job Title", "Company", "Salary", "Location", "Type", "Deadline"};
        savedJobsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        savedJobsTable = new JTable(savedJobsTableModel);
        savedJobsTable.setFont(UIUtils.FONT_REGULAR);
        savedJobsTable.setRowHeight(28);
        savedJobsTable.getTableHeader().setFont(UIUtils.FONT_BOLD);
        savedJobsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(savedJobsTable);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private void fetchSavedJobs() {
        SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, Object> doInBackground() throws Exception {
                return ApiClient.get("/api/students/saved-jobs", new TypeReference<Map<String, Object>>() {});
            }

            @Override
            protected void done() {
                try {
                    Map<String, Object> resp = get();
                    if (Boolean.TRUE.equals(resp.get("success"))) {
                        @SuppressWarnings("unchecked")
                        List<Map<String, Object>> data = (List<Map<String, Object>>) resp.get("data");
                        savedJobsTableModel.setRowCount(0);
                        if (data != null) {
                            for (Map<String, Object> item : data) {
                                savedJobsTableModel.addRow(new Object[]{
                                        item.get("jobId"),
                                        item.get("jobTitle"),
                                        item.get("companyName"),
                                        item.get("salary"),
                                        item.get("jobLocation"),
                                        item.get("jobType"),
                                        item.get("applicationDeadline")
                                });
                            }
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(StudentDashboardFrame.this, ex.getMessage(), "Error Fetching Saved Jobs", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void handleRemoveSavedJob() {
        int row = savedJobsTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a saved job to remove.", "Notice", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Long jobId = Long.valueOf(savedJobsTableModel.getValueAt(row, 0).toString());
        SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, Object> doInBackground() throws Exception {
                return ApiClient.delete("/api/students/saved-jobs/" + jobId, new TypeReference<Map<String, Object>>() {});
            }

            @Override
            protected void done() {
                try {
                    Map<String, Object> resp = get();
                    if (Boolean.TRUE.equals(resp.get("success"))) {
                        fetchSavedJobs();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(StudentDashboardFrame.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void handleApplyFromSaved() {
        int row = savedJobsTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a saved job to apply.", "Notice", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Long jobId = Long.valueOf(savedJobsTableModel.getValueAt(row, 0).toString());
        handleApplyJob(jobId);
    }

    private void handleViewSavedJobDetails() {
        int row = savedJobsTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a saved job to view details.", "Notice", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Long jobId = Long.valueOf(savedJobsTableModel.getValueAt(row, 0).toString());
        SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, Object> doInBackground() throws Exception {
                return ApiClient.get("/api/jobs/" + jobId, new TypeReference<Map<String, Object>>() {});
            }

            @Override
            protected void done() {
                try {
                    Map<String, Object> resp = get();
                    @SuppressWarnings("unchecked")
                    Map<String, Object> job = (Map<String, Object>) resp.get("data");
                    String msg = String.format("Job Title: %s\nCompany: %s\nLocation: %s\nSalary: %s\nType: %s\nDeadline: %s\nSkills: %s\n\nDescription:\n%s",
                            job.get("jobTitle"),
                            job.get("companyName"),
                            job.get("jobLocation"),
                            job.get("salary"),
                            job.get("jobType"),
                            job.get("applicationDeadline"),
                            job.get("requiredSkills"),
                            job.get("jobDescription"));
                    JOptionPane.showMessageDialog(StudentDashboardFrame.this, msg, "Job Details", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(StudentDashboardFrame.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    // ==========================================
    // 3. MY APPLICATIONS
    // ==========================================
    private JPanel buildApplicationsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(UIUtils.COLOR_BG);
        panel.setBorder(new EmptyBorder(25, 30, 25, 30));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UIUtils.COLOR_BG);

        JLabel titleLbl = new JLabel("My Applications");
        titleLbl.setFont(UIUtils.FONT_TITLE);
        titleLbl.setForeground(UIUtils.COLOR_PRIMARY);
        topBar.add(titleLbl, BorderLayout.WEST);

        JButton refreshBtn = UIUtils.createSecondaryButton("Refresh Applications");
        refreshBtn.addActionListener(e -> fetchStudentApplications());
        topBar.add(refreshBtn, BorderLayout.EAST);

        panel.add(topBar, BorderLayout.NORTH);

        String[] columns = {"Job Title", "Company", "Applied Date", "Status", "Interview Info", "Feedback"};
        applicationsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        applicationsTable = new JTable(applicationsTableModel);
        applicationsTable.setFont(UIUtils.FONT_REGULAR);
        applicationsTable.setRowHeight(30);
        applicationsTable.getTableHeader().setFont(UIUtils.FONT_BOLD);
        applicationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Status color renderer: APPLIED (blue), SHORTLISTED (green), REJECTED (red), INTERVIEW_SCHEDULED (purple)
        applicationsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                if (!isSelected && col == 3 && value != null) {
                    String st = value.toString();
                    if ("SHORTLISTED".equalsIgnoreCase(st)) {
                        c.setForeground(UIUtils.COLOR_SUCCESS);
                    } else if ("REJECTED".equalsIgnoreCase(st)) {
                        c.setForeground(UIUtils.COLOR_ERROR);
                    } else if ("INTERVIEW_SCHEDULED".equalsIgnoreCase(st)) {
                        c.setForeground(UIUtils.COLOR_PURPLE);
                    } else {
                        c.setForeground(UIUtils.COLOR_ACTION);
                    }
                } else if (!isSelected) {
                    c.setForeground(UIUtils.COLOR_TEXT_MAIN);
                }
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(applicationsTable);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private void fetchStudentApplications() {
        SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, Object> doInBackground() throws Exception {
                return ApiClient.get("/api/students/applications", new TypeReference<Map<String, Object>>() {});
            }

            @Override
            protected void done() {
                try {
                    Map<String, Object> resp = get();
                    if (Boolean.TRUE.equals(resp.get("success"))) {
                        @SuppressWarnings("unchecked")
                        List<Map<String, Object>> list = (List<Map<String, Object>>) resp.get("data");
                        applicationsTableModel.setRowCount(0);
                        if (list != null) {
                            for (Map<String, Object> item : list) {
                                String interviewInfo = "None";
                                if (item.get("interviewDate") != null) {
                                    interviewInfo = String.format("%s at %s (%s)",
                                            item.get("interviewDate"),
                                            item.get("interviewTime"),
                                            item.get("interviewMode"));
                                }
                                applicationsTableModel.addRow(new Object[]{
                                        item.get("jobTitle"),
                                        item.get("companyName"),
                                        item.get("appliedAt"),
                                        item.get("status"),
                                        interviewInfo,
                                        item.get("companyFeedback") != null ? item.get("companyFeedback") : "No feedback"
                                });
                            }
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(StudentDashboardFrame.this, ex.getMessage(), "Error Fetching Applications", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    // ==========================================
    // 4. INTERVIEW SCHEDULE (MONTHLY CALENDAR)
    // ==========================================
    private JPanel buildInterviewSchedulePanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(UIUtils.COLOR_BG);
        panel.setBorder(new EmptyBorder(20, 25, 20, 25));

        // Top Navigation for Month
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UIUtils.COLOR_BG);

        JPanel monthNav = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        monthNav.setBackground(UIUtils.COLOR_BG);

        JButton prevBtn = UIUtils.createSecondaryButton("< Previous");
        prevBtn.addActionListener(e -> {
            currentCalMonth = currentCalMonth.minusMonths(1);
            renderCalendar();
        });

        calendarMonthYearLabel = new JLabel("", SwingConstants.CENTER);
        calendarMonthYearLabel.setFont(UIUtils.FONT_TITLE);
        calendarMonthYearLabel.setForeground(UIUtils.COLOR_PRIMARY);

        JButton nextBtn = UIUtils.createSecondaryButton("Next >");
        nextBtn.addActionListener(e -> {
            currentCalMonth = currentCalMonth.plusMonths(1);
            renderCalendar();
        });

        monthNav.add(prevBtn);
        monthNav.add(calendarMonthYearLabel);
        monthNav.add(nextBtn);
        topBar.add(monthNav, BorderLayout.CENTER);

        JButton refreshBtn = UIUtils.createSecondaryButton("Refresh");
        refreshBtn.addActionListener(e -> fetchInterviews());
        topBar.add(refreshBtn, BorderLayout.EAST);

        panel.add(topBar, BorderLayout.NORTH);

        // Split Pane: Calendar Grid on left, Details on right
        calendarGridPanel = new JPanel();
        calendarGridPanel.setBackground(UIUtils.COLOR_WHITE);
        calendarGridPanel.setBorder(new LineBorder(UIUtils.COLOR_BORDER, 1));

        JPanel detailsCard = UIUtils.createCardPanel();
        detailsCard.setLayout(new BorderLayout(0, 10));
        detailsCard.setPreferredSize(new Dimension(340, 0));

        JLabel detailsTitle = new JLabel("Interview Details");
        detailsTitle.setFont(UIUtils.FONT_SUBTITLE);
        detailsTitle.setForeground(UIUtils.COLOR_PRIMARY);
        detailsCard.add(detailsTitle, BorderLayout.NORTH);

        interviewDetailsArea = new JTextArea("Select a highlighted date on the calendar to view interview schedules.");
        interviewDetailsArea.setEditable(false);
        interviewDetailsArea.setLineWrap(true);
        interviewDetailsArea.setWrapStyleWord(true);
        interviewDetailsArea.setFont(UIUtils.FONT_REGULAR);
        detailsCard.add(new JScrollPane(interviewDetailsArea), BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, calendarGridPanel, detailsCard);
        splitPane.setResizeWeight(0.65);
        splitPane.setDividerLocation(520);
        splitPane.setBorder(null);

        panel.add(splitPane, BorderLayout.CENTER);

        return panel;
    }

    private void fetchInterviews() {
        SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, Object> doInBackground() throws Exception {
                return ApiClient.get("/api/students/interviews", new TypeReference<Map<String, Object>>() {});
            }

            @Override
            protected void done() {
                try {
                    Map<String, Object> resp = get();
                    if (Boolean.TRUE.equals(resp.get("success"))) {
                        @SuppressWarnings("unchecked")
                        List<Map<String, Object>> data = (List<Map<String, Object>>) resp.get("data");
                        interviewsByDate.clear();
                        if (data != null) {
                            for (Map<String, Object> item : data) {
                                String dateStr = (String) item.get("interviewDate");
                                if (dateStr != null && !dateStr.trim().isEmpty()) {
                                    try {
                                        LocalDate d = LocalDate.parse(dateStr.trim());
                                        StudentApplicationItem app = new StudentApplicationItem();
                                        app.setJobTitle((String) item.get("jobTitle"));
                                        app.setCompanyName((String) item.get("companyName"));
                                        app.setInterviewDate(dateStr);
                                        app.setInterviewTime((String) item.get("interviewTime"));
                                        app.setInterviewMode((String) item.get("interviewMode"));
                                        app.setInterviewLocationOrLink((String) item.get("interviewLocationOrLink"));
                                        app.setCompanyFeedback((String) item.get("companyFeedback"));

                                        interviewsByDate.computeIfAbsent(d, k -> new ArrayList<>()).add(app);
                                    } catch (Exception ignored) {}
                                }
                            }
                        }
                        renderCalendar();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(StudentDashboardFrame.this, ex.getMessage(), "Error Fetching Interviews", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void renderCalendar() {
        calendarMonthYearLabel.setText(currentCalMonth.getMonth().name() + " " + currentCalMonth.getYear());
        calendarGridPanel.removeAll();
        calendarGridPanel.setLayout(new GridLayout(7, 7, 2, 2));

        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : days) {
            JLabel lbl = new JLabel(day, SwingConstants.CENTER);
            lbl.setFont(UIUtils.FONT_BOLD);
            lbl.setForeground(UIUtils.COLOR_PRIMARY);
            lbl.setOpaque(true);
            lbl.setBackground(new Color(0xEA, 0xF1, 0xFA));
            calendarGridPanel.add(lbl);
        }

        LocalDate firstOfMonth = currentCalMonth.atDay(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue() % 7; // Sunday = 0
        int daysInMonth = currentCalMonth.lengthOfMonth();

        // Leading blanks
        for (int i = 0; i < dayOfWeek; i++) {
            JLabel empty = new JLabel("");
            empty.setOpaque(true);
            empty.setBackground(UIUtils.COLOR_WHITE);
            calendarGridPanel.add(empty);
        }

        // Days
        for (int d = 1; d <= daysInMonth; d++) {
            LocalDate date = currentCalMonth.atDay(d);
            JButton dayBtn = new JButton(String.valueOf(d));
            dayBtn.setFont(UIUtils.FONT_REGULAR);
            dayBtn.setFocusPainted(false);
            dayBtn.setBorder(new LineBorder(UIUtils.COLOR_BORDER, 1));
            dayBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            boolean hasInterview = interviewsByDate.containsKey(date);
            if (hasInterview) {
                dayBtn.setBackground(new Color(0xE8, 0xDA, 0xEF)); // Soft purple highlight
                dayBtn.setForeground(UIUtils.COLOR_PURPLE);
                dayBtn.setFont(UIUtils.FONT_BOLD);
                dayBtn.setText(d + " 📅");
            } else {
                dayBtn.setBackground(UIUtils.COLOR_WHITE);
                dayBtn.setForeground(UIUtils.COLOR_TEXT_MAIN);
            }

            dayBtn.addActionListener(e -> {
                if (hasInterview) {
                    List<StudentApplicationItem> list = interviewsByDate.get(date);
                    StringBuilder sb = new StringBuilder();
                    sb.append("Scheduled Interviews on ").append(date).append(":\n\n");
                    for (StudentApplicationItem item : list) {
                        sb.append("• Company: ").append(item.getCompanyName()).append("\n");
                        sb.append("  Position: ").append(item.getJobTitle()).append("\n");
                        sb.append("  Time: ").append(item.getInterviewTime()).append("\n");
                        sb.append("  Mode: ").append(item.getInterviewMode()).append("\n");
                        sb.append("  Location/Link: ").append(item.getInterviewLocationOrLink()).append("\n");
                        if (item.getCompanyFeedback() != null && !item.getCompanyFeedback().isEmpty()) {
                            sb.append("  Notes: ").append(item.getCompanyFeedback()).append("\n");
                        }
                        sb.append("\n------------------------------\n\n");
                    }
                    interviewDetailsArea.setText(sb.toString());
                } else {
                    interviewDetailsArea.setText("No interviews scheduled on " + date);
                }
            });

            calendarGridPanel.add(dayBtn);
        }

        // Trailing blanks to complete 49 cells grid (7x7)
        int remaining = 42 - (dayOfWeek + daysInMonth);
        if (remaining < 0) remaining += 7;
        for (int i = 0; i < remaining; i++) {
            JLabel empty = new JLabel("");
            empty.setOpaque(true);
            empty.setBackground(UIUtils.COLOR_WHITE);
            calendarGridPanel.add(empty);
        }

        calendarGridPanel.revalidate();
        calendarGridPanel.repaint();
    }

    // ==========================================
    // 5. ACCOUNT
    // ==========================================
    private JPanel buildAccountPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIUtils.COLOR_BG);
        panel.setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel titleLbl = new JLabel("Student Profile");
        titleLbl.setFont(UIUtils.FONT_TITLE);
        titleLbl.setForeground(UIUtils.COLOR_PRIMARY);
        panel.add(titleLbl, BorderLayout.NORTH);

        JPanel card = UIUtils.createCardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        accNameField = UIUtils.createTextField();
        accEmailField = UIUtils.createTextField();
        accEmailField.setEditable(false);
        accPhoneField = UIUtils.createTextField();
        accDeptField = UIUtils.createTextField();
        accGradYearField = UIUtils.createTextField();

        card.add(createFormRow("Full Name", accNameField));
        card.add(createFormRow("Email Address (Registered)", accEmailField));
        card.add(createFormRow("Phone Number", accPhoneField));
        card.add(createFormRow("Department (e.g. Computer Science)", accDeptField));
        card.add(createFormRow("Graduation Year", accGradYearField));

        JPanel btnPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPnl.setBackground(UIUtils.COLOR_WHITE);
        JButton saveBtn = UIUtils.createActionButton("Save Profile");
        saveBtn.addActionListener(e -> handleUpdateProfile());
        btnPnl.add(saveBtn);
        card.add(btnPnl);

        panel.add(new JScrollPane(card), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFormRow(String label, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setBackground(UIUtils.COLOR_WHITE);
        p.setBorder(new EmptyBorder(4, 5, 4, 5));
        JLabel lbl = new JLabel(label);
        lbl.setFont(UIUtils.FONT_BOLD);
        p.add(lbl, BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);
        return p;
    }

    private void loadProfileAndInit() {
        SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, Object> doInBackground() throws Exception {
                return ApiClient.get("/api/students/me", new TypeReference<Map<String, Object>>() {});
            }

            @Override
            protected void done() {
                try {
                    Map<String, Object> resp = get();
                    if (Boolean.TRUE.equals(resp.get("success"))) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> d = (Map<String, Object>) resp.get("data");
                        currentProfile = new StudentProfile();
                        currentProfile.setId(Long.valueOf(d.get("id").toString()));
                        currentProfile.setFullName((String) d.get("fullName"));
                        currentProfile.setEmail((String) d.get("email"));
                        currentProfile.setPhone((String) d.get("phone"));
                        currentProfile.setDepartment((String) d.get("department"));
                        if (d.get("graduationYear") != null) {
                            currentProfile.setGraduationYear(Integer.valueOf(d.get("graduationYear").toString()));
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(StudentDashboardFrame.this, ex.getMessage(), "Error Loading Profile", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void populateAccountForm() {
        if (currentProfile != null) {
            accNameField.setText(currentProfile.getFullName());
            accEmailField.setText(currentProfile.getEmail());
            accPhoneField.setText(currentProfile.getPhone());
            accDeptField.setText(currentProfile.getDepartment());
            accGradYearField.setText(currentProfile.getGraduationYear() != null ? currentProfile.getGraduationYear().toString() : "");
        }
    }

    private void handleUpdateProfile() {
        String name = accNameField.getText().trim();
        String phone = accPhoneField.getText().trim();
        String dept = accDeptField.getText().trim();
        String gradStr = accGradYearField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Full name cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer gradYear = null;
        if (!gradStr.isEmpty()) {
            try {
                gradYear = Integer.parseInt(gradStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Graduation year must be a valid number.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        Map<String, Object> body = new HashMap<>();
        body.put("fullName", name);
        body.put("phone", phone);
        body.put("department", dept);
        body.put("graduationYear", gradYear);

        SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, Object> doInBackground() throws Exception {
                return ApiClient.put("/api/students/me", body, new TypeReference<Map<String, Object>>() {});
            }

            @Override
            protected void done() {
                try {
                    Map<String, Object> resp = get();
                    if (Boolean.TRUE.equals(resp.get("success"))) {
                        JOptionPane.showMessageDialog(StudentDashboardFrame.this, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadProfileAndInit();
                    } else {
                        JOptionPane.showMessageDialog(StudentDashboardFrame.this, (String) resp.get("message"), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(StudentDashboardFrame.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() throws Exception {
                    ApiClient.post("/api/auth/logout", null, new TypeReference<Map<String, Object>>() {});
                    ApiClient.clearAuthToken();
                    return null;
                }

                @Override
                protected void done() {
                    dispose();
                    SwingUtilities.invokeLater(() -> new StudentLoginFrame().setVisible(true));
                }
            };
            worker.execute();
        }
    }
}
