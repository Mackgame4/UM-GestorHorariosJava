package Classes;

import Data.*;
import UI.Notify;
import Utils.CSVparser;

import java.util.*;

public class GestorAlunosFacade implements IGestorAlunosFacade {
    private AlunoDAO alunoDAO;
    private HorarioDAO horarioDAO;
    private Set<Aluno> alunos; // Professor é que vai dar load através do menu não é o construtor da classe, o construtor pode é trazer os já registados na base de dados
    private Set<Aluno> naoInscritos;
    private Map<String,Horario> horarios;

    public GestorAlunosFacade(AlunoDAO alunoDAO, HorarioDAO horarioDAO) {
        this.alunoDAO = alunoDAO;
        this.alunos = alunoDAO.getAlunosList();
        this.horarioDAO = horarioDAO;
        this.horarios = new HashMap<>();
        this.naoInscritos = new HashSet<>();
    }

    public Set<Aluno> getAlunos() {
        return this.alunos;
    }

    public Set<Aluno> getNaoInscritos() {
        return this.naoInscritos;
    }

    public void listAlunos() {
        for (Aluno aluno : alunos) {
            Notify.notify("info", aluno.getNumero()+ ":", aluno.toString());
        }
    }

    public void loadAlunos() {
        Map<String, Aluno> listAlunos = CSVparser.parseAlunosCsv();

        for (Aluno a : listAlunos.values()) {
            a.setPassword("password"); // NOTE: for test purposes all users have the same password

            // Check if the student already exists
            Aluno existingAluno = this.alunos.stream()
                    .filter(aluno -> aluno.getNumero().equals(a.getNumero()))
                    .findFirst()
                    .orElse(null);

            if (existingAluno != null) {
                // Replace the existing student
                this.alunos.remove(existingAluno);
                Notify.debug("Replaced existing student with numero: " + a.getNumero());
            }

            // Add the new/updated student
            this.alunos.add(a);
            // Also add/update in the database
            alunoDAO.createAluno(a);
        }
    }

    public void storeUCsAlunos() {
        for (Aluno aluno : this.alunos) {
            this.alunoDAO.updateUCs(aluno);
        }
    }

    public void alunoPorInscrever(Aluno aluno) {
        this.naoInscritos.add(aluno);
    }

    public void addHorario(Aluno aluno, Horario horario) {
        this.horarios.put(aluno.getNumero(), horario);
        this.horarioDAO.createHorario(horario, aluno);
    }

    public Horario getHorarioAluno(Aluno a) {
        // first load the horario from the database
        if (!this.horarios.containsKey(a.getNumero())) {
            Horario h = this.horarioDAO.getHorarioAluno(a);
            if (h != null) {
                this.horarios.put(a.getNumero(), h);
                a.setHorario(h);
            }
        }
        return this.horarios.get(a.getNumero());
    }

    public Aluno alunoNaoInscrito(String code) {
        Set<Aluno> nI = this.getNaoInscritos();

        Aluno aluno = nI.stream()
                .filter(a -> a.getNumero().equals(code))
                .findFirst()
                .orElse(null);

        if (aluno == null) {
            Notify.error("O aluno não apresenta UCs por inscrever.");
            return null;
        }

        return aluno;
    }

    public Set<UnidadeCurricular> getUCsAluno(Aluno a) {
        return this.alunoDAO.getUCsInscrito(a.getNumero());
    }


    public void listHorarios(UCDAO ucDAO) {
        Map<String, Horario> horariosList = this.horarioDAO.getHorariosList(ucDAO);
        for (Horario h : horariosList.values()) {
            Notify.notify("info", h.getCod() + ":", h.toString());
        }
    }
}
