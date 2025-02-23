package EleicaoBully;

public class LogSistema {
    public static synchronized void log(String idProcesso, String mensagem) {
        System.out.println("----- [" + idProcesso + "] -----");
        System.out.println(mensagem);
        System.out.println("-----------------------");
    }
}

