package Data;

import Classes.Aluno;
import Classes.Horario;
import Classes.Turno;
import Classes.UnidadeCurricular;
import UI.Notify;

import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map;
import java.util.HashMap;

public class HorarioDAO {
    private Connection connection;
    private Statement statement;

    public HorarioDAO() {
        try {
            this.connection = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
        } catch (SQLException e) {
            Notify.error("failed to establish database connection: " + e.getMessage());
        }
    }

    public boolean createHorario(Horario horario, Aluno aluno) {
        try {
            statement = connection.createStatement();
            String num = aluno.getNumero();

            ResultSet rs = statement.executeQuery("SELECT COUNT(*) AS count FROM horarios WHERE numAluno = '" + num + "'");
            if (rs.next() && rs.getInt("count") > 0) {
                return false;
            }

            // If no entry exists, proceed with the insertion
            statement.executeUpdate("INSERT INTO horarios (numAluno) VALUES ('" + num + "')");

            // Retrieve the auto-generated ID for this Horario
            rs = statement.executeQuery("SELECT LAST_INSERT_ID() AS id");
            int hID = -1;
            if (rs.next()) {
                hID = rs.getInt("id");
            }

            if (hID == -1) {
                Notify.error("failed to retrieve ID.");
                return false;
            }

            for (Turno turno : horario.getTurnosInscritos()) {
                statement.executeUpdate(
                        "INSERT INTO horarios_turnos (horarioID, turnoCod) VALUES ('" + hID + "', '" + turno.getCod() + "')"
                );
            }

        } catch (SQLException e) {
            Notify.error("failed to create Horario: " + e.getMessage());
            return false;
        }

        return true;
    }

    public Horario getHorarioAluno(Aluno a) {
        Horario horario = null;
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM horarios WHERE numAluno = '" + a.getNumero() + "'");
            if (rs.next()) {
                int horarioID = rs.getInt("id");
                Set<Turno> turnosInscritos = new TreeSet<>();
                ResultSet turnosRS = statement.executeQuery(
                        "SELECT t.* FROM turnos t " +
                                "JOIN horarios_turnos ht ON t.cod = ht.turnoCod " +
                                "WHERE ht.horarioID = " + horarioID
                );

                while (turnosRS.next()) {
                    String turnoCod = turnosRS.getString("cod");
                    int tipo = turnosRS.getInt("tipo");
                    String inicio_str = turnosRS.getString("inicio");
                    String fim_str = turnosRS.getString("fim");
                    String dia = turnosRS.getString("diaSemana");
                    String sala = turnosRS.getString("sala");
                    int capacidade = turnosRS.getInt("capacidade");
                    String idUC = turnosRS.getString("idUC");

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                    LocalTime inicio = LocalTime.parse(inicio_str, formatter);
                    LocalTime fim = LocalTime.parse(fim_str, formatter);

                    String nome = new UCDAO().getNomeByCod(idUC);
                    UnidadeCurricular uc = new UnidadeCurricular(nome, idUC);

                    Turno turno = new Turno(turnoCod, tipo, inicio, fim, dia, sala, uc, capacidade);
                    turnosInscritos.add(turno);
                }

                horario = new Horario(a.getNumero(), turnosInscritos);
            }
        } catch (SQLException e) {
            Notify.error("failed to retrieve Horario: " + e.getMessage());
        }

        return horario;
    }

    public Map<String, Horario> getHorariosList(UCDAO ucDAO) {
        Map<String, Horario> horarios = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        try {
            Statement horarioStatement = connection.createStatement();
            Statement turnoStatement = connection.createStatement();

            ResultSet rs = horarioStatement.executeQuery("SELECT * FROM horarios");

            while (rs.next()) {
                int horarioID = rs.getInt("id");
                String numAluno = rs.getString("numAluno");

                Set<Turno> turnosInscritos = new TreeSet<>();
                ResultSet turnosRS = turnoStatement.executeQuery(
                        "SELECT t.* FROM turnos t " +
                                "JOIN horarios_turnos ht ON t.cod = ht.turnoCod " +
                                "WHERE ht.horarioID = " + horarioID
                );

                while (turnosRS.next()) {
                    String turnoCod = turnosRS.getString("cod");
                    int tipo = turnosRS.getInt("tipo");
                    String inicio_str = turnosRS.getString("inicio");
                    String fim_str = turnosRS.getString("fim");
                    String dia = turnosRS.getString("diaSemana");
                    String sala = turnosRS.getString("sala");
                    int capacidade = turnosRS.getInt("capacidade");
                    String idUC = turnosRS.getString("idUC");

                    LocalTime inicio = LocalTime.parse(inicio_str, formatter);
                    LocalTime fim = LocalTime.parse(fim_str, formatter);

                    String nome = ucDAO.getNomeByCod(idUC);
                    UnidadeCurricular uc = new UnidadeCurricular(nome, idUC);

                    Turno turno = new Turno(turnoCod, tipo, inicio, fim, dia, sala, uc, capacidade);
                    turnosInscritos.add(turno);
                }

                Horario horario = new Horario(numAluno, turnosInscritos);
                horarios.put(numAluno, horario);
            }
        } catch (SQLException e) {
            Notify.error("failed to retrieve Horarios: " + e.getMessage());
        }

        return horarios;
    }

}
