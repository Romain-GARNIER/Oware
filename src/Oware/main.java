package Oware;

public class main{
    public static void main (String[] args){
        GameControler gameControler = new GameControler();

        Position pos = new Position();
        pos.initDefault();
        pos.cells_player[0].nb_special_seeds = 1;

        IHM.level = 1;

        IHM.console("Joueur 1 (1) / Joueur 2 (2) ? :");
        gameControler.definePlayer();

        gameControler.initSpecialSeeds();

        gameControler.startGame();
        IHM.console(gameControler.position.toString());

        IHM.console("Joueur 1 : "+gameControler.totalSeeds(1)+" graines");
        IHM.console("Joueur 2 : "+gameControler.totalSeeds(2)+" graines");
        IHM.console("Vainqueur : joueur "+gameControler.winner());

    }
}

