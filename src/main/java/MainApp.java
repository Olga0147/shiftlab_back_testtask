import dao.H2DBCustomizer;
import httpWorker.Server;

import java.io.IOException;
import java.sql.SQLException;

/***
 * Главный класс, создает в базе данных нужные таблицы и запускает сервер
 */
public class MainApp {

    public static void main(String[] args) throws SQLException {
        try {
            H2DBCustomizer.customize();
            new Server();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
