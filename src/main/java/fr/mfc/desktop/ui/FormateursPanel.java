package fr.mfc.desktop.ui;

import fr.mfc.desktop.dao.FormateurDao;
import fr.mfc.desktop.model.Formateur;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class FormateursPanel extends JPanel {
    private final ConnSupplier connSupplier;
    private final boolean canEdit;
    private final FormateurTableModel tableModel;
    private final JTable table;

    public FormateursPanel(ConnSupplier connSupplier, boolean canEdit) {
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

        tableModel = new FormateurTableModel();
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
            tableModel.setRows(FormateurDao.list(conn));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onAdd() {
        if (!canEdit) return;
        Connection conn = connSupplier.get();
        if (conn == null) return;
        Formateur f = new Formateur();
        if (FormateurDialog.edit(this, f, true)) {
            try {
                FormateurDao.upsert(conn, f);
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
        Formateur original = tableModel.getAt(row);
        Formateur copy = copy(original);
        if (FormateurDialog.edit(this, copy, false)) {
            try {
                FormateurDao.upsert(conn, copy);
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
        Formateur f = tableModel.getAt(row);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Supprimer " + f.getPrenom() + " " + f.getNom() + " (" + f.getMatricule() + ") ?",
                "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        Connection conn = connSupplier.get();
        if (conn == null) return;
        try {
            FormateurDao.delete(conn, f.getMatricule());
            refresh();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Formateur copy(Formateur f) {
        Formateur c = new Formateur();
        c.setMatricule(f.getMatricule());
        c.setNom(f.getNom());
        c.setPrenom(f.getPrenom());
        c.setEmail(f.getEmail());
        c.setTel(f.getTel());
        c.setSpecialite(f.getSpecialite());
        return c;
    }

    private static class FormateurTableModel extends AbstractTableModel {
        private final String[] cols = new String[]{"Matricule", "Nom", "Prénom", "Email", "Téléphone", "Spécialité"};
        private List<Formateur> rows = new ArrayList<>();

        public void setRows(List<Formateur> rows) {
            this.rows = rows != null ? rows : new ArrayList<>();
            fireTableDataChanged();
        }

        public Formateur getAt(int idx) {
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
            Formateur f = rows.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return f.getMatricule();
                case 1:
                    return f.getNom();
                case 2:
                    return f.getPrenom();
                case 3:
                    return f.getEmail();
                case 4:
                    return f.getTel();
                case 5:
                    return f.getSpecialite();
                default:
                    return "";
            }
        }
    }
}

