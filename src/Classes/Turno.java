package Classes;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public class Turno implements Comparable<Turno> {
    private String cod;
    private int tipo; // 0: teorica, 1: pratica, 2: laboratorial
    private LocalTime inicio;
    private LocalTime fim;
    private String diaSemana;
    private String sala;
    private UnidadeCurricular uc;
    private int capacidade;
    private Set<Aluno> inscritos;

    public Turno(String cod, int tipo, LocalTime inicio, LocalTime fim, String diaSemana, String sala, UnidadeCurricular uc, int capacidade) {
        this.cod = cod;
        this.tipo = tipo;
        this.inicio = inicio;
        this.fim = fim;
        this.diaSemana = diaSemana;
        this.sala = sala;
        this.uc = uc;
        this.capacidade = capacidade;
        this.inscritos = new HashSet<Aluno>();
    }

    public String getCod() {
        return cod;
    }

    public int getTipo() {
        return tipo;
    }

    public LocalTime getInicio() {
        return inicio;
    }

    public LocalTime getFim() {
        return fim;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public String getSala() {
        return sala;
    }

    public UnidadeCurricular getUC() {
        return uc;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public Set<Aluno> getInscritos() {
        return inscritos;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public void setInicio(LocalTime inicio) {
        this.inicio = inicio;
    }

    public void setFim(LocalTime fim) {
        this.fim = fim;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public void setUC(UnidadeCurricular uc) {
        this.uc = uc;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public void setInscritos(Set<Aluno> inscritos) {this.inscritos = inscritos;}

    public void inscreverAluno(Aluno aluno) {
        this.inscritos.add(aluno);
    }

    public int compareTo(Turno t) {
        return this.cod.compareTo(t.getCod());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Turno {")
                .append("cod = '").append(cod).append('\'')
                .append(", tipo = ").append(tipo)
                .append(", início = ").append(inicio)
                .append(", fim = ").append(fim)
                .append(", diaSemana = '").append(diaSemana).append('\'')
                .append(", sala = '").append(sala).append('\'')
                .append(", uc = ").append(uc.getCodigo() + " " + uc.getNome())
                .append(", capacidade = ").append(capacidade)
                .append('}');
        return sb.toString();
    }
}