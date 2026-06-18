package fr.mfc.desktop.ui;

import fr.mfc.desktop.auth.UserContext;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class MainFrame extends JFrame {
    private final Connection connection;
    private final UserContext user;

    public MainFrame(Connection connection, UserContext user) {
        this.connection = connection;
        this.user = user;
        setTitle("MFC — Maison de la Formation Continue | " + user.displayName());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 600));
        setSize(1200, 750);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        setContentPane(root);

        JPanel top = new JPanel(new BorderLayout());
        JLabel status = new JLabel("Connecté — " + user.displayName() + " (" + user.getRole().name().toLowerCase() + ")");
        JButton logoutBtn = new JButton("Déconnexion");
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        top.add(status, BorderLayout.WEST);
        top.add(logoutBtn, BorderLayout.EAST);
        root.add(top, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        DashboardPanel dashboardPanel = new DashboardPanel(() -> connection);
        FormationsPanel formationsPanel = new FormationsPanel(() -> connection, user.canEdit());
        SessionsPanel sessionsPanel = new SessionsPanel(() -> connection, user.canEdit());
        FormateursPanel formateursPanel = new FormateursPanel(() -> connection, user.canEdit());
        StagiairesPanel stagiairesPanel = new StagiairesPanel(() -> connection, user.canEdit());
        InscriptionsPanel inscriptionsPanel = new InscriptionsPanel(() -> connection);
        tabs.addTab("Tableau de bord", dashboardPanel);
        tabs.addTab("Formations", formationsPanel);
        tabs.addTab("Sessions", sessionsPanel);
        tabs.addTab("Formateurs", formateursPanel);
        tabs.addTab("Stagiaires", stagiairesPanel);
        tabs.addTab("Inscriptions", inscriptionsPanel);
        root.add(tabs, BorderLayout.CENTER);

        dashboardPanel.refresh();
        formationsPanel.refresh();
        sessionsPanel.refresh();
        formateursPanel.refresh();
        stagiairesPanel.refresh();
        inscriptionsPanel.refresh();

        logoutBtn.addActionListener(e -> {
            try {
                connection.close();
            } catch (Exception ignored) {
            }
            dispose();
        });
    }
}
