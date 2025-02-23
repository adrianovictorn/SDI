package EstadoGlobal;

import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Processo p1 = new Processo("P1", Arrays.asList("P2", "P3"));
        Processo p2 = new Processo("P2", Arrays.asList("P1", "P3"));
        Processo p3 = new Processo("P3", Arrays.asList("P1", "P2"));

        Rede.processos.put("P1", p1);
        Rede.processos.put("P2", p2);
        Rede.processos.put("P3", p3);

        Thread t1 = new Thread(p1);
        Thread t2 = new Thread(p2);
        Thread t3 = new Thread(p3);
        t1.start();
        t2.start();
        t3.start();

        Thread.sleep(500);
        p1.enviarMensagem("P2", "Mensagem 1 de P1 para P2");
        p2.enviarMensagem("P3", "Mensagem 1 de P2 para P3");
        p3.enviarMensagem("P1", "Mensagem 1 de P3 para P1");

        Thread.sleep(500);
        System.out.println("\n--- Iniciando Snapshot pelo P1 ---");
        p1.iniciarSnapshot();

        Thread.sleep(500);
        p2.enviarMensagem("P1", "Mensagem 2 de P2 para P1");
        p3.enviarMensagem("P2", "Mensagem 2 de P3 para P2");
        p1.enviarMensagem("P3", "Mensagem 2 de P1 para P3");

        Thread.sleep(2000);

        t1.interrupt();
        t2.interrupt();
        t3.interrupt();

        System.out.println("\n--- Snapshot Global ---");
        p1.imprimirSnapshot();
        p2.imprimirSnapshot();
        p3.imprimirSnapshot();
    }
}
