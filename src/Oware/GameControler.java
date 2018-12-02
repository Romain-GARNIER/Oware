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
    public static void playMove(Position pos_next, Position pos_current,boolean computer_play, int move){
        pos_next = pos_current.clone();
        int[] plateau1,plateau2;
        int nbSeed, position;
        int nbCapturedSeed = 0;
        if(computer_play){
            plateau1 = pos_current.cells_computer.clone();
            plateau2 = pos_current.cells_player.clone();
        }
        else{
            plateau1 = pos_current.cells_player.clone();
            plateau2 = pos_current.cells_computer.clone();
        }

        nbSeed = plateau1[move];
        position = move+1;

        for(int i=0;i<nbSeed;i++){
            position += i;
            // position du trou sur un camp (varie entre 1 et 6)
            int holePosition = position%6;
            // renvoi vrai si le reste de la division est paire (pour choisir le camp)
            if((position/6)%2 == 0){
                // si on revient sur la position de départ, on ne met pas de graine dedans
                if(holePosition == move)
                    holePosition++;
                plateau1[holePosition]++;
            }
            else{
                plateau2[holePosition-1]++;
                if(plateau2[holePosition] == 3 || plateau2[holePosition] == 2){
                    nbCapturedSeed += plateau2[holePosition-1];
                    plateau2[holePosition] = 0;
                }
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
    }

    public static boolean finalPosition(Position pos, boolean computer_play, int depth){
        for(int i=0;i<6;i++){
            if(pos.cells_computer[i] > 0 || pos.cells_player[i] > 0)
                return false;
        }
        return true;
    }

    public static int evaluation(Position pos, boolean computer_play, int depth){
        if(computer_play){
            return pos.seeds_computer - pos.seeds_player;
        }
        return pos.seeds_player - pos.seeds_computer;
    }
}
