package Oware.IA;

import Oware.GameControler;
import Oware.IHM;
import Oware.Position;
import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.Arrays;

public class IterativeDeepeningPSV {
    private Position posInit;
    private GameControler gc;
    private int TIME_LIMIT_MILLIS;
    private static boolean searchCutoff = false;
    private static final int limitDepthMax = 15;



    public IterativeDeepeningPSV(Position posInit, GameControler gc, int TIME_LIMIT_MILLIS) {
        this.posInit = posInit;
        this.gc = gc;
        this.TIME_LIMIT_MILLIS = TIME_LIMIT_MILLIS;
    }

    public String IterativeDeepeningPVSStart(Position pos_current, boolean computer_play, double alpha, double beta){
        MoveWrapper<String, Double> BestMove = PVSIterative(pos_current, computer_play, alpha, beta);
        IHM.log("MinMax :"+ BestMove.score,1);
        return BestMove.move;
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
                int cellCS = (gc.computer_player_one) ? 0 : 6;
                if(GameControler.containsSpecialSeed(pos_current, computer_play, Integer.parseInt(cellC)-1-cellCS)){
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


    public MoveWrapper<String, Double> PVSIterative(Position pos_current, boolean computer_play, double alpha, double beta){
        long startTime = System.currentTimeMillis();
        long endTime = startTime + TIME_LIMIT_MILLIS;
        int depth = 1;
        double max = -10000;
        Position pos_next;
        MoveWrapper<String, Double> BestMove = null;


        ArrayList<String> coups = coupPossible(pos_current, computer_play);
        for(String coup : coups){
            long searchTimeLimit = ((TIME_LIMIT_MILLIS - 1000) / (coups.size()));
            pos_next = gc.playMove(pos_current, computer_play, coup);
            double score = PVSIterativeSearch(pos_next, !computer_play, alpha, beta, searchTimeLimit);
            if (score > max){
                max = score;
                BestMove.move = coup;
                BestMove.score = score;
            }
        }

        return BestMove;
    }

    public double PVSIterativeSearch(Position pos_current, boolean computer_play, double alpha, double beta, long timeLimit){
        long startTime = System.currentTimeMillis();
        long endTime = startTime + timeLimit;
        int depthMax = 0;
        double score = 0;

        searchCutoff = false;

        while (depthMax <= limitDepthMax) {
            long currentTime = System.currentTimeMillis();

            if (currentTime >= endTime) {
                break;
            }

            double PVSresult = PVS(pos_current, computer_play, 1, depthMax, alpha, beta, currentTime,endTime - currentTime);

            if (!searchCutoff) {
                score = PVSresult;
            }

            depthMax++;
        }


        return score;
    }

    public double PVS(Position pos_current, boolean computer_play, int depth, int depthMax,  double alpha, double beta,  long startTime, long timeLimit) {
        Position pos_next;
        double b = beta;
        long currentTime = System.currentTimeMillis();
        long elapsedTime = (currentTime - startTime);

        if (elapsedTime >= timeLimit) {
            searchCutoff = true;
        }

        if(GameControler.finalPosition(pos_current, computer_play)){
            int evaluation = GameControler.finalEvaluation(pos_current);
            IHM.log("evaluation : "+evaluation+"\n",3);
            return -evaluation;
        }
        if (depth == depthMax){
            int eval = GameControler.evaluation(posInit, pos_current);
            //System.out.println("PVS eval : " + eval);
            return -eval;
        }
        if (searchCutoff){
            int eval = GameControler.evaluation(posInit, pos_current);
            return -eval;
        }

        ArrayList<String> coups = coupPossible(pos_current, computer_play);


        double score;
        boolean first = true;
        for(String coup : coups){
            pos_next = gc.playMove(pos_current, computer_play, coup);
            score = PVSScore(pos_next, !computer_play, first, depth, depthMax, alpha, beta, b, startTime, timeLimit);
            if (score > alpha){
                alpha = score;
                if (alpha >= beta){
                    break;
                }
            }
            b = alpha + 1;
            first = false;
        }

        return alpha;


    }

    public double PVSScore(Position pos_current, boolean computer_play, final boolean first, final int depth, final int depthMax, final double alpha, final double beta, double b, long startTime, long timeLimit){
        double score = -PVS(pos_current, computer_play, depth + 1, depthMax, -b, -alpha, startTime, timeLimit);
        if (!first && alpha < score && score < beta){
            score = -PVS(pos_current, computer_play, depth + 1, depthMax, -beta, -alpha, startTime, timeLimit);
        }
        return score;
    }
}
