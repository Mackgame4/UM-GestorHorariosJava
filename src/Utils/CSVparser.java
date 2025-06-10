package Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import Classes.Aluno;
import Classes.Turno;
import Classes.UnidadeCurricular;
import Data.UCDAO;
import UI.Notify;

public class CSVparser {
    /**
     * Parses a CSV file and returns a map of line index to line content.
     * 
     * @param filePath the path to the CSV file
     * @return a map where the key is the line number (starting from 1) and the value is the line content
     * @throws IOException if an I/O error occurs
     */
    public static Map<Integer, String> parseCsv(String filePath) {
        Map<Integer, String> data = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 1;
            reader.readLine(); // Read and skip the first line (header)
            while ((line = reader.readLine()) != null) {
                data.put(lineNumber, line);
                lineNumber++;
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
        return data;
    }

    // Return a map of (numero, Aluno)
    public static Map<String, Aluno> parseAlunosCsv() {
        Map<Integer, String> listAlunos = CSVparser.parseCsv("data/alunos.csv");
        Map<String, Aluno> alunos = new HashMap<>();
        int totalLines = 0;
        int skippedLines = 0;
        for (String aluno : listAlunos.values()) {
            totalLines++;
            String[] parts = aluno.split(";");
            // Check if the line has enough columns
            if (parts.length < 15) {
                Notify.debug("Skipping malformed line " + totalLines + ": " + aluno);
                skippedLines++;
                continue;
            }
            // Handle 'Regimes especiais de frequência' column
            boolean estatuto = parts.length > 15 && parts[15] != null && !parts[15].isEmpty();
            // Extract student data
            String numero = parts[11];
            String nome = parts[12];
            String email = parts[13];
            String cod_uc = parts[7];
            String nome_uc = parts[8];
            UnidadeCurricular uc = new UnidadeCurricular(nome_uc, cod_uc);
            // If the student already exists, add the UC to their list
            if (alunos.containsKey(numero)) {
                Notify.debug("Adding UC to existing student: " + numero);
                Aluno existingAluno = alunos.get(numero);
                existingAluno.addUc(uc);
            } else {
                // Create a new student and add their first UC
                Aluno a = new Aluno(nome, numero, estatuto, email, null);
                a.addUc(uc);
                alunos.put(numero, a);
            }
        }
        Notify.debug("Total lines processed: " + totalLines);
        Notify.debug("Total skipped lines: " + skippedLines);
        Notify.debug("Total entries in map: " + alunos.size());
        return alunos;
    }

    public static Map<String, Turno> parseTurnosCSV(UCDAO ucDAO) {
        Map<Integer, String> listTurnos = CSVparser.parseCsv("data/turnos.csv");
        Map<String, Turno> turnos = new HashMap<>();
        int totalLines = 0;
        int skippedLines = 0;
        for (String turno : listTurnos.values()) {
            totalLines++;
            String[] parts = turno.split(";");
            if (parts.length < 7) {
                Notify.debug("Skipping malformed line " + totalLines + ": " + turno);
                skippedLines++;
                continue;
            }
            String cod = parts[0];
            String tipo = parts[1];
            String start = parts[2];
            String end = parts[3];
            String dia = parts[4];
            String sala = parts[5];
            int capacidade = Integer.parseInt(parts[6]);
            String uc_code = cod.split(":")[0];
            LocalTime i = LocalTime.parse(start);
            LocalTime h = LocalTime.parse(end);
            // Determine the tipo (T = teorica, TP = pratica)
            int tipo_int;
            switch (tipo) {
                case "T":
                    tipo_int = 0; // teorica
                    break;
                case "TP":
                    tipo_int = 1; // pratica
                    break;
                case "PL":
                    tipo_int = 2; // laboratorial, if needed
                    break;
                default:
                    Notify.debug("Unknown tipo: " + tipo);
                    continue;
            }
            String nome_uc = ucDAO.getNomeByCod(uc_code);
            UnidadeCurricular uc = new UnidadeCurricular(nome_uc, uc_code);
            Turno t = new Turno(cod, tipo_int, i, h, dia, sala, uc, capacidade);
            if (turnos.containsKey(cod)) {
                Notify.debug("Updating existing turno: " + cod);
                Turno existing = turnos.get(cod);
                existing.setTipo(tipo_int);
                existing.setDiaSemana(dia);
                existing.setSala(sala);
                existing.setCapacidade(capacidade);
            } else {
                turnos.put(cod, t);
            }
        }
        Notify.debug("Total lines processed: " + totalLines);
        Notify.debug("Total skipped lines: " + skippedLines);
        Notify.debug("Total entries in map: " + turnos.size());
        return turnos;
    }
}