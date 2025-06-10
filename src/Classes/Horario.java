package Classes;

import java.util.HashSet;
import java.util.Set;

public class Horario implements Comparable<Horario> {
    private String cod;
    private Set<Turno> turnosInscritos;

    public Horario() {
        turnosInscritos = new HashSet<Turno>();
        this.cod = null;
    }

    public Horario(String cod, Set<Turno> turnosInscritos) {
        this.cod = cod;
        this.turnosInscritos = turnosInscritos;
    }

    public void inscreverTurno(Turno turno) {
        this.turnosInscritos.add(turno);
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getCod() {
        return cod;
    }

    public Set<Turno> getTurnosInscritos() {
        return this.turnosInscritos;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Turno turno : turnosInscritos) {
            sb.append(turno.toString());
        }
        return sb.toString();
    }

    @Override
    public int compareTo(Horario o) {
        return 0;
    }
}
