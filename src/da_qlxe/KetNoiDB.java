package da_qlxe;

import java.sql.Connection;
import java.sql.DriverManager;

public class KetNoiDB {
    private static final String DATABASE = "DA_QLXE";
    private static final String USER = "sa";
    private static final String PASS = "123";

    public static Connection getKetNoi() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // 1. Kết nối qua cổng chuẩn 1433 (nhanh nhất, chuẩn nhất)
            String[] urls = {
                "jdbc:sqlserver://localhost:1433;databaseName=" + DATABASE + ";encrypt=true;trustServerCertificate=true;",
                "jdbc:sqlserver://127.0.0.1:1433;databaseName=" + DATABASE + ";encrypt=true;trustServerCertificate=true;",
                "jdbc:sqlserver://DESKTOP-AGRCA7L\\SQLEXPRESS;databaseName=" + DATABASE + ";encrypt=true;trustServerCertificate=true;"
            };

            for (String url : urls) {
                // Thử tài khoản sa trước
                try {
                    Connection conn = DriverManager.getConnection(url, USER, PASS);
                    if (conn != null) return conn;
                } catch (Exception ignored) {}

                // Thử Windows Auth
                try {
                    Connection conn = DriverManager.getConnection(url + ";integratedSecurity=true;");
                    if (conn != null) return conn;
                } catch (Exception ignored) {}
            }

        } catch (Exception e) {
            System.err.println("Chưa kết nối được CSDL: " + e.getMessage());
        }
        return null;
    }
}
