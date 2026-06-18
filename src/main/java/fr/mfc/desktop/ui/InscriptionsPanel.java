package fr.mfc.desktop.ui;

import fr.mfc.desktop.dao.InscriptionDao;
import fr.mfc.desktop.model.InscriptionRow;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class InscriptionsPanel extends JPanel {
    private final ConnSupplier connSupplier;
    private final InscriptionTableModel tableModel;
    private final JTable table;

    public InscriptionsPanel(ConnSupplier connSupplier) {
        super(new BorderLayout());
        this.connSupplier = connSupplier;

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshBtn = new JButton("Rafraîchir");
        actions.add(refreshBtn);
        add(actions, BorderLayout.NORTH);

        tableModel = new InscriptionTableModel();
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        refreshBtn.addActionListener(e -> refresh());
    }

    public void refresh() {
        Connection conn = connSupplier.get();
        if (conn == null) {
            return;
        }
        try {
            tableModel.setRows(InscriptionDao.list(conn));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class InscriptionTableModel extends AbstractTableModel {
        private final String[] cols = new String[]{"Inscription", "Date", "Formation", "Session", "Début", "Fin", "Lieu"};
        private List<InscriptionRow> rows = new ArrayList<>();

        public void setRows(List<InscriptionRow> rows) {
            this.rows = rows != null ? rows : new ArrayList<>();
            fireTableDataChanged();
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
            InscriptionRow r = rows.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return r.getNumeroInscription();
                case 1:
                    return r.getDateInscription();
                case 2:
                    return r.getFormation();
                case 3:
                    return r.getSession();
                case 4:
                    return r.getDateDebut();
                case 5:
                    return r.getDateFin();
                case 6:
                    return r.getLieu();
                default:
                    return "";
            }
        }
    }
}

