package EleicaoBully;

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
    
    // Retorna o coordenador ativo, caso haja
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
        LogSistema.log(String.valueOf(id), "Inicia eleição.");
        boolean respostaMaior = false;
        for (Processo p : processos) {
            if (p.getId() > this.id && p.isAtivo()) {
                LogSistema.log(String.valueOf(id), "Envia mensagem de eleição para o Processo " + p.getId());
                respostaMaior = true;
            }
        }

        // Se nenhum processo com ID maior responder, torna-se coordenador
        if (!respostaMaior) {
            tornarCoordenador();
        } else {
            LogSistema.log(String.valueOf(id), "Aguarda anúncio do novo coordenador.");
        }
    }
    
    // Método que ceclara o processo como coordenador e notifica os demais
    public void tornarCoordenador() {
        this.coordenador = true;
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
                Processo coord = getCoordenador();
                if (coord == null || !coord.isAtivo()) {
                    LogSistema.log(String.valueOf(id), "Detecta falha do coordenador. Inicia eleição.");
                    iniciarEleicao();
                }
                Thread.sleep(3000);
            }
        } catch (InterruptedException e) {
            LogSistema.log(String.valueOf(id), "Foi interrompido.");
        }
    }
}
