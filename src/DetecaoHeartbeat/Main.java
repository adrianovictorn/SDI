package DetecaoHeartbeat;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Instânciia o monitor central e inicia sua thread
        HeartbeatMonitor monitor = new HeartbeatMonitor();
        Thread monitorThread = new Thread(monitor);
        monitorThread.start();
        
        // Instância três nós heartbeats para o monitor
        No no1 = new No("No1", monitor);
        No no2 = new No("No2", monitor);
        No no3 = new No("No3", monitor);
        
        Thread t1 = new Thread(no1);
        Thread t2 = new Thread(no2);
        Thread t3 = new Thread(no3);
        t1.start();
        t2.start();
        t3.start();
        
        Thread.sleep(5000);
        LogMonitor.log("Simulando falha no No2.");
        no2.setAtivo(false);
        
    
        Thread.sleep(10000);
        
        LogMonitor.visualizarLog();
        
        LogMonitor.log("Encerrando simulação.");
        System.exit(0);
    }
}
