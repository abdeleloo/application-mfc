package fr.mfc.desktop.ui;

import fr.mfc.desktop.dao.FormationDao;
import fr.mfc.desktop.model.Formation;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class FormationsPanel extends JPanel {
    private final ConnSupplier connSupplier;
    private final boolean canEdit;
    private final FormationTableModel tableModel;
    private final JTable table;

    public FormationsPanel(ConnSupplier connSupplier, boolean canEdit) {
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

        tableModel = new FormationTableModel();
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
            tableModel.setRows(FormationDao.list(conn));
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
        Formation f = new Formation();
        if (FormationDialog.edit(this, f)) {
            try {
                FormationDao.upsert(conn, f);
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
        Formation original = tableModel.getAt(row);
        Formation f = copy(original);
        if (FormationDialog.edit(this, f)) {
            try {
                FormationDao.upsert(conn, f);
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
        Formation f = tableModel.getAt(row);
        int confirm = JOptionPane.showConfirmDialog(this, "Supprimer " + f.getNom() + " ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            FormationDao.delete(conn, f.getCodeFormation());
            refresh();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Formation copy(Formation f) {
        Formation c = new Formation();
        c.setCodeFormation(f.getCodeFormation());
        c.setNom(f.getNom());
        c.setCategorie(f.getCategorie());
        c.setTypes(f.getTypes());
        c.setDescription(f.getDescription());
        c.setDureeJours(f.getDureeJours());
        c.setPrix(f.getPrix() != null ? new BigDecimal(f.getPrix().toPlainString()) : null);
        c.setNiveau(f.getNiveau());
        return c;
    }

    private static class FormationTableModel extends AbstractTableModel {
        private final String[] cols = new String[]{"Code", "Nom", "Catégorie", "Type", "Durée", "Prix", "Niveau"};
        private List<Formation> rows = new ArrayList<>();

        public void setRows(List<Formation> rows) {
            this.rows = rows != null ? rows : new ArrayList<>();
            fireTableDataChanged();
        }

        public Formation getAt(int idx) {
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
            Formation f = rows.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return f.getCodeFormation();
                case 1:
                    return f.getNom();
                case 2:
                    return f.getCategorie();
                case 3:
                    return f.getTypes();
                case 4:
                    return f.getDureeJours();
                case 5:
                    return f.getPrix();
                case 6:
                    return f.getNiveau();
                default:
                    return "";
            }
        }
    }
}
