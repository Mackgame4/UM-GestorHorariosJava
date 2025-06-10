package Data;

import Classes.Turno;
import Classes.UnidadeCurricular;
import UI.Notify;

import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class TurnoDAO {
    private Connection connection;
    private Statement statement;

    public TurnoDAO() {
        try {
            this.connection = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
        } catch (SQLException e) {
            Notify.error("Failed to establish database connection: " + e.getMessage());
        }
    }

    public boolean createTurno(Turno turno) {
        try {
            statement = connection.createStatement();
            String cod = turno.getCod();
            int tipo = turno.getTipo();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime inicio = turno.getInicio();
            String inicio_str = inicio.format(formatter);
            LocalTime fim = turno.getFim();
            String fim_str = fim.format(formatter);
            String dia = turno.getDiaSemana();
            String sala = turno.getSala();
            int capacidade = turno.getCapacidade();
            String idUC = turno.getUC().getCodigo();

            ResultSet rs = statement.executeQuery(
                    "SELECT * FROM turnos WHERE cod = '" + cod + "' AND idUC = '" + idUC + "'"
            );

            if (rs.next()) {
                Notify.error("Turno already exists!");
                return false;
            }

            String query = "INSERT INTO turnos (cod, tipo, inicio, fim, diaSemana, sala, idUC, capacidade) " +
                    "VALUES ('" + cod + "', '" + tipo + "', '" + inicio_str + "', '" + fim_str + "', '" +
                    dia + "', '" + sala + "', '" + idUC + "', '" + capacidade + "')";

            statement.executeUpdate(query);
        } catch (SQLException e) {
            Notify.error(e.getMessage());
            return false;
        }

        return true;
    }

    public Map<String, Set<Turno>> getTurnosList(UCDAO ucDAO) {
        Map<String, Set<Turno>> turnos = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM turnos");

            while (rs.next()) {
                String cod = rs.getString("cod");
                int tipo = rs.getInt("tipo");
                String inicio_str = rs.getString("inicio");
                String fim_str = rs.getString("fim");
                String dia = rs.getString("diaSemana");
                String sala = rs.getString("sala");
                int capacidade = rs.getInt("capacidade");
                String idUC = rs.getString("idUC");

                String nome = ucDAO.getNomeByCod(idUC);
                UnidadeCurricular uc = new UnidadeCurricular(nome, cod);

                // Convert strings to LocalTime
                LocalTime inicio = LocalTime.parse(inicio_str, formatter);
                LocalTime fim = LocalTime.parse(fim_str, formatter);

                Turno turno = new Turno(cod, tipo, inicio, fim, dia, sala, uc, capacidade);

                turnos
                        .computeIfAbsent(idUC, k -> new TreeSet<>())
                        .add(turno);
            }
        } catch (SQLException e) {
            Notify.error(e.getMessage());
        }

        return turnos;
    }

    public Turno getTurnoByCode(String cod, UCDAO ucDAO) {
        Turno turno = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM turnos WHERE cod = '" + cod + "'");

            if (rs.next()) {
                String turnoCod = rs.getString("cod");
                int tipo = rs.getInt("tipo");
                String inicio_str = rs.getString("inicio");
                String fim_str = rs.getString("fim");
                String dia = rs.getString("diaSemana");
                String sala = rs.getString("sala");
                int capacidade = rs.getInt("capacidade");
                String idUC = rs.getString("idUC");

                String nome = ucDAO.getNomeByCod(idUC);
                UnidadeCurricular uc = new UnidadeCurricular(nome, idUC);

                LocalTime inicio = LocalTime.parse(inicio_str, formatter);
                LocalTime fim = LocalTime.parse(fim_str, formatter);

                turno = new Turno(turnoCod, tipo, inicio, fim, dia, sala, uc, capacidade);
            }
        } catch (SQLException e) {
            Notify.error("error fetching Turno by code: " + e.getMessage());
        }

        return turno;
    }
}