package Data;

import Classes.UnidadeCurricular;
import UI.Notify;

import java.sql.*;
import java.util.Set;
import java.util.TreeSet;

public class UCDAO {
    private Connection connection;
    private Statement statement;

    public UCDAO() {
        try {
            this.connection = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
        } catch (SQLException e) {}
    }

    public boolean createUC(UnidadeCurricular uc) {
        try {
            statement = connection.createStatement();
            String nome = uc.getNome();
            String codigo = uc.getCodigo();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM unidades_curriculares WHERE codigo = '" + codigo + "'");
            if (resultSet.next()) {
                Notify.error("UC already exists!");
            }
            statement.executeUpdate("INSERT INTO unidades_curriculares (nome, codigo) VALUES ('" + nome + "', '" + codigo + "')");
        } catch (SQLException e) {
            Notify.error(e.getMessage());
            return false;
        }
        return true;
    }

    public Set<UnidadeCurricular> getUCsList() {
        Set<UnidadeCurricular> ucs = new TreeSet<>();
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM unidades_curriculares");
            while (resultSet.next()) {
                String nome = resultSet.getString("nome");
                String codigo = resultSet.getString("codigo");
                UnidadeCurricular uc = new UnidadeCurricular(nome, codigo);
                ucs.add(uc);
            }
        } catch (SQLException e) {
            Notify.error(e.getMessage());
        }

        return ucs;
    }

    public String getNomeByCod(String cod) {
        String nome = null;
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT nome FROM unidades_curriculares WHERE codigo = '" + cod + "'");
            if (rs.next()) {
                nome = rs.getString("nome");
            }
        } catch (SQLException e) {
            Notify.error(e.getMessage());
        }

        return nome;
    }
}
