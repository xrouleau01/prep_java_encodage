import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class App {
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        // Créer une carte
        afficherMessage("Créer une carte");
        Carte carte = new Carte(Carte.Valeur.AS, Carte.Couleur.COEUR);
        System.out.println(carte);
        System.out.println(carte.getValeur());
        System.out.println(carte.getCouleur());

        // Créer une seconde carte
        afficherMessage("Créer une seconde carte");
        Carte carte2 = new Carte(Carte.Valeur.DEUX, Carte.Couleur.PIQUE);
        System.out.println(carte2);
        System.out.println(carte2.getValeur());
        System.out.println(carte2.getCouleur());

        // Créer un paquet de cartes
        PaquetDeCarte paquetDeCarte = new PaquetDeCarte();

        // Afficher le nombre de cartes
        afficherMessage("Afficher le nombre de cartes restantes");
        System.out.println(paquetDeCarte.getNombreDeCartes());

        // Piger 3 cartes
        afficherMessage("Piger 3 cartes");
        System.out.println(paquetDeCarte.piger());
        System.out.println(paquetDeCarte.piger());
        System.out.println(paquetDeCarte.piger());

        // Afficher le nombre de cartes
        afficherMessage("Afficher le nombre de cartes restantes");
        System.out.println(paquetDeCarte.getNombreDeCartes());

        // Brasser le paquet
        paquetDeCarte.brasser();

        // Piger 3 cartes
        afficherMessage("Piger 3 cartes");
        System.out.println(paquetDeCarte.piger());
        System.out.println(paquetDeCarte.piger());
        System.out.println(paquetDeCarte.piger());

        // Afficher le nombre de cartes
        afficherMessage("Afficher le nombre de cartes restantes");
        System.out.println(paquetDeCarte.getNombreDeCartes());

        var key = CS_EncryptionUtils.generateKey(CS_EncryptionUtils.LONGUEUR_CLE.N_128);
        var iv = CS_EncryptionUtils.generateIv();
        System.out.println(CS_EncryptionUtils.encrypt("Mon nom est Xavier!", key, iv));
        var code = CS_EncryptionUtils.encrypt("Mon nom est Xavier!", key, iv);
        System.out.println(code);

        System.out.println(CS_EncryptionUtils.decrypt(code, key, iv));

    }
    private static void afficherMessage(String message) {
        System.out.println();
        System.out.printf("*** %s ***\n", message);
    }

}
