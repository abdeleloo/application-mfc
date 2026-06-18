package fr.mfc.desktop.dao;

import fr.mfc.desktop.db.DbMeta;
import fr.mfc.desktop.model.SessionMfc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public final class SessionDao {
    private SessionDao() {
    }

    public static List<SessionMfc> list(Connection conn) throws Exception {
        boolean hasCodeFormation = DbMeta.hasColumn(conn, "Session", "CodeFormation") || DbMeta.hasColumn(conn, "session", "CodeFormation");
        boolean hasLieu = DbMeta.hasColumn(conn, "Session", "Lieu") || DbMeta.hasColumn(conn, "session", "Lieu");
        boolean hasSalle = DbMeta.hasColumn(conn, "Session", "Salle") || DbMeta.hasColumn(conn, "session", "Salle");
        boolean hasPlacesTotal = DbMeta.hasColumn(conn, "Session", "PlacesTotal") || DbMeta.hasColumn(conn, "session", "PlacesTotal");
        boolean hasPlacesRestantes = DbMeta.hasColumn(conn, "Session", "PlacesRestantes") || DbMeta.hasColumn(conn, "session", "PlacesRestantes");

        StringBuilder sql = new StringBuilder("SELECT `NuméroSession` AS NumeroSession, DateDebut, DateFin");
        if (hasCodeFormation) sql.append(", CodeFormation");
        if (hasLieu) sql.append(", Lieu");
        if (hasSalle) sql.append(", Salle");
        if (hasPlacesTotal) sql.append(", PlacesTotal");
        if (hasPlacesRestantes) sql.append(", PlacesRestantes");
        sql.append(" FROM session ORDER BY DateDebut");

        List<SessionMfc> res = new ArrayList<>();
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql.toString())) {
            while (rs.next()) {
                SessionMfc s = new SessionMfc();
                s.setNumeroSession(rs.getString("NumeroSession"));
                s.setDateDebut(rs.getString("DateDebut"));
                s.setDateFin(rs.getString("DateFin"));
                if (hasCodeFormation) s.setCodeFormation(rs.getString("CodeFormation"));
                if (hasLieu) s.setLieu(rs.getString("Lieu"));
                if (hasSalle) s.setSalle(rs.getString("Salle"));
                if (hasPlacesTotal) {
                    int v = rs.getInt("PlacesTotal");
                    s.setPlacesTotal(rs.wasNull() ? null : v);
                }
                if (hasPlacesRestantes) {
                    int v = rs.getInt("PlacesRestantes");
                    s.setPlacesRestantes(rs.wasNull() ? null : v);
                }
                res.add(s);
            }
        }
        return res;
    }

    public static void upsert(Connection conn, SessionMfc s) throws Exception {
        boolean hasCodeFormation = DbMeta.hasColumn(conn, "Session", "CodeFormation") || DbMeta.hasColumn(conn, "session", "CodeFormation");
        boolean hasLieu = DbMeta.hasColumn(conn, "Session", "Lieu") || DbMeta.hasColumn(conn, "session", "Lieu");
        boolean hasSalle = DbMeta.hasColumn(conn, "Session", "Salle") || DbMeta.hasColumn(conn, "session", "Salle");
        boolean hasPlacesTotal = DbMeta.hasColumn(conn, "Session", "PlacesTotal") || DbMeta.hasColumn(conn, "session", "PlacesTotal");
        boolean hasPlacesRestantes = DbMeta.hasColumn(conn, "Session", "PlacesRestantes") || DbMeta.hasColumn(conn, "session", "PlacesRestantes");

        StringBuilder cols = new StringBuilder("`NuméroSession`, DateDebut, DateFin");
        StringBuilder vals = new StringBuilder("?, ?, ?");
        List<Object> params = new ArrayList<>();
        params.add(s.getNumeroSession());
        params.add(s.getDateDebut());
        params.add(s.getDateFin());

        if (hasCodeFormation) {
            cols.append(", CodeFormation");
            vals.append(", ?");
            params.add(s.getCodeFormation());
        }
        if (hasLieu) {
            cols.append(", Lieu");
            vals.append(", ?");
            params.add(s.getLieu());
        }
        if (hasSalle) {
            cols.append(", Salle");
            vals.append(", ?");
            params.add(s.getSalle());
        }
        if (hasPlacesTotal) {
            cols.append(", PlacesTotal");
            vals.append(", ?");
            params.add(s.getPlacesTotal());
        }
        if (hasPlacesRestantes) {
            cols.append(", PlacesRestantes");
            vals.append(", ?");
            params.add(s.getPlacesRestantes());
        }

        StringBuilder updates = new StringBuilder("DateDebut=VALUES(DateDebut), DateFin=VALUES(DateFin)");
        if (hasCodeFormation) updates.append(", CodeFormation=VALUES(CodeFormation)");
        if (hasLieu) updates.append(", Lieu=VALUES(Lieu)");
        if (hasSalle) updates.append(", Salle=VALUES(Salle)");
        if (hasPlacesTotal) updates.append(", PlacesTotal=VALUES(PlacesTotal)");
        if (hasPlacesRestantes) updates.append(", PlacesRestantes=VALUES(PlacesRestantes)");

        String sql = "INSERT INTO session (" + cols + ") VALUES (" + vals + ") ON DUPLICATE KEY UPDATE " + updates;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ps.executeUpdate();
        }
    }

    public static void delete(Connection conn, String numeroSession) throws Exception {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM session WHERE `NuméroSession` = ?")) {
            ps.setString(1, numeroSession);
            ps.executeUpdate();
        }
    }
}
