package RelogioLamport;

public class Main {
    public static void main(String[] args) {
        No no1 = new No("No1");
        No no2 = new No("No2");
        No no3 = new No("No3");

        System.out.println("\n");

        no1.eventoLocal(" Iniciar computação");
        no2.eventoLocal(" Carregar dados");
        no3.eventoLocal(" Inicializar componentes");

        System.out.println("\n");

        no1.enviarMensagem(no2, "Olá do No1");
        no2.enviarMensagem(no3, "Dados recebidos e processados");
        no3.enviarMensagem(no1, "Confirmação recebida");

        System.out.println("\n");

        no1.processarEventos();
        no2.processarEventos();
        no3.processarEventos();
    }
}
