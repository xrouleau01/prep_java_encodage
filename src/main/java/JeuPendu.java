
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.lang.String;
import java.util.regex.Pattern;

public class JeuPendu {
    String[] lsMots = {
            "critique",
            "secteur",
            "assistance",
            "cigarette",
            "syndicat",
            "peine",
            "audience",
            "fortune",
            "diamant",
            "carte",
            "amour",
            "ordure",
    };

    //nb d'essais
    Integer nbEssais = 6;

    ArrayList<Character> lsRate = new ArrayList<>();
    ArrayList<Character> lsBon = new ArrayList<>();

    public JeuPendu() {}

    private String mot()
    {
        Random rand = new Random();
        return lsMots[rand.nextInt(12)];
    }

    private String trou(String mot)
    {
        String trou = "";
        for (int i = 0; i < mot.length(); i++)
        {
            trou += "_";
        }
        return trou;
    }

    private void updateNbEssais(int update)
    {
        nbEssais += update;
    }

    private String Input(PrintWriter socketOut, BufferedReader socketIn)
    {
        String lettre = "";
        while (true)
        {
            try {
                socketOut.println("Entrez une lettre :");
                lettre = socketIn.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (lettre.length() == 1 && (Pattern.compile("[a-zA-Z]", Pattern.MULTILINE).matcher(lettre)).find())
            {
                if (!lsRate.contains(lettre.toLowerCase().charAt(0)) && !lsBon.contains(lettre.toLowerCase().charAt(0)))
                {
                    break;
                }
                else
                {
                    socketOut.println("! Vous avez déjà entré cette lettre !");
                }
            }
            else
            {
                socketOut.println("! Veuillez entrer une seule lettre !");
            }
        }
        return lettre;
    }

    private String essai(String mot, String trou, String lettre)
    {
        char charactere = lettre.charAt(0);
        char[] motEnListe = mot.toCharArray();
        char[] trouEnListe = trou.toCharArray();
        int i = 0;
        for (char c : motEnListe)
        {
            if (charactere == c)
            {
                trouEnListe[i] = charactere;
            }
            i ++;
        }
        if (String.valueOf(trouEnListe).equals(trou))
        {
            lsRate.add(charactere);
            updateNbEssais(-1);
            return String.valueOf(trouEnListe);
        }
        lsBon.add(charactere);
        return String.valueOf(trouEnListe);
    }

    private int gagne(String trou)
    {
        if (!trou.contains("_"))
        {
            return 1;
        }
        else if (nbEssais == 0) {
            return 2;
        }
        return 3;
    }

    public void jouer(PrintWriter socketOut, BufferedReader socketIn){
        String mot = mot();
        String trou = trou(mot);

        //Début du jeu
        socketOut.println("\n------- Bienvenue dans le jeu du pendu! -------\nVous avez 6 essais pour trouver le mot secret !\n---------------- BONNE CHANCE! ----------------\n");
        boolean jeutermine = false;
        while(!jeutermine)
        {
            //Affichage
            socketOut.println("Vous avez " + nbEssais + " Essais restants\n" + trou);
            //input
            String lettre = Input(socketOut, socketIn);

            //touché ou manqué
            trou = essai(mot, trou, lettre);

            //gagné?
            switch(gagne(trou))
            {
                case 1:
                    socketOut.println("Félicitation!!\nVous avez trouvé le mot " + mot + "\nMerci d'avoir joué!");
                    jeutermine = true;
                    break;
                case 2:
                    socketOut.println("Défaite... Vous êtes à cours d'essais\nLe mot était «" + mot + "»\nMerci d'avoir joué!");
                    jeutermine = true;
                    break;
                case 3:
                    break;
            }
        }
    }
}
