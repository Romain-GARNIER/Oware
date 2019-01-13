package Oware.IA;

import Oware.ComputerPlayer;
import Oware.GameControler;
import Oware.Position;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class IAEngine {
    protected Position posInit;
    protected GameControler gc;

    public IAEngine(GameControler game, Position position){
        posInit = position;
        gc = game;
    }

    ArrayList<String> coupPossible(Position pos_current, boolean player_one){
        ArrayList<String> coupsPossible = new ArrayList<>();
        ArrayList<String> cellPlayer1 = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6"));
        ArrayList<String> cellPlayer2 = new ArrayList<>(Arrays.asList("7", "8", "9", "10", "11", "12"));
        ArrayList<String> couleur = new ArrayList<>(Arrays.asList("R", "B"));

        ArrayList<String> currentCell = (player_one) ? cellPlayer1 : cellPlayer2;

        for(String cellC : currentCell){
            for(String couleurC : couleur){
                String move = cellC + "-" + couleurC;
                int cellCS = (player_one) ? 0 : 6;
                if(GameControler.containsSpecialSeed(pos_current, player_one, Integer.parseInt(cellC)-1-cellCS)){
                    for(int i=1; i<=3;i++){
                        move = cellC + "-" + couleurC + "-" + i;
                        if(GameControler.validMove(pos_current, player_one, move)){
                            coupsPossible.add(move);
                        }
                    }

                }else{
                    if(GameControler.validMove(pos_current, player_one, move)){
                        coupsPossible.add(move);
                    }
                }
            }
        }

        return coupsPossible;
    }

    ArrayList<String> sortCoupPossible(ArrayList<String> coupPossible, ArrayList<String> bestMove){
        ArrayList<String> res = new ArrayList<>();
        ArrayList<String> copyCoupPossible = new ArrayList<String>(coupPossible);
        ArrayList<String> firstCoup = new ArrayList<>();
        ArrayList<String> secondCoup = new ArrayList<>();
        for(String s : copyCoupPossible){
            if (s.split("-")[0] == bestMove.get(0)){
                firstCoup.add(s);
            }
        }
        copyCoupPossible.removeAll(firstCoup);
        res.addAll(firstCoup);
        for(String s: copyCoupPossible){
            if(s.split("-")[1] == bestMove.get(1)){
                secondCoup.add(s);
            }
        }
        copyCoupPossible.removeAll(secondCoup);
        res.addAll(secondCoup);
        res.addAll(copyCoupPossible);
        return res;
    }

    ArrayList<String> sortBestMove(){
        ArrayList<String> res = new ArrayList<>();
        int black = posInit.seeds_player_1;
        int red = posInit.seeds_player_1;

        if (black >= red){
            res.add("B");
            res.add("R");
        }else{
            res.add("R");
            res.add("B");
        }
        return res;
    }

    public abstract String start(Position pos_current, boolean player_one, int depth, int depthMax, double alpha, double beta);

    public abstract int selectSpecialSeed(Position pos_current, boolean player_one, int depth, int depthMax, double alpha, double beta);
}
