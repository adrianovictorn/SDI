package sistemasdistribuidos.lamport;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// Classe que representa um nó em um sistema distribuído utilizando o Relógio de Lamport.

public class No {

    private final String nome;                    // Nome do nó para identificação
    private final RelogioLamport relogio;           // Relógio lógico do nó
    private final BlockingQueue<Mensagem> filaMensagens; // Fila de mensagens recebidas

    // Construtor que inicializa o nó com seu nome, relógio lógico e fila de mensagens. 
     
    public No(String nome) {
        this.nome = nome;
        this.relogio = new RelogioLamport();
        this.filaMensagens = new LinkedBlockingQueue<>();
    }

    //Simula a ocorrência de um evento local, incrementando o relógio lógico.

    public void eventoLocal(String descricaoEvento) {
        int timestamp = relogio.incrementar();
        System.out.println(nome + " executou evento: '" + descricaoEvento + "' no tempo logico " + timestamp);
    }

    // Envia uma mensagem para um nó destinatário e envia para o nó destinatário.
    public void enviarMensagem(No destinatario, String conteudo) {
        int timestamp = relogio.incrementar();
        // Cria a mensagem incluindo o identificador deste nó como origem
        Mensagem mensagem = new Mensagem(timestamp, conteudo, this.nome);
        destinatario.receberMensagem(mensagem);
        System.out.println(nome + " enviou mensagem: '" + conteudo + "' com timestamp " + timestamp);
    }

    /**
     * Recebe uma mensagem de outro nó.
     * Valida a origem (não nula nem vazia) e o timestamp (deve ser >= 0) antes de atualizar o relógio.
     * Se a mensagem for considerada inválida, ela é ignorada.
     */

    public void receberMensagem(Mensagem mensagem) {
        // Validação da origem: não pode ser nula ou vazia
        if (mensagem.getOrigem() == null || mensagem.getOrigem().trim().isEmpty()) {
            System.out.println(nome + " recebeu mensagem com origem inválida. Mensagem ignorada.");
            return;
        }
        // Validação do timestamp: deve ser maior ou igual a zero
        if (mensagem.getTimestamp() < 0) {
            System.out.println(nome + " recebeu mensagem com timestamp inválido (" + mensagem.getTimestamp() + "). Mensagem ignorada.");
            return;
        }
        // Atualiza o relógio com o timestamp da mensagem, conforme o algoritmo de Lamport
        relogio.atualizar(mensagem.getTimestamp());
        // Adiciona a mensagem à fila para processamento posterior
        filaMensagens.add(mensagem);
        System.out.println(nome + " recebeu mensagem: '" + mensagem.getConteudo() + "' com timestamp " 
                           + mensagem.getTimestamp() + " da origem " + mensagem.getOrigem());
    }

    // Processa as mensagens armazenadas na fila, exibindo o conteúdo e o estado atual do relógio.

    public void processarEventos() {
        while (!filaMensagens.isEmpty()) {
            Mensagem mensagem = filaMensagens.poll();
            System.out.println(nome + " processando mensagem: '" + mensagem.getConteudo() 
                               + "' no tempo logico " + relogio.getTempo());
        }
    }
}
