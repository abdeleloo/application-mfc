package fr.mfc.desktop.dao;

import fr.mfc.desktop.auth.UserContext;
import fr.mfc.desktop.db.DbMeta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public final class AuthDao {
    private AuthDao() {
    }

    public static UserContext loginStagiaire(Connection conn, String code, String email) throws Exception {
        String table = "stagiaires";
        String codeCol = "CodeStargiaire";
        String sql = "SELECT " + codeCol + ", Nom, Prenom, Email FROM " + table + " WHERE " + codeCol + " = ? AND Email = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            ps.setString(2, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return new UserContext(UserContext.Role.STAGIAIRE, rs.getString(codeCol), rs.getString("Nom"), rs.getString("Prenom"), rs.getString("Email"));
            }
        }
    }

    public static UserContext loginFormateur(Connection conn, String matricule, String email) throws Exception {
        String table = "formateurs";
        String codeCol = "Matricule";
        String sql = "SELECT " + codeCol + ", Nom, Prenom, Email FROM " + table + " WHERE " + codeCol + " = ? AND Email = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, matricule);
            ps.setString(2, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return new UserContext(UserContext.Role.FORMATEUR, rs.getString(codeCol), rs.getString("Nom"), rs.getString("Prenom"), rs.getString("Email"));
            }
        }
    }

    public static boolean hasStagiaires(Connection conn) throws Exception {
        return DbMeta.hasColumn(conn, "Stagiaires", "CodeStargiaire") || DbMeta.hasColumn(conn, "stagiaires", "CodeStargiaire");
    }
}

