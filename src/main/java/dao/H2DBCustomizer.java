package dao;

import java.sql.*;

/***
 * Создание нужных таблиц, если их нет
 */
public class H2DBCustomizer {
    private static final String general_information =
            "  series_number  int primary key,\r\n" +
            "  manufacturer varchar(20),\r\n" +
            "  price varchar(20),\r\n" +
            "  number_products int,\r\n";

    private static final String desktop_computers_table = "create table  IF NOT EXISTS desktop_computers (\r\n" + "" +
            general_information +
            "  form_factor varchar(20),\r\n" +
            "CONSTRAINT check_ff CHECK \n" +
            "    (form_factor IN ('desktops', 'nettops','monoblocks'))" +
            "  );";

    private static final String laptop_table = "create table  IF NOT EXISTS laptop (\r\n" + "" +
            general_information +
            "  size int,\r\n" +
            "CONSTRAINT check_size CHECK \n" +
            "    (size IN (13, 14, 15, 17))"
            + "  );";

    private static final String hard_drives_table = "create table  IF NOT EXISTS hard_drives (\r\n" + "" +
            general_information +
            "   capacity int\r\n"
            + "  );";

    private static final String monitor_computers_table = "create table  IF NOT EXISTS monitor_computers (\r\n" + "" +
            general_information +
            "  diagonal varchar(20)\r\n"
            + "  );";


    public static void customize() throws SQLException {
        createTable(desktop_computers_table);
        createTable(laptop_table);
        createTable(hard_drives_table);
        createTable(monitor_computers_table);
    }

    private static void createTable(String createTableSQL) throws SQLException {
        Connection connection = H2JDBCUtils.getConnection();
        Statement statement = connection.createStatement();
        statement.execute(createTableSQL);
    }
}
