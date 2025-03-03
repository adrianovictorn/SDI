package sistemasdistribuidos.heartbeat;

/**
 * Classe principal que inicia o monitor de heartbeat e os nós, simulando falhas e reconexões.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Instancia o monitor central e inicia sua thread
        HeartbeatMonitor monitor = new HeartbeatMonitor();
        Thread monitorThread = new Thread(monitor);
        monitorThread.start();
        
        // Instancia três nós que enviarão heartbeats para o monitor
        No no1 = new No("No1", monitor);
        No no2 = new No("No2", monitor);
        No no3 = new No("No3", monitor);
        
        Thread t1 = new Thread(no1);
        Thread t2 = new Thread(no2);
        Thread t3 = new Thread(no3);
        t1.start();
        t2.start();
        t3.start();
        
        // Permite que os nós operem normalmente por 5 segundos
        Thread.sleep(5000);
        
        // Simula a falha do nó No2
        LogMonitor.log("Simulando falha no No2.");
        no2.setAtivo(false);
        
        // Após 5 segundos, simula a falha do nó No3
        Thread.sleep(5000);
        LogMonitor.log("Simulando falha no No3.");
        no3.setAtivo(false);
        
        // Aguarda 15 segundos para permitir que os nós tentem se reconectar automaticamente
        Thread.sleep(15000);
        
        // Exibe o log completo para ver todas as tentativas de reconexão e os heartbeats recebidos
        LogMonitor.visualizarLog();
        
        LogMonitor.log("Encerrando simulação.");
        System.exit(0);
    }
}
        

/*
 * Envio de Heartbeat:
Quando o nó está ativo, ele envia um heartbeat para o monitor a cada 1 segundo. Se o nó for marcado como inativo (por exemplo, devido a uma simulação de falha via setAtivo(false)), o método run() entra em uma rotina será exibida uma mensagem informando que o nó está inativo e que ele tentará se reconectar em 5 segundos.
Após aguardar 5 segundos, o nó automaticamente se reconecta, ou seja, a flag ativo é definida como true novamente, e ele volta a enviar heartbeats.


Desse jeito, mesmo que um nó falhe (seja simulado como inativo), ele tentará se reintegrar ao sistema automaticamente ao retomar o envio de heartbeats, permitindo ao monitor identificar sua presença novamente.
*/


