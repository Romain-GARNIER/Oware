package Oware.IA;

import Oware.GameControler;
import Oware.IHM;
import Oware.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MinMax extends IAEngine {

    public MinMax(GameControler gameControler, Position posInit){
        super(gameControler, posInit);
    }

    @Override
    public int selectSpecialSeed(Position pos_current, boolean computer_play, int depth, int depthMax, double alpha, double beta){
        int bestCell = 0;
        Position pos_next;
        int maxValue = -10000;

        ArrayList<Integer> cellPossible = new ArrayList<>(Arrays.asList(0,1 ,2 ,3 ,4 ,5));
        ArrayList<String> coupPossible = coupPossible(pos_current, computer_play);

        for (int cell : cellPossible){
            Position posFictif = pos_current.clone();
            if(computer_play)
                posFictif.cells_player_1[cell].setSpecialSeed(1);
            else
                posFictif.cells_player_2[cell].setSpecialSeed(1);
            for (String coup : coupPossible) {
                pos_next = GameControler.playMove(posFictif, computer_play, coup);
                int value = minMaxValue(pos_next, !computer_play, depth + 1, depthMax);
                //System.out.println(value);
                if (maxValue < value || (maxValue == value && new Random().nextInt() % 2 == 0)) {
                    maxValue = value;
                    bestCell = cell;
                }
            }
        }
        return bestCell;
    }

    int minMaxValue(Position pos_current, boolean player_one, int depth, int depthMax){

        //System.out.println("depth : "+depth);
        //System.out.println("computer : "+player_one);
        //System.out.println(pos_current.toString(true));
        // player_one is true if the computer has to play and false otherwise
        int[] tab_values_red = new int[6];
        int[] tab_values_black = new int[6];
        Position pos_next = new Position(); // In C : created on the stack: = very fast
        if (GameControler.finalPosition(pos_current, player_one)){
            // WRITE the code: returns VALMAX (=96) if the computer wins, -96 if it loses; 0 if draw
            for (int i = 0; i < pos_current.cells_player_2.length; i++){
                pos_current.seeds_player_2 += pos_current.cells_player_2[i].totalSeeds();
            }

            for (int i = 0; i < pos_current.cells_player_1.length; i++){
                pos_current.seeds_player_1 += pos_current.cells_player_1[i].totalSeeds();
            }

            if(pos_current.seeds_player_1 > pos_current.seeds_player_2)
                return 96;
            if(pos_current.seeds_player_1 < pos_current.seeds_player_2)
                return -96;
            if(pos_current.seeds_player_1 == pos_current.seeds_player_2)
                return 0;
        }
        if (depth == depthMax) {
            int evaluation = GameControler.evaluation(posInit,pos_current, player_one);
            IHM.log("evaluation : "+evaluation+"\n",3);
            return evaluation;
            // the simplest evealution fucntion is the difference of the taken seeds
        }
        for(int i=0;i<6;i++){ // /!\ Question : on essaie de faire 6 coups (parcourt du camp) ou 12 coups (parcourt du plateau) ?
            // we play the move i
            // WRITE function validMove(pos_current, player_one,i)
            // it checks whether we can select the seeds in cell i and play (if there is no seed the function returns false)
            String move = (i+1)+"-R";
            if (GameControler.validMove(pos_current, player_one,move)){
                // WRITE function playMove(&pos_next,pos_current, player_one,i)
                // we play the move i from pos_current and obtain the new position pos_next
                pos_next = GameControler.playMove(pos_current, player_one,move);
                // pos_next is the new current poisition and we change the player
                tab_values_red[i]=minMaxValue(pos_next,!player_one,depth+1,depthMax);
            } else {
                if (player_one) tab_values_red[i]=-100;
                else tab_values_red[i]=+100;
            }

            move = (i+1)+"-B";
            if (GameControler.validMove(pos_current, player_one,move)){
                // WRITE function playMove(&pos_next,pos_current, player_one,i)
                // we play the move i from pos_current and obtain the new position pos_next
                pos_next = GameControler.playMove(pos_current, player_one,move);
                // pos_next is the new current poisition and we change the player
                tab_values_black[i]=minMaxValue(pos_next,!player_one,depth+1,depthMax);
            } else {
                if (player_one) tab_values_black[i]=-100;
                else tab_values_black[i]=+100;
            }
        }
        int res = 0;
        int res_red = tab_values_red[0];
        int res_black = tab_values_black[0];
        if (depth == 0){
            int indice_red = 0;
            int indice_black = 0;
            int indice = 0;
            // WRITE the code: res contains the MAX of tab_values
            for(int i=0;i<tab_values_red.length;i++){
                if(res_red < tab_values_red[i]){
                    res_red = tab_values_red[i];
                    indice_red = i;
                }
                if(res_black < tab_values_black[i]){
                    res_black = tab_values_black[i];
                    indice_black = i;
                }
            }
            if(res_red > res_black){
                res = res_red;
                indice = indice_red;
            }
            else{
                res = res_black;
                indice = indice_black;
            }
            IHM.log("MinMax :"+res,1);
            res = indice;
        }else{
            if (player_one){
                // WRITE the code: res contains the MAX of tab_values
                for(int i=0;i<tab_values_red.length;i++){
                    if(res_red < tab_values_red[i])
                        res_red = tab_values_red[i];
                    if(res_black < tab_values_black[i])
                        res_black = tab_values_black[i];
                }
            } else {
                // WRITE the code: res contains the MIN of tab_valuess
                res_red = tab_values_red[0];
                for(int i=0;i<tab_values_red.length;i++){
                    if(res_red > tab_values_red[i])
                        res_red = tab_values_red[i];
                    if(res_black > tab_values_black[i])
                        res_black = tab_values_black[i];
                }
            }
            if(res_red > res_black)
                res = res_red;
            else
                res = res_black;
        }

        IHM.log("-------",2);
        String str_tab_red = "";
        String str_tab_black = "";
        for(int i=0;i<tab_values_red.length;i++){
            str_tab_red+="["+tab_values_red[i]+"]";
        }
        for(int i=0;i<tab_values_black.length;i++){
            str_tab_black+="["+tab_values_black[i]+"]";
        }
        IHM.log("tab value : "+str_tab_red,2);
        IHM.log("res red : "+res_red,2);
        IHM.log("tab value : "+str_tab_black,2);
        IHM.log("res black : "+res_black,2);
        IHM.log("res : "+res,2);
        IHM.log("-------\n",2);
        return res;
    }

    @Override
    public String start(Position pos_current, boolean player_one, int depth, int depthMax, double alpha, double beta){

        //System.out.println("depth : "+depth);
        //System.out.println("computer : "+player_one);
        //System.out.println(pos_current.toString(true));
        // player_one is true if the computer has to play and false otherwise
        int[] tab_values_red = new int[6];
        int[] tab_values_black = new int[6];
        Position pos_next = new Position(); // In C : created on the stack: = very fast
        for(int i=0;i<6;i++){ // /!\ Question : on essaie de faire 6 coups (parcourt du camp) ou 12 coups (parcourt du plateau) ?
            // we play the move i
            // WRITE function validMove(pos_current, player_one,i)
            // it checks whether we can select the seeds in cell i and play (if there is no seed the function returns false)
            String move = (i+1)+"-R";
            if (GameControler.validMove(pos_current, player_one,move)){
                // WRITE function playMove(&pos_next,pos_current, player_one,i)
                // we play the move i from pos_current and obtain the new position pos_next
                pos_next = GameControler.playMove(pos_current, player_one,move);
                // pos_next is the new current poisition and we change the player
                tab_values_red[i]=minMaxValue(pos_next,!player_one,depth+1,depthMax);
            } else {
                if (player_one) tab_values_red[i]=-100;
                else tab_values_red[i]=+100;
            }
            move = (i+1)+"-B";
            if (GameControler.validMove(pos_current, player_one,move)){
                // WRITE function playMove(&pos_next,pos_current, player_one,i)
                // we play the move i from pos_current and obtain the new position pos_next
                pos_next = GameControler.playMove(pos_current, player_one,move);
                // pos_next is the new current poisition and we change the player
                tab_values_black[i]=minMaxValue(pos_next,!player_one,depth+1,depthMax);
            } else {
                if (player_one) tab_values_black[i]=-100;
                else tab_values_black[i]=+100;
            }
        }
        int res = 0;
        int res_red = tab_values_red[0];
        int res_black = tab_values_black[0];
        int indice_red = 0;
        int indice_black = 0;
        String move = "1-R";
        // WRITE the code: res contains the MAX of tab_values
        for(int i=0;i<tab_values_red.length;i++){
            if(res_red < tab_values_red[i]){
                res_red = tab_values_red[i];
                indice_red = i;
            }
            if(res_black < tab_values_black[i]){
                res_black = tab_values_black[i];
                indice_black = i;
            }
        }

        int n = 1;
        if(!player_one)
            n=7;

        if(res_red > res_black){
            res = res_red;
            move = (indice_red+n)+"-R";
        }
        else{
            res = res_black;
            move = (indice_black+n)+"-B";
        }
        IHM.log("MinMax :"+res,1);
        return move;
    }

}
