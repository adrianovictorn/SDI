package EstadoGlobal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Processo implements Runnable {
    // Identificador único do processo
    private String id;
    // Fila para armazenar as mensagens recebidas
    private BlockingQueue<Mensagem> filaMensagens = new LinkedBlockingQueue<>();
    // Estado local do processo (simulado com um contador)
    private int estadoLocal = 0;
    // Atributo que identifica se o estado local já foi gravado durante o snapshot
    private boolean gravouEstado = false;
    // Mapeia cada canal (identificado pelo remetente) a uma lista de mensagens que foram registradas durante o snapshot
    private Map<String, List<Mensagem>> estadoCanais = new ConcurrentHashMap<>();
    // Indica se o processo está gravando mensagens de um canal específico durante o snapshot
    private Map<String, Boolean> recordingChannel = new ConcurrentHashMap<>();
    // Lista de identificadores dos processos vizinhos (dos quais esse processo pode receber mensagens)
    private List<String> vizinhos;

    // Construtor que inicializa o processo com seu identificador e vizinhos
    public Processo(String id, List<String> vizinhos) {
        this.id = id;
        this.vizinhos = vizinhos;
        
        for (String viz : vizinhos) {
            estadoCanais.put(viz, new ArrayList<>());
            recordingChannel.put(viz, false);
        }
    }

    // Retorna o identificador do processo
    public String getId() {
        return id;
    }

    // Método para receber uma mensagem e adicioná-la à fila de mensagens
    public void receberMensagem(Mensagem m) {
        filaMensagens.add(m);
    }

    // Método para enviar uma mensagem normal para um processo destinatário
    public void enviarMensagem(String destinatario, String conteudo) {
        Mensagem m = new Mensagem(TipoMensagem.NORMAL, conteudo, id);
        System.out.println("Processo " + id + " enviando mensagem normal para " + destinatario + ": " + conteudo);
        
        // Utiliza a rede para enviar a mensagem
        Rede.enviar(destinatario, m);
    }

    // Método para enviar a mensagem marcador para todos os vizinhos
    public void enviarMarcador() {
        for (String viz : vizinhos) {
            Mensagem marcador = new Mensagem(TipoMensagem.MARCADOR, "Marcador", id);
            System.out.println("Processo " + id + " enviando marcador para " + viz);
            
            // Envia o marcador através da rede
            Rede.enviar(viz, marcador);
        }
    }

    // Método que inicia o snapshot no processo
    public void iniciarSnapshot() {
        // Registra o estado local atual e marca que o snapshot foi iniciado
        gravouEstado = true;
        estadoLocal = obterEstadoLocal();
        System.out.println("Processo " + id + " registrou estado local: " + estadoLocal);
        // Coloca todos os canais de entrada em modo de gravação
        for (String viz : vizinhos) {
            recordingChannel.put(viz, true);
        }
        
        enviarMarcador();
    }

    // Função que simula a obtenção do estado local do processo (aqui, apenas o contador)
    public int obterEstadoLocal() {
        return estadoLocal;
    }

    // Função para processar as mensagens recebidas
    public void processar() throws InterruptedException {
        // Processa mensagens da fila com timeout para sair do loop quando não houver mais mensagens
        while (true) {
            Mensagem m = filaMensagens.poll(100, TimeUnit.MILLISECONDS);
            if (m == null) break; // Se não houver mensagens, sai do loop
            // Se a mensagem for do tipo NORMAL
            if (m.getTipo() == TipoMensagem.NORMAL) {
                String remetente = m.getRemetente();
                // Se o snapshot foi iniciado e o canal de onde veio a mensagem está em modo de gravação, registra a mensagem no estado do canal
                if (gravouEstado && recordingChannel.get(remetente)) {
                    estadoCanais.get(remetente).add(m);
                    System.out.println("Processo " + id + " gravou mensagem do canal " + remetente + ": " + m.getConteudo());
                }
                // Processa a mensagem normal incrementando o estado local
                estadoLocal++;
                System.out.println("Processo " + id + " processou mensagem normal de " + remetente + ". Estado local agora: " + estadoLocal);
            }
            // Se a mensagem for do tipo MARCADOR
            else if (m.getTipo() == TipoMensagem.MARCADOR) {
                String remetente = m.getRemetente();
                System.out.println("Processo " + id + " recebeu marcador de " + remetente);
                // Se o processo ainda não registrou seu estado local
                if (!gravouEstado) {
                    // Registra o estado local imediatamente
                    gravouEstado = true;
                    estadoLocal = obterEstadoLocal();
                    System.out.println("Processo " + id + " registrou estado local: " + estadoLocal + " ao receber primeiro marcador de " + remetente);
                    // Para todos os canais, ativa a gravação
                    for (String viz : vizinhos) {
                        if (!viz.equals(remetente)) {
                            recordingChannel.put(viz, true);
                        } else {
                            recordingChannel.put(remetente, false);
                        }
                    }
                    // Envia marcadoress para os demais processos, propagando o snapshot
                    enviarMarcador();
                } else {
                    // Se o processo já regstrou seu estado, interrompe a gravação do canal do remetente
                    recordingChannel.put(remetente, false);
                }
            }
        }
    }

    // Função que executa o processamento contínuo de mensagens (chamado em uma thread)
    @Override
    public void run() {
        try {
            while (true) {
                processar();
                Thread.sleep(100); // Pequena pausa para evitar uso excessivo da CPU
            }
        } catch (InterruptedException e) {
            System.out.println("Processo " + id + " interrompido.");
        }
    }

    // Método para imprimir o snapshot global do processo (estado local e estado dos canais)
    public void imprimirSnapshot() {
        System.out.println("\nSnapshot do Processo " + id + ":");
        System.out.println("Estado local: " + estadoLocal);
        for (String viz : vizinhos) {
            System.out.println("Estado do canal do " + viz + ": " + estadoCanais.get(viz));
        }
    }
}
