package Classes;

public class UnidadeCurricular implements Comparable<UnidadeCurricular> {
    private String nome;
    private String codigo;

    public UnidadeCurricular(String nome, String codigo) {
        this.nome = nome;
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Override
    public int compareTo(UnidadeCurricular uc) {
        return this.codigo.compareTo(uc.getCodigo());
    }

    @Override
    public String toString() {
        return this.codigo + ": " + this.nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnidadeCurricular uc = (UnidadeCurricular) o;
        return codigo.equals(uc.codigo);
    }
}