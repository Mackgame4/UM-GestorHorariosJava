package Data;

import Classes.Aluno;
import Classes.UnidadeCurricular;
import UI.Notify;
import Utils.DataEncoder;

import java.sql.*;
import java.util.Set;
import java.util.TreeSet;

public class AlunoDAO {
    private Connection connection;
    private Statement statement;

    public AlunoDAO() {
        try {
            connection = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Create a student
    public boolean createAluno(Aluno aluno) {
        try {
            statement = connection.createStatement();
            String nome = aluno.getNome();
            String numero = aluno.getNumero();
            boolean estatuto = aluno.isEstatuto();
            String email = aluno.getEmail();
            String password = aluno.getPassword();
            password = DataEncoder.encode(password);
            // Check if user already exists
            ResultSet resultSet = statement.executeQuery("SELECT * FROM alunos WHERE email = '" + email + "'");
            if (resultSet.next()) {
                //throw new SQLException("Aluno already exists!");
                Notify.error("Aluno already exists!");
            }
            statement.executeUpdate("INSERT INTO alunos (nome, numero, estatuto, email, password) VALUES ('" + nome + "', '" + numero + "', " + estatuto + ", '" + email + "', '" + password + "')");

        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public boolean updateUCs(Aluno aluno) {
        try {
            statement = connection.createStatement();
            for (UnidadeCurricular uc : aluno.getUCs()) {
                statement.executeUpdate("INSERT INTO aluno_UCs (aluno_num, uc_cod) VALUES ('" + aluno.getNumero() + "', '" + uc.getCodigo() + "')");
            }
        } catch (SQLException e) {
            return false;
        }

        return true;
    }

    // List all students
    public Set<Aluno> getAlunosList() {
        Set<Aluno> alunos = new TreeSet<>();
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM alunos");
            while (resultSet.next()) {
                String nome = resultSet.getString("nome");
                String numero = resultSet.getString("numero");
                boolean estatuto = resultSet.getBoolean("estatuto");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                password = DataEncoder.decode(password);
                Aluno aluno = new Aluno(nome, numero, estatuto, email, password);
                alunos.add(aluno);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alunos;
    }

    // Retrieve UCs for a specific aluno
    public Set<UnidadeCurricular> getUCsInscrito(String num) {
        Set<UnidadeCurricular> ucs = new TreeSet<>();
        try {
            statement = connection.createStatement();

            String query =
                    "SELECT uc.codigo, uc.nome " +
                            "FROM unidades_curriculares uc " +
                            "JOIN aluno_UCs auc ON uc.codigo = auc.uc_cod " +
                            "WHERE auc.aluno_num = '" + num + "'";
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                String codigo = rs.getString("codigo");
                String nome = rs.getString("nome");
                UnidadeCurricular uc = new UnidadeCurricular(nome, codigo);
                ucs.add(uc);
            }
        } catch (SQLException e) {
            return null;
        }
        return ucs;
    }

}
