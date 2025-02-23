package RelogioLamport;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class No {
    private final String nome;
    private final RelogioLamport relogio;
    private final BlockingQueue<Mensagem> filaMensagens;

    public No(String nome) {
        this.nome = nome;
        this.relogio = new RelogioLamport();
        this.filaMensagens = new LinkedBlockingQueue<>();
    }

    public void eventoLocal(String descricaoEvento) {
        int timestamp = relogio.incrementar();
        System.out.println(nome + " executou evento: '" + descricaoEvento + "' no tempo lógico " + timestamp);
    }

    public void enviarMensagem(No destinatario, String conteudo) {
        int timestamp = relogio.incrementar();
        Mensagem mensagem = new Mensagem(timestamp, conteudo);
        destinatario.receberMensagem(mensagem);
        System.out.println(nome + " enviou mensagem: '" + conteudo + "' com timestamp " + timestamp);
    }

    public void receberMensagem(Mensagem mensagem) {
        relogio.atualizar(mensagem.getTimestamp());
        filaMensagens.add(mensagem);
        System.out.println(nome + " recebeu mensagem: '" + mensagem.getConteudo() + "' com timestamp " + mensagem.getTimestamp());
    }

    public void processarEventos() {
        while (!filaMensagens.isEmpty()) {
            Mensagem mensagem = filaMensagens.poll();
            System.out.println(nome + " processando mensagem: '" + mensagem.getConteudo() + "' no tempo lógico " + relogio.getTempo());
        }
    }
}
