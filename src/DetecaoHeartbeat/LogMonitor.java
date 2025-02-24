package DetecaoHeartbeat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Classe apenas para melhorar a visualização dos Logs

public class LogMonitor {
    private static final List<String> logs = new ArrayList<>();

    public static synchronized void log(String message) {
        String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
        String logEntry = "[" + timestamp + "] " + message;
        logs.add(logEntry);
        System.out.println(logEntry);
    }

    public static synchronized void visualizarLog() {
        System.out.println("\n====== LOG COMPLETO ======");
        for (String log : logs) {
            System.out.println(log);
        }
        System.out.println("==========================\n");
    }
}
