package fr.mfc.desktop;

import com.formdev.flatlaf.FlatLightLaf;
import fr.mfc.desktop.db.Db;
import fr.mfc.desktop.ui.LoginDialog;
import fr.mfc.desktop.ui.MainFrame;

import javax.swing.*;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8");

        SwingUtilities.invokeLater(() -> {
            try {
                FlatLightLaf.setup();
            } catch (Exception ignored) {
            }
            try {
                Connection conn = Db.connect();
                LoginDialog.Result result = LoginDialog.show(null, conn);
                if (result == null) {
                    conn.close();
                    return;
                }
                MainFrame frame = new MainFrame(conn, result.user);
                frame.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
