package Classes;

import Data.TurnoDAO;
import Data.UCDAO;
import UI.Notify;
import Utils.CSVparser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GestorTurnosFacade implements IGestorTurnosFacade {
    private TurnoDAO turnoDAO;// Professor é que vai dar load através do menu não é o construtor da classe, o construtor pode é trazer os já registados na base de dados
    private UCDAO ucDAO;
    private Map<String,Set<Turno>> turnos;
    private Set<UnidadeCurricular> UCs;

    public GestorTurnosFacade() {
        this.turnoDAO = null;
        this.turnos = new HashMap<String,Set<Turno>>();
        this.UCs = new HashSet<UnidadeCurricular>();
    }

    public GestorTurnosFacade(TurnoDAO turnoDAO, UCDAO ucDAO) {
        this.turnoDAO = turnoDAO;
        this.ucDAO = ucDAO;
        this.turnos = turnoDAO.getTurnosList(this.ucDAO);
        this.UCs = ucDAO.getUCsList();
    }

    public UCDAO getUCDAO() {
        return this.ucDAO;
    }

    public TurnoDAO getTurnoDAO() {
        return this.turnoDAO;
    }

    public void listUCs() {
        for (UnidadeCurricular uc : this.UCs) {
            Notify.notify("info", uc.getCodigo() + ":", uc.getNome());
        }
    }

    public void listTurnos() {
        for (Set<Turno> turnosUC : this.turnos.values()) {
            for (Turno turno : turnosUC) {
                Notify.notify("info", turno.getCod() + ":", turno.toString());
            }
        }


    }

    public Map<String,Set<Turno>> getTurnos() {
        return this.turnos;
    }

    public Set<Turno> getTurnosbyUCcode(String codUC) {
        return this.turnos.get(codUC);
    }

    public UnidadeCurricular getUCbycod(String cod) {
        for (UnidadeCurricular uc : this.UCs) {
            if (uc.getCodigo().equals(cod)) {
                return uc;
            }
        }
        return null;
    }

    public Turno getTurnobycod(String cod) {
        for (Set<Turno> ts : this.turnos.values()) {
            for (Turno turno : ts) {
                if (turno.getCod().equals(cod)) {
                    return turno;
                }
            }
        }

        return null;
    }

    public void loadUCs (Set<Aluno> alunos){
        for (Aluno aluno : alunos) {
            for (UnidadeCurricular uc : aluno.getUCs()) {
                this.UCs.add(uc);
                ucDAO.createUC(uc);
            }
        }
    }

    /*
    public void loadTurnos() { // exemplo de turnos
        for (UnidadeCurricular uc : this.UCs) {
            Set<Turno> turnosUC= new HashSet<Turno>();
            Turno t = new Turno(uc.getCodigo() + ":T1", 0, LocalTime.of(14,00), LocalTime.of(16,00), "terça-feira", "Ed.2 2.01", uc, 20);
            turnosUC.add(t);
            turnoDAO.createTurno(t);
            Turno tp = new Turno(uc.getCodigo() + ":TP1", 1, LocalTime.of(16,00), LocalTime.of(18,00), "terça-feira", "Ed.1 1.11", uc, 10);
            turnosUC.add(tp);
            turnoDAO.createTurno(tp);
            this.turnos.put(uc.getCodigo(), turnosUC);
        }
    }
    */

    public void loadTurnos() {
        Map<String, Turno> listTurnos = CSVparser.parseTurnosCSV(ucDAO);

        for (Turno t : listTurnos.values()) {
            String uc_code = t.getUC().getCodigo();

            Set<Turno> existingTurnos = this.turnos.getOrDefault(uc_code, new HashSet<>());

            Turno existing = existingTurnos.stream()
                    .filter(turno -> turno.getCod().equals(t.getCod()))
                    .findFirst()
                    .orElse(null);

            if (existing != null) {
                existingTurnos.remove(existing);
                Notify.debug("replaced existing turno with cod: " + t.getCod() + " for UC: " + uc_code);
            }

            existingTurnos.add(t);

            this.turnos.put(uc_code, existingTurnos);

            turnoDAO.createTurno(t);
        }
    }

    public Set<Turno> getTurnoInscrever(Aluno aluno, String uc_code, Set<UnidadeCurricular> ucs, Boolean isRealoc) {
        UnidadeCurricular unidade_curricular = ucs.stream()
                .filter(uc -> uc.getCodigo().equals(uc_code))
                .findFirst()
                .orElse(null);

        if (unidade_curricular == null) {
            Notify.error("O aluno não está inscrito nessa UC.");
            return null;
        }

        Horario horario = aluno.getHorario();
        if (horario == null) {
            Notify.error("O aluno não tem horário.");
            return null;
        }
        Set<Turno> turnosInscritos = horario.getTurnosInscritos();
        boolean isInscrito = turnosInscritos.stream()
                .anyMatch(turno -> turno.getUC().equals(unidade_curricular));

        if (isInscrito && !isRealoc) {
            Notify.error("O aluno já está inscrito nessa UC.");
            return null;
        }

        Map<String, Set<Turno>> turnosMap = turnoDAO.getTurnosList(ucDAO);
        Set<Turno> turnos = turnosMap.get(uc_code);

        if (turnos == null || turnos.isEmpty()) {
            Notify.error("Não existem turnos para esta UC.");
            return null;
        }

        return turnos;
    }

}
