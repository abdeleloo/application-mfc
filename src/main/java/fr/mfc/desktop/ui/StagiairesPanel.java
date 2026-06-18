package fr.mfc.desktop.ui;

import fr.mfc.desktop.dao.StagiaireDao;
import fr.mfc.desktop.model.Stagiaire;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class StagiairesPanel extends JPanel {
    private final ConnSupplier connSupplier;
    private final boolean canEdit;
    private final StagiaireTableModel tableModel;
    private final JTable table;

    public StagiairesPanel(ConnSupplier connSupplier, boolean canEdit) {
        super(new BorderLayout());
        this.connSupplier = connSupplier;
        this.canEdit = canEdit;

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshBtn = new JButton("Rafraîchir");
        JButton addBtn     = new JButton("Ajouter");
        JButton editBtn    = new JButton("Modifier");
        JButton deleteBtn  = new JButton("Supprimer");
        actions.add(refreshBtn);
        actions.add(addBtn);
        actions.add(editBtn);
        actions.add(deleteBtn);
        add(actions, BorderLayout.NORTH);

        tableModel = new StagiaireTableModel();
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
            tableModel.setRows(StagiaireDao.list(conn));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onAdd() {
        if (!canEdit) return;
        Connection conn = connSupplier.get();
        if (conn == null) return;
        Stagiaire s = new Stagiaire();
        if (StagiaireDialog.edit(this, s, true)) {
            try {
                StagiaireDao.upsert(conn, s);
                refresh();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onEdit() {
        if (!canEdit) return;
        int row = table.getSelectedRow();
        if (row < 0) return;
        Connection conn = connSupplier.get();
        if (conn == null) return;
        Stagiaire original = tableModel.getAt(row);
        Stagiaire copy = copy(original);
        if (StagiaireDialog.edit(this, copy, false)) {
            try {
                StagiaireDao.upsert(conn, copy);
                refresh();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onDelete() {
        if (!canEdit) return;
        int row = table.getSelectedRow();
        if (row < 0) return;
        Stagiaire s = tableModel.getAt(row);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Supprimer " + s.getPrenom() + " " + s.getNom() + " (" + s.getCodeStagiaire() + ") ?",
                "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        Connection conn = connSupplier.get();
        if (conn == null) return;
        try {
            StagiaireDao.delete(conn, s.getCodeStagiaire());
            refresh();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Stagiaire copy(Stagiaire s) {
        Stagiaire c = new Stagiaire();
        c.setCodeStagiaire(s.getCodeStagiaire());
        c.setNom(s.getNom());
        c.setPrenom(s.getPrenom());
        c.setEmail(s.getEmail());
        c.setTel(s.getTel());
        c.setVille(s.getVille());
        c.setSociete(s.getSociete());
        return c;
    }

    private static class StagiaireTableModel extends AbstractTableModel {
        private final String[] cols = new String[]{"Code", "Nom", "Prénom", "Email", "Téléphone", "Ville", "Société"};
        private List<Stagiaire> rows = new ArrayList<>();

        public void setRows(List<Stagiaire> rows) {
            this.rows = rows != null ? rows : new ArrayList<>();
            fireTableDataChanged();
        }

        public Stagiaire getAt(int idx) {
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
            Stagiaire s = rows.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return s.getCodeStagiaire();
                case 1:
                    return s.getNom();
                case 2:
                    return s.getPrenom();
                case 3:
                    return s.getEmail();
                case 4:
                    return s.getTel();
                case 5:
                    return s.getVille();
                case 6:
                    return s.getSociete();
                default:
                    return "";
            }
        }
    }
}

