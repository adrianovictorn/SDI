package EstadoGlobal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;



public class Processo implements Runnable {
    private String id;
    private BlockingQueue<Mensagem> filaMensagens = new LinkedBlockingQueue<>();
    private int estadoLocal = 0;
    private boolean gravouEstado = false;
    private Map<String, List<Mensagem>> estadoCanais = new ConcurrentHashMap<>();
    private Map<String, Boolean> recordingChannel = new ConcurrentHashMap<>();
    private List<String> vizinhos;

    public Processo(String id, List<String> vizinhos) {
        this.id = id;
        this.vizinhos = vizinhos;
        for (String viz : vizinhos) {
            estadoCanais.put(viz, new ArrayList<>());
            recordingChannel.put(viz, false);
        }
    }

    public String getId() {
        return id;
    }

   public void receberMensagem(Mensagem m) {
        filaMensagens.add(m);
    }

    public void enviarMensagem(String destinatario, String conteudo) {
        Mensagem m = new Mensagem(TipoMensagem.NORMAL, conteudo, id);
        System.out.println("Processo " + id + " enviando mensagem normal para " + destinatario + ": " + conteudo);
        Rede.enviar(destinatario, m);
    }

    public void enviarMarcador() {
        for (String viz : vizinhos) {
            Mensagem marcador = new Mensagem(TipoMensagem.MARCADOR, "Marcador", id);
            System.out.println("Processo " + id + " enviando marcador para " + viz);
            Rede.enviar(viz, marcador);
        }
    }

    public void iniciarSnapshot() {
        gravouEstado = true;
        estadoLocal = obterEstadoLocal();
        System.out.println("Processo " + id + " registrou estado local: " + estadoLocal);
        for (String viz : vizinhos) {
            recordingChannel.put(viz, true);
        }
        enviarMarcador();
    }

    public int obterEstadoLocal() {
        return estadoLocal;
    }

    public void processar() throws InterruptedException {
        while (true) {
            Mensagem m = filaMensagens.poll(100, TimeUnit.MILLISECONDS);
            if (m == null) break; 
            if (m.getTipo() == TipoMensagem.NORMAL) {
                String remetente = m.getRemetente();
                if (gravouEstado && recordingChannel.get(remetente)) {
                    estadoCanais.get(remetente).add(m);
                    System.out.println("Processo " + id + " gravou mensagem do canal " + remetente + ": " + m.getConteudo());
                }
                estadoLocal++;
                System.out.println("Processo " + id + " processou mensagem normal de " + remetente + ". Estado local agora: " + estadoLocal);
            } else if (m.getTipo() == TipoMensagem.MARCADOR) {
                String remetente = m.getRemetente();
                System.out.println("Processo " + id + " recebeu marcador de " + remetente);
                if (!gravouEstado) {
                    gravouEstado = true;
                    estadoLocal = obterEstadoLocal();
                    System.out.println("Processo " + id + " registrou estado local: " + estadoLocal + " ao receber primeiro marcador de " + remetente);
                    for (String viz : vizinhos) {
                        if (!viz.equals(remetente)) {
                            recordingChannel.put(viz, true);
                        } else {
                            recordingChannel.put(remetente, false);
                        }
                    }
                    enviarMarcador();
                } else {
                    recordingChannel.put(remetente, false);
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                processar();
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            System.out.println("Processo " + id + " interrompido.");
        }
    }

    public void imprimirSnapshot() {
        System.out.println("\nSnapshot do Processo " + id + ":");
        System.out.println("Estado local: " + estadoLocal);
        for (String viz : vizinhos) {
            System.out.println("Estado do canal do " + viz + ": " + estadoCanais.get(viz));
        }
    }
}