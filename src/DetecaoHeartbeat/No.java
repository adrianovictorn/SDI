package DetecaoHeartbeat;

public class No implements Runnable {
    private String id;
    private HeartbeatMonitor monitor;
    private volatile boolean ativo;
    
    public No(String id, HeartbeatMonitor monitor) {
        this.id = id;
        this.monitor = monitor;
        this.ativo = true;
    }
    
    public String getId() {
        return id;
    }
    
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
    
    @Override
    public void run() {
        while (true) {
            if (ativo) {
                // Envia heartbeat para o monitor
                monitor.receiveHeartbeat(id);
            } else {
                LogMonitor.log("No " + id + " está inativo e não envia heartbeat.");
            }
            try {
                Thread.sleep(1000); 
            } catch (InterruptedException e) {
                LogMonitor.log("No " + id + " foi interrompido.");
                break;
            }
        }
    }
}
