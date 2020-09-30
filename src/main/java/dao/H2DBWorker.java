package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class H2DBWorker {

    /***
     * Вставка в базу данных в таблицу
     * @param tableName - имя таблицы
     * @param param - вставляемые данные, где ключ = имя столбца и значение = вставляемое значение
     * @return - информация от базы данных
     */
    public static String insertH2(String tableName, Map<String, String> param) {

        StringBuilder insertStr = new StringBuilder("INSERT INTO " + tableName + "  ( ");
        StringBuilder insertStrTail = new StringBuilder(") VALUES ( ");

        for (Map.Entry<String, String> s:param.entrySet()) {
            insertStr.append(s.getKey()).append(" ,");
            insertStrTail.append(s.getValue()).append(" ,");
        }

        insertStr.delete(insertStr.lastIndexOf(","),insertStr.length());
        insertStrTail.delete(insertStrTail.lastIndexOf(","),insertStrTail.length()).append(");");

        return executeUpdateH2(insertStr, insertStrTail);
    }

    /***
     * Обновление значения из базы данных
     * @param tableName - имя таблицы
     * @param param - обновляемые данные, где ключ = имя столбца и значение = новое значение
     *              ОБЯЗАТЕЛЬНО ДОЛЖНА БЫТЬ ПАРА (series_number,<число>), по нему происходит идентификация продукта
     * @return - информация от базы данных
     */
    public static String updateH2(String tableName,Map<String, String> param) {

        StringBuilder insertStr = new StringBuilder("UPDATE " + tableName + " SET ");
        StringBuilder insertStrTail = new StringBuilder("WHERE ");

        for (Map.Entry<String, String> s:param.entrySet()) {
            if(s.getKey().equals("series_number")){
                insertStrTail.append("series_number = ").append(s.getValue());
            }
            else{
                insertStr.append(s.getKey()).append(" = '").append(s.getValue()).append("' ");
            }
        }

        return executeUpdateH2(insertStr, insertStrTail);
    }

    private static String executeUpdateH2(StringBuilder insertStr, StringBuilder insertStrTail) {
        int i;
        try (Connection connection = H2JDBCUtils.getConnection();
             PreparedStatement qwertyStatement = connection.prepareStatement(insertStr.append(insertStrTail).toString())) {

            i = qwertyStatement.executeUpdate();
            System.out.println(i);

        } catch (SQLException e) {
            return  e.getMessage();
        }

        return Integer.toString(i);
    }

    /***
     * Выборка по типу продукта
     * @param tableName - имя таблицы
     * @return - список продукции
     */
    public static String selectByType(String tableName){

        String qwerty = "select * from \n" + tableName;
        StringBuilder result = new StringBuilder();

        try (Connection connection = H2JDBCUtils.getConnection();

             PreparedStatement qwertyStatement = connection.prepareStatement(qwerty)) {
            ResultSet rs = qwertyStatement.executeQuery();

            result.append("| ");
            for (int i = 1; i < rs.getMetaData().getColumnCount(); i++) {
                result.append(rs.getMetaData().getColumnLabel(i)).append(" |");
            }
            result.append("<br>");

            while (rs.next()) {
                result.append("| ");
                for (int i = 1; i < rs.getMetaData().getColumnCount(); i++) {
                    result.append(rs.getString(i)).append(" |");
                }

                result.append("<br>");
            }

        } catch (SQLException e) {
            H2JDBCUtils.printSQLException(e);
        }
        return result.toString();
    }

    /***
     * Выборка по series_number
     * @param id = series_number
     * @return - список продукции
     */
    public static String selectById(Integer id){

        String str = "";

        String[] tables = {"DESKTOP_COMPUTERS", "HARD_DRIVES","LAPTOP","MONITOR_COMPUTERS"};

        for (String table : tables) {
            str = selectByTypeAndId(table, id);
            if (!str.equals("")) {
                break;
            }
        }

        return str;
    }

    public static String selectByTypeAndId(String tableName,Integer id){

        String qwerty = "select * from " + tableName+" where SERIES_NUMBER = "+id;
        StringBuilder result = new StringBuilder();

        try (Connection connection = H2JDBCUtils.getConnection();

             PreparedStatement qwertyStatement = connection.prepareStatement(qwerty)) {
            ResultSet rs = qwertyStatement.executeQuery();

            if(!rs.next()){
                return "";
            }

            result.append("| ");
            for (int i = 1; i < rs.getMetaData().getColumnCount(); i++) {
                result.append(rs.getMetaData().getColumnLabel(i)).append(" |");
            }
            result.append("<br>");

            do{
                result.append("| ");
                for (int i = 1; i < rs.getMetaData().getColumnCount(); i++) {
                    result.append(rs.getString(i)).append(" |");
                }

                result.append("<br>");
            }while (rs.next());

        } catch (SQLException e) {
            H2JDBCUtils.printSQLException(e);
        }
        return result.toString();
    }


}
