package awele.bot.competitor.minmaxbetter;

import awele.bot.CompetitorBot;
import awele.core.Board;
import awele.core.InvalidBotException;

import java.util.Arrays;
import java.util.Objects;


public class MinMaxBetter extends CompetitorBot {

    private int depth[];  //le tableau de profondeur en fonction du nombre de graine, eventuellement suppl�mente par une heuristique

    /* coefficients pour la fonction d'evaluation */

    private double krouscoeff;
    private double dangercoeff;
    private double scorecoeff;


    public MinMaxBetter() throws InvalidBotException {
        this.setBotName("IAWELEBetter - avec heuristique et profondeur 15");
        this.addAuthor("R. LAGLER, T. FELKER");
    }

    @Override
    public void initialize() {
        // TODO Auto-generated method stub
    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub

    }

    /**
     * la fonction de d�cision, calcule la profondeur a utiliser pour calculer l'efficacit� des coups comme suit :
     * Pour chaque coup valide, prend la valeur du tableau de profondeur calcul� pendant learn et lui ajoute 1 tous les 2 coups invalides
     * @param board Le plateau de jeu courant
     * @return un tableau avec l'efficacit� de chaque coup
     */
    @Override
    public double[] getDecision(Board board) {

        double[] decision = new double[6];
        int curseed = board.getNbSeeds();
        int curp = board.getCurrentPlayer();
        boolean[] validMoves = board.validMoves(curp);
        double[] decisionBis = new double[6];
        int unvalid = 0;
        if(board.getNbSeeds() == 49){
            if (Arrays.equals(board.getPlayerHoles(), new int[]{4, 4, 4, 4, 4, 4})){
                return new double[]{0,0,0,0,0,10};
            }
        }
        for(int j = 0;j < 6;j++) {
            if(!validMoves[j])
                unvalid +=1;
        }

        for(int i = 0; i < 6; i++) {
            if (validMoves[i]) {
                decisionBis[i] = i+1;
                try {
                    decision[i] = miniMax(-100,100, (int) (depth[curseed]) , board.playMoveSimulationBoard(curp, decisionBis), false);

                } catch (InvalidBotException e) {}
            }
        }
        return decision;
    }

    /**
     * @param board le plateau de jeu courant
     * @param curp le joueur courant
     * @return si le plateau actuel repr�sente une partie finie ou non
     */
    public boolean isFinalLeaf(Board board,int curp) {
        return
                board.getScore(1-curp) > 24 ||
                        board.getNbSeeds() < 7 ||
                        isOnlyInvalidMoves(board.validMoves(curp));
    }

    /**
     * @param validMoves un tableau disant si les 6 coups sont valides ou non
     * @return true si aucun coup n'est valide
     */
    private boolean isOnlyInvalidMoves(boolean[] validMoves) {
        for(int i = 0; i < 6; i++)
            if (validMoves[i]) return false;
        return true;
    }


    public static final int TALLY_WEIGHT = 25;

    /** Weight of houses that contain more than 12 seeds */
    public static final int ATTACK_WEIGHT = 28;

    /** Weight of houses that contain 1 or 2 seeds */
    public static final int DEFENSE_WEIGHT = -36;

    /** Weight of houses that do not contain any seeds */
    public static final int MOBILITY_WEIGHT = -54;

    /**
     * @param board le plateau de jeu courant
     * @return une partie de la fonction evaluation en fonction du nombre de krous et trous dangereux du joueur
     */
    public double getBoardState(Board board) {
        int score = TALLY_WEIGHT * (board.getScore(board.getCurrentPlayer()) - board.getScore(1 - board.getCurrentPlayer()));
        int[] oppHoles = board.getOpponentHoles();
        int[] playerHoles = board.getPlayerHoles();

        for(int i =0; i <=5; i++){
            final int seedOpp = oppHoles[i];
            final int seedPlayer = playerHoles[i];
            if (seedPlayer > (12-i)) {
                score += ATTACK_WEIGHT;
            } else if (seedPlayer == 0) {
                score += MOBILITY_WEIGHT;
            } else if (seedPlayer < 3) {
                score += DEFENSE_WEIGHT;
            }

            if (seedOpp > (12-i)) {
                score -= ATTACK_WEIGHT;
            } else if (seedOpp == 0) {
                score -= MOBILITY_WEIGHT;
            } else if (seedOpp < 3) {
                score -= DEFENSE_WEIGHT;
            }
        }

        return score;/**
        int scoreSeeds = 0;

        int cHasKrou = 0;
        int aHasKrou = 0;

        boolean isPair;
        if(depth % 2 == 0) {
            isPair = true;
        }else {
            isPair = false;
        }


        for(int i = 0; i < Board.NB_HOLES; i++) {
            if(board.getPlayerHoles()[i] < 3 ) {
                if(isPair)
                    scoreSeeds++;
                else
                    scoreSeeds--;
            }

            if(board.getOpponentHoles()[i] < 3 ) {
                if(isPair)
                    scoreSeeds--;
                else
                    scoreSeeds++;
            }

            if(board.getPlayerHoles()[i] >= 11) {
                cHasKrou = 1;
            }

            if(board.getOpponentHoles()[i] >= 11) {
                aHasKrou = 1;
            }

        }

        return scoreSeeds + aHasKrou - cHasKrou;*/
    }

    /**
     * @param alpha La valeur alpha de l'algorithme minimax
     * @param beta La valeur beta de l'algorithme minimax
     * @param depth La profondeur qu'il reste a parcourir
     * @param board Le plateau de jeu courant
     * @param isMax S'il s'agit du joueur devant maximiser ou non
     * @return La valeur du coup �tudi� s'il s'agit d'une feuille ou si la profondeur maximale est atteinte, joue les coups suivant sinon
     * @throws InvalidBotException Recup�re les erreurs si le bot plante pour ne pas faire s'arr�ter le programme
     */
    public double miniMax(double alpha, double beta, int depth, Board board, boolean isMax) throws InvalidBotException {

        int curp = board.getCurrentPlayer();

        if (depth == 0 || isFinalLeaf(board,curp))
            return getBoardState(board) * (isMax? 1 : -1); //la fonction d'�valuation, �crite ici car appel�e enormement de fois

        double v = 0;
        boolean[] validMoves = board.validMoves(curp);
        double[] decisionBis = new double[6];

        if (isMax) {
            v = -100;
            for (int i = 0; i < 6; i++) {
                if (validMoves[i]) {
                    decisionBis[i] = i+1;
                    double minmax = miniMax(alpha, beta, depth - 1, board.playMoveSimulationBoard(curp, decisionBis), false);
                    v = v > minmax ? v : minmax;
                    if (v >= beta) return v;
                    alpha = v > alpha ? v : alpha;
                }
            }
        }
        else {
            v = 100;
            for (int i = 0; i < 6; i++) {
                if (validMoves[i]) {
                    decisionBis[i] = i+1;
                    double minmax = miniMax(alpha, beta, depth - 1, board.playMoveSimulationBoard(curp, decisionBis), true);
                    v = v < minmax ? v : minmax;
                    if (v <= alpha) return v;
                    beta = v < beta ? v : beta;
                }
            }
        }


        return v;
    }


    /**
     * la fonction d'apprentissage permettant d'initialiser les variables et �galement de les ajuster pendant les tests
     */
    @Override
    public void learn() {

        /* initialisation des variables */

        depth = new int[49];

        krouscoeff = 0.41;
        dangercoeff = 0.11;
        scorecoeff = 0.89;


        /*valeurs par d�faut du tableau de profondeur. Plus la partie augmente plus la profondeur est grande */
        for(int i = 0;i < 49;i++) {
            depth[i] = 15 - (i/10);
        }

    }

}
