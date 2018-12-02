package Oware;

public class MinMax {

    int minMaxValue(Position pos_current, boolean computer_play, int depth, int depthMax){
        // computer_play is true if the computer has to play and false otherwise
        int[] tab_values = new int[12];
        Position pos_next = new Position(); // In C : created on the stack: = very fast
        if (GameControler.finalPosition(pos_current, computer_play, depth)){
            // WRITE the code: returns VALMAX (=96) if the computer wins, -96 if it loses; 0 if draw
            if(pos_current.seeds_computer > pos_current.seeds_player)
                return 96;
            if(pos_current.seeds_computer < pos_current.seeds_player)
                return -96;
            if(pos_current.seeds_computer == pos_current.seeds_player)
                return 0;
        }
        if (depth == depthMax) {
            return GameControler.evaluation(pos_current, computer_play, depth);
            // the simplest evealution fucntion is the difference of the taken seeds
        }
        for(int i=0;i<6;i++){ // /!\ Question : on essaie de faire 6 coups (parcourt du camp) ou 12 coups (parcourt du plateau) ?
            // we play the move i
            // WRITE function validMove(pos_current, computer_play,i)
            // it checks whether we can select the seeds in cell i and play (if there is no seed the function returns false)
            if (GameControler.validMove(pos_current, computer_play,i)){
                // WRITE function playMove(&pos_next,pos_current, computer_play,i)
                // we play the move i from pos_current and obtain the new position pos_next
                GameControler.playMove(pos_next,pos_current, computer_play,i);
                // pos_next is the new current poisition and we change the player
                tab_values[i]=minMaxValue(pos_next,!computer_play,depth+1,depthMax);
            } else {
                if (computer_play) tab_values[i]=-100;
                else tab_values[i]=+100;
            }
        }
        int res = 0;
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
        return res;
    }

}
