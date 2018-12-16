package Oware;

import java.util.Scanner;

public class GameControler {
    boolean computer_player_one;
    Scanner sc;
    MinMax minMax;
    AlphaBetaCut alphaBetaCut;
    Position position;

    public GameControler(){
        sc = new Scanner(System.in);
        minMax = new MinMax();
        alphaBetaCut = new AlphaBetaCut();
        position = new Position();
        position.init();
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
            int hole;

            IHM.console("---------------------------------------------------------------------------------------------------------------");
            IHM.console(position.toString(computer_player_one));
            IHM.console("Joueur 1 :");

            if(computer_player_one){
                //hole = minMax.minMaxValue(position,true,0,8);
                hole = alphaBetaCut.AlphaBetaCutValue(position, true, 0, 9, -100, 100);
                IHM.console("coup choisi par le bot : "+(hole+1));
            }
            else{
                IHM.console("Choisissez un trou entre 1 et 6 :");
                hole = Integer.parseInt(sc.next());
                hole = hole - 1;
                if(!validMove(position,false,hole)){
                    while (!validMove(position,false,hole)){
                        IHM.console("Le coup n'est pas valide : ");
                        IHM.console("Choisissez un trou entre 1 et 6 :");
                        hole = Integer.parseInt(sc.next());
                        hole = hole-1;
                    }
                }
            }
            position = GameControler.playMove(position,computer_player_one,hole);

//            System.out.println("MinMax : "+minMax.minMaxValue(position,!gameControler.computer_player_one,0,3));

//            if(computer_player_one)
                IHM.console(position.toString(true));

            if(!GameControler.finalPosition(position,false,0)){
                IHM.console("---------------------------------------------------------------------------------------------------------------");
                IHM.console("Joueur 2 :");

                if(!computer_player_one) {
                    //hole = minMax.minMaxValue(position, true, 0, 8);
                    hole = alphaBetaCut.AlphaBetaCutValue(position, true, 0, 9, -100, 100);
                    IHM.console("coup choisi par le bot : "+(hole+7));
                }
                else{
                    IHM.console("Choisissez un trou entre 7 et 12 :");
                    hole = Integer.parseInt(sc.next());
                    hole = (hole-1)%6;
                    if(!validMove(position,false,hole)){
                        while (!validMove(position,false,hole)){
                            IHM.console("Le coup n'est pas valide : ");
                            IHM.console("Choisissez un trou entre 7 et 12 :");
                            hole = Integer.parseInt(sc.next());
                            hole = (hole-1)%6;
                        }
                    }
                }

                position = GameControler.playMove(position,!computer_player_one,hole);

                //System.out.println("MinMax : "+minMax.minMaxValue(position,gameControler.computer_player_one,0,3));
            }
        }
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
        boolean ennemySide = false;
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
                ennemySide = false;
            }
            else{
                plateau2[holePosition]++;
                lastHole = holePosition;
                ennemySide = true;
            }
        }

        if(lastHole >= 0 && ennemySide){
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

    public static int evaluation(Position pos){
//        if(computer_play){
        int seeds_player = pos.seeds_player;
        int seeds_computer = pos.seeds_computer;
        for (int i = 0; i < pos.cells_player.length; i++){
            seeds_player += pos.cells_player[i];
        }

        for (int i = 0; i < pos.cells_computer.length; i++){
            seeds_computer += pos.cells_computer[i];
        }
        return seeds_computer - seeds_player;
//        }
//        return pos.seeds_player - pos.seeds_computer;
    }

    public int winner(){
        int scorePlayer1 = totalSeeds(1);
        int scorePlayer2 = totalSeeds(2);

        if(scorePlayer1 > scorePlayer2)
            return 1;
        return 2;

    }

    public int totalSeeds(int player){
        int seeds_player = position.seeds_player;
        int seeds_computer = position.seeds_computer;
        for (int i = 0; i < position.cells_player.length; i++){
            seeds_player += position.cells_player[i];
        }

        for (int i = 0; i < position.cells_computer.length; i++){
            seeds_computer += position.cells_computer[i];
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
