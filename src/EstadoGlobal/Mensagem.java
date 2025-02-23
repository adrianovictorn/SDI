package EstadoGlobal;

public class Mensagem {
    private TipoMensagem tipo;
    private String conteudo;
    private String remetente;

    public Mensagem(TipoMensagem tipo, String conteudo, String remetente) {
        this.tipo = tipo;
        this.conteudo = conteudo;
        this.remetente = remetente;
    }

    public TipoMensagem getTipo() {
        return tipo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public String getRemetente() {
        return remetente;
    }
}

