package RelogioLamport;

class Mensagem {
    private final int timestamp;
    private final String conteudo;

    public Mensagem(int timestamp, String conteudo) {
        this.timestamp = timestamp;
        this.conteudo = conteudo;
    }

    //Retorna o timestamp
    public int getTimestamp() {
        return timestamp;
    }

    //Retorna o conteudo
    public String getConteudo() {
        return conteudo;
    }
}
