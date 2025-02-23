package EleicaoBully;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 1; i <= 5; i++) {
            Processo p = new Processo(i);
            Processo.processos.add(p);
            new Thread(p).start();
        }
        
        Thread.sleep(5000);
        
        if (Processo.getCoordenador() == null) {
            LogSistema.log("Sistema", "Nenhum coordenador detectado. Processo 1 inicia eleição.");
            Processo.processos.get(0).iniciarEleicao();
        }
        
        Thread.sleep(5000);
        for (Processo p : Processo.processos) {
            if (p.getId() == 5) {
                p.setAtivo(false);
                break;
            }
        }
        
        Thread.sleep(10000);
        
        for (Processo p : Processo.processos) {
            if (p.getId() == 5) {
                p.setAtivo(true);
                break;
            }
        }
        
        Thread.sleep(10000);
        System.exit(0);
    }
}
