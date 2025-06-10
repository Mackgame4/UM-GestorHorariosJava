package UI;

import java.util.*;

public class MenuManager {
    // Message constants
    private static final String INVALID_OPTION = "Opção inválida!";
    private static final String UNAVAILABLE_OPTION = "Opção indisponível! Tente novamente.";
    private static final String NON_IMPLEMENTED = "ATENÇÃO: Opção não implementada!";
    private static final String EXIT = "Sair";
    private static final String BACK = "Voltar";
    private static final String CHOOSE_OPTION = "Escolha uma opção: ";
    private static final String EXITING = "Saindo...";

    public interface Handler {
        void execute();
    }

    public interface PreCondition {
        boolean validate();
    }

    private static Scanner is = new Scanner(System.in);
    private String titulo;
    private List<String> opcoes;
    private List<PreCondition> disponivel;
    private List<Handler> handlers;
    private boolean returnable;

    public MenuManager() {
        this("Menu", new ArrayList<>());
    }

    public MenuManager(String titulo) {
        this(titulo, new ArrayList<>());
    }

    public MenuManager(String titulo, boolean returnable) {
        this(titulo, new ArrayList<>(), returnable);
    }

    public MenuManager(String titulo, List<String> opcoes) {
        this.titulo = titulo;
        this.opcoes = new ArrayList<>(opcoes);
        this.disponivel = new ArrayList<>();
        this.handlers = new ArrayList<>();
        this.opcoes.forEach(s -> {
            this.disponivel.add(() -> true);
            this.handlers.add(() -> System.out.println(NON_IMPLEMENTED));
        });
        this.returnable = false;
    }

    public MenuManager(String titulo, List<String> opcoes, boolean returnable) {
        this(titulo, opcoes);
        this.returnable = returnable;
    }

    public void option(String name, PreCondition p, Handler h) {
        this.opcoes.add(name);
        this.disponivel.add(p);
        this.handlers.add(h);
    }

    public void runOnce() {
        int op;
        draw();
        op = readOption();
        if (op > 0 && !this.disponivel.get(op - 1).validate()) {
            Notify.error(UNAVAILABLE_OPTION);
        } else if (op > 0) {
            this.handlers.get(op - 1).execute();
        }
    }

    public void show() {
        int op;
        do {
            draw();
            op = readOption();
            if (op > 0 && !this.disponivel.get(op - 1).validate()) {
                Notify.error(UNAVAILABLE_OPTION);
            } else if (op > 0) {
                this.handlers.get(op - 1).execute();
            }
        } while (op != 0);
        if (!this.returnable) {
            exit();
        }
    }

    public void setPreCondition(int i, PreCondition b) {
        this.disponivel.set(i - 1, b);
    }

    public void setHandler(int i, Handler h) {
        this.handlers.set(i - 1, h);
    }

    public void drawTitle() {
        System.out.println(Terminal.ANSI_BLUE + "\n--- " + Terminal.ANSI_BLUE_BACKGROUND + this.titulo + Terminal.ANSI_RESET + Terminal.ANSI_BLUE + " ---" + Terminal.ANSI_RESET);
    }

    private void draw() {
        drawTitle();
        for (int i = 0; i < this.opcoes.size(); i++) {
            System.out.print(Terminal.ANSI_YELLOW);
            System.out.print(i + 1);
            System.out.print(". ");
            System.out.print(Terminal.ANSI_RESET);
            System.out.println(this.disponivel.get(i).validate() ? this.opcoes.get(i) : "(" + this.opcoes.get(i) + ")");
        }
        if (this.returnable) {
            System.out.println(Terminal.ANSI_YELLOW + "0. " + Terminal.ANSI_RESET + BACK);
        } else {
            System.out.println(Terminal.ANSI_YELLOW + "0. " + Terminal.ANSI_RESET + EXIT);
        }
    }

    private int readOption() {
        int op;
        System.out.print(Terminal.ANSI_YELLOW + CHOOSE_OPTION + Terminal.ANSI_RESET);
        try {
            String line = is.nextLine();
            op = Integer.parseInt(line);
        } catch (NumberFormatException e) {
            op = -1;
        }
        if (op < 0 || op > this.opcoes.size()) {
            Notify.error(INVALID_OPTION);
            op = -1;
        }
        return op;
    }

    public String promptText(String message) {
        System.out.print(message);
        return is.nextLine();
    }

    public static void exit() {
        Notify.notify("error", EXITING);
        System.exit(0);
    }
}