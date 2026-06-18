package fr.mfc.desktop.ui;

import fr.mfc.desktop.model.Formation;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public final class FormationDialog {
    private FormationDialog() {
    }

    public static boolean edit(Component parent, Formation f) {
        JTextField code = new JTextField(value(f.getCodeFormation()));
        JTextField nom = new JTextField(value(f.getNom()));
        JTextField categorie = new JTextField(value(f.getCategorie()));
        JTextField types = new JTextField(value(f.getTypes()));
        JTextArea description = new JTextArea(value(f.getDescription()), 4, 30);
        JTextField duree = new JTextField(f.getDureeJours() != null ? String.valueOf(f.getDureeJours()) : "");
        JTextField prix = new JTextField(f.getPrix() != null ? f.getPrix().toPlainString() : "");
        JTextField niveau = new JTextField(value(f.getNiveau()));

        description.setLineWrap(true);
        description.setWrapStyleWord(true);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        addRow(panel, gbc, "CodeFormation", code);
        addRow(panel, gbc, "Nom", nom);
        addRow(panel, gbc, "Catégorie", categorie);
        addRow(panel, gbc, "Types", types);
        addRow(panel, gbc, "Niveau", niveau);
        addRow(panel, gbc, "Durée (jours)", duree);
        addRow(panel, gbc, "Prix (€)", prix);

        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Description"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(new JScrollPane(description), gbc);
        gbc.gridy++;

        int res = JOptionPane.showConfirmDialog(parent, panel, "Formation", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) {
            return false;
        }

        String codeV = code.getText().trim();
        String nomV = nom.getText().trim();
        if (codeV.isEmpty() || nomV.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "CodeFormation et Nom sont obligatoires.", "Validation", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        f.setCodeFormation(codeV);
        f.setNom(nomV);
        f.setCategorie(emptyToNull(categorie.getText()));
        f.setTypes(emptyToNull(types.getText()));
        f.setNiveau(emptyToNull(niveau.getText()));
        f.setDescription(emptyToNull(description.getText()));

        Integer dureeV = parseIntOrNull(duree.getText());
        f.setDureeJours(dureeV);

        BigDecimal prixV = parseDecimalOrNull(prix.getText());
        f.setPrix(prixV);

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

    private static BigDecimal parseDecimalOrNull(String v) {
        String t = v == null ? "" : v.trim().replace(',', '.');
        if (t.isEmpty()) return null;
        try {
            return new BigDecimal(t);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

