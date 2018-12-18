package Oware;

public class Hole
{

    public int nb_red_seeds;
    public int nb_black_seeds;
    public int nb_special_seeds;

    public Hole(){
        this.nb_red_seeds = 0;
        this.nb_black_seeds = 0;
        this.nb_special_seeds = 0;
    }

    public Hole(int nb_red_seeds, int nb_black_seeds, int nb_special_seeds) {
        this.nb_red_seeds = nb_red_seeds;
        this.nb_black_seeds = nb_black_seeds;
        this.nb_special_seeds = nb_special_seeds;
    }

    public void addSeeds(int nb_seeds, String color){
        switch (color){
            case GameControler.COLOR_RED :
                this.nb_red_seeds += nb_seeds;
                break;
            case GameControler.COLOR_BLACK :
                this.nb_black_seeds += nb_seeds;
                break;
            case GameControler.SPECIAL_SEED :
                this.nb_special_seeds += nb_seeds;
                break;
        }
    }

    public void setSpecialSeed(int nb_seeds){
        this.nb_special_seeds = nb_seeds;
    }

    public int getNbSeeds(String color){
        switch (color){
            case GameControler.COLOR_RED :
                return nb_red_seeds;
            case GameControler.COLOR_BLACK :
                return nb_black_seeds;
            case GameControler.SPECIAL_SEED :
                return nb_special_seeds;
        }
        return -1;
    }

    public void setNbSeeds(String color, int nb_seeds){
        switch (color){
            case GameControler.COLOR_RED :
                nb_red_seeds = nb_seeds;
            case GameControler.COLOR_BLACK :
                nb_black_seeds = nb_seeds;;
            case GameControler.SPECIAL_SEED :
                nb_special_seeds = nb_seeds;
        }
    }

    public void setAllSeeds(int nbSeeds){
        this.nb_red_seeds = nbSeeds;
        this.nb_black_seeds = nbSeeds;
        this.nb_special_seeds = nbSeeds;
    }

    public int totalSeeds(){
        return nb_special_seeds + nb_red_seeds + nb_black_seeds;
    }

    @Override
    public Hole clone(){
        return new Hole(nb_red_seeds,nb_black_seeds,nb_special_seeds);
    }

    @Override
    public String toString(){
        return nb_red_seeds + "-" + nb_black_seeds + "-" + nb_special_seeds;
    }

}
