package Oware;

import java.lang.reflect.Array;

class Position {
    boolean computer_play; // boolean true if the computer has to play and false otherwise
    Hole[] cells_computer = new Hole[6];
    Hole[] cells_player = new Hole[6]; // each cell contains a certain number of seeds

    int seeds_red_player; // seeds taken by the player
    int seeds_red_computer; // seeds taken by the computer
    int seeds_black_player; // seeds taken by the player
    int seeds_black_computer; // seeds taken by the computer
    int captured_special_seed_computer;
    int captured_special_seed_player;

    @Override
    public Position clone(){
        Position pos = new Position();

        pos.computer_play = computer_play;

        for(int i = 0; i < cells_player.length; i++){
            pos.cells_player[i] = cells_player[i].clone();
            pos.cells_computer[i] = cells_computer[i].clone();
        }

        pos.seeds_red_player = seeds_red_player;
        pos.seeds_red_computer = seeds_red_computer;
        pos.seeds_black_player = seeds_black_player;
        pos.seeds_black_computer = seeds_black_computer;

        pos.captured_special_seed_computer = captured_special_seed_computer;
        pos.captured_special_seed_player = captured_special_seed_player;



        return pos;
    }

    public void init(){
        for(int i=0;i<6;i++){
            cells_player[i] = new Hole(3,3,0);
            cells_computer[i] = new Hole(3,3,0);
        }
    }

    public String toString(boolean computer_player_one){
        String res ="";
        String res1 = "";
        String res2 = "";
        Hole[] plateau_1;
        Hole[] plateau_2;
        int nbCaptured_1_red, nbCaptured_1_black;
        int nbCaptured_2_red, nbCaptured_2_black;
        int nb_captured_special_seed_1, nb_captured_special_seed_2;
        //res += " - - - - - -\n";

        if(computer_player_one){
            plateau_1 = cells_computer.clone();
            plateau_2 = cells_player.clone();

            nbCaptured_1_red = seeds_red_computer;
            nbCaptured_2_red = seeds_red_player;

            nbCaptured_1_black = seeds_black_computer;
            nbCaptured_2_black = seeds_black_player;

            nb_captured_special_seed_1 = captured_special_seed_computer;
            nb_captured_special_seed_2 = captured_special_seed_player;

        }
        else{
            plateau_1 = cells_player.clone();
            plateau_2 = cells_computer.clone();

            nbCaptured_1_red = seeds_red_player;
            nbCaptured_2_red = seeds_red_computer;
            nbCaptured_1_black = seeds_black_player;
            nbCaptured_2_black = seeds_black_computer;

            nb_captured_special_seed_1 = captured_special_seed_player;
            nb_captured_special_seed_2 = captured_special_seed_computer;
        }

        for(int i=0;i<6;i++){
            res1 += "|"+plateau_1[i].toString();
            res2 += "|"+plateau_2[5-i].toString();
        }
        res1+="| capturee :" + nbCaptured_1_red + "-" + nbCaptured_1_black + "-" + nb_captured_special_seed_1 + "\n";
        res2+="| capturee :" + nbCaptured_2_red + "-" + nbCaptured_2_black + "-" + nb_captured_special_seed_2 + "\n";

        return res1+res2;
    }
}
