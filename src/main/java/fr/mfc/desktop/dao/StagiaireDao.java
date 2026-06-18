package fr.mfc.desktop.dao;

import fr.mfc.desktop.model.Stagiaire;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public final class StagiaireDao {
    private StagiaireDao() {
    }

    public static List<Stagiaire> list(Connection conn) throws Exception {
        List<Stagiaire> res = new ArrayList<>();
        String sql = "SELECT CodeStargiaire, Nom, Prenom, Email, Tel, Ville, `Societé` AS Societe FROM stagiaires ORDER BY Nom";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                res.add(map(rs));
            }
        }
        return res;
    }

    public static void upsert(Connection conn, Stagiaire s) throws Exception {
        String sql = "INSERT INTO stagiaires (CodeStargiaire, Nom, Prenom, Email, Tel, Ville, `Societé`)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?)" +
                " ON DUPLICATE KEY UPDATE Nom=VALUES(Nom), Prenom=VALUES(Prenom)," +
                " Email=VALUES(Email), Tel=VALUES(Tel), Ville=VALUES(Ville), `Societé`=VALUES(`Societé`)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getCodeStagiaire());
            ps.setString(2, s.getNom());
            ps.setString(3, s.getPrenom());
            ps.setString(4, s.getEmail());
            ps.setString(5, s.getTel());
            ps.setString(6, s.getVille());
            ps.setString(7, s.getSociete());
            ps.executeUpdate();
        }
    }

    public static void delete(Connection conn, String code) throws Exception {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM stagiaires WHERE CodeStargiaire = ?")) {
            ps.setString(1, code);
            ps.executeUpdate();
        }
    }

    private static Stagiaire map(ResultSet rs) throws Exception {
        Stagiaire s = new Stagiaire();
        s.setCodeStagiaire(rs.getString("CodeStargiaire"));
        s.setNom(rs.getString("Nom"));
        s.setPrenom(rs.getString("Prenom"));
        s.setEmail(rs.getString("Email"));
        s.setTel(rs.getString("Tel"));
        s.setVille(rs.getString("Ville"));
        s.setSociete(rs.getString("Societe"));
        return s;
    }
}

