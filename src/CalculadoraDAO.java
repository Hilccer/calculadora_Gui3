import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;


public class CalculadoraDAO {
    public void salvarOperacao (String expressao, double resultado) {
        String sql = "INSERT INTO historico (conta, RESULTADO) VALUES (?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement conecta = conn.prepareStatement(sql)) {
              conecta.setString(1, expressao);
              conecta.setDouble(2, resultado);
              conecta.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
