package EleicaoBully;

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
        
        // Se não houver coordenador, inicia eleição a partir do Processo 1
        if (Processo.getCoordenador() == null) {
            LogSistema.log("Sistema", "Nenhum coordenador detectado. Processo 1 inicia eleição.");
            Processo.processos.get(0).iniciarEleicao();
        }
        
        // Simula a falha do coordenador (Processo com maior ID, ou seja, 5)
        Thread.sleep(5000);
        for (Processo p : Processo.processos) {
            if (p.getId() == 5) {
                p.setAtivo(false);
                break;
            }
        }
        
        // Aguarda para que a eleição ocorra após a falha
        Thread.sleep(10000);
        
        // Simula a recuperação do processo que falhou (Processo 5)
        for (Processo p : Processo.processos) {
            if (p.getId() == 5) {
                p.setAtivo(true);
                break;
            }
        }
        
        // Permite que a simulação continue por um tempo e encerra
        Thread.sleep(10000);
        System.exit(0);
    }
}
