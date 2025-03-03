package sistemasdistribuidos.chandy_lamport;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe auxiliar para formatar e exibir logs de snapshot com timestamps.
 */
public class LogSnapshot {
    
    /**
     * Imprime uma mensagem com o timestamp atual.
     * @param message Mensagem a ser exibida.
     */
    public static void log(String message) {
         String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
         System.out.println("[" + timestamp + "] " + message);
    }
    
    /**
     * Exibe um separador para melhorar a visualização.
     */
    public static void printSeparator() {
         System.out.println("======================================");
    }
}
