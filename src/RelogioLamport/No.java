package RelogioLamport;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class No {

    // Nome do nó para identificação.
    private final String nome;
    
    // Instância do relógio lógico do nó.
    private final RelogioLamport relogio;
    
    // Fila de mensagens recebidas, que mantém a ordem dos eventos.
    private final BlockingQueue<Mensagem> filaMensagens;

    // Construtor 
    public No(String nome) {
        this.nome = nome;
        this.relogio = new RelogioLamport();
        this.filaMensagens = new LinkedBlockingQueue<>();
    }

    // Método que simula a ocorrência de um evento local e incrementa o relógio lógico e exibe o evento com o timestamp atualizado.
    public void eventoLocal(String descricaoEvento) {
        int timestamp = relogio.incrementar();
        System.out.println(nome + " executou evento: '" + descricaoEvento + "' no tempo lógico " + timestamp);
    }

    // Método para enviar uma mensagem para outro nó e incrementa o relógio, cria a mensagem com o timestamp e envia para o nó destinatário.
    public void enviarMensagem(No destinatario, String conteudo) {
        int timestamp = relogio.incrementar();
        Mensagem mensagem = new Mensagem(timestamp, conteudo);
        destinatario.receberMensagem(mensagem);
        System.out.println(nome + " enviou mensagem: '" + conteudo + "' com timestamp " + timestamp);
    }

    // Método para receber uma mensagem de outro nó e atualizar o relógio lógico com o timestamp da mensagem e adiciona a mensagem à fila para posterior processamento.
    public void receberMensagem(Mensagem mensagem) {
        relogio.atualizar(mensagem.getTimestamp());
        filaMensagens.add(mensagem);
        System.out.println(nome + " recebeu mensagem: '" + mensagem.getConteudo() + "' com timestamp " + mensagem.getTimestamp());
    }

    // Método que processa as mensagens armazenadas na fila e retira as mensagens da fila e as exibe, utilizando o estado atual do relógio lógico.
    public void processarEventos() {
        while (!filaMensagens.isEmpty()) {
            Mensagem mensagem = filaMensagens.poll();
            System.out.println(nome + " processando mensagem: '" + mensagem.getConteudo() + "' no tempo lógico " + relogio.getTempo());
        }
    }
}
