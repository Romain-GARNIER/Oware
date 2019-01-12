package Oware.IA;

import Oware.GameControler;
import Oware.Position;

public class IterativeDeepening {
    private static final int TIME_LIMIT_MILLIS = 3000;
    private static final int EVALS_PER_SECOND = 100;
    PVS pvs;
    AlphaBetaCut alphaBetaCut;

    public IterativeDeepening(Position posInit, GameControler gc) {
        this.pvs = new PVS(posInit, gc);
        //this.alphaBetaCut = alphaBetaCut;
    }


}
