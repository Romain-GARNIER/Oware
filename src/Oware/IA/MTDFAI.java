package Oware.IA;

import Oware.GameControler;
import Oware.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MTDFAI {
    private GameControler gc;
    private int depthMax;

    public MTDFAI(GameControler gc, int depthMax) {
        this.gc = gc;
        this.depthMax = depthMax;
    }

    private String[] MTDF(Position position, int bestValue, int depth, boolean computer_play){
        int value = bestValue;
        String[] result = new String[2];
        int upperBound = Integer.MAX_VALUE;
        int lowerBound = Integer.MIN_VALUE + 1;
        int beta;

        do{
            beta = (value == lowerBound) ? value + 1 : value;
            result = runAlphaBetaWithMemory(position, beta - 1, beta, depth, result, computer_play);
            value = Integer.parseInt(result[0]);
            if(value < beta){
                upperBound = value;
            }else{
                lowerBound = value;
            }
        }while (lowerBound >= upperBound);

        return result;
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

    private String[] runAlphaBetaWithMemory(Position position, int startAlpha, int startBeta, int depth, String[] result, boolean computer_play){
        ArrayList<String> moves = coupPossible(position, computer_play);
        int bestValue = Integer.MIN_VALUE + 1;
        String bestMove = "";
        Position pos_next;

        for(String move : moves){
            pos_next = GameControler.playMove(position, !computer_play, move);
            int value = AlphaBetaWithMemory(pos_next, bestValue, Integer.MAX_VALUE, depth - 1, !computer_play);

            if (value > bestValue || value == bestValue && new Random().nextBoolean()){
                bestValue = value;
                bestMove = move;
            }
        }
        result[0] = String.valueOf(bestValue);
        result[1] = bestMove;
        return result;
    }

    private int AlphaBetaWithMemory(Position position, int alpha, int beta, int depth, boolean computer_play){
        int origineAlpha = alpha;
        int origineBeta = beta;
        return origineAlpha;
    }

}
