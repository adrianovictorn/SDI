package sistemasdistribuidos.chandy_lamport;


// Classe que encapsula uma mensagem enviada entre processos
public class Mensagem {
    // Tipo da mensagem: NORMAL ou MARCADOR (usado no snapshot)
    private TipoMensagem tipo;
    // Conteúdo da mensagem
    private String conteudo;
    // Identificador do remetente da mensagem
    private String remetente;

    // Construtor para criar uma mensagem com seu tipo, conteúdo e remetente
    public Mensagem(TipoMensagem tipo, String conteudo, String remetente) {
        this.tipo = tipo;
        this.conteudo = conteudo;
        this.remetente = remetente;
    }

    // Retorna o tipo da mensagem
    public TipoMensagem getTipo() {
        return tipo;
    }

    // Retorna o conteúdo da mensagem
    public String getConteudo() {
        return conteudo;
    }

    // Retorna o identificador do remetente
    public String getRemetente() {
        return remetente;
    }
}
