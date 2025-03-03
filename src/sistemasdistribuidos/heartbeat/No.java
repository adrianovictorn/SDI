package sistemasdistribuidos.heartbeat;

public class No implements Runnable {
    private String id;
    private HeartbeatMonitor monitor;
    private volatile boolean ativo; // Indica se o nó está ativo (enviando heartbeats) ou inativo (falho)
    
    public No(String id, HeartbeatMonitor monitor) {
        this.id = id;
        this.monitor = monitor;
        this.ativo = true;
    }
    
    public String getId() {
        return id;
    }
    
    /**
     * Permite definir o estado do nó (ativo/inativo).
     * @param ativo true se o nó deve estar ativo; false caso contrário.
     */
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
    
    @Override
    public void run() {
        while (true) {
            // Se o nó estiver ativo, envia heartbeat normalmente
            if (ativo) {
                monitor.receiveHeartbeat(id);
                try {
                    Thread.sleep(1000); // Envia heartbeat a cada 1 segundo
                } catch (InterruptedException e) {
                    LogMonitor.log("No " + id + " foi interrompido.");
                    break;
                }
            } else {
                // Se o nó estiver inativo, tenta se reconectar automaticamente
                LogMonitor.log("No " + id + " está inativo e não envia heartbeat. Tentando reconexão em 5 segundos...");
                try {
                    Thread.sleep(5000); // Aguarda 5 segundos antes de tentar reconectar
                } catch (InterruptedException e) {
                    LogMonitor.log("No " + id + " foi interrompido durante reconexão.");
                    break;
                }
                // Simula a reconexão: o nó volta a estar ativo e retoma o envio de heartbeats
                ativo = true;
                LogMonitor.log("No " + id + " se reconectou automaticamente.");
            }
        }
    }
}
