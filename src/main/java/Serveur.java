import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Serveur {
    public static void main(String[] args) {
        ServerSocket serverSocket;
        Socket clientSocket;

        try {
            // Écouter sur le port
            serverSocket = new ServerSocket(8080);

            // Attendre la connexion et l'accepter
            clientSocket = serverSocket.accept();

            // Obtenir les flux d'entrée et de sortie
            PrintWriter socketOut = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader socketIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            JeuPendu jeu = new JeuPendu();
            jeu.jouer(socketOut, socketIn);

            // Fermer la connexion
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
