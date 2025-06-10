package Classes;

import java.util.Set;
import java.util.HashSet;

public class Aluno implements Comparable<Aluno> {
    private String nome;
    private String email;
    private String password;
    private String numero;
    private boolean estatuto; // 0: normal, 1: trabalhador-estudante/atleta
    private Set<UnidadeCurricular> ucs;
    private Horario horario;

    public Aluno() {
        this.nome = "";
        this.numero = "";
        this.estatuto = false;
        this.email = "";
        this.password = "";
        this.ucs = new HashSet<>();
        this.horario = null;
    }

    public Aluno(String nome, String numero, boolean estatuto, String email, String password) {
        this.nome = nome;
        this.numero = numero;
        this.estatuto = estatuto;
        this.email = email;
        this.password = password;
        this.ucs = new HashSet<>();
        this.horario = null;
    }

    public Aluno(String nome, String numero, boolean estatuto, String email, String password, Set<UnidadeCurricular> ucs) {
        this.nome = nome;
        this.numero = numero;
        this.estatuto = estatuto;
        this.email = email;
        this.password = password;
        this.ucs = ucs;
        this.horario = null;
    }

    public String getNome() {
        return nome;
    }

    public String getNumero() {
        return numero;
    }

    public boolean isEstatuto() {
        return estatuto;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Set<UnidadeCurricular> getUCs() {
        return ucs;
    }

    public Horario getHorario() {
        return this.horario;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setEstatuto(boolean estatuto) {
        this.estatuto = estatuto;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUCs(Set<UnidadeCurricular> ucs) {
        this.ucs = ucs;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public void addUc(UnidadeCurricular uc) {
        this.ucs.add(uc);
    }

    @Override
    public int compareTo(Aluno other) {
        return this.numero.compareTo(other.numero); // Compare by numero for ordering
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aluno aluno = (Aluno) o;
        return numero.equals(aluno.numero);
    }

    @Override
    public int hashCode() {
        return numero.hashCode();
    }

    @Override
    public String toString() {
        return "Aluno{" +
                "nome='" + nome + '\'' +
                ", numero='" + numero + '\'' +
                ", estatuto=" + estatuto +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", ucs=" + ucs +
                '}';
    }
}