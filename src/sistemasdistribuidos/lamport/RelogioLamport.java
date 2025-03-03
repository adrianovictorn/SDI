package sistemasdistribuidos.lamport;

// Classe que implementa o Relógio Lógico de Lamport.

class RelogioLamport {

    private int tempo; // Variável para armazenar o tempo lógico atual


    // Construtor que inicializa o relógio com tempo 0.
     
    public RelogioLamport() {
        this.tempo = 0;
    }

    /**
     * Método sincronizado para incrementar o tempo lógico.
     * Cada chamada representa a ocorrência de um evento.
     * 
     * @return O novo valor do tempo após o incremento.
     */
    public synchronized int incrementar() {
        return ++tempo;
    }

    //Método sincronizado para atualizar o relógio com um tempo recebido de uma mensagem. Compara o tempo atual com o tempo recebido e define o tempo como o maior entre eles, acrescido de 1.

    public synchronized int atualizar(int tempoRecebido) {
        tempo = Math.max(tempo, tempoRecebido) + 1;
        return tempo;
    }

    // Método sincronizado para obter o tempo lógico atual.

    public synchronized int getTempo() {
        return tempo;
    }
}
