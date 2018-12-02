package Oware;

class Position {
    int cells_player[] = new int[12]; // each cell contains a certain number of seeds
    int cells_computer[] = new int[12];
    boolean computer_play; // boolean true if the computer has to play and false otherwise
    int seeds_player; // seeds taken by the player
    int seeds_computer; // seeds taken by the computer

    @Override
    public Position clone(){
        Position pos = new Position();
        pos.cells_computer = cells_computer.clone();
        pos.cells_player = cells_player.clone();
        pos.computer_play = computer_play;
        pos.seeds_player = seeds_player;
        pos.seeds_computer = seeds_computer;

        return pos;
    }

    public void init(){
        for(int i=0;i<6;i++){
            cells_player[i] = 4;
            cells_computer[i] = 4;
        }
    }

    public String toString(boolean computer_player_one){
        String res ="";
        String res1 = "";
        String res2 = "";
        int[] plateau1,plateau2;
        //res += " - - - - - -\n";

        if(computer_player_one){
            plateau1 = cells_computer.clone();
            plateau2 = cells_player.clone();
        }
        else{
            plateau1 = cells_player.clone();
            plateau2 = cells_computer.clone();
        }

        for(int i=0;i<6;i++){
            res1 += "|"+plateau1[i];
            res2 += "|"+plateau1[5-i];
        }
        res1+="|\n";
        res2+="|\n";

        return res1+res2;
    }
}
