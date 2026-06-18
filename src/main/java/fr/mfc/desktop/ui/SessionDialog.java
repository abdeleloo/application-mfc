package fr.mfc.desktop.ui;

import fr.mfc.desktop.model.SessionMfc;

import javax.swing.*;
import java.awt.*;

public final class SessionDialog {
    private SessionDialog() {
    }

    public static boolean edit(Component parent, SessionMfc s) {
        JTextField numero = new JTextField(value(s.getNumeroSession()));
        JTextField codeFormation = new JTextField(value(s.getCodeFormation()));
        JTextField dateDebut = new JTextField(value(s.getDateDebut()));
        JTextField dateFin = new JTextField(value(s.getDateFin()));
        JTextField lieu = new JTextField(value(s.getLieu()));
        JTextField salle = new JTextField(value(s.getSalle()));
        JTextField placesTotal = new JTextField(s.getPlacesTotal() != null ? String.valueOf(s.getPlacesTotal()) : "");
        JTextField placesRestantes = new JTextField(s.getPlacesRestantes() != null ? String.valueOf(s.getPlacesRestantes()) : "");

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        addRow(panel, gbc, "NuméroSession", numero);
        addRow(panel, gbc, "CodeFormation", codeFormation);
        addRow(panel, gbc, "DateDebut (YYYY-MM-DD)", dateDebut);
        addRow(panel, gbc, "DateFin (YYYY-MM-DD)", dateFin);
        addRow(panel, gbc, "Lieu", lieu);
        addRow(panel, gbc, "Salle", salle);
        addRow(panel, gbc, "PlacesTotal", placesTotal);
        addRow(panel, gbc, "PlacesRestantes", placesRestantes);

        int res = JOptionPane.showConfirmDialog(parent, panel, "Session", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) {
            return false;
        }

        String numeroV = numero.getText().trim();
        String debutV = dateDebut.getText().trim();
        String finV = dateFin.getText().trim();
        if (numeroV.isEmpty() || debutV.isEmpty() || finV.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "NuméroSession, DateDebut et DateFin sont obligatoires.", "Validation", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        s.setNumeroSession(numeroV);
        s.setCodeFormation(emptyToNull(codeFormation.getText()));
        s.setDateDebut(debutV);
        s.setDateFin(finV);
        s.setLieu(emptyToNull(lieu.getText()));
        s.setSalle(emptyToNull(salle.getText()));
        s.setPlacesTotal(parseIntOrNull(placesTotal.getText()));
        s.setPlacesRestantes(parseIntOrNull(placesRestantes.getText()));
        return true;
    }

    private static void addRow(JPanel panel, GridBagConstraints gbc, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, gbc);
        gbc.gridy++;
    }

    private static String value(String v) {
        return v == null ? "" : v;
    }

    private static String emptyToNull(String v) {
        String t = v == null ? "" : v.trim();
        return t.isEmpty() ? null : t;
    }

    private static Integer parseIntOrNull(String v) {
        String t = v == null ? "" : v.trim();
        if (t.isEmpty()) return null;
        try {
            return Integer.parseInt(t);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

