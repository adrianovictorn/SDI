package sistemasdistribuidos.bully;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Cria processos com IDs de 1 a 5
        for (int i = 1; i <= 5; i++) {
            Processo p = new Processo(i);
            Processo.processos.add(p);
            new Thread(p).start();
        }
        
        // Aguarda alguns segundos para estabilizar o sistema
        Thread.sleep(5000);
        
        
        // Simula a falha do coordenador
        System.out.println("Simulando falha do coordenador...");
        System.out.println("\n");
        // Verifica se há coordenador, caso não, inicia eleicao a partir do Processo 1
        if (Processo.getCoordenador() == null) {
            LogSistema.log("Sistema", "Nenhum coordenador detectado. Processo 1 inicia eleicao.");
            Processo.processos.get(0).iniciarEleicao();
        }
        
        Thread.sleep(5000);
        for (Processo p : Processo.processos) {
            if (p.getId() == 5) {
                p.setAtivo(false);
                break;
            }
        }
        
        
        // Aguarda para que a eleicao ocorra após a falha
        Thread.sleep(10000);
        
        // Simula a recuperacao do processo que falhou
        for (Processo p : Processo.processos) {
            if (p.getId() == 5) {
                p.setAtivo(true);
                break;
            }
        }
        
        // Permite que a simulacao continue por um tempo e encerra
        Thread.sleep(10000);
        System.exit(0);
    }
}
