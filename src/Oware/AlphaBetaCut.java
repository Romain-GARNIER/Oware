package Oware;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class AlphaBetaCut {
    private Position posInit;
    private GameControler gc;

    public void setPosInit(Position posInit) {
        this.posInit = posInit;
    }

    public AlphaBetaCut(GameControler gc, Position posInit) {
        this.gc = gc;
        this.posInit = posInit;
    }

    ArrayList<String> coupPossible(Position pos_current, boolean computer_play){
        ArrayList<String> coupsPossible = new ArrayList<>();
        ArrayList<String> cellComputer = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6"));
        ArrayList<String> cellPlayer = new ArrayList<>(Arrays.asList("7", "8", "9", "10", "11", "12"));
        ArrayList<String> couleur = new ArrayList<>(Arrays.asList("R", "B"));

        ArrayList<String> currentCell = (gc.computer_player_one) ? cellComputer : cellPlayer;

        for(String cellC : currentCell){
            for(String couleurC : couleur){
                String move = cellC + "-" + couleurC;
                if(GameControler.containsSpecialSeed(pos_current, computer_play, Integer.parseInt(cellC)-1)){
                    for(int i=1; i<=3;i++){
                        move = cellC + "-" + couleurC + "-" + i;
                        if(GameControler.validMove(pos_current, computer_play, move)){
                            coupsPossible.add(move);
                        }
                    }

                }else{
                    if(GameControler.validMove(pos_current, computer_play, move)){
                        coupsPossible.add(move);
                    }
                }
            }
        }

        return coupsPossible;
    }

    String AlphaBetaCutStart(Position pos_current, boolean computer_play, int depth, int depthMax, int a, int b) {
        String bestCoup = "";
        Position pos_next;
        int maxValue = -10000;

        ArrayList<String> coupPossible = coupPossible(pos_current, computer_play);

        for (String coup : coupPossible) {
            pos_next = GameControler.playMove(pos_current, computer_play, coup);
            int value = AlphaBetaCutValue(pos_next, !computer_play, depth + 1, depthMax, a, b);
            //System.out.println(value + " - " + maxValue + " - " + coup + " " + bestCoup);
            if (maxValue < value || (maxValue == value && new Random().nextInt() % 2 == 0)) {
                maxValue = value;
                bestCoup = coup;
            }
        }
        IHM.log("MinMax :"+maxValue,1);
        return bestCoup;
    }

    int AlphaBetaCutValue(Position pos_current, boolean computer_play, int depth, int depthMax, int a, int b){
        Position pos_next; // In C : created on the stack: = very fast

        if (depth == depthMax || GameControler.finalPosition(pos_current)) {
            int evaluation = GameControler.evaluation(posInit,pos_current);
            IHM.log("evaluation : "+evaluation+"\n",3);
            return evaluation;
            // the simplest evealution fucntion is the difference of the taken seeds
        }

        int alpha = a;
        int beta = b;
        ArrayList<String> coupPossible = coupPossible(pos_current, computer_play);

        if (computer_play){
            for(String coup: coupPossible){
                pos_next = GameControler.playMove(pos_current, computer_play, coup);
                int value = AlphaBetaCutValue(pos_next, !computer_play, depth+1, depthMax, alpha, beta);
                alpha = Math.max(alpha, value);
                if (alpha >= beta) return beta;
            }
            return alpha;
        }else{
            for(String coup: coupPossible){
                pos_next = GameControler.playMove(pos_current, computer_play, coup);
                int value = AlphaBetaCutValue(pos_next, !computer_play, depth+1, depthMax, alpha, beta);
                beta = Math.min(beta, value);
                if(alpha>=beta) return alpha;
            }
            return beta;
        }
    }
}
