import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        try {
            // Créer une connexion
            Socket socket = new Socket("127.0.0.1", 8080);

            // stocke flux d'entrée et de sortie
            PrintWriter socketOut = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Lit le message de bienvenue
            System.out.println(socketIn.readLine());

            boolean flag = true;
            // while qui se termine si le mot merci est reçu
            while(flag) {
                // lit le message du serveur
                String message = socketIn.readLine();
                System.out.println(message);

                // si le message contient ":" c'est que le joueur doit faire un essai
                if (message.contains(":")) {
                    // input du joueur
                    Scanner scanner = new Scanner(System.in);
                    socketOut.println(scanner.nextLine());
                    // si le message contient "merci" c'est que la partie est finie
                } else if (message.contains("Merci")) {
                    // met fin au while
                    flag = false;
                }
            }
            // ferme la connexion
            socket.close();
        } catch (IOException var7) {
            throw new RuntimeException(var7);
        }
    }
}
