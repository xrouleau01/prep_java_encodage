import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

class Main {
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

            // Lancer le jeu blackjack
            JeuBlackjack jeu = new JeuBlackjack();
            jeu.jouer(socketOut, socketIn);

            // Fermer la connexion
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}