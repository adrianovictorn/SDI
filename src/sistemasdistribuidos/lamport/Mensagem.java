package sistemasdistribuidos.lamport;


//Classe que encapsula uma mensagem enviada entre nós.

public class Mensagem {
    private final int timestamp;       // Timestamp associado à mensagem
    private final String conteudo;     // Conteúdo da mensagem
    private final String origem;       // Identificador do nó que enviou a mensagem

  
    // Construtor
    public Mensagem(int timestamp, String conteudo, String origem) {
        this.timestamp = timestamp;
        this.conteudo = conteudo;
        this.origem = origem;
    }

    // Retorna o timestamp da mensagem
    public int getTimestamp() {
        return timestamp;
    }

    // Retorna o conteúdo da mensagem
    public String getConteudo() {
        return conteudo;
    }

    // Retorna o identificador do nó de origem
    public String getOrigem() {
        return origem;
    }
}
