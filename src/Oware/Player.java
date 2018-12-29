package Oware;

public interface Player {
    public String name = "Player";

    public String chooseMove(Position position);

    public int chooseStartSpecialSeed(Position position);
}
