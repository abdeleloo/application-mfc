package fr.mfc.desktop.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DashboardPanel extends JPanel {
    private final ConnSupplier connSupplier;

    private final JLabel lblFormations   = stat("0");
    private final JLabel lblSessions     = stat("0");
    private final JLabel lblStagiaires   = stat("0");
    private final JLabel lblFormateurs   = stat("0");
    private final JLabel lblInscriptions = stat("0");

    private final DefaultTableModel sessionModel = new DefaultTableModel(
            new String[]{"Formation", "Début", "Fin", "Lieu", "Places"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };

    public DashboardPanel(ConnSupplier connSupplier) {
        super(new BorderLayout(10, 10));
        this.connSupplier = connSupplier;
        setBorder(new EmptyBorder(16, 16, 16, 16));

        add(buildStatsPanel(), BorderLayout.NORTH);
        add(buildSessionsPanel(), BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshBtn = new JButton("Actualiser");
        refreshBtn.addActionListener(e -> refresh());
        south.add(refreshBtn);
        add(south, BorderLayout.SOUTH);
    }

    private JPanel buildStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 5, 10, 0));
        panel.add(statBox("Formations",   lblFormations,   new Color(219, 234, 254)));
        panel.add(statBox("Sessions",     lblSessions,     new Color(220, 252, 231)));
        panel.add(statBox("Stagiaires",   lblStagiaires,   new Color(254, 243, 199)));
        panel.add(statBox("Formateurs",   lblFormateurs,   new Color(243, 232, 255)));
        panel.add(statBox("Inscriptions", lblInscriptions, new Color(255, 237, 213)));
        return panel;
    }

    private JPanel statBox(String title, JLabel valueLabel, Color bg) {
        JPanel box = new JPanel(new BorderLayout());
        box.setBackground(bg);
        box.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bg.darker(), 1),
                new EmptyBorder(14, 14, 14, 14)
        ));
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        titleLabel.setForeground(new Color(71, 85, 105));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        box.add(valueLabel, BorderLayout.CENTER);
        box.add(titleLabel, BorderLayout.SOUTH);
        box.setOpaque(true);
        return box;
    }

    private JPanel buildSessionsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        JLabel title = new JLabel("Prochaines sessions");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        title.setForeground(new Color(30, 58, 138));
        panel.add(title, BorderLayout.NORTH);

        JTable table = new JTable(sessionModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(24);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private static JLabel stat(String text) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
        lbl.setForeground(new Color(30, 58, 138));
        return lbl;
    }

    public void refresh() {
        Connection conn = connSupplier.get();
        if (conn == null) return;
        try (Statement st = conn.createStatement()) {
            lblFormations.setText(count(st,   "SELECT COUNT(*) FROM formations"));
            lblSessions.setText(count(st,     "SELECT COUNT(*) FROM session"));
            lblStagiaires.setText(count(st,   "SELECT COUNT(*) FROM stagiaires"));
            lblFormateurs.setText(count(st,   "SELECT COUNT(*) FROM formateurs"));
            lblInscriptions.setText(count(st, "SELECT COUNT(*) FROM fiche_inscription"));
        } catch (Exception e) {
            lblFormations.setText("-");
            lblSessions.setText("-");
            lblStagiaires.setText("-");
            lblFormateurs.setText("-");
            lblInscriptions.setText("-");
        }

        sessionModel.setRowCount(0);
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT f.Nom AS Formation, s.DateDebut, s.DateFin, " +
                     "COALESCE(s.Lieu, '-') AS Lieu, " +
                     "CONCAT(COALESCE(s.PlacesRestantes, '?'), '/', COALESCE(s.PlacesTotal, '?')) AS Places " +
                     "FROM session s " +
                     "LEFT JOIN formations f ON f.CodeFormation = s.CodeFormation " +
                     "ORDER BY s.DateDebut LIMIT 10")) {
            while (rs.next()) {
                sessionModel.addRow(new Object[]{
                        rs.getString("Formation"),
                        rs.getString("DateDebut"),
                        rs.getString("DateFin"),
                        rs.getString("Lieu"),
                        rs.getString("Places")
                });
            }
        } catch (Exception ignored) {
        }
    }

    private String count(Statement st, String sql) throws Exception {
        try (ResultSet rs = st.executeQuery(sql)) {
            return rs.next() ? String.valueOf(rs.getInt(1)) : "0";
        }
    }
}
