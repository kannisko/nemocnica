package org.nemocnica.database;

import java.sql.ResultSet;
import java.sql.SQLException;

//enum z funkcjami
public enum ROW_EXPORTER {
    TO_SQL_INSERT(".sql", new SqlExporter()),
    TO_CSV(".csv", new CsvExporter());

    private String fileExtension;
    private IRowExporter rowExporter;

    private ROW_EXPORTER(String fileExtension, IRowExporter rowExporter) {
        this.fileExtension = fileExtension;
        this.rowExporter = rowExporter;
    }

    public String exportRow(ResultSet rs, int columnCount, String tableName) throws SQLException {
        return rowExporter.doExport(rs, columnCount, tableName);
    }

    public String getFileExtension() {
        return fileExtension;
    }

    private static interface IRowExporter {
        String doExport(ResultSet rs, int columnCount, String tableName) throws SQLException;
    }

    private static class SqlExporter implements IRowExporter {
        @Override
        public String doExport(ResultSet rs, int columnCount, String tableName) throws SQLException {
            String result = "INSERT INTO " + tableName + " VALUES(";
            for (int col = 1; col <= columnCount; col++) {
                if (col != 1) {
                    result += ", ";
                }

                Object object = rs.getObject(col);
                if (!(object instanceof String)) {
                    result += object;
                } else {
                    result += "'"+object+"'";
                }
            }
            result += ");";
            return result;
        }
    }

    private static class CsvExporter implements IRowExporter {

        @Override
        public String doExport(ResultSet rs, int columnCount,  String tableName) throws SQLException {
            String result = "";
            for (int col = 1; col <= columnCount; col++) {
                if (col != 1) {
                    result += ";";
                }
                result += rs.getObject(col);
            }
            return result;

        }
    }


}
