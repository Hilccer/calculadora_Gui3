import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private  static final String DATABASE = "historico_contas";

    private static final String USER = "root";
    private static final String PASSWORD = "teste";

    public static Connection getConnection() {
        String url = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?useSSL=false&serverTimezone=UTC";

        try{
            return DriverManager.getConnection(url, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco" + e.getMessage());
        }
    }
}

// criar usuario para este esquema no mysql, e criar um metodo de conexao get connection!