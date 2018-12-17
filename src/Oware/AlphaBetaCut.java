package Oware;

public class AlphaBetaCut {
    private int bestCell;
    private int[] tab_values;

    int[] coupPossible(Position pos_current, boolean computer_play){
        int[] coupsPossible = new int[6];
        for (int i=0; i<6; i++){
            if(GameControler.validMove(pos_current, computer_play, i)){
                coupsPossible[i] = 1;
            } else {
                if (computer_play) coupsPossible[i] =-100;
                else coupsPossible[i]=+100;
            }
        }
        return coupsPossible;
    }

    int AlphaBetaCutValue(Position pos_current, boolean computer_play, int depth, int depthMax, int a, int b){
        this.tab_values = new int[6];
        int[] coupsPossible = new int[6];

        Position pos_next; // In C : created on the stack: = very fast

        if (depth == depthMax || GameControler.finalPosition(pos_current, computer_play, depth)) {
            int evaluation = GameControler.evaluation(pos_current);
            IHM.log("evaluation : "+evaluation+"\n",3);
            return evaluation;
            // the simplest evealution fucntion is the difference of the taken seeds
        }

        int alpha = a;
        int beta = b;

        if (computer_play){
            for(int i =0; i<tab_values.length; i++){
                pos_next = GameControler.playMove(pos_current, computer_play, i);
                int value = AlphaBetaCutValue(pos_next, !computer_play, depth+1, depthMax, alpha, beta);
                alpha = Math.max(alpha, value);
                tab_values[i] = Math.max(tab_values[i], value);
                if (alpha >= beta) return beta;
            }
            return alpha;
        }else{
            for(int i=0; i<tab_values.length;i++){
                pos_next = GameControler.playMove(pos_current, computer_play, i);
                int value = AlphaBetaCutValue(pos_next, !computer_play, depth+1, depthMax, alpha, beta);
                beta = Math.min(beta, value);
                tab_values[i] = Math.min(tab_values[i], value);
                if(alpha>=beta) return alpha;
            }
            return beta;
        }
    }

    int runAlphaBetaCut(Position pos_current, boolean computer_play, int depthMax){
        AlphaBetaCutValue(pos_current, computer_play, 0, depthMax, -100, 100);
        int res = this.tab_values[0];
        int indice = 0;
        for(int i=0;i<this.tab_values.length;i++){
            if(res < this.tab_values[i]){
                res = this.tab_values[i];
                indice = i;
            }
        }

        IHM.log("-------",2);
        String str_tab = "";
        for(int i=0;i<tab_values.length;i++){
            str_tab+="["+tab_values[i]+"]";
        }
        IHM.log("tab value : "+str_tab,2);
        IHM.log("res : "+res,2);
        IHM.log("-------\n",2);

        IHM.log("MinMax :"+tab_values[indice],1);
        res = indice;
        return res;
    }


}
