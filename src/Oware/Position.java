package Oware;

public class Position {
    boolean computer_play; // boolean true if the computer has to play and false otherwise
    Hole[] cells_computer;
    Hole[] cells_player; // each cell contains a certain number of seeds

    int seeds_player; // seeds taken by the player
    int seeds_computer; // seeds taken by the computer

    public Position(){
        cells_computer = new Hole[6];
        cells_player = new Hole[6];
        seeds_player = 0;
        seeds_computer = 0;
        computer_play = true;
    }

    public int getSeeds_computer() {
        return seeds_computer;
    }

    public int getSeeds_player() {
        return seeds_player;
    }

    public Hole[] getCells_computer() {
        return cells_computer;
    }

    public Hole[] getCells_player() {
        return cells_player;
    }

    public void initDefault(){
        for(int i=0;i<6;i++){
            cells_player[i] = new Hole(3,3,0);
            cells_computer[i] = new Hole(3,3,0);
        }
    }

    public void init(int n){
        for(int i=0;i<6;i++){
            cells_player[i] = new Hole(n,n,0);
            cells_computer[i] = new Hole(n,n,0);
        }
    }

    public void defineSpecialSeed(boolean computer_play, int hole){
        if(computer_play)
            cells_computer[hole].setSpecialSeed(1);
        else
            cells_player[hole].setSpecialSeed(1);
    }

    @Override
    public Position clone(){
        Position pos = new Position();

        pos.computer_play = computer_play;

        for(int i = 0; i < cells_player.length; i++){
            pos.cells_player[i] = cells_player[i].clone();
            pos.cells_computer[i] = cells_computer[i].clone();
        }

        pos.seeds_player = seeds_player;
        pos.seeds_computer = seeds_computer;

        return pos;
    }

    public String toString(boolean computer_player_one){
        String res ="";
        String res1 = "";
        String res2 = "";
        Hole[] plateau_1;
        Hole[] plateau_2;
        int nbCaptured_player_1;
        int nbCaptured_player_2;
        int nb_captured_special_seed_1, nb_captured_special_seed_2;
        //res += " - - - - - -\n";

        if(computer_player_one){
            plateau_1 = cells_computer.clone();
            plateau_2 = cells_player.clone();

            nbCaptured_player_1 = seeds_computer;
            nbCaptured_player_2 = seeds_player;

        }
        else{
            plateau_1 = cells_player.clone();
            plateau_2 = cells_computer.clone();

            nbCaptured_player_1 = seeds_player;
            nbCaptured_player_2 = seeds_computer;
        }

        for(int i=0;i<6;i++){
            res1 += "|"+plateau_1[i].toString();
            res2 += "|"+plateau_2[5-i].toString();
        }
        res1+="| capturee :" + nbCaptured_player_1 + "\n";
        res2+="| capturee :" + nbCaptured_player_2 + "\n";

        return res1+res2;
    }
}
