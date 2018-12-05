package Oware;

public class GameControler {
    boolean computer_player_one;

    public void definePlayer(int number){
        if(number == 1){
            this.computer_player_one = false;
        }
        if(number == 2)
            this.computer_player_one = true;
    }

    public static boolean validMove(Position pos, boolean computer_play, int move){
        int[] plateau;
        if(computer_play)
            plateau = pos.cells_computer.clone();
        else
            plateau = pos.cells_player.clone();

        return plateau[move] != 0;
    }

    // Joue le coup avec une prise simple sans s'occuper de la continuité
    public static Position playMove(Position pos_current,boolean computer_play, int move){
        Position pos_next = pos_current.clone();
        int[] plateau1,plateau2;
        int nbSeed, position;
        int nbCapturedSeed = 0;
        int holeStart;
        if(computer_play){
            plateau1 = pos_current.cells_computer.clone();
            plateau2 = pos_current.cells_player.clone();
        }
        else{
            plateau1 = pos_current.cells_player.clone();
            plateau2 = pos_current.cells_computer.clone();
        }

        nbSeed = plateau1[move];
        plateau1[move] = 0;
        holeStart = move+1;

        // Dernier coup dans le camp ennemmi
        int lastHole = -1;
        for(int i=0;i<nbSeed;i++){
            position = holeStart + i;
            // position du trou sur un camp (varie entre 1 et 6)
            int holePosition = position%6;
            // renvoi vrai si le reste de la division est paire (pour choisir le camp)
            int tmp = (position/6)%2;
            boolean tmpBool = true;
            if(tmp == 0){
                // si on revient sur la position de départ, on ne met pas de graine dedans
                if(holePosition == move)
                    if (holePosition == 5){ //Mod pour cas holepositon = 6;
                        holePosition = 0;
                        plateau2[holePosition] = 0;
                        tmpBool = false;
                    }else{
                        holePosition++;
                    }
                if(tmpBool){
                    plateau1[holePosition]++;
                }
            }
            else{
                plateau2[holePosition]++;
                lastHole = holePosition;
            }
        }

        if(lastHole >= 0){
            int i = lastHole;
            boolean stop = false;
            while (i >= 0 && !stop){
                if(plateau2[i] == 2 || plateau2[i] == 3 ){
                    nbCapturedSeed += plateau2[i];
                    plateau2[i] = 0;
                }else {
                    stop = true;
                }
                i--;
            }
        }

        if(computer_play){
            pos_next.cells_computer = plateau1;
            pos_next.cells_player = plateau2;
            pos_next.seeds_computer += nbCapturedSeed;
        }
        else{
            pos_next.cells_player = plateau1;
            pos_next.cells_computer = plateau2;
            pos_next.seeds_player += nbCapturedSeed;
        }
        return pos_next;
    }

    public static boolean finalPosition(Position pos, boolean computer_play, int depth){
        int sum1, sum2;
        sum1 = 0;
        sum2 = 0;
        for(int i=0;i<6;i++){
            sum1 += pos.cells_computer[i];
            sum2 += pos.cells_player[i];
        }
        return sum1 == 0 || sum2 == 0;
    }

    public static int evaluation(Position pos, boolean computer_play, int depth){
//        if(computer_play){
            return pos.seeds_computer - pos.seeds_player;
//        }
//        return pos.seeds_player - pos.seeds_computer;
    }
}
