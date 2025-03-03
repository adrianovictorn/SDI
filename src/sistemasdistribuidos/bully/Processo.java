package sistemasdistribuidos.bully;


import java.util.ArrayList;
import java.util.List;

public class Processo implements Runnable {
    private int id;
    private volatile boolean ativo = true;
    private volatile boolean coordenador = false;
    public static List<Processo> processos = new ArrayList<>();

    public Processo(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
    public boolean isAtivo() {
        return ativo;
    }
    
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
        if (ativo) {
            LogSistema.log(String.valueOf(id), "Se recuperou.");
            // Aguarda um curto período antes de iniciar eleição para reconfirmação
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                LogSistema.log(String.valueOf(id), "Reconexao interrompida.");
            }
            iniciarEleicao();
        } else {
            LogSistema.log(String.valueOf(id), "Falhou.");
        }
    }
    
    public boolean isCoordenador() {
        return coordenador;
    }
    
    public void setCoordenador(boolean coordenador) {
        this.coordenador = coordenador;
    }
    
    // Retorna o coordenador ativo, se houver
    public static Processo getCoordenador() {
        for (Processo p : processos) {
            if (p.isCoordenador() && p.isAtivo()) {
                return p;
            }
        }
        return null;
    }
    
    // Inicia a eleição enviando mensagem para processos com ID maior
    public void iniciarEleicao() {
        if (!ativo) return;
        System.out.println("Iniciando eleicao...");
        LogSistema.log(String.valueOf(id), "Inicia eleicao.");
        boolean respostaMaior = false;
        for (Processo p : processos) {
            if (p.getId() > this.id && p.isAtivo()) {
                LogSistema.log(String.valueOf(id), "Envia mensagem de eleicao para o Processo " + p.getId());
                respostaMaior = true;
            }
        }
        // Se nenhum processo com ID maior responder, torna-se coordenador
        if (!respostaMaior) {
            tornarCoordenador();
        } else {
            LogSistema.log(String.valueOf(id), "Aguarda anuncio do novo coordenador.");
        }
    }
    
    // Declara o processo atual como coordenador e notifica os demais
    public void tornarCoordenador() {
        this.coordenador = true;
        System.out.println("Processo " + id + " se torna o novo coordenador.");
        LogSistema.log(String.valueOf(id), "Se torna o novo coordenador.");
        for (Processo p : processos) {
            if (p.getId() < this.id && p.isAtivo()) {
                LogSistema.log(String.valueOf(id), "Notifica o Processo " + p.getId() + " que ele é o novo coordenador.");
                p.setCoordenador(false);
            }
        }
    }
    
    @Override
    public void run() {
        try {
            while (true) {
                if (!ativo) {
                    Thread.sleep(1000);
                    continue;
                }
                
                // Verifica periodicamente se o coordenador está ativo
                Processo coord = getCoordenador();
                if (coord == null || !coord.isAtivo()) {
                    LogSistema.log(String.valueOf(id), "Detecta falha do coordenador. Aguardando reconfirmação...");
                    // Aguarda um curto período para reconfirmação da falha
                    Thread.sleep(2000);
                    // Verifica novamente se há um coordenador ativo
                    Processo novoCoord = getCoordenador();
                    if (novoCoord == null || !novoCoord.isAtivo()) {
                        LogSistema.log(String.valueOf(id), "Falha confirmada. Iniciando eleicao.");
                        iniciarEleicao();
                    } else {
                        LogSistema.log(String.valueOf(id), "Novo coordenador detectado: Processo " + novoCoord.getId());
                    }
                }
                Thread.sleep(3000);
            }
        } catch (InterruptedException e) {
            LogSistema.log(String.valueOf(id), "Foi interrompido.");
        }
    }
}
