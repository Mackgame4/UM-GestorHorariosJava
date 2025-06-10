package Classes;

import UI.Notify;

import java.util.Optional;
import java.util.Set;

public class GestorHorarios implements IGestorHorarios {
    private GestorAlunosFacade alunosFacade;
    private GestorTurnosFacade turnosFacade;
    private boolean dataLoaded;

    public GestorHorarios(GestorAlunosFacade alunosFacade, GestorTurnosFacade turnosFacade) {
        this.alunosFacade = alunosFacade;
        this.turnosFacade = turnosFacade;
        this.dataLoaded = false;
    }

    public void getAlunos() {
        this.alunosFacade.listAlunos();
    }

    public void getUCs() {
        this.turnosFacade.listUCs();
    }

    public void getTurnos() {
        this.turnosFacade.listTurnos();
    }

    public void load_data() {
        this.alunosFacade.loadAlunos();
        this.turnosFacade.loadUCs(this.alunosFacade.getAlunos());
        this.alunosFacade.storeUCsAlunos();
        this.turnosFacade.loadTurnos();
        this.dataLoaded = true;
    }

    public boolean isDataLoaded() {
        return this.dataLoaded;
    }

    public void gerarHorarios() {
        for (Aluno aluno : this.alunosFacade.getAlunos()) {
            Horario h = new Horario();
            h.setCod(aluno.getNumero());

            for (UnidadeCurricular uc : aluno.getUCs()) {
                String cod = uc.getCodigo();
                Set<Turno> turnosUC = this.turnosFacade.getTurnos().get(cod);
                Optional<Turno> turnoT = turnosUC.stream()
                        .filter(turno -> turno.getTipo() == 0)
                        .filter(turno -> turno.getInscritos().size() < turno.getCapacidade())
                        .findFirst();

                if (turnoT.isPresent()) {
                    Turno t = turnoT.get();
                    h.inscreverTurno(t);
                    t.inscreverAluno(aluno);
                } else {
                    Notify.error("not possible to enroll student " + aluno.getNumero() + " in a T class.");
                    this.alunosFacade.alunoPorInscrever(aluno);
                }

                Optional<Turno> turnoTP = turnosUC.stream()
                        .filter(turno -> turno.getTipo() == 1)
                        .filter(turno -> turno.getInscritos().size() < turno.getCapacidade())
                        .findFirst();

                if (turnoTP.isPresent()) {
                    Turno tp = turnoTP.get();
                    h.inscreverTurno(tp);
                    tp.inscreverAluno(aluno);
                } else {
                    Notify.error("Not possible to enroll student in a TP class.");
                    this.alunosFacade.alunoPorInscrever(aluno);
                }

            }
            aluno.setHorario(h);
            this.alunosFacade.addHorario(aluno, h);
        }
    }

    public void showNaoInscritos() {
        Set<Aluno> naoInscritos = this.alunosFacade.getNaoInscritos();
        for (Aluno aluno : naoInscritos) {
            Notify.notify("info", aluno.getNumero() + ":", aluno.getNome());
        }
    }

    public void showHorarios() {
        this.alunosFacade.listHorarios(this.turnosFacade.getUCDAO());
    }

    public void inscreverTurnoManualmente(String turno_code, Set<Turno> turnos, Aluno aluno) {
        Turno turno = turnos.stream()
                .filter(t -> t.getCod().equals(turno_code))
                .findFirst()
                .orElse(null);

        if (turno == null) {
            Notify.error("O turno indicado não existe.");
            return;
        }

        aluno.getHorario().inscreverTurno(turno);
        turno.inscreverAluno(aluno);
    }

    public void showHorarioAluno(Aluno a) {
        Horario h = this.alunosFacade.getHorarioAluno(a);
        Notify.notify("info", h.getCod()+ ":", h.toString());
    }
}