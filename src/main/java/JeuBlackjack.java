import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class JeuBlackjack {
    public JeuBlackjack () {}

    public void jouer(PrintWriter socketOut, BufferedReader socketIn) throws IOException {
        // message bienvenu
        socketOut.println("*** BIENVENUE AU JEU DE BLACKJACK ***");

        // liste des résumés de partie
        ArrayList<Resume> resumes = new ArrayList<>();
        while (true) {
            // Nouveau paquet de carte
            PaquetDeCarte paquet = new PaquetDeCarte();
            // brasser le paquet
            paquet.brasser();

            // Nouvelle main pour le joueur
            MainBlackjack mainJoueur = new MainBlackjack();
            // Ajoute deux cartes à la main du joueur
            mainJoueur.ajouterCarte(paquet.piger());
            mainJoueur.ajouterCarte(paquet.piger());

            // 0 = continuer le jeu, 1 = arrêter le jeu et faire un jeu au croupier, 2 = arrêter le jeu mais ne pas faire de jeu au croupier
            int tourCroupier = 0;
            do {
                // Affiche les cartes et le score du joueur
                socketOut.println(Arrays.toString(mainJoueur.getMain()));

                // Affiche le score de la main du joueur
                int valeur = mainJoueur.getValeurTotale();
                socketOut.println("Votre score: " + valeur);

                // Affiche soit blackjack, brulé, pleine, ou option de hit ou stand
                if (mainJoueur.estBlackjack()) {
                    //fini la partie
                    socketOut.println("!! BLACKJACK !!");
                    tourCroupier = 1;
                } else if (mainJoueur.estBrulee()) {
                    //fini la partie
                    socketOut.println("!! VOUS AVEZ BUST !!");
                    tourCroupier = 2;
                } else if (mainJoueur.estPleine()) {
                    //fini la partie
                    socketOut.println("!! VOTRE MAIN EST PLEINE !!");
                    tourCroupier = 1;
                } else {
                    while (true) {
                        // input hit ou stand
                        socketOut.println("Voulez-vous Hit ou Stand? [H/S]");

                        String choix = socketIn.readLine();
                        if (choix.equalsIgnoreCase("H")) {
                            //ajoute une carte et continue le jeu
                            mainJoueur.ajouterCarte(paquet.piger());
                            break;
                        } else if (choix.equalsIgnoreCase("S")) {
                            //fini la partie
                            tourCroupier = 1;
                            break;
                        } else {
                            socketOut.println("Veuillez entrer un choix valide: H pour Hit ou S pour Stand");
                        }
                    }
                }
            //
            } while (tourCroupier == 0);
            // variable contenant le gagnant de la partie
            String gagnant;
            switch (tourCroupier) {
                // case 1 = devait arrêter le jeu et faire un jeu au croupier
                case 1:
                    MainBlackjack mainCroupier = new MainBlackjack();
                    mainCroupier.ajouterCarte(paquet.piger());
                    mainCroupier.ajouterCarte(paquet.piger());
                    while (mainCroupier.getValeurTotale() < 16 && !mainCroupier.estPleine()) {
                        mainCroupier.ajouterCarte(paquet.piger());
                    }
                    socketOut.println("Score du Croupier: " + mainCroupier.getValeurTotale());
                    if (mainJoueur.getValeurTotale() <= 21 && mainCroupier.getValeurTotale() > 21) {
                        socketOut.println("VOUS AVEZ GAGNÉ!");
                        gagnant = "Joueur";
                    }else if (mainCroupier.getValeurTotale() >= mainJoueur.getValeurTotale()) {
                        socketOut.println("VOUS AVEZ PERDU!");
                        gagnant = "Croupier";
                    } else {
                        socketOut.println("VOUS AVEZ GAGNÉ!");
                        gagnant = "Joueur";
                    }
                    // crée résumé de la partie avec score croupier
                    resumes.add(new Resume(gagnant, Arrays.toString(mainCroupier.getMain()), String.valueOf(mainCroupier.getValeurTotale()), Arrays.toString(mainJoueur.getMain()), String.valueOf(mainJoueur.getValeurTotale())));
                    break;
                // devait arrêter le jeu mais pas faire un jeu au croupier
                case 2:
                    socketOut.println("VOUS AVEZ PERDU!");
                    gagnant = "Croupier";
                    // crée résumé de la partie sans score croupier
                    resumes.add(new Resume(gagnant, Arrays.toString(mainJoueur.getMain()), String.valueOf(mainJoueur.getValeurTotale())));

            }
            boolean startAgain;
            while (true) {
                // input restart
                socketOut.println("Voulez-vous jouer une nouvelle partie? [O/N]");

                String nouvellePartie = socketIn.readLine();
                if (nouvellePartie.equalsIgnoreCase("O")) {
                    // redémarre partie
                    startAgain = true;
                    break;
                } else if (nouvellePartie.equalsIgnoreCase("N")) {
                    // arrête partie
                    startAgain = false;
                    break;
                } else {
                    socketOut.println("Veuillez entrer un choix valide: O pour Oui ou N pour Non");
                }
            }
            if (!startAgain) {
                break;
            }
        }
        // message de fin
        socketOut.println("Merci d'avoir joué!");
        //
        Resume[] resumesRecuperes;
        try {
            Gson gson = new Gson();
            System.out.println("rendu 1");
            BufferedReader br = new BufferedReader(new FileReader("resumes.json"));
            System.out.println("rendu 2");
            resumesRecuperes = gson.fromJson(br, Resume[].class);
            System.out.println("rendu 3");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            ArrayList<Resume> resumesRecuperesListe = new ArrayList<>(Arrays.asList(resumesRecuperes));
            resumesRecuperesListe.addAll(resumes);
            System.out.println("rendu 5");

            try {
                Gson gson = new Gson();
                System.out.println("rendu 6");
                String resume = gson.toJson(resumesRecuperesListe, ArrayList.class);
                System.out.println("rendu 7");
                FileWriter writer = new FileWriter("resumes.json");
                writer.append(resume);
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        catch (NullPointerException n) {
            try {
                Gson gson = new Gson();
                System.out.println("rendu 6");
                String resume = gson.toJson(resumes, ArrayList.class);
                System.out.println("rendu 7");
                FileWriter writer = new FileWriter("resumes.json");
                writer.append(resume);
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
