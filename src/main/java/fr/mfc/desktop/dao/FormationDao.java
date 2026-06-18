package fr.mfc.desktop.dao;

import fr.mfc.desktop.db.DbMeta;
import fr.mfc.desktop.model.Formation;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public final class FormationDao {
    private FormationDao() {
    }

    public static List<Formation> list(Connection conn) throws Exception {
        boolean hasCategorie = DbMeta.hasColumn(conn, "Formations", "Categorie") || DbMeta.hasColumn(conn, "formations", "Categorie");
        boolean hasTypes = DbMeta.hasColumn(conn, "Formations", "Types") || DbMeta.hasColumn(conn, "formations", "Types");
        boolean hasDescription = DbMeta.hasColumn(conn, "Formations", "Description") || DbMeta.hasColumn(conn, "formations", "Description");
        boolean hasDuree = DbMeta.hasColumn(conn, "Formations", "DureeJours") || DbMeta.hasColumn(conn, "formations", "DureeJours");
        boolean hasPrix = DbMeta.hasColumn(conn, "Formations", "Prix") || DbMeta.hasColumn(conn, "formations", "Prix");
        boolean hasNiveau = DbMeta.hasColumn(conn, "Formations", "Niveau") || DbMeta.hasColumn(conn, "formations", "Niveau");

        StringBuilder sql = new StringBuilder("SELECT CodeFormation, Nom");
        if (hasCategorie) sql.append(", Categorie");
        if (hasTypes) sql.append(", Types");
        if (hasDescription) sql.append(", Description");
        if (hasDuree) sql.append(", DureeJours");
        if (hasPrix) sql.append(", Prix");
        if (hasNiveau) sql.append(", Niveau");
        sql.append(" FROM formations ORDER BY Nom");

        List<Formation> res = new ArrayList<>();
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql.toString())) {
            while (rs.next()) {
                Formation f = new Formation();
                f.setCodeFormation(rs.getString("CodeFormation"));
                f.setNom(rs.getString("Nom"));
                if (hasCategorie) f.setCategorie(rs.getString("Categorie"));
                if (hasTypes) f.setTypes(rs.getString("Types"));
                if (hasDescription) f.setDescription(rs.getString("Description"));
                if (hasDuree) {
                    int v = rs.getInt("DureeJours");
                    f.setDureeJours(rs.wasNull() ? null : v);
                }
                if (hasPrix) f.setPrix(rs.getBigDecimal("Prix"));
                if (hasNiveau) f.setNiveau(rs.getString("Niveau"));
                res.add(f);
            }
        }
        return res;
    }

    public static void upsert(Connection conn, Formation f) throws Exception {
        boolean hasCategorie = DbMeta.hasColumn(conn, "Formations", "Categorie") || DbMeta.hasColumn(conn, "formations", "Categorie");
        boolean hasTypes = DbMeta.hasColumn(conn, "Formations", "Types") || DbMeta.hasColumn(conn, "formations", "Types");
        boolean hasDescription = DbMeta.hasColumn(conn, "Formations", "Description") || DbMeta.hasColumn(conn, "formations", "Description");
        boolean hasDuree = DbMeta.hasColumn(conn, "Formations", "DureeJours") || DbMeta.hasColumn(conn, "formations", "DureeJours");
        boolean hasPrix = DbMeta.hasColumn(conn, "Formations", "Prix") || DbMeta.hasColumn(conn, "formations", "Prix");
        boolean hasNiveau = DbMeta.hasColumn(conn, "Formations", "Niveau") || DbMeta.hasColumn(conn, "formations", "Niveau");

        StringBuilder cols = new StringBuilder("CodeFormation, Nom");
        StringBuilder vals = new StringBuilder("?, ?");
        List<Object> params = new ArrayList<>();
        params.add(f.getCodeFormation());
        params.add(f.getNom());

        if (hasCategorie) {
            cols.append(", Categorie");
            vals.append(", ?");
            params.add(f.getCategorie());
        }
        if (hasTypes) {
            cols.append(", Types");
            vals.append(", ?");
            params.add(f.getTypes());
        }
        if (hasDescription) {
            cols.append(", Description");
            vals.append(", ?");
            params.add(f.getDescription());
        }
        if (hasDuree) {
            cols.append(", DureeJours");
            vals.append(", ?");
            params.add(f.getDureeJours());
        }
        if (hasPrix) {
            cols.append(", Prix");
            vals.append(", ?");
            params.add(f.getPrix());
        }
        if (hasNiveau) {
            cols.append(", Niveau");
            vals.append(", ?");
            params.add(f.getNiveau());
        }

        StringBuilder updates = new StringBuilder("Nom=VALUES(Nom)");
        if (hasCategorie) updates.append(", Categorie=VALUES(Categorie)");
        if (hasTypes) updates.append(", Types=VALUES(Types)");
        if (hasDescription) updates.append(", Description=VALUES(Description)");
        if (hasDuree) updates.append(", DureeJours=VALUES(DureeJours)");
        if (hasPrix) updates.append(", Prix=VALUES(Prix)");
        if (hasNiveau) updates.append(", Niveau=VALUES(Niveau)");

        String sql = "INSERT INTO formations (" + cols + ") VALUES (" + vals + ") ON DUPLICATE KEY UPDATE " + updates;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                Object p = params.get(i);
                if (p instanceof BigDecimal) {
                    ps.setBigDecimal(i + 1, (BigDecimal) p);
                } else if (p instanceof Integer) {
                    ps.setInt(i + 1, (Integer) p);
                } else {
                    ps.setObject(i + 1, p);
                }
            }
            ps.executeUpdate();
        }
    }

    public static void delete(Connection conn, String codeFormation) throws Exception {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM formations WHERE CodeFormation = ?")) {
            ps.setString(1, codeFormation);
            ps.executeUpdate();
        }
    }
}
