package RelogioLamport;

class Mensagem {
    private final int timestamp;
    private final String conteudo;

    public Mensagem(int timestamp, String conteudo) {
        this.timestamp = timestamp;
        this.conteudo = conteudo;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public String getConteudo() {
        return conteudo;
    }
}
