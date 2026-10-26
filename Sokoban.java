import java.util.Scanner;

public class Sokoban {

    public static void main(String[] args) {
        Sokoban jeu = new Sokoban();
        jeu.jouer();
    }
    
    
    
    private char[][] grille = {

    {'#', '#', '#', '#', '#', '#', '#', '#'},
    {'#', ' ', ' ', ' ', ' ', ' ', ' ', '#'},
    {'#', ' ', ' ', '.', ' ', ' ', ' ', '#'},
    {'#', ' ', ' ', '$', ' ', ' ', ' ', '#'},
    {'#', ' ', ' ', '@', ' ', ' ', ' ', '#'},
    {'#', ' ', ' ', ' ', ' ', ' ', ' ', '#'},
    {'#', '#', '#', '#', '#', '#', '#', '#'}
    
    };

    private boolean[][] objectifs = {
        {false, false, false, false, false, false, false, false},
        {false, false, false, false, false, false, false, false},
        {false, false, false, true, false, false, false, false},
        {false, false, false, false, false, false, false, false},
        {false, false, false, false, false, false, false, false},
        {false, false, false, false, false, false, false, false},
        {false, false, false, false, false, false, false, false}
    };

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
            } else {
                System.out.println("Impossible de pousser la caisse.");
            }
        } else if (deplacementValide(nouvelleLigne, nouvelleColonne)) {
            grille[joueurLigne][joueurColonne] = objectifs[joueurLigne][joueurColonne] ? '.' : ' ';
            grille[nouvelleLigne][nouvelleColonne] = '@';
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
        return false;
    }

    public void chargerNiveau() {
    }

    public void jouer() {
        Scanner scanner = new Scanner(System.in);
        boolean partieEnCours = true;

        while (partieEnCours) {
            afficherGrille();
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

        scanner.close();
        System.out.println("Partie terminee.");
    }

    public void resoudreDFS() {
    }

    public void resoudreBFS() {
    }
}
