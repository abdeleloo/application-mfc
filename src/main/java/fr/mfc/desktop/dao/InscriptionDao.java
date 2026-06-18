package fr.mfc.desktop.dao;

import fr.mfc.desktop.db.DbMeta;
import fr.mfc.desktop.model.InscriptionRow;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public final class InscriptionDao {
    private InscriptionDao() {
    }

    public static List<InscriptionRow> list(Connection conn) throws Exception {
        boolean hasSessionLink = DbMeta.hasColumn(conn, "Asso_7", "NuméroSession") || DbMeta.hasColumn(conn, "asso_7", "NuméroSession");
        boolean hasSessionCodeFormation = DbMeta.hasColumn(conn, "Session", "CodeFormation") || DbMeta.hasColumn(conn, "session", "CodeFormation");
        boolean hasLieu = DbMeta.hasColumn(conn, "Session", "Lieu") || DbMeta.hasColumn(conn, "session", "Lieu");

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT fi.NumeroInsciption AS NumeroInscription, fi.Date_ AS DateInscription, f.Nom AS Formation");
        if (hasSessionLink) {
            sql.append(", a7.`NuméroSession` AS SessionNumero");
        } else {
            sql.append(", NULL AS SessionNumero");
        }
        if (hasSessionLink) {
            sql.append(", s.DateDebut AS DateDebut, s.DateFin AS DateFin");
            if (hasLieu) {
                sql.append(", s.Lieu AS Lieu");
            } else {
                sql.append(", NULL AS Lieu");
            }
        } else {
            sql.append(", NULL AS DateDebut, NULL AS DateFin, NULL AS Lieu");
        }
        sql.append(" FROM fiche_inscription fi ");
        sql.append(" JOIN asso_5 a5 ON `5.NumeroInsciption = fi.NumeroInsciption ");
        sql.append(" JOIN formations f ON f.CodeFormation = a5.CodeFormation ");
        if (hasSessionLink) {
            sql.append(" LEFT JOIN asso_7 a7 ON a7.NumeroInsciption = fi.NumeroInsciption ");
            sql.append(" LEFT JOIN session s ON s.`NuméroSession` = a7.`NuméroSession` ");
            if (hasSessionCodeFormation) {
                sql.append(" AND (s.CodeFormation IS NULL OR s.CodeFormation = a5.CodeFormation) ");
            }
        }
        sql.append(" ORDER BY fi.Date_ DESC, fi.NumeroInsciption DESC ");

        List<InscriptionRow> res = new ArrayList<>();
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql.toString())) {
            while (rs.next()) {
                InscriptionRow r = new InscriptionRow();
                r.setNumeroInscription(rs.getString("NumeroInscription"));
                r.setDateInscription(rs.getString("DateInscription"));
                r.setFormation(rs.getString("Formation"));
                r.setSession(rs.getString("SessionNumero"));
                r.setDateDebut(rs.getString("DateDebut"));
                r.setDateFin(rs.getString("DateFin"));
                r.setLieu(rs.getString("Lieu"));
                res.add(r);
            }
        }
        return res;
    }
}

