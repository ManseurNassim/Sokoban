import java.util.Scanner;

public class Sokoban {

    public static void main(String[] args) {
        Sokoban jeu = new Sokoban();
        jeu.jouer();
    }
    
    
    
    private char[][][] niveaux = {
        {
            {'#', '#', '#', '#', '#', '#', '#', '#'},
            {'#', ' ', ' ', ' ', ' ', ' ', ' ', '#'},
            {'#', ' ', ' ', '.', ' ', ' ', ' ', '#'},
            {'#', ' ', ' ', '$', ' ', ' ', ' ', '#'},
            {'#', ' ', ' ', '@', ' ', ' ', ' ', '#'},
            {'#', ' ', ' ', ' ', ' ', ' ', ' ', '#'},
            {'#', '#', '#', '#', '#', '#', '#', '#'}
        },
        {
            {'#', '#', '#', '#', '#', '#', '#'},
            {'#', ' ', ' ', ' ', ' ', ' ', '#'},
            {'#', ' ', '$', ' ', '$', ' ', '#'},
            {'#', ' ', '.', '@', '.', ' ', '#'},
            {'#', ' ', ' ', ' ', ' ', ' ', '#'},
            {'#', '#', '#', '#', '#', '#', '#'}
        },
        {
            {'#', '#', '#', '#', '#', '#', '#', '#'},
            {'#', ' ', ' ', ' ', ' ', ' ', ' ', '#'},
            {'#', ' ', '$', '.', '$', ' ', ' ', '#'},
            {'#', ' ', ' ', '@', ' ', ' ', ' ', '#'},
            {'#', ' ', ' ', '.', ' ', ' ', ' ', '#'},
            {'#', ' ', ' ', ' ', ' ', ' ', ' ', '#'},
            {'#', '#', '#', '#', '#', '#', '#', '#'}
        }
    };

    private char[][] grille;
    private boolean[][] objectifs;
    private int numeroNiveau = 1;
    private int nombreMouvements = 0;

    public void afficherGrille() {
        for (int ligne = 0; ligne < grille.length; ligne++) {
            for (int colonne = 0; colonne < grille[ligne].length; colonne++) {
                System.out.print(grille[ligne][colonne] + " ");
            }
            System.out.println();
        }
    }

    public void deplacerJoueur(char commande) {
        int deltaLigne = 0;
        int deltaColonne = 0;

        switch (commande) {
            case 'z':
                deltaLigne = -1;
                break;
            case 's':
                deltaLigne = 1;
                break;
            case 'q':
                deltaColonne = -1;
                break;
            case 'd':
                deltaColonne = 1;
                break;
            default:
                System.out.println("Commande invalide.");
                return;
        }

        int joueurLigne = -1;
        int joueurColonne = -1;

        for (int ligne = 0; ligne < grille.length; ligne++) {
            for (int colonne = 0; colonne < grille[ligne].length; colonne++) {
                if (grille[ligne][colonne] == '@') {
                    joueurLigne = ligne;
                    joueurColonne = colonne;
                }
            }
        }

        int nouvelleLigne = joueurLigne + deltaLigne;
        int nouvelleColonne = joueurColonne + deltaColonne;

        if (nouvelleLigne < 0 || nouvelleLigne >= grille.length
            || nouvelleColonne < 0 || nouvelleColonne >= grille[nouvelleLigne].length) {
            System.out.println("Deplacement impossible.");
            return;
        }

        if (grille[nouvelleLigne][nouvelleColonne] == '$'
            || grille[nouvelleLigne][nouvelleColonne] == '*') {
            if (peutPousser(nouvelleLigne, nouvelleColonne, deltaLigne, deltaColonne)) {
                int caisseLigneApres = nouvelleLigne + deltaLigne;
                int caisseColonneApres = nouvelleColonne + deltaColonne;

                deplacerCaisse(nouvelleLigne, nouvelleColonne, caisseLigneApres, caisseColonneApres);
                grille[joueurLigne][joueurColonne] = objectifs[joueurLigne][joueurColonne] ? '.' : ' ';
                grille[nouvelleLigne][nouvelleColonne] = '@';
                nombreMouvements++;
            } else {
                System.out.println("Impossible de pousser la caisse.");
            }
        } else if (deplacementValide(nouvelleLigne, nouvelleColonne)) {
            grille[joueurLigne][joueurColonne] = objectifs[joueurLigne][joueurColonne] ? '.' : ' ';
            grille[nouvelleLigne][nouvelleColonne] = '@';
            nombreMouvements++;
        } else {
            System.out.println("Deplacement impossible.");
        }
    }

    public boolean deplacementValide(int ligne, int colonne) {
        if (ligne < 0 || ligne >= grille.length || colonne < 0 || colonne >= grille[ligne].length) {
            return false;
        }

        return grille[ligne][colonne] == ' ' || grille[ligne][colonne] == '.';
    }

    public boolean peutPousser(int caisseLigne, int caisseColonne, int deltaLigne, int deltaColonne) {
        int destinationLigne = caisseLigne + deltaLigne;
        int destinationColonne = caisseColonne + deltaColonne;

        return deplacementValide(destinationLigne, destinationColonne);
    }

    public void deplacerCaisse(int ancienneLigne, int ancienneColonne,
            int nouvelleLigne, int nouvelleColonne) {
        grille[ancienneLigne][ancienneColonne] = objectifs[ancienneLigne][ancienneColonne] ? '.' : ' ';

        if (objectifs[nouvelleLigne][nouvelleColonne]) {
            grille[nouvelleLigne][nouvelleColonne] = '*';
        } else {
            grille[nouvelleLigne][nouvelleColonne] = '$';
        }
    }

    public boolean partieTerminee() {
        for (int ligne = 0; ligne < grille.length; ligne++) {
            for (int colonne = 0; colonne < grille[ligne].length; colonne++) {
                if (objectifs[ligne][colonne] && grille[ligne][colonne] != '*') {
                    return false;
                }
            }
        }
        return true;
    }

    public void chargerNiveau() {
        char[][] source = niveaux[numeroNiveau - 1];
        grille = new char[source.length][];
        objectifs = new boolean[source.length][];

        for (int ligne = 0; ligne < source.length; ligne++) {
            grille[ligne] = new char[source[ligne].length];
            objectifs[ligne] = new boolean[source[ligne].length];
            for (int colonne = 0; colonne < source[ligne].length; colonne++) {
                grille[ligne][colonne] = source[ligne][colonne];
                objectifs[ligne][colonne] = source[ligne][colonne] == '.'
                    || source[ligne][colonne] == '*';
            }
        }

        nombreMouvements = 0;
    }

    public void afficherMenuNiveaux() {
        System.out.println("===== SOKOBAN =====");
        System.out.println("1. Niveau 1");
        System.out.println("2. Niveau 2");
        System.out.println("3. Niveau 3");
        System.out.println("4. Quitter");
        System.out.print("Votre choix : ");
    }

    public void jouer() {
        Scanner scanner = new Scanner(System.in);
        boolean continuer = true;

        while (continuer) {
            afficherMenuNiveaux();
            String saisie = scanner.nextLine();

            if (saisie.length() == 0) {
                continue;
            }

            char choix = saisie.charAt(0);

            if (choix == '4') {
                continuer = false;
            } else if (choix == '1' || choix == '2' || choix == '3') {
                numeroNiveau = choix - '0';
                chargerNiveau();
                jouerPartie(scanner);
            } else {
                System.out.println("Choix invalide.");
            }
        }

        scanner.close();
        System.out.println("Partie terminee.");
    }

    public void jouerPartie(Scanner scanner) {
        boolean partieEnCours = true;

        while (partieEnCours) {
            afficherGrille();

            if (partieTerminee()) {
                System.out.println("Victoire !");
                System.out.println("Nombre de mouvements : " + nombreMouvements);
                break;
            }

            System.out.println("z: haut, s: bas, q: gauche, d: droite, x: quitter");
            System.out.print("Votre choix : ");

            String saisie = scanner.nextLine().toLowerCase();

            if (saisie.length() == 0) {
                continue;
            }

            char commande = saisie.charAt(0);

            if (commande == 'x') {
                partieEnCours = false;
            } else {
                deplacerJoueur(commande);
            }
        }
    }

    public void resoudreDFS() {
    }

    public void resoudreBFS() {
    }
}
