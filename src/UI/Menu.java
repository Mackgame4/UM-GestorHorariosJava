package UI;

import java.util.Set;

import Classes.*;
import Data.*;

public class Menu {
    private AlunoDAO alunoDAO = new AlunoDAO();
    private DiretorDAO diretorDAO = new DiretorDAO();
    private HorarioDAO horarioDAO = new HorarioDAO();
    private UCDAO ucDAO = new UCDAO();
    private TurnoDAO turnoDAO = new TurnoDAO();
    private GestorTurnosFacade gestorTurnosFacade = new GestorTurnosFacade(turnoDAO, ucDAO);
    private GestorAlunosFacade gestorAlunosFacade = new GestorAlunosFacade(alunoDAO, horarioDAO);
    private GestorHorarios gestor = new GestorHorarios(gestorAlunosFacade, gestorTurnosFacade);
    
    private void showMenuDiretor(Diretor diretor) {
        MenuManager diretorMenu = new MenuManager("Menu do Diretor", true);
        diretorMenu.option("Carregar Dados Alunos", () -> true, gestor::load_data);
        diretorMenu.option("Ver Listagem de Alunos", () -> true, gestor::getAlunos);
        diretorMenu.option("Ver Listagem de Turnos", () -> true, gestor::getTurnos);
        diretorMenu.option("Ver Listagem de UCs", () -> true, gestor::getUCs);
        //diretorMenu.option("Alocar Alunos", () -> true, () -> System.out.println("Alocar Alunos"));
        diretorMenu.option("Gerar Horário", () -> true, gestor::gerarHorarios);
        diretorMenu.option("Gerar Credenciais (SignUp)", () -> true, this::showSignUp);
        diretorMenu.option("Ver Horários", () -> true, gestor::showHorarios);
        diretorMenu.option("Ver Alunos Não Inscritos", () -> true, gestor::showNaoInscritos);
        diretorMenu.option("Alocação Manual", () -> true, this::showMenuAlocarManualmente);
        diretorMenu.show();
    }

    private void showMenuAlocarManualmente() {
        MenuManager alocar_menu = new MenuManager("Alocação manual");
        String code = alocar_menu.promptText(Terminal.ANSI_YELLOW + "Insira o código de aluno: " + Terminal.ANSI_RESET);
        Aluno aluno = gestorAlunosFacade.alunoNaoInscrito(code);
        if (aluno == null) return;

        String uc_code = alocar_menu.promptText(Terminal.ANSI_YELLOW + "Indique o código da UC que pretende alocar o aluno: " + Terminal.ANSI_RESET);
        Set<UnidadeCurricular> ucs = this.gestorAlunosFacade.getUCsAluno(aluno);
        Set<Turno> turnos = gestorTurnosFacade.getTurnoInscrever(aluno, uc_code, ucs, false);
        if (turnos == null) return;

        String turno_type = alocar_menu.promptText(Terminal.ANSI_YELLOW + "Indique o turno que pretende alocar o aluno: " + Terminal.ANSI_RESET);
        String turno_code = uc_code + ":" + turno_type;
        gestor.inscreverTurnoManualmente(turno_code, turnos, aluno);

        Notify.success("Aluno inscrito no turno " + turno_code);
    }


    private void showMenuAluno(Aluno aluno) {
        MenuManager alunoMenu = new MenuManager("Menu do Aluno", true);
        if (!gestor.isDataLoaded()) {
            Notify.warning("Os dados não estão carregados! A carregar...");
            gestor.load_data();
            //return;
        }
        alunoMenu.option("Ver Horário", () -> true, () -> gestor.showHorarioAluno(aluno));
        alunoMenu.option("Escolher Horário", aluno::isEstatuto, () -> escolherHorarioMenu(aluno));
        alunoMenu.show();
    }

    private void escolherHorarioMenu(Aluno aluno) {
        MenuManager escolherHorarioMenu = new MenuManager("Escolher Horário");
        if (aluno == null) {
            Notify.error("Aluno não encontrado!");
            return;
        }
        String uc_code = escolherHorarioMenu.promptText(Terminal.ANSI_YELLOW + "Indique o código da UC que pretende re-alocar: " + Terminal.ANSI_RESET);
        Set<UnidadeCurricular> ucs = this.gestorAlunosFacade.getUCsAluno(aluno);
        Set<Turno> turnos = gestorTurnosFacade.getTurnoInscrever(aluno, uc_code, ucs, true);
        if (turnos == null) return;

        String turno_type = escolherHorarioMenu.promptText(Terminal.ANSI_YELLOW + "Indique o turno que pretende alocar o aluno: " + Terminal.ANSI_RESET);
        String turno_code = uc_code + ":" + turno_type;
        gestor.inscreverTurnoManualmente(turno_code, turnos, aluno);

        Notify.success("Aluno inscrito no turno " + turno_code);
    }

    public void showLogin() {
        MenuManager loginMenu = new MenuManager("Login");
        Terminal.clear();
        loginMenu.drawTitle();
        String email = loginMenu.promptText(Terminal.ANSI_YELLOW + "Email: " + Terminal.ANSI_RESET);
        String password = loginMenu.promptText(Terminal.ANSI_YELLOW + "Password: " + Terminal.ANSI_RESET);
        Set<Aluno> alunos = alunoDAO.getAlunosList();
        Set<Diretor> diretores = diretorDAO.getDiretoresList();
        boolean found = false;
        for (Aluno aluno : alunos) {
            if (aluno.getEmail().equals(email) && aluno.getPassword().equals(password)) {
                showMenuAluno(aluno);
                found = true;
                break;
            }
        }
        if (!found) {
            for (Diretor diretor : diretores) {
                if (diretor.getEmail().equals(email) && diretor.getPassword().equals(password)) {
                    showMenuDiretor(diretor);
                    found = true;
                    break;
                }
            }
        }
        Notify.error("Email ou password incorretos!");
    }

    public void showAlunoSignUp(String email, String password) {
        MenuManager signUpMenu = new MenuManager("Aluno Sign Up");
        String nome = signUpMenu.promptText(Terminal.ANSI_YELLOW + "Nome: " + Terminal.ANSI_RESET);
        String numero = signUpMenu.promptText(Terminal.ANSI_YELLOW + "Número: " + Terminal.ANSI_RESET);
        String estatutoStr = signUpMenu.promptText(Terminal.ANSI_YELLOW + "Aluno com Estatuto (y/N): " + Terminal.ANSI_RESET);
        estatutoStr = estatutoStr.toLowerCase();
        boolean estatuto = estatutoStr.equalsIgnoreCase("y");
        Aluno aluno = new Aluno(nome, numero, estatuto, email, password);
        if (alunoDAO.createAluno(aluno)) {
            Notify.success("Aluno criado com sucesso!");
        } else {
            Notify.error("Erro ao criar aluno!");
        }
    }

    public void showDirectorSignUp(String email, String password) {
        MenuManager signUpMenu = new MenuManager("Director Sign Up");
        String nome = signUpMenu.promptText(Terminal.ANSI_YELLOW + "Nome: " + Terminal.ANSI_RESET);
        Diretor diretor = new Diretor(nome, email, password);
        if (diretorDAO.createDiretor(diretor)) {
            Notify.success("Diretor criado com sucesso!");
        } else {
            Notify.error("Erro ao criar diretor!");
        }
    }

    public void showSignUp() {
        MenuManager signUpMenu = new MenuManager("Sign Up");
        Terminal.clear();
        signUpMenu.drawTitle();
        String email = signUpMenu.promptText(Terminal.ANSI_YELLOW + "Email: " + Terminal.ANSI_RESET);
        String password = signUpMenu.promptText(Terminal.ANSI_YELLOW + "Password: " + Terminal.ANSI_RESET);
        String isDirectorStr = signUpMenu.promptText(Terminal.ANSI_YELLOW + "Esta conta é de diretor? (y/N): " + Terminal.ANSI_RESET);
        isDirectorStr = isDirectorStr.toLowerCase();
        boolean isDirector = isDirectorStr.equalsIgnoreCase("y");
        if (isDirector) {
            showDirectorSignUp(email, password);
        } else {
            showAlunoSignUp(email, password);
        }
    }

    public void showMenu() {
        MenuManager mainMenu = new MenuManager("Bem-vindo ao Gestor de Horários");
        mainMenu.option("Login", () -> true, this::showLogin);
        mainMenu.option("Sign Up", () -> true, this::showSignUp); // NOTE: put false in the lambda function to disable the option (Only director can create accounts)
        Terminal.clear();
        mainMenu.show();
    }
}