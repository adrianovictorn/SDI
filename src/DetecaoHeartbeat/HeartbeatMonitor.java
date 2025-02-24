package DetecaoHeartbeat;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;

public class HeartbeatMonitor implements Runnable {
    // Mapeia o id do nó para o timestamp do último heartbeat recebido.

    private final ConcurrentHashMap<String, Long> lastHeartbeatMap = new ConcurrentHashMap<>();

    // Define o tempo limite para considerar um nó como falho.
    private final long TIMEOUT = 3000; 

    // Método chamado pelos nós para enviar heartbeat.
    public void receiveHeartbeat(String noId) {
        lastHeartbeatMap.put(noId, System.currentTimeMillis());
        LogMonitor.log("Monitor: Recebeu heartbeat de " + noId);
    }

    // Verificação contínua dos heartbeats.
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000); 
            } catch (InterruptedException e) {
                LogMonitor.log("Monitor: Interrompido.");
                break;
            }
            long currentTime = System.currentTimeMillis();
            Set<String> nos = lastHeartbeatMap.keySet();
            for (String noId : nos) {
                long lastTime = lastHeartbeatMap.get(noId);
                if (currentTime - lastTime > TIMEOUT) {
                    LogMonitor.log("Monitor: Detectou falha no " + noId 
                        + " (último heartbeat há " + (currentTime - lastTime) + "ms).");
                    
                    // Remove o nó para evitar notificações repetidas
                    lastHeartbeatMap.remove(noId);
                    
                }
            }
        }
    }
}
