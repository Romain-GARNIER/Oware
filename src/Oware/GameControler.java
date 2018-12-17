package Oware;

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

    public GameControler(){
        sc = new Scanner(System.in);
        alphaBetaCut = new AlphaBetaCut();
        position = new Position();
        position.init();
        minMax = new MinMax(this, position);
    }

    public void definePlayer(){
        int number = sc.nextInt();
        if(number == 1){
            this.computer_player_one = false;
        }
        if(number == 2)
            this.computer_player_one = true;
    }

    public void startGame(){

        while (!GameControler.finalPosition(position,false,0)){
            String move;

            IHM.console("---------------------------------------------------------------------------------------------------------------");
            IHM.console(position.toString(computer_player_one));
            IHM.console("Joueur 1 :");

            if(computer_player_one){
                move = minMax.minMaxMove(position,true,0,6);
                //hole = alphaBetaCut.AlphaBetaCutValue(position, true, 0, 9, -100, 100);
                //IHM.console("coup choisi par le bot : "+(hole+1));
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

            if(!GameControler.finalPosition(position,false,0)){
                IHM.console("---------------------------------------------------------------------------------------------------------------");
                IHM.console("Joueur 2 :");

                if(!computer_player_one) {
                    move = minMax.minMaxMove(position, true, 0, 6);
                    //hole = alphaBetaCut.AlphaBetaCutValue(position, true, 0, 9, -100, 100);
                    IHM.console("coup choisi par le bot : "+move);
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
        String[] tabMove = move.split("-");
        int hole = (Integer.parseInt(tabMove[0])-1)%6;
        String color = tabMove[1];
        int[] tableau;
        if(computer_play){
            if(color.equals(COLOR_RED))
                tableau = pos.cells_red_computer.clone();
            else
                tableau = pos.cells_black_computer.clone();
        }
        else{
            if(color.equals(COLOR_RED))
                tableau = pos.cells_red_player.clone();
            else
                tableau = pos.cells_black_player.clone();
        }
        return tableau[hole] != 0;
    }

    // Joue le coup avec une prise simple sans s'occuper de la continuité
    public static Position playMove(Position pos_current,boolean computer_play, String move){
        String[] tabMove = move.split("-");
        int hole = (Integer.parseInt(tabMove[0])-1)%6;
        String color = tabMove[1];

        Position pos_next = pos_current.clone();
        int[] plateau_player_1_color_1,plateau_player_1_color_2;
        int[] plateau_player_2_color_1,plateau_player_2_color_2;
        int nb_seed_color_1, nb_seed_color_2;
        int nb_captured_seed_color_1 = 0;
        int nb_captured_seed_color_2 = 0;
        int last_hole_1, last_hole_2;
        boolean enemy_side_1, enemy_side_2;
        int holeStart;

        if(computer_play){
            if(color.equals(COLOR_RED)){
                plateau_player_1_color_1 = pos_current.cells_red_computer.clone();
                plateau_player_2_color_1 = pos_current.cells_red_player.clone();
                plateau_player_1_color_2 = pos_current.cells_black_computer.clone();
                plateau_player_2_color_2 = pos_current.cells_black_player.clone();
            }else {
                plateau_player_1_color_1 = pos_current.cells_black_computer.clone();
                plateau_player_2_color_1 = pos_current.cells_black_player.clone();
                plateau_player_1_color_2 = pos_current.cells_red_computer.clone();
                plateau_player_2_color_2 = pos_current.cells_red_player.clone();
            }
        }
        else{
            if(color.equals(COLOR_RED)){
                plateau_player_1_color_1 = pos_current.cells_red_player.clone();
                plateau_player_2_color_1 = pos_current.cells_red_computer.clone();
                plateau_player_1_color_2 = pos_current.cells_black_player.clone();
                plateau_player_2_color_2 = pos_current.cells_black_computer.clone();
            }else {
                plateau_player_1_color_1 = pos_current.cells_black_player.clone();
                plateau_player_2_color_1 = pos_current.cells_black_computer.clone();
                plateau_player_1_color_2 = pos_current.cells_red_player.clone();
                plateau_player_2_color_2 = pos_current.cells_red_computer.clone();
            }
        }

        nb_seed_color_1 = plateau_player_1_color_1[hole];
        plateau_player_1_color_1[hole] = 0;
        holeStart = hole+1;
        last_hole_1 = -1;
        enemy_side_1 = false;
        int position = 0;
        for(int i=0;i<nb_seed_color_1;i++){
            position = holeStart + i;
            // position du trou sur un camp (varie entre 1 et 6)
            int holePosition = position%6;
            // renvoi vrai si le reste de la division est paire (pour choisir le camp)
            int tmp = (position/6)%2;
            boolean tmpBool = true;
            if(tmp == 0){
                // si on revient sur la position de départ, on ne met pas de graine dedans
                if(holePosition == hole) {
                    holeStart++;
                    position++;
                    if (holePosition == 5) { //Mod pour cas holepositon = 6;
                        holePosition = 0;
                        plateau_player_2_color_1[holePosition]++;
                        tmpBool = false;
                    } else {
                        holePosition++;
                    }
                }
                if(tmpBool){
                    plateau_player_1_color_1[holePosition]++;
                }
                enemy_side_1 = false;
            }
            else{
                plateau_player_2_color_1[holePosition]++;
                enemy_side_1 = true;
            }
            last_hole_1 = holePosition;
        }

        nb_seed_color_2 = plateau_player_1_color_2[hole];
        plateau_player_1_color_2[hole] = 0;
        holeStart = position+1;
        last_hole_2 = -1;
        enemy_side_2 = enemy_side_1;
        // Dernier coup dans le camp ennemmi
        for(int i=0;i<nb_seed_color_2;i++){
            position = holeStart + i;
            // position du trou sur un camp (varie entre 1 et 6)
            int holePosition = position%6;
            // renvoi vrai si le reste de la division est paire (pour choisir le camp)
            int tmp = (position/6)%2;
            boolean tmpBool = true;
            if(tmp == 0){
                // si on revient sur la position de départ, on ne met pas de graine dedans
                if(holePosition == hole) {
                    holeStart++;
                    if (holePosition == 5) { //Mod pour cas holepositon = 6;
                        holePosition = 0;
                        plateau_player_2_color_2[holePosition]++;
                        tmpBool = false;
                    } else {
                        holePosition++;
                    }
                }
                if(tmpBool){
                    plateau_player_1_color_2[holePosition]++;
                }
                enemy_side_1 = false;
                enemy_side_2 = false;
            }
            else{
                plateau_player_2_color_2[holePosition]++;
                enemy_side_2 = true;
            }
            last_hole_2 = holePosition;
        }

        int i = -1;
        boolean stop = false;
        if(last_hole_2 >= 0 && enemy_side_2){
            i = last_hole_2;
            while (i >= 0 && nb_seed_color_2 > 0 && !stop){
                int nb_seed = plateau_player_2_color_2[i];
                if( nb_seed == 2 || nb_seed == 3 ){
                    nb_captured_seed_color_2 += nb_seed;
                    plateau_player_2_color_2[i] = 0;
                }else {
                    stop = true;
                }
                nb_seed_color_2--;
                i--;
            }
        }
        if(last_hole_1 >= 0 && enemy_side_1 && !stop){
            while (i >= 0 && nb_seed_color_1 > 0 && !stop){
                int nb_seed = plateau_player_2_color_1[i];
                if( nb_seed == 2 || nb_seed == 3 ){
                    nb_captured_seed_color_1 += nb_seed;
                    plateau_player_2_color_1[i] = 0;
                }else {
                    stop = true;
                }
                nb_seed_color_1--;
                i--;
            }
        }

        if(computer_play){
            if(color.equals(COLOR_RED)){
                pos_next.cells_red_computer = plateau_player_1_color_1;
                pos_next.cells_red_player = plateau_player_2_color_1;
                pos_next.cells_black_computer = plateau_player_1_color_2;
                pos_next.cells_black_player = plateau_player_2_color_2;
                pos_next.seeds_red_computer += nb_captured_seed_color_1;
                pos_next.seeds_black_computer += nb_captured_seed_color_2;
            }else {
                pos_next.cells_black_computer = plateau_player_1_color_1;
                pos_next.cells_black_player = plateau_player_2_color_1;
                pos_next.cells_red_computer = plateau_player_1_color_2;
                pos_next.cells_red_player = plateau_player_2_color_2;
                pos_next.seeds_black_computer += nb_captured_seed_color_1;
                pos_next.seeds_red_computer += nb_captured_seed_color_2;
            }
        }
        else{
            if(color.equals(COLOR_RED)){
                pos_next.cells_red_player = plateau_player_1_color_1;
                pos_next.cells_red_computer = plateau_player_2_color_1;
                pos_next.cells_black_player = plateau_player_1_color_2;
                pos_next.cells_black_computer = plateau_player_2_color_2;
                pos_next.seeds_red_player += nb_captured_seed_color_1;
                pos_next.seeds_black_player += nb_captured_seed_color_2;
            }else {
                pos_next.cells_black_player = plateau_player_1_color_1;
                pos_next.cells_black_computer = plateau_player_2_color_1;
                pos_next.cells_red_player = plateau_player_1_color_2;
                pos_next.cells_red_computer = plateau_player_2_color_2;
                pos_next.seeds_black_player += nb_captured_seed_color_1;
                pos_next.seeds_red_player += nb_captured_seed_color_2;
            }
        }
        return pos_next;
    }

    public static boolean finalPosition(Position pos, boolean computer_play, int depth){
        int sum1, sum2;
        sum1 = 0;
        sum2 = 0;
        for(int i=0;i<6;i++){
            sum1 += pos.cells_red_computer[i];
            sum2 += pos.cells_red_player[i];
        }
        return sum1 == 0 || sum2 == 0;
    }

    public static int evaluation(Position posInit, Position pos){
        int eval = 1;
//        if(computer_play){

        //                                  SEED
        int seeds_player = (pos.seeds_red_player + pos.seeds_black_player);
        int seeds_playerInit = posInit.seeds_red_player + posInit.seeds_black_player;

        int seeds_computer = (pos.seeds_red_computer + pos.seeds_black_computer);
        int seeds_computerInit = posInit.seeds_red_computer + posInit.seeds_black_player;

        //                                  CELL
        //      PLAYER
        int cell_player = 0;
        for(int i =0; i<pos.cells_black_player.length; i++){
            cell_player += pos.cells_black_player[i];
        }
        for(int i = 0; i<pos.cells_red_player.length; i++){
            cell_player += pos.cells_red_player[i];
        }
        //      COMPUTER
        int cell_computer = 0;
        for(int i =0; i<pos.cells_black_computer.length; i++){
            cell_computer += pos.cells_black_computer[i];
        }
        for(int i = 0; i<pos.cells_red_computer.length; i++){
            cell_computer += pos.cells_red_computer[i];
        }


        //*                             Prise en compte que avancer Joueur Courant

        //** Prise en compte graine capturer
        if (seeds_player - seeds_playerInit > 0){
            eval += (seeds_player - seeds_playerInit)*2;
        }

        //** Prise en compte graine capturer
        if (cell_player > cell_computer){
            eval += (cell_player -  cell_computer);
        }

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
        int seeds_player = position.seeds_red_player + position.seeds_black_player;
        int seeds_computer = position.seeds_red_computer + position.seeds_black_computer;
        for (int i = 0; i < position.cells_red_player.length; i++){
            seeds_player += position.cells_red_player[i];
            seeds_player += position.cells_black_player[i];
        }

        for (int i = 0; i < position.cells_red_computer.length; i++){
            seeds_computer += position.cells_red_computer[i];
            seeds_computer += position.cells_black_computer[i];
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

}
