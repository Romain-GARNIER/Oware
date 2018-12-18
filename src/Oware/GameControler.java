package Oware;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class GameControler {
    boolean computer_player_one;
    Scanner sc;
    MinMax minMax;
    AlphaBetaCut alphaBetaCut;
    Position position;
    private static int depthMAX;

    static final String COLOR_RED = "R";
    static final String COLOR_BLACK = "B";
    static final String SPECIAL_SEED = "S";

    public GameControler(){
        sc = new Scanner(System.in);

        position = new Position();
        position.init();
        minMax = new MinMax(this, position);
        alphaBetaCut = new AlphaBetaCut(this, position);
    }

    public void definePlayer(){
        int number = sc.nextInt();
        if(number == 1){
            this.computer_player_one = false;
        }
        if(number == 2)
            this.computer_player_one = true;
    }

    public void defineSpecialSeed(boolean computer_play,int hole){
        if(computer_play)
            position.cells_computer[hole].setSpecialSeed(1);
        else
            position.cells_player[hole].setSpecialSeed(1);
    }

    public void initSpecialSeeds(){
        int hole;
        IHM.console("Choisissez un trou pour la graine spéciale :");
        hole = sc.nextInt();
        defineSpecialSeed(computer_player_one, hole-1);

        IHM.console("Choisissez un trou pour la graine spéciale :");
        hole = sc.nextInt();
        defineSpecialSeed(!computer_player_one, hole-1);
    }

    public void startGame(){
        int a = 0;
        int b = 96;
        int depth_start = 0;
        int depth_max = 7;
        while (!GameControler.finalPosition(position)){
            String move;

            IHM.console("---------------------------------------------------------------------------------------------------------------");
            IHM.console(position.toString(computer_player_one));
            IHM.console("Joueur 1 :");

            if(computer_player_one){
//                move = minMax.minMaxMove(position,true,0,3);
                move = alphaBetaCut.AlphaBetaCutStart(position,true,depth_start,depth_max,a,b);
                IHM.console("coup choisi par le bot : "+move);
            }
            else{
                IHM.console("Choisissez un trou entre 1 et 6 :");
                move = sc.next();
                if(!validMove(position,false,move)){
                    while (!validMove(position,false,move)){
                        IHM.console("Le coup n'est pas valide : ");
                        IHM.console("Choisissez un trou entre 1 et 6 :");
                        move = sc.next();
                    }
                }
            }
            position = GameControler.playMove(position,computer_player_one,move);

//            System.out.println("MinMax : "+minMax.minMaxValue(position,!gameControler.computer_player_one,0,3));

//            if(computer_player_one)
            IHM.console(position.toString(computer_player_one));

            if(!GameControler.finalPosition(position)){
                IHM.console("---------------------------------------------------------------------------------------------------------------");
                IHM.console("Joueur 2 :");

                if(!computer_player_one) {
//                    move = minMax.minMaxMove(position, true, 0, 3);
                    move = alphaBetaCut.AlphaBetaCutStart(position,true,depth_start,depth_max,a,b);
                    IHM.console("coup choisi par le bot : "+(move));
                }
                else{
                    IHM.console("Choisissez un trou entre 7 et 12 :");
                    move = sc.next();
                    if(!validMove(position,false,move)){
                        while (!validMove(position,false,move)){
                            IHM.console("Le coup n'est pas valide : ");
                            IHM.console("Choisissez un trou entre 7 et 12 :");
                            move = sc.next();
                        }
                    }
                }

                position = GameControler.playMove(position,!computer_player_one,move);

                //System.out.println("MinMax : "+minMax.minMaxValue(position,gameControler.computer_player_one,0,3));
            }
        }
    }

    public static boolean validMove(Position pos, boolean computer_play, String move){
        String[] tabMove;
        int hole, special_seed;
        String color;
        Hole[] tableau;
        int nb_seeds_color = -1;
        int nb_seeds_special = -1;

        tabMove = move.split("-");
        hole = (Integer.parseInt(tabMove[0])-1)%6;
        color = tabMove[1];
        special_seed = -1;

        if(tabMove.length == 3)
            special_seed = Integer.parseInt(tabMove[0]);

        if(computer_play){
            tableau = pos.cells_computer.clone();
        }
        else{
            tableau = pos.cells_player.clone();
        }
        nb_seeds_color = tableau[hole].getNbSeeds(color);
        if(special_seed != -1){
            nb_seeds_special = tableau[hole].getNbSeeds(SPECIAL_SEED);
            return nb_seeds_color > 0 && nb_seeds_special > 0;
        }
        return nb_seeds_color > 0;
    }

    // Joue le coup avec une prise simple sans s'occuper de la continuité
    public static Position playMove(Position pos_current,boolean computer_play, String move){
        String[] tabMove;
        int hole, special_seed;
        int last_hole = -1;
        String color;

        Position pos_next = pos_current.clone();
        Hole[] plateau_player_1;
        Hole[] plateau_player_2;

        int nb_captured_red_seeds = 0;
        int nb_captured_black_seeds = 0;
        int nb_captured_special_seeds = 0;
        int hole_start;
        ArrayList<Boolean> enemy_side = new ArrayList<>();
        ArrayList<Integer> lasts_hole = new ArrayList<>();

        tabMove = move.split("-");
        hole = (Integer.parseInt(tabMove[0])-1)%6;
        color = tabMove[1];
        special_seed = -1;

        if(tabMove.length == 3)
            special_seed = Integer.parseInt(tabMove[2]);

        if(computer_play){
            plateau_player_1 = pos_next.cells_computer;
            plateau_player_2 = pos_next.cells_player;
        }
        else{
            plateau_player_1 = pos_next.cells_player;
            plateau_player_2 = pos_next.cells_computer;
        }

        String[] ordre;
        String color_2;
        if(color.equals(COLOR_RED))
            color_2 = COLOR_BLACK;
        else
            color_2 = COLOR_RED;

        if(special_seed == -1){
            ordre = new String[2];
            ordre[0] = color;
            ordre[1] = color_2;
        }else {
            ordre = new String[3];
            switch (special_seed){
                case 1 :
                    ordre[0] = SPECIAL_SEED;
                    ordre[1] = color;
                    ordre[2] = color_2;
                    break;
                case 2 :
                    ordre[0] = color;
                    ordre[1] = SPECIAL_SEED;
                    ordre[2] = color_2;
                    break;
                case 3 :
                    ordre[0] = color;
                    ordre[1] = color_2;
                    ordre[2] = SPECIAL_SEED;
                    break;
            }
        }

        int nb_red_seeds = plateau_player_1[hole].getNbSeeds(COLOR_RED);
        int nb_black_seeds = plateau_player_1[hole].getNbSeeds(COLOR_BLACK);
        int nb_special_seeds = plateau_player_1[hole].getNbSeeds(SPECIAL_SEED);
        for(String color_seed : ordre){
            hole_start = hole+1;
            if(last_hole != -1)
                hole_start = last_hole+1;
            switch (color_seed){
                case COLOR_RED :
                    last_hole = sow(plateau_player_1, plateau_player_2, nb_red_seeds,hole_start, hole, enemy_side, COLOR_RED);
                    lasts_hole.add(last_hole);
                    break;
                case COLOR_BLACK :
                    last_hole = sow(plateau_player_1, plateau_player_2, nb_black_seeds,hole_start, hole, enemy_side, COLOR_BLACK);
                    lasts_hole.add(last_hole);
                    break;
                case SPECIAL_SEED :
                    last_hole = sow(plateau_player_1, plateau_player_2, nb_special_seeds,hole_start, hole, enemy_side, SPECIAL_SEED);
                    lasts_hole.add(last_hole);
                    break;
            }
        }

        boolean stop = false;
        int i = ordre.length-1;
        String color_seed = SPECIAL_SEED;
        boolean enemy = enemy_side.get(i);
        boolean special_seeds = false;
        while (!stop && i>0 && enemy){
            if(color_seed.equals(SPECIAL_SEED)){
                color_seed = ordre[i];
                if(color_seed.equals(SPECIAL_SEED))
                    special_seeds = true;
                else
                    special_seeds = false;
            }
            else {
                if(!ordre[i].equals(SPECIAL_SEED)){
                    stop = true;
                }else{
                    special_seeds = true;
                }
            }
            if(!stop){
                int nb_seeds = 0;
                boolean capture_red = false;
                boolean capture_black = false;
                switch (color_seed){
                    case COLOR_RED :
                        nb_seeds = nb_red_seeds;
                        capture_red = true;
                        break;
                    case COLOR_BLACK :
                        nb_seeds = nb_black_seeds;
                        capture_black = true;
                        break;
                    case SPECIAL_SEED :
                        nb_seeds = nb_special_seeds;
                        capture_red = true;
                        capture_black = true;
                        break;
                }
                if(special_seeds)
                    nb_seeds = nb_special_seeds;
                int j = lasts_hole.get(i);
                ArrayList<Integer> nb_captured_seeds = new ArrayList<>();
                while (j >= 0 && nb_seeds > 0 && !stop){
                    color_seed = collect_seeds(plateau_player_2,capture_red,capture_black,j, nb_captured_seeds,special_seeds);
                    if( color_seed.equals("-1") ){
                        stop = true;
                    }else{
                        switch (color_seed){
                            case COLOR_RED :
                                nb_captured_red_seeds += nb_captured_seeds.get(0);
                                if(special_seeds)
                                    nb_captured_special_seeds += nb_captured_seeds.get(2);
                                break;
                            case COLOR_BLACK :
                                nb_captured_black_seeds += nb_captured_seeds.get(1);
                                if(special_seeds)
                                    nb_captured_special_seeds += nb_captured_seeds.get(2);
                                break;
                            case SPECIAL_SEED :
                                nb_captured_red_seeds += nb_captured_seeds.get(0);
                                nb_captured_black_seeds += nb_captured_seeds.get(1);
                                nb_captured_special_seeds += nb_captured_seeds.get(2);
                                break;
                        }
                        nb_seeds--;
                        j--;
                    }
                }
                i--;
            }
        }

        if(computer_play){
            pos_next.cells_player = plateau_player_2;
            pos_next.cells_computer = plateau_player_1;
            pos_next.seeds_red_computer += nb_captured_red_seeds;
            pos_next.seeds_black_computer += nb_captured_black_seeds;
            pos_next.captured_special_seed_computer += nb_captured_special_seeds;
        }
        else{
            pos_next.cells_player = plateau_player_1;
            pos_next.cells_computer = plateau_player_2;
            pos_next.seeds_red_player += nb_captured_red_seeds;
            pos_next.seeds_black_player += nb_captured_black_seeds;
            pos_next.captured_special_seed_player += nb_captured_special_seeds;

        }
        return pos_next;
    }

    public static boolean finalPosition(Position pos){
        int sum1, sum2;
        sum1 = 0;
        sum2 = 0;
        for(int i=0;i<6;i++){
            sum1 += pos.cells_computer[i].totalSeeds();
            sum2 += pos.cells_player[i].totalSeeds();
        }
        return sum1 == 0 || sum2 == 0;
    }

    public static int sow(Hole[] plateau_1, Hole[] plateau_2, int nb_seed, int hole_start, int hole, ArrayList<Boolean> enemy_side_list, String color){
        int position;
        int last_hole = -1;
        int enemy_indice;
        boolean enemy_side = false;
        if(!enemy_side_list.isEmpty())
            enemy_side = enemy_side_list.get(enemy_side_list.size()-1);

        plateau_1[hole].setAllSeeds(0);

        enemy_side_list.add(false);
        enemy_indice = enemy_side_list.size()-1;

        for(int i=0;i<nb_seed;i++){
            position = hole_start + i;
            // position du trou sur un camp (varie entre 1 et 6)
            int holePosition = position%6;
            // renvoi vrai si le reste de la division est paire (pour choisir le camp)
            int tmp = (position/6)%2;
            boolean tmpBool = true;
            if(tmp == 0 && !enemy_side){
                // si on revient sur la position de départ, on ne met pas de graine dedans
                if(holePosition == hole) {
                    hole_start++;
                    position++;
                    if (holePosition == 5) { //Mod pour cas holepositon = 6;
                        holePosition = 0;
                        plateau_2[holePosition].addSeeds(1,color);
                        tmpBool = false;
                    } else {
                        holePosition++;
                    }
                }
                if(tmpBool){
                    plateau_1[holePosition].addSeeds(1,color);
                }
                Collections.replaceAll(enemy_side_list,true,false);
            }
            else{
                plateau_2[holePosition].addSeeds(1,color);
                enemy_side_list.set(enemy_indice,true);
            }
            last_hole = holePosition;
        }
        return last_hole;
    }

    public static String collect_seeds(Hole[] plateau, boolean red_seeds, boolean black_seeds, int hole, ArrayList<Integer> nb_seeds, boolean special_seeds) {
        int nb_red_seeds, nb_black_seeds, nb_special_seeds;

        nb_red_seeds = plateau[hole].getNbSeeds(COLOR_RED);
        nb_black_seeds = plateau[hole].getNbSeeds(COLOR_BLACK);
        nb_special_seeds = plateau[hole].getNbSeeds(SPECIAL_SEED);
        nb_seeds.add(nb_red_seeds);
        nb_seeds.add(nb_black_seeds);
        nb_seeds.add(nb_special_seeds);
        if (red_seeds && black_seeds) {
            if (nb_red_seeds > 0 && nb_red_seeds > 0) {
                if ((nb_red_seeds == 1 || nb_red_seeds == 2) && (nb_black_seeds == 1 || nb_black_seeds == 2)) {
                    plateau[hole].setNbSeeds(COLOR_RED, 0);
                    plateau[hole].setNbSeeds(COLOR_BLACK, 0);
                    plateau[hole].setNbSeeds(SPECIAL_SEED, 0);
                    return SPECIAL_SEED;
                }
            }
            if (nb_red_seeds > 0 && (nb_red_seeds == 1 || nb_red_seeds == 2)) {
                plateau[hole].setNbSeeds(COLOR_RED, 0);
                plateau[hole].setNbSeeds(SPECIAL_SEED, 0);
                return COLOR_RED;
            }
            if (nb_black_seeds > 0 && (nb_black_seeds == 1 || nb_black_seeds == 2)) {
                plateau[hole].setNbSeeds(COLOR_BLACK, 0);
                plateau[hole].setNbSeeds(SPECIAL_SEED, 0);
                return COLOR_BLACK;
            }
        }else {
            if (red_seeds && nb_red_seeds > 0 && (nb_red_seeds == 2 || nb_red_seeds == 3)) {
                plateau[hole].setNbSeeds(COLOR_RED, 0);
                if(special_seeds)
                    plateau[hole].setNbSeeds(SPECIAL_SEED, 0);
                return COLOR_RED;
            }
            if (black_seeds && nb_black_seeds > 0 && (nb_black_seeds == 2 || nb_black_seeds == 3)) {
                plateau[hole].setNbSeeds(COLOR_BLACK, 0);
                if(special_seeds)
                    plateau[hole].setNbSeeds(SPECIAL_SEED, 0);
                return COLOR_BLACK;
            }
        }
        return "-1";
    }

    public static int evaluation(Position posInit, Position pos){
        int eval = 1;
//        if(computer_play){

        //                                  SEED
        int seeds_player = (pos.seeds_red_player + pos.seeds_black_player + pos.captured_special_seed_player);
        int seeds_playerInit = posInit.seeds_red_player + posInit.seeds_black_player + pos.captured_special_seed_player;

        int seeds_computer = (pos.seeds_red_computer + pos.seeds_black_computer + pos.captured_special_seed_computer);
        int seeds_computerInit = posInit.seeds_red_computer + posInit.seeds_black_computer + pos.captured_special_seed_computer;

        //                                  CELL
        //      PLAYER
        int cell_player = 0;
        for(int i =0; i<pos.cells_player.length; i++){
            cell_player += pos.cells_player[i].totalSeeds();
        }
        //      COMPUTER
        int cell_computer = 0;
        for(int i = 0; i<pos.cells_computer.length; i++){
            cell_computer += pos.cells_computer[i].totalSeeds();
        }

        //*                             Prise en compte que avancer Joueur Courant

//        //** Prise en compte graine capturer
//        if (seeds_player - seeds_playerInit > 0){
//            eval += (seeds_player - seeds_playerInit)*2;
//        }
//
//        //** Prise en compte graine capturer
//        if (cell_player > cell_computer){
//            eval += (cell_player -  cell_computer);
//        }

        seeds_computer += cell_computer;
        seeds_player += cell_player;

        eval = seeds_computer - seeds_player;

        return eval;
    }

    public int winner(){
        int scorePlayer1 = totalSeeds(1);
        int scorePlayer2 = totalSeeds(2);

        if(scorePlayer1 > scorePlayer2)
            return 1;
        return 2;

    }

    public int totalSeeds(int player){
        int seeds_player = position.seeds_red_player + position.seeds_black_player + position.captured_special_seed_computer;
        int seeds_computer = position.seeds_red_computer + position.seeds_black_computer + position.captured_special_seed_computer;

        for (int i = 0; i < position.cells_player.length; i++){
            seeds_player += position.cells_player[i].totalSeeds();
        }

        for (int i = 0; i < position.cells_computer.length; i++){
            seeds_computer += position.cells_computer[i].totalSeeds();
        }

        if(computer_player_one){
            if(player == 1)
                return seeds_computer;
            return seeds_player;
        }
        else{
            if(player == 1)
                return seeds_player;
            return seeds_computer;
        }
    }

    public static boolean containsSpecialSeed(Position position, boolean computer_play, int hole){
        Hole[] tableau;
        int nb_seeds = -1;

        if(computer_play){
            tableau = position.cells_computer.clone();
        }
        else{
            tableau = position.cells_player.clone();
        }
        nb_seeds = tableau[hole].getNbSeeds(SPECIAL_SEED);
        return nb_seeds > 0;
    }
}
