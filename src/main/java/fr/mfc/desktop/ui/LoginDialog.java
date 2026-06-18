package fr.mfc.desktop.ui;

import fr.mfc.desktop.auth.UserContext;
import fr.mfc.desktop.config.AppConfig;
import fr.mfc.desktop.dao.AuthDao;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.Properties;

public final class LoginDialog {
    private LoginDialog() {
    }

    public static class Result {
        public final UserContext user;

        public Result(UserContext user) {
            this.user = user;
        }
    }

    public static Result show(Component parent, Connection conn) throws Exception {
        Properties props = AppConfig.load();

        JComboBox<String> role = new JComboBox<>(new String[]{"Stagiaire", "Formateur", "Admin"});
        JTextField code = new JTextField();
        JTextField email = new JTextField();
        JPasswordField password = new JPasswordField();
        JLabel hint = new JLabel("Stagiaire/Formateur : Code + Email");

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        addRow(panel, gbc, "Rôle", role);
        addRow(panel, gbc, "Code/Matricule", code);
        addRow(panel, gbc, "Email", email);
        addRow(panel, gbc, "Mot de passe (Admin)", password);

        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        panel.add(hint, gbc);

        role.addActionListener(e -> {
            String r = (String) role.getSelectedItem();
            boolean isAdmin = "Admin".equals(r);
            code.setEnabled(!isAdmin);
            email.setEnabled(!isAdmin);
            password.setEnabled(isAdmin);
            hint.setText(isAdmin ? "Admin : identifiants depuis app.properties" : "Stagiaire/Formateur : Code + Email");
        });
        role.setSelectedIndex(0);

        while (true) {
            int res = JOptionPane.showConfirmDialog(parent, panel, "Connexion", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (res != JOptionPane.OK_OPTION) {
                return null;
            }

            String r = (String) role.getSelectedItem();
            if ("Admin".equals(r)) {
                String adminLogin = props.getProperty("admin.login", "admin");
                String adminPass = props.getProperty("admin.password", "admin123");
                String entered = new String(password.getPassword());
                if (!entered.equals(adminPass)) {
                    JOptionPane.showMessageDialog(parent, "Mot de passe admin incorrect.", "Connexion", JOptionPane.WARNING_MESSAGE);
                    continue;
                }
                return new Result(new UserContext(UserContext.Role.ADMIN, adminLogin, "Admin", "", null));
            }

            String c = code.getText().trim();
            String m = email.getText().trim();
            if (c.isEmpty() || m.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "Code/Matricule et Email sont obligatoires.", "Connexion", JOptionPane.WARNING_MESSAGE);
                continue;
            }

            UserContext user;
            if ("Stagiaire".equals(r)) {
                user = AuthDao.loginStagiaire(conn, c, m);
            } else {
                user = AuthDao.loginFormateur(conn, c, m);
            }

            if (user == null) {
                JOptionPane.showMessageDialog(parent, "Identifiants introuvables.", "Connexion", JOptionPane.WARNING_MESSAGE);
                continue;
            }

            return new Result(user);
        }
    }

    private static void addRow(JPanel panel, GridBagConstraints gbc, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
        gbc.gridy++;
    }
}

