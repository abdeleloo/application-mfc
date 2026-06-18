package fr.mfc.desktop.ui;

import fr.mfc.desktop.model.Formateur;

import javax.swing.*;
import java.awt.*;

public final class FormateurDialog {
    private FormateurDialog() {
    }

    public static boolean edit(Component parent, Formateur f, boolean isNew) {
        JTextField matricule  = new JTextField(f.getMatricule() != null ? f.getMatricule() : "");
        JTextField nom        = new JTextField(f.getNom() != null ? f.getNom() : "");
        JTextField prenom     = new JTextField(f.getPrenom() != null ? f.getPrenom() : "");
        JTextField email      = new JTextField(f.getEmail() != null ? f.getEmail() : "");
        JTextField tel        = new JTextField(f.getTel() != null ? f.getTel() : "");
        JTextField specialite = new JTextField(f.getSpecialite() != null ? f.getSpecialite() : "");

        matricule.setEnabled(isNew);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        addRow(panel, gbc, "Matricule *",   matricule);
        addRow(panel, gbc, "Nom *",         nom);
        addRow(panel, gbc, "Prénom *",      prenom);
        addRow(panel, gbc, "Email",         email);
        addRow(panel, gbc, "Téléphone",     tel);
        addRow(panel, gbc, "Spécialité",    specialite);

        String title = isNew ? "Ajouter un formateur" : "Modifier le formateur";
        while (true) {
            int res = JOptionPane.showConfirmDialog(parent, panel, title,
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (res != JOptionPane.OK_OPTION) {
                return false;
            }
            String matriculeVal = matricule.getText().trim();
            String nomVal       = nom.getText().trim();
            String prenomVal    = prenom.getText().trim();
            if (matriculeVal.isEmpty() || nomVal.isEmpty() || prenomVal.isEmpty()) {
                JOptionPane.showMessageDialog(parent,
                        "Matricule, Nom et Prénom sont obligatoires.", "Validation",
                        JOptionPane.WARNING_MESSAGE);
                continue;
            }
            f.setMatricule(matriculeVal);
            f.setNom(nomVal);
            f.setPrenom(prenomVal);
            f.setEmail(email.getText().trim());
            f.setTel(tel.getText().trim());
            f.setSpecialite(specialite.getText().trim());
            return true;
        }
    }

    private static void addRow(JPanel panel, GridBagConstraints gbc, String label, JComponent field) {
        gbc.gridx = 0; gbc.gridwidth = 1; gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        field.setPreferredSize(new Dimension(220, field.getPreferredSize().height));
        panel.add(field, gbc);
        gbc.gridy++;
    }
}
