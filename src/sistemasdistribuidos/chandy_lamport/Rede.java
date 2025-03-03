package sistemasdistribuidos.chandy_lamport;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class Rede {
    public static Map<String, Processo> processos = new ConcurrentHashMap<>();

    public static void enviar(String destinatario, Mensagem mensagem) {
        Processo proc = processos.get(destinatario);
        if (proc != null) {
            proc.receberMensagem(mensagem);
        }
    }
}