package Oware;

import java.util.Hashtable;

public class MinMax {

    int minMaxValue(Position pos_current, boolean computer_play, int depth, int depthMax){
        //System.out.println("depth : "+depth);
        //System.out.println("computer : "+computer_play);
        //System.out.println(pos_current.toString(true));
        // computer_play is true if the computer has to play and false otherwise
        int[] tab_values = new int[6];
        Position pos_next = new Position(); // In C : created on the stack: = very fast
        if (GameControler.finalPosition(pos_current, computer_play, depth)){
            // WRITE the code: returns VALMAX (=96) if the computer wins, -96 if it loses; 0 if draw
            for (int i = 0; i < pos_current.cells_player.length; i++){
                pos_current.seeds_player += pos_current.cells_player[i];
            }

            for (int i = 0; i < pos_current.cells_computer.length; i++){
                pos_current.seeds_computer += pos_current.cells_computer[i];
            }

            if(pos_current.seeds_computer > pos_current.seeds_player)
                return 96;
            if(pos_current.seeds_computer < pos_current.seeds_player)
                return -96;
            if(pos_current.seeds_computer == pos_current.seeds_player)
                return 0;
        }
        if (depth == depthMax) {
            int evaluation = GameControler.evaluation(pos_current, computer_play, depth);
            System.out.println("evaluation : "+evaluation+"\n");
            return evaluation;
            // the simplest evealution fucntion is the difference of the taken seeds
        }
        for(int i=0;i<6;i++){ // /!\ Question : on essaie de faire 6 coups (parcourt du camp) ou 12 coups (parcourt du plateau) ?
            // we play the move i
            // WRITE function validMove(pos_current, computer_play,i)
            // it checks whether we can select the seeds in cell i and play (if there is no seed the function returns false)
            if (GameControler.validMove(pos_current, computer_play,i)){
                // WRITE function playMove(&pos_next,pos_current, computer_play,i)
                // we play the move i from pos_current and obtain the new position pos_next
                pos_next = GameControler.playMove(pos_current, computer_play,i);
                // pos_next is the new current poisition and we change the player
                tab_values[i]=minMaxValue(pos_next,!computer_play,depth+1,depthMax);
            } else {
                if (computer_play) tab_values[i]=-100;
                else tab_values[i]=+100;
            }
        }
        int res = tab_values[0];

        if (depth == 0){
            int indice = 0;
            // WRITE the code: res contains the MAX of tab_values
            for(int i=0;i<tab_values.length;i++){
                if(res < tab_values[i]){
                    res = tab_values[i];
                    indice = i;
                }
            }
            res = indice;
        }else{
            if (computer_play){
                // WRITE the code: res contains the MAX of tab_values
                for(int i=0;i<tab_values.length;i++){
                    if(res < tab_values[i])
                        res = tab_values[i];
                }
            } else {
                // WRITE the code: res contains the MIN of tab_valuess
                res = tab_values[0];
                for(int i=0;i<tab_values.length;i++){
                    if(res > tab_values[i])
                        res = tab_values[i];
                }
            }
        }


        System.out.println("-------");
        String str_tab = "";
        for(int i=0;i<tab_values.length;i++){
            str_tab+="["+tab_values[i]+"]";
        }
        System.out.println("tab value : "+str_tab);
        System.out.println("res : "+res);
        System.out.println("-------\n");
        return res;
    }

}
