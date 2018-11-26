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
}
