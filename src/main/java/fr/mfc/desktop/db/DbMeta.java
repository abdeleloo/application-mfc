package fr.mfc.desktop.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public final class DbMeta {
    private DbMeta() {
    }

    public static boolean hasColumn(Connection conn, String table, String column) throws Exception {
        Set<String> columns = new HashSet<>();
        DatabaseMetaData meta = conn.getMetaData();
        try (ResultSet rs = meta.getColumns(conn.getCatalog(), null, table, null)) {
            while (rs.next()) {
                String col = rs.getString("COLUMN_NAME");
                if (col != null) {
                    columns.add(col.toLowerCase(Locale.ROOT));
                }
            }
        }
        return columns.contains(column.toLowerCase(Locale.ROOT));
    }
}
