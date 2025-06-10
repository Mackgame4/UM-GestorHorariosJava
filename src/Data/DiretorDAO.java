package Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;
import java.util.TreeSet;

import Classes.Diretor;
import UI.Notify;
import Utils.DataEncoder;

public class DiretorDAO {
    private Connection connection;
    private Statement statement;

    public DiretorDAO() {
        try {
            connection = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Create a director
    public boolean createDiretor(Diretor diretor) {
        try {
            statement = connection.createStatement();
            String nome = diretor.getNome();
            String email = diretor.getEmail();
            String password = diretor.getPassword();
            password = DataEncoder.encode(password);
            // Check if user already exists
            ResultSet resultSet = statement.executeQuery("SELECT * FROM diretores WHERE email = '" + email + "'");
            if (resultSet.next()) {
                //throw new SQLException("Director already exists!");
                Notify.error("Director already exists!");
            }
            statement.executeUpdate("INSERT INTO diretores (nome, email, password) VALUES ('" + nome + "', '" + email + "', '" + password + "')");
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    // List all directors
    public Set<Diretor> getDiretoresList() {
        Set<Diretor> diretores = new TreeSet<>();
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM diretores");
            while (resultSet.next()) {
                String nome = resultSet.getString("nome");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                password = DataEncoder.decode(password);
                Diretor diretor = new Diretor(nome, email, password);
                diretores.add(diretor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return diretores;
    }
}
