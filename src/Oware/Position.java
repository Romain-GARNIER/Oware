package Oware;

import java.util.Arrays;

public class Position {
    public Hole[] cells_player_1;
    public Hole[] cells_player_2; // each cell contains a certain number of seeds

    public int seeds_player_1; // seeds taken by the player 1
    public int seeds_player_2; // seeds taken by the player 2

    public Position(){
        cells_player_1 = new Hole[6];
        cells_player_2 = new Hole[6];
        seeds_player_2 = 0;
        seeds_player_1 = 0;
    }

    public int getSeeds_player_1() {
        return seeds_player_1;
    }

    public int getSeeds_player_2() {
        return seeds_player_2;
    }

    public Hole[] getCells_player_1() {
        return cells_player_1;
    }

    public Hole[] getCells_player_2() {
        return cells_player_2;
    }

    public void initDefault(){
        for(int i=0;i<6;i++){
            cells_player_2[i] = new Hole(3,3,0);
            cells_player_1[i] = new Hole(3,3,0);
        }
    }

    public void init(int n){
        for(int i=0;i<6;i++){
            cells_player_2[i] = new Hole(n,n,0);
            cells_player_1[i] = new Hole(n,n,0);
        }
    }

    public void defineSpecialSeed(int hole){
        if(hole < 6)
            cells_player_1[hole].addSpecialSeed();
        else{
            hole = hole%6;
            cells_player_2[hole].addSpecialSeed();
        }
    }

    public String toString(){
        String res ="";
        String res1 = "";
        String res2 = "";
        int nb_captured_special_seed_1, nb_captured_special_seed_2;
        //res += " - - - - - -\n";

        for(int i=0;i<6;i++){
            res1 += "|"+cells_player_1[i].toString();
            res2 += "|"+cells_player_2[5-i].toString();
        }
        res1+="| capturee :" + seeds_player_1 + "\n";
        res2+="| capturee :" + seeds_player_2 + "\n";

        return res1+res2;
    }
    @Override
    public Position clone(){
        Position pos = new Position();

        for(int i = 0; i < cells_player_2.length; i++){
            pos.cells_player_2[i] = cells_player_2[i].clone();
            pos.cells_player_1[i] = cells_player_1[i].clone();
        }

        pos.seeds_player_2 = seeds_player_2;
        pos.seeds_player_1 = seeds_player_1;

        return pos;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash*13+ seeds_player_2;
        hash = hash*17+ seeds_player_1;
        hash = hash*12 + Arrays.hashCode(cells_player_2);
        hash = hash*3 + Arrays.hashCode(cells_player_1);
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return seeds_player_2 == position.getSeeds_player_2()
                && seeds_player_1 == position.getSeeds_player_1()
                && Arrays.equals(cells_player_2,position.cells_player_2)
                && Arrays.equals(cells_player_1,position.cells_player_1);
    }
}
