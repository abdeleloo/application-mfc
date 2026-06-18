package fr.mfc.desktop.dao;

import fr.mfc.desktop.model.Formateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public final class FormateurDao {
    private FormateurDao() {
    }

    public static List<Formateur> list(Connection conn) throws Exception {
        List<Formateur> res = new ArrayList<>();
        String sql = "SELECT Matricule, Nom, Prenom, Email, Tel, `Spécialité` AS Specialite FROM formateurs ORDER BY Nom";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                res.add(map(rs));
            }
        }
        return res;
    }

    public static void upsert(Connection conn, Formateur f) throws Exception {
        String sql = "INSERT INTO formateurs (Matricule, Nom, Prenom, Email, Tel, `Spécialité`)" +
                " VALUES (?, ?, ?, ?, ?, ?)" +
                " ON DUPLICATE KEY UPDATE Nom=VALUES(Nom), Prenom=VALUES(Prenom)," +
                " Email=VALUES(Email), Tel=VALUES(Tel), `Spécialité`=VALUES(`Spécialité`)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, f.getMatricule());
            ps.setString(2, f.getNom());
            ps.setString(3, f.getPrenom());
            ps.setString(4, f.getEmail());
            ps.setString(5, f.getTel());
            ps.setString(6, f.getSpecialite());
            ps.executeUpdate();
        }
    }

    public static void delete(Connection conn, String matricule) throws Exception {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM formateurs WHERE Matricule = ?")) {
            ps.setString(1, matricule);
            ps.executeUpdate();
        }
    }

    private static Formateur map(ResultSet rs) throws Exception {
        Formateur f = new Formateur();
        f.setMatricule(rs.getString("Matricule"));
        f.setNom(rs.getString("Nom"));
        f.setPrenom(rs.getString("Prenom"));
        f.setEmail(rs.getString("Email"));
        f.setTel(rs.getString("Tel"));
        f.setSpecialite(rs.getString("Specialite"));
        return f;
    }
}

