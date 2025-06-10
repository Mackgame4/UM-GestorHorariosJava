package Classes;
import Data.UCDAO;

import java.util.Set;

public interface IGestorAlunosFacade {
    Set<Aluno> getAlunos();
    void listAlunos();
    void loadAlunos();
    void alunoPorInscrever(Aluno aluno);
    void addHorario(Aluno aluno, Horario horario);
    void listHorarios(UCDAO ucDAO);
}
