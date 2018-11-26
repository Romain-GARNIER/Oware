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

    public static void playMove(Position pos_next, Position pos_current,boolean computer_play, int move){
        pos_next = pos_current.clone();
        int[] plateau1,plateau2;
        int nbSeed, position;
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

    }

    public static boolean finalPosition(Position pos, boolean computer_play, int depth){
        return false;
    }

    public static int evaluation(Position pos, boolean computer_play, int depth){
        return 0;
    }
}
