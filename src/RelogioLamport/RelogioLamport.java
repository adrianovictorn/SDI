package RelogioLamport;

class RelogioLamport {
    private int tempo;

    public RelogioLamport() {
        this.tempo = 0;
    }

    public synchronized int incrementar() {
        return ++tempo;
    }

    public synchronized int atualizar(int tempoRecebido) {
        tempo = Math.max(tempo, tempoRecebido) + 1;
        return tempo;
    }

    public synchronized int getTempo() {
        return tempo;
    }
}
