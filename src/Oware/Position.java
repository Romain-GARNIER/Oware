package Oware;

class Position {
    boolean computer_play; // boolean true if the computer has to play and false otherwise
    int cells_red_player[] = new int[6]; // each cell contains a certain number of seeds
    int cells_red_computer[] = new int[6];
    int cells_black_player[] = new int[6]; // each cell contains a certain number of seeds
    int cells_black_computer[] = new int[6];

    int seeds_red_player; // seeds taken by the player
    int seeds_red_computer; // seeds taken by the computer
    int seeds_black_player; // seeds taken by the player
    int seeds_black_computer; // seeds taken by the computer

    @Override
    public Position clone(){
        Position pos = new Position();

        pos.computer_play = computer_play;

        pos.cells_red_computer = cells_red_computer.clone();
        pos.cells_red_player = cells_red_player.clone();
        pos.cells_black_computer = cells_black_computer.clone();
        pos.cells_black_player = cells_black_player.clone();

        pos.seeds_red_player = seeds_red_player;
        pos.seeds_red_computer = seeds_red_computer;
        pos.seeds_black_player = seeds_black_player;
        pos.seeds_black_computer = seeds_black_computer;

        return pos;
    }

    public void init(){
        for(int i=0;i<6;i++){
            cells_red_player[i] = 3;
            cells_red_computer[i] = 3;
            cells_black_player[i] = 3;
            cells_black_computer[i] = 3;
        }
    }

    public String toString(boolean computer_player_one){
        String res ="";
        String res1 = "";
        String res2 = "";
        int[] plateau_1_red, plateau_1_black;
        int[] plateau_2_red, plateau_2_black;
        int nbCaptured_1_red, nbCaptured_1_black;
        int nbCaptured_2_red, nbCaptured_2_black;
        //res += " - - - - - -\n";

        if(computer_player_one){
            plateau_1_red = cells_red_computer.clone();
            plateau_2_red = cells_red_player.clone();
            plateau_1_black = cells_black_computer.clone();
            plateau_2_black = cells_black_player.clone();

            nbCaptured_1_red = seeds_red_computer;
            nbCaptured_2_red = seeds_red_player;
            nbCaptured_1_black = seeds_black_computer;
            nbCaptured_2_black = seeds_black_player;

        }
        else{
            plateau_1_red = cells_red_player.clone();
            plateau_2_red = cells_red_computer.clone();
            plateau_1_black = cells_black_player.clone();
            plateau_2_black = cells_black_computer.clone();

            nbCaptured_1_red = seeds_red_player;
            nbCaptured_2_red = seeds_red_computer;
            nbCaptured_1_black = seeds_black_player;
            nbCaptured_2_black = seeds_black_computer;
        }

        for(int i=0;i<6;i++){
            res1 += "|"+plateau_1_red[i] + "-" + plateau_1_black[i];
            res2 += "|"+plateau_2_red[5-i]+"-"+plateau_2_black[5-i];
        }
        res1+="| capturee :" + nbCaptured_1_red + "-" + nbCaptured_1_black + "\n";
        res2+="| capturee :" + nbCaptured_2_red + "-" + nbCaptured_2_black + "\n";

        return res1+res2;
    }
}
