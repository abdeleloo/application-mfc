package fr.mfc.desktop.ui;

import fr.mfc.desktop.model.Stagiaire;

import javax.swing.*;
import java.awt.*;

public final class StagiaireDialog {
    private StagiaireDialog() {
    }

    public static boolean edit(Component parent, Stagiaire s, boolean isNew) {
        JTextField code     = new JTextField(s.getCodeStagiaire() != null ? s.getCodeStagiaire() : "");
        JTextField nom      = new JTextField(s.getNom() != null ? s.getNom() : "");
        JTextField prenom   = new JTextField(s.getPrenom() != null ? s.getPrenom() : "");
        JTextField email    = new JTextField(s.getEmail() != null ? s.getEmail() : "");
        JTextField tel      = new JTextField(s.getTel() != null ? s.getTel() : "");
        JTextField ville    = new JTextField(s.getVille() != null ? s.getVille() : "");
        JTextField societe  = new JTextField(s.getSociete() != null ? s.getSociete() : "");

        code.setEnabled(isNew);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        addRow(panel, gbc, "Code stagiaire *", code);
        addRow(panel, gbc, "Nom *",            nom);
        addRow(panel, gbc, "Prénom *",         prenom);
        addRow(panel, gbc, "Email",            email);
        addRow(panel, gbc, "Téléphone",        tel);
        addRow(panel, gbc, "Ville",            ville);
        addRow(panel, gbc, "Société",          societe);

        String title = isNew ? "Ajouter un stagiaire" : "Modifier le stagiaire";
        while (true) {
            int res = JOptionPane.showConfirmDialog(parent, panel, title,
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (res != JOptionPane.OK_OPTION) {
                return false;
            }
            String codeVal = code.getText().trim();
            String nomVal  = nom.getText().trim();
            String prenomVal = prenom.getText().trim();
            if (codeVal.isEmpty() || nomVal.isEmpty() || prenomVal.isEmpty()) {
                JOptionPane.showMessageDialog(parent,
                        "Code, Nom et Prénom sont obligatoires.", "Validation",
                        JOptionPane.WARNING_MESSAGE);
                continue;
            }
            s.setCodeStagiaire(codeVal);
            s.setNom(nomVal);
            s.setPrenom(prenomVal);
            s.setEmail(email.getText().trim());
            s.setTel(tel.getText().trim());
            s.setVille(ville.getText().trim());
            s.setSociete(societe.getText().trim());
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
