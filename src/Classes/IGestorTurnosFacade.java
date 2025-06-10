package Classes;

import java.util.Map;
import java.util.Set;

public interface IGestorTurnosFacade {

    void listUCs();

    void listTurnos();

    Map<String, Set<Turno>> getTurnos();

    UnidadeCurricular getUCbycod(String cod);

    void loadUCs(Set<Aluno> alunos);

    void loadTurnos();
}
