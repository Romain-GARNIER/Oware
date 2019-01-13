package Oware;

public abstract class Player {
    public boolean player_one;

    public abstract String chooseMove(Position position);

    public abstract int chooseStartSpecialSeed(Position position);
}
