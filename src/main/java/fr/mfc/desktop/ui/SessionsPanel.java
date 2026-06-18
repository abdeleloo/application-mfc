package fr.mfc.desktop.ui;

import fr.mfc.desktop.dao.SessionDao;
import fr.mfc.desktop.model.SessionMfc;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class SessionsPanel extends JPanel {
    private final ConnSupplier connSupplier;
    private final boolean canEdit;
    private final SessionTableModel tableModel;
    private final JTable table;

    public SessionsPanel(ConnSupplier connSupplier, boolean canEdit) {
        super(new BorderLayout());
        this.connSupplier = connSupplier;
        this.canEdit = canEdit;

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshBtn = new JButton("Rafraîchir");
        JButton addBtn = new JButton("Ajouter");
        JButton editBtn = new JButton("Modifier");
        JButton deleteBtn = new JButton("Supprimer");
        actions.add(refreshBtn);
        actions.add(addBtn);
        actions.add(editBtn);
        actions.add(deleteBtn);
        add(actions, BorderLayout.NORTH);

        tableModel = new SessionTableModel();
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        refreshBtn.addActionListener(e -> refresh());
        addBtn.addActionListener(e -> onAdd());
        editBtn.addActionListener(e -> onEdit());
        deleteBtn.addActionListener(e -> onDelete());

        addBtn.setEnabled(canEdit);
        editBtn.setEnabled(canEdit);
        deleteBtn.setEnabled(canEdit);
    }

    public void refresh() {
        Connection conn = connSupplier.get();
        if (conn == null) {
            return;
        }
        try {
            tableModel.setRows(SessionDao.list(conn));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onAdd() {
        if (!canEdit) {
            return;
        }
        Connection conn = connSupplier.get();
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Connecte-toi à la BDD.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        SessionMfc s = new SessionMfc();
        if (SessionDialog.edit(this, s)) {
            try {
                SessionDao.upsert(conn, s);
                refresh();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onEdit() {
        if (!canEdit) {
            return;
        }
        Connection conn = connSupplier.get();
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Connecte-toi à la BDD.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int row = table.getSelectedRow();
        if (row < 0) {
            return;
        }
        SessionMfc original = tableModel.getAt(row);
        SessionMfc s = copy(original);
        if (SessionDialog.edit(this, s)) {
            try {
                SessionDao.upsert(conn, s);
                refresh();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onDelete() {
        if (!canEdit) {
            return;
        }
        Connection conn = connSupplier.get();
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Connecte-toi à la BDD.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int row = table.getSelectedRow();
        if (row < 0) {
            return;
        }
        SessionMfc s = tableModel.getAt(row);
        int confirm = JOptionPane.showConfirmDialog(this, "Supprimer la session " + s.getNumeroSession() + " ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            SessionDao.delete(conn, s.getNumeroSession());
            refresh();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private SessionMfc copy(SessionMfc s) {
        SessionMfc c = new SessionMfc();
        c.setNumeroSession(s.getNumeroSession());
        c.setCodeFormation(s.getCodeFormation());
        c.setDateDebut(s.getDateDebut());
        c.setDateFin(s.getDateFin());
        c.setLieu(s.getLieu());
        c.setSalle(s.getSalle());
        c.setPlacesTotal(s.getPlacesTotal());
        c.setPlacesRestantes(s.getPlacesRestantes());
        return c;
    }

    private static class SessionTableModel extends AbstractTableModel {
        private final String[] cols = new String[]{"Session", "Formation", "Début", "Fin", "Lieu", "Salle", "Total", "Restantes"};
        private List<SessionMfc> rows = new ArrayList<>();

        public void setRows(List<SessionMfc> rows) {
            this.rows = rows != null ? rows : new ArrayList<>();
            fireTableDataChanged();
        }

        public SessionMfc getAt(int idx) {
            return rows.get(idx);
        }

        @Override
        public int getRowCount() {
            return rows.size();
        }

        @Override
        public int getColumnCount() {
            return cols.length;
        }

        @Override
        public String getColumnName(int column) {
            return cols[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            SessionMfc s = rows.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return s.getNumeroSession();
                case 1:
                    return s.getCodeFormation();
                case 2:
                    return s.getDateDebut();
                case 3:
                    return s.getDateFin();
                case 4:
                    return s.getLieu();
                case 5:
                    return s.getSalle();
                case 6:
                    return s.getPlacesTotal();
                case 7:
                    return s.getPlacesRestantes();
                default:
                    return "";
            }
        }
    }
}
