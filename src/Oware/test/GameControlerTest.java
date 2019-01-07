package Oware.test;

import Oware.GameControler;
import Oware.Hole;
import Oware.Position;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class GameControlerTest {
    Position position_to_test;

    @BeforeEach
    void initialize(){
        position_to_test = new Position();
    }

    @Test
    void validMove() {
        String move;
        int nb_seeds = 3;

        position_to_test.init(nb_seeds);

        //region Cas sans graine spéciale
        move = "1-R";
        Assert.assertTrue(GameControler.validMove(position_to_test, true, move));
        Assert.assertTrue(GameControler.validMove(position_to_test, false, move));

        //endregion

        //region Premier trou avec graines spéciale

        position_to_test.defineSpecialSeed(true,0);
        position_to_test.defineSpecialSeed(false,0);

        //Cas simple graine spéciale en première position
        move = "1-R-1";
        Assert.assertTrue(GameControler.validMove(position_to_test, true, move));
        Assert.assertTrue(GameControler.validMove(position_to_test, false, move));

        //Cas impossible avec 3 graines de chaque couleur
        move = "1-R-8";
        Assert.assertFalse(GameControler.validMove(position_to_test, true, move));
        Assert.assertFalse(GameControler.validMove(position_to_test, false, move));

        //endregion

        //region Dernier trou sans graines spéciale

        move = "6-R";
        Assert.assertTrue(GameControler.validMove(position_to_test, true, move));
        Assert.assertTrue(GameControler.validMove(position_to_test, false, move));

        move = "6-R-1";
        Assert.assertFalse(GameControler.validMove(position_to_test, true, move));
        Assert.assertFalse(GameControler.validMove(position_to_test, false, move));

        //endregion

        //region Premier trou camp averse avec graines spéciale

        move = "7-R-2";
        Assert.assertTrue(GameControler.validMove(position_to_test, true, move));
        Assert.assertTrue(GameControler.validMove(position_to_test, false, move));

        move = "7-R";
        Assert.assertFalse(GameControler.validMove(position_to_test, true, move));
        Assert.assertFalse(GameControler.validMove(position_to_test, false, move));

        //endregion

        //region Dernier trou camp adverse sans graines spéciale

        move = "12-R";
        Assert.assertTrue(GameControler.validMove(position_to_test, true, move));
        Assert.assertTrue(GameControler.validMove(position_to_test, false, move));

        move = "12-R-1";
        Assert.assertFalse(GameControler.validMove(position_to_test, true, move));
        Assert.assertFalse(GameControler.validMove(position_to_test, false, move));

        //endregion

        //region coup avec graine spéciale
        move = "1-R-1";

        position_to_test.cells_player_1[0] = new Hole(0,0,1);
        position_to_test.cells_player_2[0] = new Hole(0,1,1);

        Assert.assertTrue(GameControler.validMove(position_to_test, true, move));
        Assert.assertTrue(GameControler.validMove(position_to_test, false, move));

        //endregion

        //region Trou impossible

        move = "13-R-1";
        Assert.assertFalse(GameControler.validMove(position_to_test, true, move));
        Assert.assertFalse(GameControler.validMove(position_to_test, false, move));

        move = "13-R";
        Assert.assertFalse(GameControler.validMove(position_to_test, true, move));
        Assert.assertFalse(GameControler.validMove(position_to_test, false, move));

        move = "0-R-1";
        Assert.assertFalse(GameControler.validMove(position_to_test, true, move));
        Assert.assertFalse(GameControler.validMove(position_to_test, false, move));

        move = "0-R";
        Assert.assertFalse(GameControler.validMove(position_to_test, true, move));
        Assert.assertFalse(GameControler.validMove(position_to_test, false, move));

        //endregion

        //region Chaine incorrecte
        move = "0R";
        Assert.assertFalse(GameControler.validMove(position_to_test, true, move));

        //endregion

        //region coup invalide, trou vide
        move = "1-R";

        position_to_test.cells_player_1[0] = new Hole(0,0,0);
        position_to_test.cells_player_2[0] = new Hole(1,0,0);

        Assert.assertFalse(GameControler.validMove(position_to_test, true, move));
        Assert.assertTrue(GameControler.validMove(position_to_test, false, move));

        //endregion

    }

    @Test
    void playMove() {
        Position position_to_verify;
        boolean player_one;
        String move;
        int nbSeeds;

        position_to_verify = new Position();
        player_one = true;

        //region sans graine spéciale

        //region coup simple sans capture

        nbSeeds = 3;
        position_to_verify.init(nbSeeds);
        position_to_test.init(nbSeeds);
        move = "1-R";

        position_to_verify.cells_player_1[0] = new Hole(0,0,0);
        position_to_verify.cells_player_1[1] = new Hole(nbSeeds+1,nbSeeds,0);
        position_to_verify.cells_player_1[2] = new Hole(nbSeeds+1,nbSeeds,0);
        position_to_verify.cells_player_1[3] = new Hole(nbSeeds+1,nbSeeds,0);
        position_to_verify.cells_player_1[4] = new Hole(nbSeeds,nbSeeds+1,0);
        position_to_verify.cells_player_1[5] = new Hole(nbSeeds,nbSeeds+1,0);
        position_to_verify.cells_player_2[0] = new Hole(nbSeeds,nbSeeds+1,0);

        position_to_test = GameControler.playMove(position_to_test, player_one, move);

        Assert.assertArrayEquals(position_to_verify.cells_player_1,position_to_test.cells_player_1);
        Assert.assertArrayEquals(position_to_verify.cells_player_2,position_to_test.cells_player_2);
        Assert.assertEquals(position_to_verify.seeds_player_1,position_to_test.seeds_player_1);
        Assert.assertEquals(position_to_verify.seeds_player_2,position_to_test.seeds_player_2);


        //endregion

        //region tests avec captures

        //region coup sans graine spéciale avec capture

        nbSeeds = 2;
        position_to_verify.init(nbSeeds);
        position_to_test.init(nbSeeds);

        move = "3-R";

        position_to_verify.cells_player_1[0] = new Hole(nbSeeds,nbSeeds,0);
        position_to_verify.cells_player_1[1] = new Hole(nbSeeds,nbSeeds,0);
        position_to_verify.cells_player_1[2] = new Hole(0,0,0);
        position_to_verify.cells_player_1[3] = new Hole(nbSeeds+1,nbSeeds,0);
        position_to_verify.cells_player_1[4] = new Hole(nbSeeds+1,nbSeeds,0);
        position_to_verify.cells_player_1[5] = new Hole(nbSeeds,nbSeeds+1,0);
        position_to_verify.cells_player_2[0] = new Hole(nbSeeds,0,0);

        position_to_verify.seeds_player_1 = nbSeeds+1;

        position_to_test = GameControler.playMove(position_to_test, player_one, move);

        Assert.assertArrayEquals(position_to_verify.cells_player_1,position_to_test.cells_player_1);
        Assert.assertArrayEquals(position_to_verify.cells_player_2,position_to_test.cells_player_2);
        Assert.assertEquals(position_to_verify.seeds_player_1,position_to_test.seeds_player_1);
        Assert.assertEquals(position_to_verify.seeds_player_2,position_to_test.seeds_player_2);

        //endregion

        //region coup avec capture simple en début de plateau

        nbSeeds = 0;
        position_to_verify.init(nbSeeds);
        position_to_test.init(nbSeeds);

        position_to_verify.seeds_player_1 = 0;
        position_to_verify.seeds_player_2 = 0;
        position_to_test.seeds_player_1 = 0;
        position_to_test.seeds_player_2 = 0;

        position_to_test.cells_player_1[5] = new Hole(1,0,0);
        position_to_test.cells_player_2[0] = new Hole(1,0,0);

        move = "6-R";

        position_to_verify.cells_player_1[5] = new Hole(0,0,0);
        position_to_verify.cells_player_2[0] = new Hole(0,0,0);

        position_to_verify.seeds_player_1 = 2;

        position_to_test = GameControler.playMove(position_to_test, player_one, move);

        Assert.assertArrayEquals(position_to_verify.cells_player_1,position_to_test.cells_player_1);
        Assert.assertArrayEquals(position_to_verify.cells_player_2,position_to_test.cells_player_2);
        Assert.assertEquals(position_to_verify.seeds_player_1,position_to_test.seeds_player_1);
        Assert.assertEquals(position_to_verify.seeds_player_2,position_to_test.seeds_player_2);

        //endregion

        //region coup sans graine spéciale avec capture multiple

        nbSeeds = 3;
        position_to_verify.init(nbSeeds);
        position_to_test.init(nbSeeds);

        position_to_verify.seeds_player_1 = 0;
        position_to_verify.seeds_player_2 = 0;
        position_to_test.seeds_player_1 = 0;
        position_to_test.seeds_player_2 = 0;

        move = "2-R";

        position_to_verify.cells_player_1[0] = new Hole(nbSeeds,nbSeeds,0);
        position_to_verify.cells_player_1[1] = new Hole(0,0,0);
        position_to_verify.cells_player_1[2] = new Hole(nbSeeds+1,nbSeeds,0);
        position_to_verify.cells_player_1[3] = new Hole(nbSeeds+1,nbSeeds,0);
        position_to_verify.cells_player_1[4] = new Hole(nbSeeds+1,nbSeeds,0);
        position_to_verify.cells_player_1[5] = new Hole(nbSeeds,nbSeeds+1,0);
        position_to_verify.cells_player_2[0] = new Hole(nbSeeds,0,0);
        position_to_verify.cells_player_2[1] = new Hole(nbSeeds,0,0);

        position_to_verify.seeds_player_1 = 6;

        position_to_test.cells_player_2[0] = new Hole(3,2,0);
        position_to_test.cells_player_2[1] = new Hole(3,2,0);

        position_to_test = GameControler.playMove(position_to_test, player_one, move);

        Assert.assertArrayEquals(position_to_verify.cells_player_1,position_to_test.cells_player_1);
        Assert.assertArrayEquals(position_to_verify.cells_player_2,position_to_test.cells_player_2);
        Assert.assertEquals(position_to_verify.seeds_player_1,position_to_test.seeds_player_1);
        Assert.assertEquals(position_to_verify.seeds_player_2,position_to_test.seeds_player_2);

        //endregion

        //region coup sans graine spéciale avec capture multiple et changement de couleur

        nbSeeds = 2;
        position_to_verify.init(nbSeeds);
        position_to_test.init(nbSeeds);

        position_to_verify.seeds_player_1 = 0;
        position_to_verify.seeds_player_2 = 0;
        position_to_test.seeds_player_1 = 0;
        position_to_test.seeds_player_2 = 0;

        move = "6-R";

        position_to_verify.cells_player_1[5] = new Hole(0,0,0);
        position_to_verify.cells_player_2[0] = new Hole(nbSeeds+1,0,0);
        position_to_verify.cells_player_2[1] = new Hole(nbSeeds+1,0,0);
        position_to_verify.cells_player_2[2] = new Hole(nbSeeds,0,0);
        position_to_verify.cells_player_2[3] = new Hole(nbSeeds,0,0);

        position_to_verify.seeds_player_1 = 10;

        position_to_test = GameControler.playMove(position_to_test, player_one, move);

        Assert.assertArrayEquals(position_to_verify.cells_player_1,position_to_test.cells_player_1);
        Assert.assertArrayEquals(position_to_verify.cells_player_2,position_to_test.cells_player_2);
        Assert.assertEquals(position_to_verify.seeds_player_1,position_to_test.seeds_player_1);
        Assert.assertEquals(position_to_verify.seeds_player_2,position_to_test.seeds_player_2);

        //endregion*

        //region coup avec capture avec graine spéciale uniquement

        nbSeeds = 0;
        position_to_verify.init(nbSeeds);
        position_to_test.init(nbSeeds);

        position_to_verify.seeds_player_1 = 0;
        position_to_verify.seeds_player_2 = 0;
        position_to_test.seeds_player_1 = 0;
        position_to_test.seeds_player_2 = 0;

        position_to_test.cells_player_1[5] = new Hole(0,0,1);
        position_to_test.cells_player_2[0] = new Hole(1,1,0);

        move = "6-R-1";

        position_to_verify.cells_player_1[5] = new Hole(0,0,0);
        position_to_verify.cells_player_2[0] = new Hole(0,0,0);

        position_to_verify.seeds_player_1 = 3;

        position_to_test = GameControler.playMove(position_to_test, player_one, move);

        Assert.assertArrayEquals(position_to_verify.cells_player_1,position_to_test.cells_player_1);
        Assert.assertArrayEquals(position_to_verify.cells_player_2,position_to_test.cells_player_2);
        Assert.assertEquals(position_to_verify.seeds_player_1,position_to_test.seeds_player_1);
        Assert.assertEquals(position_to_verify.seeds_player_2,position_to_test.seeds_player_2);

        //endregion

        //endregion

        //endregion

        //region avec graine spéciale

        //region tests sans capture

        //region coup avec graine spéciale en première position sans capture

        nbSeeds = 3;
        position_to_verify.init(nbSeeds);
        position_to_test.init(nbSeeds);

        position_to_verify.defineSpecialSeed(true,0);
        position_to_test.defineSpecialSeed(true,0);

        move = "1-R-1";

        position_to_verify.cells_player_1[0] = new Hole(0,0,0);
        position_to_verify.cells_player_1[1] = new Hole(nbSeeds,nbSeeds,1);
        position_to_verify.cells_player_1[2] = new Hole(nbSeeds+1,nbSeeds,0);
        position_to_verify.cells_player_1[3] = new Hole(nbSeeds+1,nbSeeds,0);
        position_to_verify.cells_player_1[4] = new Hole(nbSeeds+1,nbSeeds,0);
        position_to_verify.cells_player_1[5] = new Hole(nbSeeds,nbSeeds+1,0);
        position_to_verify.cells_player_2[0] = new Hole(nbSeeds,nbSeeds+1,0);
        position_to_verify.cells_player_2[1] = new Hole(nbSeeds,nbSeeds+1,0);

        position_to_test = GameControler.playMove(position_to_test, player_one, move);

        Assert.assertArrayEquals(position_to_verify.cells_player_1,position_to_test.cells_player_1);
        Assert.assertArrayEquals(position_to_verify.cells_player_2,position_to_test.cells_player_2);
        Assert.assertEquals(position_to_verify.seeds_player_1,position_to_test.seeds_player_1);
        Assert.assertEquals(position_to_verify.seeds_player_2,position_to_test.seeds_player_2);

        //endregion

        //region coup avec graine spéciale en dernière position sans capture

        nbSeeds = 3;
        position_to_verify.init(nbSeeds);
        position_to_test.init(nbSeeds);

        position_to_verify.defineSpecialSeed(true,0);
        position_to_test.defineSpecialSeed(true,0);

        move = "1-R-7";

        position_to_verify.cells_player_1[0] = new Hole(0,0,0);
        position_to_verify.cells_player_1[1] = new Hole(nbSeeds+1,nbSeeds,0);
        position_to_verify.cells_player_1[2] = new Hole(nbSeeds+1,nbSeeds,0);
        position_to_verify.cells_player_1[3] = new Hole(nbSeeds+1,nbSeeds,0);
        position_to_verify.cells_player_1[4] = new Hole(nbSeeds,nbSeeds+1,0);
        position_to_verify.cells_player_1[5] = new Hole(nbSeeds,nbSeeds+1,0);
        position_to_verify.cells_player_2[0] = new Hole(nbSeeds,nbSeeds+1,0);
        position_to_verify.cells_player_2[1] = new Hole(nbSeeds,nbSeeds,1);

        position_to_test = GameControler.playMove(position_to_test, player_one, move);

        Assert.assertArrayEquals(position_to_verify.cells_player_1,position_to_test.cells_player_1);
        Assert.assertArrayEquals(position_to_verify.cells_player_2,position_to_test.cells_player_2);
        Assert.assertEquals(position_to_verify.seeds_player_1,position_to_test.seeds_player_1);
        Assert.assertEquals(position_to_verify.seeds_player_2,position_to_test.seeds_player_2);

        //endregion

        //region coup avec graine spéciale et mauvaise couleur donnée

        nbSeeds = 0;
        position_to_verify.init(nbSeeds);
        position_to_test.init(nbSeeds);

        position_to_verify.seeds_player_1 = 0;
        position_to_verify.seeds_player_2 = 0;
        position_to_test.seeds_player_1 = 0;
        position_to_test.seeds_player_2 = 0;

        position_to_test.cells_player_1[0] = new Hole(1,0,1);

        move = "1-B-1";

        position_to_verify.cells_player_1[1] = new Hole(0,0,1);
        position_to_verify.cells_player_1[2] = new Hole(1,0,0);

        position_to_test = GameControler.playMove(position_to_test, player_one, move);

        Assert.assertArrayEquals(position_to_verify.cells_player_1,position_to_test.cells_player_1);
        Assert.assertArrayEquals(position_to_verify.cells_player_2,position_to_test.cells_player_2);
        Assert.assertEquals(position_to_verify.seeds_player_1,position_to_test.seeds_player_1);
        Assert.assertEquals(position_to_verify.seeds_player_2,position_to_test.seeds_player_2);

        //endregion

        //endregion

        //region tests avec captures

        //region coup avec graine spéciale et avec capture multiple

        nbSeeds = 2;
        position_to_verify.init(nbSeeds);
        position_to_test.init(nbSeeds);

        position_to_verify.defineSpecialSeed(true,2);
        position_to_test.defineSpecialSeed(true,2);

        position_to_verify.seeds_player_1 = 0;
        position_to_verify.seeds_player_2 = 0;
        position_to_test.seeds_player_1 = 0;
        position_to_test.seeds_player_2 = 0;

        move = "3-R-5";

        position_to_verify.cells_player_1[0] = new Hole(nbSeeds,nbSeeds,0);
        position_to_verify.cells_player_1[1] = new Hole(nbSeeds,nbSeeds,0);
        position_to_verify.cells_player_1[2] = new Hole(0,0,0);
        position_to_verify.cells_player_1[3] = new Hole(nbSeeds+1,nbSeeds,0);
        position_to_verify.cells_player_1[4] = new Hole(nbSeeds+1,nbSeeds,0);
        position_to_verify.cells_player_1[5] = new Hole(nbSeeds,nbSeeds+1,0);
        position_to_verify.cells_player_2[0] = new Hole(0,0,0);
        position_to_verify.cells_player_2[1] = new Hole(0,0,0);

        position_to_verify.seeds_player_1 = 10;

        position_to_test = GameControler.playMove(position_to_test, player_one, move);

        Assert.assertArrayEquals(position_to_verify.cells_player_1,position_to_test.cells_player_1);
        Assert.assertArrayEquals(position_to_verify.cells_player_2,position_to_test.cells_player_2);
        Assert.assertEquals(position_to_verify.seeds_player_1,position_to_test.seeds_player_1);
        Assert.assertEquals(position_to_verify.seeds_player_2,position_to_test.seeds_player_2);

        //endregion

        //region coup avec graine spéciale et avec capture multiple et changement de couleur

        nbSeeds = 2;
        position_to_verify.init(nbSeeds);
        position_to_test.init(nbSeeds);

        position_to_verify.defineSpecialSeed(true,2);
        position_to_test.defineSpecialSeed(true,3);

        position_to_verify.seeds_player_1 = 0;
        position_to_verify.seeds_player_2 = 0;
        position_to_test.seeds_player_1 = 0;
        position_to_test.seeds_player_2 = 0;

        move = "4-R-5";

        position_to_test.cells_player_2[0] = new Hole(2,2,0);
        position_to_test.cells_player_2[1] = new Hole(2,3,0);

        position_to_verify.cells_player_1[0] = new Hole(nbSeeds,nbSeeds,0);
        position_to_verify.cells_player_1[1] = new Hole(nbSeeds,nbSeeds,0);
        position_to_verify.cells_player_1[2] = new Hole(nbSeeds,nbSeeds,0);
        position_to_verify.cells_player_1[3] = new Hole(0,0,0);
        position_to_verify.cells_player_1[4] = new Hole(nbSeeds+1,nbSeeds,0);
        position_to_verify.cells_player_1[5] = new Hole(nbSeeds+1,nbSeeds,0);
        position_to_verify.cells_player_2[0] = new Hole(0,3,0);
        position_to_verify.cells_player_2[1] = new Hole(0,4,0);
        position_to_verify.cells_player_2[2] = new Hole(0,0,0);

        position_to_verify.seeds_player_1 = 9;

        position_to_test = GameControler.playMove(position_to_test, player_one, move);

        Assert.assertArrayEquals(position_to_verify.cells_player_1,position_to_test.cells_player_1);
        Assert.assertArrayEquals(position_to_verify.cells_player_2,position_to_test.cells_player_2);
        Assert.assertEquals(position_to_verify.seeds_player_1,position_to_test.seeds_player_1);
        Assert.assertEquals(position_to_verify.seeds_player_2,position_to_test.seeds_player_2);

        //endregion

        //endregion

        //endregion
    }

    @Test
    void finalPosition() {
        Position pos;
        boolean player_one;

        pos = new Position();
        player_one = true;

        pos.init(0);

        pos.cells_player_2[0] = new Hole(1,1,1);

        Assert.assertTrue(GameControler.finalPosition(pos, player_one));
        Assert.assertFalse(GameControler.finalPosition(pos, !player_one));

        pos.cells_player_2[0] = new Hole(0,0,0);
        pos.cells_player_1[0] = new Hole(1,1,1);

        Assert.assertTrue(GameControler.finalPosition(pos, !player_one));
        Assert.assertFalse(GameControler.finalPosition(pos, player_one));
    }

    @Test
    void sow(){
        String color;
        int special_seed;
        int hole_to_sow;
        Hole[] board_player_1_to_test, board_player_2_to_test;
        Hole[] board_player_1_to_verify, board_player_2_to_verify;
        ArrayList<Boolean> enemy_side_to_test, enemy_side_to_verify;
        ArrayList<Integer> last_hole_to_test, last_hole_to_verify;
        int nb_hole_sow;
        int nb_seeds;
        int size;

        nb_seeds = 3;
        size = 6;

        board_player_1_to_test = new Hole[size];
        board_player_2_to_test = new Hole[size];
        board_player_1_to_verify = new Hole[size];
        board_player_2_to_verify = new Hole[size];

        enemy_side_to_verify = new ArrayList<>();
        last_hole_to_verify = new ArrayList<>();

        //region SEMAGE SANS GRAINE SPECIALE

        //region Semage simple sans graine spéciale

        enemy_side_to_test = new ArrayList<>();
        last_hole_to_test = new ArrayList<>();

        for(int i=0;i<size;i++){
            board_player_1_to_test[i] = new Hole(nb_seeds,nb_seeds,0);
            board_player_2_to_test[i] = new Hole(nb_seeds,nb_seeds,0);
            board_player_2_to_verify[i] = new Hole(nb_seeds,nb_seeds,0);
        }


        board_player_1_to_verify[0] = new Hole(0,0,0);
        board_player_1_to_verify[1] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_1_to_verify[2] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_1_to_verify[3] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_1_to_verify[4] = new Hole(nb_seeds,nb_seeds+1,0);
        board_player_1_to_verify[5] = new Hole(nb_seeds,nb_seeds+1,0);
        board_player_2_to_verify[0] = new Hole(nb_seeds,nb_seeds+1,0);

        enemy_side_to_verify = new ArrayList(Arrays.asList(false,false,false,false,false,true));
        last_hole_to_verify = new ArrayList(Arrays.asList(1,2,3,4,5,0));

        hole_to_sow = 0;
        color = GameControler.COLOR_RED;
        special_seed = -1;
        GameControler.sow(board_player_1_to_test,board_player_2_to_test,hole_to_sow,enemy_side_to_test,last_hole_to_test,color,special_seed);

        Assert.assertArrayEquals(board_player_1_to_verify,board_player_1_to_test);
        Assert.assertArrayEquals(board_player_2_to_verify,board_player_2_to_test);
        Assert.assertArrayEquals(enemy_side_to_verify.toArray(),enemy_side_to_test.toArray());
        Assert.assertArrayEquals(last_hole_to_verify.toArray(),last_hole_to_test.toArray());

        //endregion

        //region Semage uniquement dans le camp adverse sans graine spéciale

        enemy_side_to_test = new ArrayList<>();
        last_hole_to_test = new ArrayList<>();

        for(int i=0;i<size;i++){
            board_player_1_to_test[i] = new Hole(nb_seeds,nb_seeds,0);
            board_player_2_to_test[i] = new Hole(nb_seeds,nb_seeds,0);
            board_player_1_to_verify[i] = new Hole(nb_seeds,nb_seeds,0);
            board_player_2_to_verify[i] = new Hole(nb_seeds,nb_seeds,0);
        }

        board_player_1_to_verify[5] = new Hole(0,0,0);
        board_player_2_to_verify[0] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_2_to_verify[1] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_2_to_verify[2] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_2_to_verify[3] = new Hole(nb_seeds,nb_seeds+1,0);
        board_player_2_to_verify[4] = new Hole(nb_seeds,nb_seeds+1,0);
        board_player_2_to_verify[5] = new Hole(nb_seeds,nb_seeds+1,0);

        enemy_side_to_verify = new ArrayList(Arrays.asList(true,true,true,true,true,true));
        last_hole_to_verify = new ArrayList(Arrays.asList(0,1,2,3,4,5));

        hole_to_sow = 5;
        color = GameControler.COLOR_RED;
        special_seed = -1;
        GameControler.sow(board_player_1_to_test,board_player_2_to_test,hole_to_sow,enemy_side_to_test,last_hole_to_test,color,special_seed);

        Assert.assertArrayEquals(board_player_1_to_verify,board_player_1_to_test);
        Assert.assertArrayEquals(board_player_2_to_verify,board_player_2_to_test);
        Assert.assertArrayEquals(enemy_side_to_verify.toArray(),enemy_side_to_test.toArray());
        Assert.assertArrayEquals(last_hole_to_verify.toArray(),last_hole_to_test.toArray());

        //endregion

        //region Semage en revenant dans son camp sans graine spéciale

        enemy_side_to_test = new ArrayList<>();
        last_hole_to_test = new ArrayList<>();

        nb_seeds = 4;
        for(int i=0;i<size;i++){
            board_player_1_to_test[i] = new Hole(nb_seeds,nb_seeds,0);
            board_player_2_to_test[i] = new Hole(nb_seeds,nb_seeds,0);
            board_player_1_to_verify[i] = new Hole(nb_seeds,nb_seeds,0);
            board_player_2_to_verify[i] = new Hole(nb_seeds,nb_seeds,0);
        }

        board_player_1_to_verify[0] = new Hole(nb_seeds,nb_seeds+1,0);
        board_player_1_to_verify[1] = new Hole(nb_seeds,nb_seeds+1,0);
        board_player_1_to_verify[5] = new Hole(0,0,0);
        board_player_2_to_verify[0] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_2_to_verify[1] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_2_to_verify[2] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_2_to_verify[3] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_2_to_verify[4] = new Hole(nb_seeds,nb_seeds+1,0);
        board_player_2_to_verify[5] = new Hole(nb_seeds,nb_seeds+1,0);

        enemy_side_to_verify = new ArrayList(Arrays.asList(false,false,false,false,false,false,false,false));
        last_hole_to_verify = new ArrayList(Arrays.asList(0,1,2,3,4,5,0,1));

        hole_to_sow = 5;
        color = GameControler.COLOR_RED;
        special_seed = -1;
        GameControler.sow(board_player_1_to_test,board_player_2_to_test,hole_to_sow,enemy_side_to_test,last_hole_to_test,color,special_seed);

        Assert.assertArrayEquals(board_player_1_to_verify,board_player_1_to_test);
        Assert.assertArrayEquals(board_player_2_to_verify,board_player_2_to_test);
        Assert.assertArrayEquals(enemy_side_to_verify.toArray(),enemy_side_to_test.toArray());
        Assert.assertArrayEquals(last_hole_to_verify.toArray(),last_hole_to_test.toArray());

        //endregion

        //region Semage avec un tour complet sans graine spéciale

        enemy_side_to_test = new ArrayList<>();
        last_hole_to_test = new ArrayList<>();

        nb_seeds = 3;
        for(int i=0;i<size;i++){
            board_player_1_to_test[i] = new Hole(nb_seeds,nb_seeds,0);
            board_player_2_to_test[i] = new Hole(nb_seeds,nb_seeds,0);
            board_player_1_to_verify[i] = new Hole(nb_seeds,nb_seeds,0);
            board_player_2_to_verify[i] = new Hole(nb_seeds,nb_seeds,0);
        }

        board_player_1_to_test[2] = new Hole(6,6,0);

        board_player_1_to_verify[0] = new Hole(nb_seeds,nb_seeds+1,0);
        board_player_1_to_verify[1] = new Hole(nb_seeds,nb_seeds+1,0);
        board_player_1_to_verify[2] = new Hole(0,0,0);
        board_player_1_to_verify[3] = new Hole(nb_seeds+1,nb_seeds+1,0);
        board_player_1_to_verify[4] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_1_to_verify[5] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_2_to_verify[0] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_2_to_verify[1] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_2_to_verify[2] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_2_to_verify[3] = new Hole(nb_seeds,nb_seeds+1,0);
        board_player_2_to_verify[4] = new Hole(nb_seeds,nb_seeds+1,0);
        board_player_2_to_verify[5] = new Hole(nb_seeds,nb_seeds+1,0);

        enemy_side_to_verify = new ArrayList(Arrays.asList(false,false,false,false,false,false,false,false,false,false,false,false));
        last_hole_to_verify = new ArrayList(Arrays.asList(3,4,5,0,1,2,3,4,5,0,1,3));

        hole_to_sow = 2;
        color = GameControler.COLOR_RED;
        special_seed = -1;
        GameControler.sow(board_player_1_to_test,board_player_2_to_test,hole_to_sow,enemy_side_to_test,last_hole_to_test,color,special_seed);

        Assert.assertArrayEquals(board_player_1_to_verify,board_player_1_to_test);
        Assert.assertArrayEquals(board_player_2_to_verify,board_player_2_to_test);
        Assert.assertArrayEquals(enemy_side_to_verify.toArray(),enemy_side_to_test.toArray());
        Assert.assertArrayEquals(last_hole_to_verify.toArray(),last_hole_to_test.toArray());

        //endregion

        //region Semage avec un tour complet en revenant dans le camp adverse sans graine spéciale

        enemy_side_to_test = new ArrayList<>();
        last_hole_to_test = new ArrayList<>();

        nb_seeds = 3;
        for(int i=0;i<size;i++){
            board_player_1_to_test[i] = new Hole(nb_seeds,nb_seeds,0);
            board_player_2_to_test[i] = new Hole(nb_seeds,nb_seeds,0);
            board_player_1_to_verify[i] = new Hole(nb_seeds,nb_seeds,0);
            board_player_2_to_verify[i] = new Hole(nb_seeds,nb_seeds,0);
        }

        board_player_1_to_test[4] = new Hole(6,7,0);

        board_player_1_to_verify[0] = new Hole(nb_seeds,nb_seeds+1,0);
        board_player_1_to_verify[1] = new Hole(nb_seeds,nb_seeds+1,0);
        board_player_1_to_verify[2] = new Hole(nb_seeds,nb_seeds+1,0);
        board_player_1_to_verify[3] = new Hole(nb_seeds,nb_seeds+1,0);
        board_player_1_to_verify[4] = new Hole(0,0,0);
        board_player_1_to_verify[5] = new Hole(nb_seeds+1,nb_seeds+1,0);
        board_player_2_to_verify[0] = new Hole(nb_seeds+1,nb_seeds+1,0);
        board_player_2_to_verify[1] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_2_to_verify[2] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_2_to_verify[3] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_2_to_verify[4] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_2_to_verify[5] = new Hole(nb_seeds,nb_seeds+1,0);

        enemy_side_to_verify = new ArrayList(Arrays.asList(false,false,false,false,false,false,false,false,false,false,false,false,true));
        last_hole_to_verify = new ArrayList(Arrays.asList(5,0,1,2,3,4,5,0,1,2,3,5,0));

        hole_to_sow = 4;
        color = GameControler.COLOR_RED;
        special_seed = -1;
        GameControler.sow(board_player_1_to_test,board_player_2_to_test,hole_to_sow,enemy_side_to_test,last_hole_to_test,color,special_seed);

        Assert.assertArrayEquals(board_player_1_to_verify,board_player_1_to_test);
        Assert.assertArrayEquals(board_player_2_to_verify,board_player_2_to_test);
        Assert.assertArrayEquals(enemy_side_to_verify.toArray(),enemy_side_to_test.toArray());
        Assert.assertArrayEquals(last_hole_to_verify.toArray(),last_hole_to_test.toArray());

        //endregion

        //region Semage avec un tour complet en revenant dans le camp adverse sans graine spéciale (Cas particulier en partant de la dernière graine)

        enemy_side_to_test = new ArrayList<>();
        last_hole_to_test = new ArrayList<>();

        nb_seeds = 3;
        for(int i=0;i<size;i++){
            board_player_1_to_test[i] = new Hole(nb_seeds,nb_seeds,0);
            board_player_2_to_test[i] = new Hole(nb_seeds,nb_seeds,0);
            board_player_1_to_verify[i] = new Hole(nb_seeds,nb_seeds,0);
            board_player_2_to_verify[i] = new Hole(nb_seeds,nb_seeds,0);
        }

        board_player_1_to_test[5] = new Hole(6,6,0);

        board_player_1_to_verify[0] = new Hole(nb_seeds,nb_seeds+1,0);
        board_player_1_to_verify[1] = new Hole(nb_seeds,nb_seeds+1,0);
        board_player_1_to_verify[2] = new Hole(nb_seeds,nb_seeds+1,0);
        board_player_1_to_verify[3] = new Hole(nb_seeds,nb_seeds+1,0);
        board_player_1_to_verify[4] = new Hole(nb_seeds,nb_seeds+1,0);
        board_player_1_to_verify[5] = new Hole(0,0,0);
        board_player_2_to_verify[0] = new Hole(nb_seeds+1,nb_seeds+1,0);
        board_player_2_to_verify[1] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_2_to_verify[2] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_2_to_verify[3] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_2_to_verify[4] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_2_to_verify[5] = new Hole(nb_seeds+1,nb_seeds,0);

        enemy_side_to_verify = new ArrayList(Arrays.asList(false,false,false,false,false,false,false,false,false,false,false,true));
        last_hole_to_verify = new ArrayList(Arrays.asList(0,1,2,3,4,5,0,1,2,3,4,0));

        hole_to_sow = 5;
        color = GameControler.COLOR_RED;
        special_seed = -1;
        GameControler.sow(board_player_1_to_test,board_player_2_to_test,hole_to_sow,enemy_side_to_test,last_hole_to_test,color,special_seed);

        Assert.assertArrayEquals(board_player_1_to_verify,board_player_1_to_test);
        Assert.assertArrayEquals(board_player_2_to_verify,board_player_2_to_test);
        Assert.assertArrayEquals(enemy_side_to_verify.toArray(),enemy_side_to_test.toArray());
        Assert.assertArrayEquals(last_hole_to_verify.toArray(),last_hole_to_test.toArray());

        //endregion

        //endregion

        //region SEMAGE AVEC GRAINE SPECIALE

        //region Semage simple avec graine spéciale en première position

        nb_seeds = 3;

        enemy_side_to_test = new ArrayList<>();
        last_hole_to_test = new ArrayList<>();

        for(int i=0;i<size;i++){
            board_player_1_to_test[i] = new Hole(nb_seeds,nb_seeds,0);
            board_player_2_to_test[i] = new Hole(nb_seeds,nb_seeds,0);
            board_player_2_to_verify[i] = new Hole(nb_seeds,nb_seeds,0);
        }

        board_player_1_to_test[0] = new Hole(nb_seeds,nb_seeds,1);

        board_player_1_to_verify[0] = new Hole(0,0,0);
        board_player_1_to_verify[1] = new Hole(nb_seeds,nb_seeds,1);
        board_player_1_to_verify[2] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_1_to_verify[3] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_1_to_verify[4] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_1_to_verify[5] = new Hole(nb_seeds,nb_seeds+1,0);
        board_player_2_to_verify[0] = new Hole(nb_seeds,nb_seeds+1,0);
        board_player_2_to_verify[1] = new Hole(nb_seeds,nb_seeds+1,0);

        enemy_side_to_verify = new ArrayList(Arrays.asList(false,false,false,false,false,true,true));
        last_hole_to_verify = new ArrayList(Arrays.asList(1,2,3,4,5,0,1));

        hole_to_sow = 0;
        color = GameControler.COLOR_RED;
        special_seed = 0;
        GameControler.sow(board_player_1_to_test,board_player_2_to_test,hole_to_sow,enemy_side_to_test,last_hole_to_test,color,special_seed);

        Assert.assertArrayEquals(board_player_1_to_verify,board_player_1_to_test);
        Assert.assertArrayEquals(board_player_2_to_verify,board_player_2_to_test);
        Assert.assertArrayEquals(enemy_side_to_verify.toArray(),enemy_side_to_test.toArray());
        Assert.assertArrayEquals(last_hole_to_verify.toArray(),last_hole_to_test.toArray());

        //endregion

        //region Semage simple avec graine spéciale en dernière position

        nb_seeds = 3;

        enemy_side_to_test = new ArrayList<>();
        last_hole_to_test = new ArrayList<>();

        for(int i=0;i<size;i++){
            board_player_1_to_test[i] = new Hole(nb_seeds,nb_seeds,0);
            board_player_2_to_test[i] = new Hole(nb_seeds,nb_seeds,0);
            board_player_2_to_verify[i] = new Hole(nb_seeds,nb_seeds,0);
        }

        board_player_1_to_test[0] = new Hole(nb_seeds,nb_seeds,1);

        board_player_1_to_verify[0] = new Hole(0,0,0);
        board_player_1_to_verify[1] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_1_to_verify[2] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_1_to_verify[3] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_1_to_verify[4] = new Hole(nb_seeds,nb_seeds+1,0);
        board_player_1_to_verify[5] = new Hole(nb_seeds,nb_seeds+1,0);
        board_player_2_to_verify[0] = new Hole(nb_seeds,nb_seeds+1,0);
        board_player_2_to_verify[1] = new Hole(nb_seeds,nb_seeds,1);

        enemy_side_to_verify = new ArrayList(Arrays.asList(false,false,false,false,false,true,true));
        last_hole_to_verify = new ArrayList(Arrays.asList(1,2,3,4,5,0,1));

        hole_to_sow = 0;
        color = GameControler.COLOR_RED;
        special_seed = 6;
        GameControler.sow(board_player_1_to_test,board_player_2_to_test,hole_to_sow,enemy_side_to_test,last_hole_to_test,color,special_seed);

        Assert.assertArrayEquals(board_player_1_to_verify,board_player_1_to_test);
        Assert.assertArrayEquals(board_player_2_to_verify,board_player_2_to_test);
        Assert.assertArrayEquals(enemy_side_to_verify.toArray(),enemy_side_to_test.toArray());
        Assert.assertArrayEquals(last_hole_to_verify.toArray(),last_hole_to_test.toArray());

        //endregion

        //region Semage simple avec graine spéciale entre les deux couleurs

        nb_seeds = 3;

        enemy_side_to_test = new ArrayList<>();
        last_hole_to_test = new ArrayList<>();

        for(int i=0;i<size;i++){
            board_player_1_to_test[i] = new Hole(nb_seeds,nb_seeds,0);
            board_player_2_to_test[i] = new Hole(nb_seeds,nb_seeds,0);
            board_player_2_to_verify[i] = new Hole(nb_seeds,nb_seeds,0);
        }

        board_player_1_to_test[0] = new Hole(nb_seeds,nb_seeds,1);

        board_player_1_to_verify[0] = new Hole(0,0,0);
        board_player_1_to_verify[1] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_1_to_verify[2] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_1_to_verify[3] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_1_to_verify[4] = new Hole(nb_seeds,nb_seeds,1);
        board_player_1_to_verify[5] = new Hole(nb_seeds,nb_seeds+1,0);
        board_player_2_to_verify[0] = new Hole(nb_seeds,nb_seeds+1,0);
        board_player_2_to_verify[1] = new Hole(nb_seeds,nb_seeds+1,0);

        enemy_side_to_verify = new ArrayList(Arrays.asList(false,false,false,false,false,true,true));
        last_hole_to_verify = new ArrayList(Arrays.asList(1,2,3,4,5,0,1));

        hole_to_sow = 0;
        color = GameControler.COLOR_RED;
        special_seed = 3;
        GameControler.sow(board_player_1_to_test,board_player_2_to_test,hole_to_sow,enemy_side_to_test,last_hole_to_test,color,special_seed);

        Assert.assertArrayEquals(board_player_1_to_verify,board_player_1_to_test);
        Assert.assertArrayEquals(board_player_2_to_verify,board_player_2_to_test);
        Assert.assertArrayEquals(enemy_side_to_verify.toArray(),enemy_side_to_test.toArray());
        Assert.assertArrayEquals(last_hole_to_verify.toArray(),last_hole_to_test.toArray());

        //endregion

        //region Semage simple avec graine spéciale au milieu du semage de la première couleur

        nb_seeds = 3;

        enemy_side_to_test = new ArrayList<>();
        last_hole_to_test = new ArrayList<>();

        for(int i=0;i<size;i++){
            board_player_1_to_test[i] = new Hole(nb_seeds,nb_seeds,0);
            board_player_2_to_test[i] = new Hole(nb_seeds,nb_seeds,0);
            board_player_2_to_verify[i] = new Hole(nb_seeds,nb_seeds,0);
        }

        board_player_1_to_test[0] = new Hole(nb_seeds,nb_seeds,1);

        board_player_1_to_verify[0] = new Hole(0,0,0);
        board_player_1_to_verify[1] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_1_to_verify[2] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_1_to_verify[3] = new Hole(nb_seeds,nb_seeds,1);
        board_player_1_to_verify[4] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_1_to_verify[5] = new Hole(nb_seeds,nb_seeds+1,0);
        board_player_2_to_verify[0] = new Hole(nb_seeds,nb_seeds+1,0);
        board_player_2_to_verify[1] = new Hole(nb_seeds,nb_seeds+1,0);

        enemy_side_to_verify = new ArrayList(Arrays.asList(false,false,false,false,false,true,true));
        last_hole_to_verify = new ArrayList(Arrays.asList(1,2,3,4,5,0,1));

        hole_to_sow = 0;
        color = GameControler.COLOR_RED;
        special_seed = 2;
        GameControler.sow(board_player_1_to_test,board_player_2_to_test,hole_to_sow,enemy_side_to_test,last_hole_to_test,color,special_seed);

        Assert.assertArrayEquals(board_player_1_to_verify,board_player_1_to_test);
        Assert.assertArrayEquals(board_player_2_to_verify,board_player_2_to_test);
        Assert.assertArrayEquals(enemy_side_to_verify.toArray(),enemy_side_to_test.toArray());
        Assert.assertArrayEquals(last_hole_to_verify.toArray(),last_hole_to_test.toArray());

        //endregion

        //region Semage simple avec graine spéciale au milieu du semage de la deuxième couleur

        nb_seeds = 3;

        enemy_side_to_test = new ArrayList<>();
        last_hole_to_test = new ArrayList<>();

        for(int i=0;i<size;i++){
            board_player_1_to_test[i] = new Hole(nb_seeds,nb_seeds,0);
            board_player_2_to_test[i] = new Hole(nb_seeds,nb_seeds,0);
            board_player_2_to_verify[i] = new Hole(nb_seeds,nb_seeds,0);
        }

        board_player_1_to_test[0] = new Hole(nb_seeds,nb_seeds,1);

        board_player_1_to_verify[0] = new Hole(0,0,0);
        board_player_1_to_verify[1] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_1_to_verify[2] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_1_to_verify[3] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_1_to_verify[4] = new Hole(nb_seeds,nb_seeds+1,0);
        board_player_1_to_verify[5] = new Hole(nb_seeds,nb_seeds,1);
        board_player_2_to_verify[0] = new Hole(nb_seeds,nb_seeds+1,0);
        board_player_2_to_verify[1] = new Hole(nb_seeds,nb_seeds+1,0);

        enemy_side_to_verify = new ArrayList(Arrays.asList(false,false,false,false,false,true,true));
        last_hole_to_verify = new ArrayList(Arrays.asList(1,2,3,4,5,0,1));

        hole_to_sow = 0;
        color = GameControler.COLOR_RED;
        special_seed = 4;
        GameControler.sow(board_player_1_to_test,board_player_2_to_test,hole_to_sow,enemy_side_to_test,last_hole_to_test,color,special_seed);

        Assert.assertArrayEquals(board_player_1_to_verify,board_player_1_to_test);
        Assert.assertArrayEquals(board_player_2_to_verify,board_player_2_to_test);
        Assert.assertArrayEquals(enemy_side_to_verify.toArray(),enemy_side_to_test.toArray());
        Assert.assertArrayEquals(last_hole_to_verify.toArray(),last_hole_to_test.toArray());

        //endregion

        //region Semage simple avec deux graines spéciale en première position

        nb_seeds = 3;

        enemy_side_to_test = new ArrayList<>();
        last_hole_to_test = new ArrayList<>();

        for(int i=0;i<size;i++){
            board_player_1_to_test[i] = new Hole(nb_seeds,nb_seeds,0);
            board_player_2_to_test[i] = new Hole(nb_seeds,nb_seeds,0);
            board_player_2_to_verify[i] = new Hole(nb_seeds,nb_seeds,0);
        }

        board_player_1_to_test[0] = new Hole(nb_seeds,nb_seeds,2);

        board_player_1_to_verify[0] = new Hole(0,0,0);
        board_player_1_to_verify[1] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_1_to_verify[2] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_1_to_verify[3] = new Hole(nb_seeds+1,nb_seeds,0);
        board_player_1_to_verify[4] = new Hole(nb_seeds,nb_seeds+1,0);
        board_player_1_to_verify[5] = new Hole(nb_seeds,nb_seeds,1);
        board_player_2_to_verify[0] = new Hole(nb_seeds,nb_seeds,1);
        board_player_2_to_verify[1] = new Hole(nb_seeds,nb_seeds+1,0);
        board_player_2_to_verify[2] = new Hole(nb_seeds,nb_seeds+1,0);

        enemy_side_to_verify = new ArrayList(Arrays.asList(false,false,false,false,false,true,true,true));
        last_hole_to_verify = new ArrayList(Arrays.asList(1,2,3,4,5,0,1,2));

        hole_to_sow = 0;
        color = GameControler.COLOR_RED;
        special_seed = 4;
        GameControler.sow(board_player_1_to_test,board_player_2_to_test,hole_to_sow,enemy_side_to_test,last_hole_to_test,color,special_seed);

        Assert.assertArrayEquals(board_player_1_to_verify,board_player_1_to_test);
        Assert.assertArrayEquals(board_player_2_to_verify,board_player_2_to_test);
        Assert.assertArrayEquals(enemy_side_to_verify.toArray(),enemy_side_to_test.toArray());
        Assert.assertArrayEquals(last_hole_to_verify.toArray(),last_hole_to_test.toArray());

        //endregion

        //endregion
    }

    @Test
    void addSeed() {
        String color;
        int hole_to_sow, hole_to_increase;
        Hole[] board_player_1_to_test, board_player_2_to_test;
        Hole[] board_player_1_to_verify, board_player_2_to_verify;
        ArrayList<Boolean> enemy_side_to_test, enemy_side_to_verify;
        ArrayList<Integer> last_hole_to_test, last_hole_to_verify;
        int nb_hole_sow;
        int nb_seeds, nb_total_seeds;

        nb_seeds = 3;
        nb_total_seeds = nb_seeds*2;

        board_player_1_to_test = new Hole[nb_total_seeds];
        board_player_2_to_test = new Hole[nb_total_seeds];
        board_player_1_to_verify = new Hole[nb_total_seeds];
        board_player_2_to_verify = new Hole[nb_total_seeds];

        enemy_side_to_test = new ArrayList<>();
        enemy_side_to_verify = new ArrayList<>();

        last_hole_to_test = new ArrayList<>();
        last_hole_to_verify = new ArrayList<>();

        for(int i=0;i<(nb_seeds*2);i++){
            board_player_1_to_test[i] = new Hole(nb_seeds,nb_seeds,0);
            board_player_2_to_test[i] = new Hole(nb_seeds,nb_seeds,0);
            board_player_1_to_verify[i] = new Hole(nb_seeds,nb_seeds,0);
            board_player_2_to_verify[i] = new Hole(nb_seeds,nb_seeds,0);
        }

        hole_to_sow = 1;

        //---------------- ajout d'une graine de couleur rouge ----------------
        color = GameControler.COLOR_RED;
        hole_to_increase = 2;
        nb_hole_sow = GameControler.addSeed(board_player_1_to_test,board_player_2_to_test,hole_to_sow,hole_to_increase,enemy_side_to_test,last_hole_to_test,color);
        board_player_1_to_verify[hole_to_increase] = new Hole(nb_seeds+1,nb_seeds,0);
        enemy_side_to_verify.add(false);
        last_hole_to_verify.add(hole_to_increase);

        Assert.assertArrayEquals(board_player_1_to_verify,board_player_1_to_test);
        Assert.assertArrayEquals(board_player_2_to_verify,board_player_2_to_test);
        Assert.assertArrayEquals(enemy_side_to_verify.toArray(),enemy_side_to_test.toArray());
        Assert.assertArrayEquals(last_hole_to_verify.toArray(),last_hole_to_test.toArray());
        Assert.assertEquals(1,nb_hole_sow);

        //---------------- ajout d'une graine de couleur noir ----------------
        color = GameControler.COLOR_BLACK;
        hole_to_increase = 3;
        nb_hole_sow = GameControler.addSeed(board_player_1_to_test,board_player_2_to_test,hole_to_sow,hole_to_increase,enemy_side_to_test,last_hole_to_test,color);
        board_player_1_to_verify[hole_to_increase] = new Hole(nb_seeds,nb_seeds+1,0);
        enemy_side_to_verify.add(false);
        last_hole_to_verify.add(hole_to_increase);

        Assert.assertArrayEquals(board_player_1_to_verify,board_player_1_to_test);
        Assert.assertArrayEquals(board_player_2_to_verify,board_player_2_to_test);
        Assert.assertArrayEquals(enemy_side_to_verify.toArray(),enemy_side_to_test.toArray());
        Assert.assertArrayEquals(last_hole_to_verify.toArray(),last_hole_to_test.toArray());
        Assert.assertEquals(1,nb_hole_sow);

        //---------------- ajout d'une graine de couleur noir ----------------
        color = GameControler.SPECIAL_SEED;
        hole_to_increase = 4;
        nb_hole_sow = GameControler.addSeed(board_player_1_to_test,board_player_2_to_test,hole_to_sow,hole_to_increase,enemy_side_to_test,last_hole_to_test,color);
        board_player_1_to_verify[hole_to_increase] = new Hole(nb_seeds,nb_seeds,1);
        enemy_side_to_verify.add(false);
        last_hole_to_verify.add(hole_to_increase);

        Assert.assertArrayEquals(board_player_1_to_verify,board_player_1_to_test);
        Assert.assertArrayEquals(board_player_2_to_verify,board_player_2_to_test);
        Assert.assertArrayEquals(enemy_side_to_verify.toArray(),enemy_side_to_test.toArray());
        Assert.assertArrayEquals(last_hole_to_verify.toArray(),last_hole_to_test.toArray());
        Assert.assertEquals(1,nb_hole_sow);

        //---------------- ajout d'une graine de couleur rouge dans le camp de joueur 2 ----------------
        color = GameControler.COLOR_RED;
        hole_to_increase = 6;
        nb_hole_sow = GameControler.addSeed(board_player_1_to_test,board_player_2_to_test,hole_to_sow,hole_to_increase,enemy_side_to_test,last_hole_to_test,color);
        board_player_2_to_verify[0] = new Hole(nb_seeds+1,nb_seeds,0);
        enemy_side_to_verify.add(true);
        last_hole_to_verify.add(0);

        Assert.assertArrayEquals(board_player_1_to_verify,board_player_1_to_test);
        Assert.assertArrayEquals(board_player_2_to_verify,board_player_2_to_test);
        Assert.assertArrayEquals(enemy_side_to_verify.toArray(),enemy_side_to_test.toArray());
        Assert.assertArrayEquals(last_hole_to_verify.toArray(),last_hole_to_test.toArray());
        Assert.assertEquals(1,nb_hole_sow);

        //---------------- ajout d'une graine de couleur rouge en revant dans le camp du joueur 1 ----------------
        color = GameControler.COLOR_RED;
        hole_to_increase = 12;
        nb_hole_sow = GameControler.addSeed(board_player_1_to_test,board_player_2_to_test,hole_to_sow,hole_to_increase,enemy_side_to_test,last_hole_to_test,color);
        board_player_1_to_verify[0] = new Hole(nb_seeds+1,nb_seeds,0);
        enemy_side_to_verify.set(3,false);
        enemy_side_to_verify.add(false);
        last_hole_to_verify.add(0);

        Assert.assertArrayEquals(board_player_1_to_verify,board_player_1_to_test);
        Assert.assertArrayEquals(board_player_2_to_verify,board_player_2_to_test);
        Assert.assertArrayEquals(enemy_side_to_verify.toArray(),enemy_side_to_test.toArray());
        Assert.assertArrayEquals(last_hole_to_verify.toArray(),last_hole_to_test.toArray());
        Assert.assertEquals(1,nb_hole_sow);

        //---------------- ajout d'une graine de couleur rouge avec un tour complet ----------------
        color = GameControler.COLOR_RED;
        hole_to_increase = 13;
        nb_hole_sow = GameControler.addSeed(board_player_1_to_test,board_player_2_to_test,hole_to_sow,hole_to_increase,enemy_side_to_test,last_hole_to_test,color);
        board_player_1_to_verify[2] = new Hole(nb_seeds+2,nb_seeds,0);
        enemy_side_to_verify.add(false);
        last_hole_to_verify.add(2);

        Assert.assertArrayEquals(board_player_1_to_verify,board_player_1_to_test);
        Assert.assertArrayEquals(board_player_2_to_verify,board_player_2_to_test);
        Assert.assertArrayEquals(enemy_side_to_verify.toArray(),enemy_side_to_test.toArray());
        Assert.assertArrayEquals(last_hole_to_verify.toArray(),last_hole_to_test.toArray());
        Assert.assertEquals(2,nb_hole_sow);

        //---------------- ajout d'une graine de couleur rouge en revenant dans le camp de joueur 2 ----------------
        color = GameControler.COLOR_RED;
        hole_to_increase = 18;
        nb_hole_sow = GameControler.addSeed(board_player_1_to_test,board_player_2_to_test,hole_to_sow,hole_to_increase,enemy_side_to_test,last_hole_to_test,color);
        board_player_2_to_verify[0] = new Hole(nb_seeds+2,nb_seeds,0);
        enemy_side_to_verify.add(true);
        last_hole_to_verify.add(0);

        Assert.assertArrayEquals(board_player_1_to_verify,board_player_1_to_test);
        Assert.assertArrayEquals(board_player_2_to_verify,board_player_2_to_test);
        Assert.assertArrayEquals(enemy_side_to_verify.toArray(),enemy_side_to_test.toArray());
        Assert.assertArrayEquals(last_hole_to_verify.toArray(),last_hole_to_test.toArray());
        Assert.assertEquals(1,nb_hole_sow);

    }

    @Test
    void capture_seeds(){
        Hole[] board;
        int hole_index;
        String color;
        int board_size;
        int nb_captured_seeds;

        board_size = 6;
        board = new Hole[board_size];

        //region capture sans graine spéciale

        //region capture impossible

        for(int i=0;i<board_size;i++){
            board[i] = new Hole(4,4,0);
        }

        hole_index = 2;
        color = GameControler.COLOR_RED;
        nb_captured_seeds = GameControler.capture_seeds(board, hole_index, color);

        Assert.assertEquals(0,nb_captured_seeds);

        //endregion

        //region capture simple sans dépasser le plateau

        for(int i=0;i<board_size;i++){
            board[i] = new Hole(3,4,0);
        }

        hole_index = 0;
        color = GameControler.COLOR_RED;
        nb_captured_seeds = GameControler.capture_seeds(board, hole_index, color);

        Assert.assertEquals(3,nb_captured_seeds);

        //endregion

        //region capture simple en début de plateau

        for(int i=0;i<board_size;i++){
            board[i] = new Hole(2,0,0);
        }

        hole_index = 0;
        color = GameControler.COLOR_RED;
        nb_captured_seeds = GameControler.capture_seeds(board, hole_index, color);

        Assert.assertEquals(2,nb_captured_seeds);

        //endregion

        //region capture multiple uniquement sur le plateau

        for(int i=0;i<board_size;i++){
            board[i] = new Hole(3,4,0);
        }

        hole_index = 2;
        color = GameControler.COLOR_RED;
        nb_captured_seeds = GameControler.capture_seeds(board, hole_index, color);

        Assert.assertEquals(9,nb_captured_seeds);

        //endregion

        //region capture multiple sans dépasser le plateau

        for(int i=0;i<board_size;i++){
            board[i] = new Hole(3,4,0);
        }

        hole_index = 1;
        color = GameControler.COLOR_RED;
        nb_captured_seeds = GameControler.capture_seeds(board, hole_index, color);

        Assert.assertEquals(6,nb_captured_seeds);

        //endregion

        //region capture simple avec interruption

        for(int i=0;i<board_size;i++){
            board[i] = new Hole(3,4,0);
        }

        board[1] = new Hole(4,4,0);

        hole_index = 2;
        color = GameControler.COLOR_RED;
        nb_captured_seeds = GameControler.capture_seeds(board, hole_index, color);

        Assert.assertEquals(3,nb_captured_seeds);

        //endregion

        //region capture multiple avec changement de couleur

        Hole[] board_to_verify = new Hole[board_size];

        for(int i=0;i<board_size;i++){
            board[i] = new Hole(3,3,0);
            board_to_verify[i] = new Hole(3,3,0);
        }

        board_to_verify[4] = new Hole(0,3,0);
        board_to_verify[3] = new Hole(0,3,0);
        board_to_verify[2] = new Hole(0,3,0);
        board_to_verify[1] = new Hole(0,3,0);
        board_to_verify[0] = new Hole(0,3,0);

        hole_index = 4;
        color = GameControler.COLOR_RED;
        nb_captured_seeds = GameControler.capture_seeds(board, hole_index, color);

        Assert.assertEquals(15,nb_captured_seeds);
        Assert.assertArrayEquals(board_to_verify,board);

        //endregion

        //endregion

        //region capture avec graine spéciale

        //region capture impossible

        board_to_verify = new Hole[board_size];

        for(int i=0;i<board_size;i++){
            board[i] = new Hole(4,4,0);
            board_to_verify[i] = new Hole(4,4,0);
        }

        board[2] = new Hole(4,4,1);
        board_to_verify[2] = new Hole(4,4,1);

        hole_index = 2;
        color = GameControler.SPECIAL_SEED;
        nb_captured_seeds = GameControler.capture_seeds(board, hole_index, color);

        Assert.assertEquals(0,nb_captured_seeds);
        Assert.assertArrayEquals(board_to_verify,board);

        //endregion

        //region capture simple avec graine spéciale en dernière position

        board_to_verify = new Hole[board_size];

        for(int i=0;i<board_size;i++){
            board[i] = new Hole(3,3,0);
            board_to_verify[i] = new Hole(3,3,0);
        }

        board[0] = new Hole(2,2,1);

        board_to_verify[0] = new Hole(0,0,0);

        hole_index = 0;
        color = GameControler.SPECIAL_SEED;
        nb_captured_seeds = GameControler.capture_seeds(board, hole_index, color);

        Assert.assertEquals(5,nb_captured_seeds);
        Assert.assertArrayEquals(board_to_verify,board);

        //endregion

        //region capture multiple avec graine spéciale en avant dernière position

        board_to_verify = new Hole[board_size];

        for(int i=0;i<board_size;i++){
            board[i] = new Hole(3,4,0);
            board_to_verify[i] = new Hole(3,4,0);
        }

        board[1] = new Hole(2,4,1);

        board_to_verify[2] = new Hole(0,4,0);
        board_to_verify[1] = new Hole(0,4,0);
        board_to_verify[0] = new Hole(0,4,0);

        hole_index = 2;
        color = GameControler.COLOR_RED;
        nb_captured_seeds = GameControler.capture_seeds(board, hole_index, color);

        Assert.assertEquals(9,nb_captured_seeds);
        Assert.assertArrayEquals(board_to_verify,board);

        //endregion

        //region capture multiple avec graine spéciale en dernière position

        board_to_verify = new Hole[board_size];

        for(int i=0;i<board_size;i++){
            board[i] = new Hole(3,3,0);
            board_to_verify[i] = new Hole(3,3,0);
        }

        board[1] = new Hole(2,2,1);

        board_to_verify[1] = new Hole(0,0,0);
        board_to_verify[0] = new Hole(0,0,0);

        hole_index = 1;
        color = GameControler.SPECIAL_SEED;
        nb_captured_seeds = GameControler.capture_seeds(board, hole_index, color);

        Assert.assertEquals(11,nb_captured_seeds);
        Assert.assertArrayEquals(board_to_verify,board);

        //endregion

        //region capture multiple avec graine spéciale en dernière position avec changement de couleur

        board_to_verify = new Hole[board_size];

        for(int i=0;i<board_size;i++){
            board[i] = new Hole(3,3,0);
            board_to_verify[i] = new Hole(3,3,0);
        }

        board[2] = new Hole(2,2,1);
        board[1] = new Hole(3,0,0);

        board_to_verify[2] = new Hole(0,0,0);
        board_to_verify[1] = new Hole(0,0,0);
        board_to_verify[0] = new Hole(0,3,0);

        hole_index = 2;
        color = GameControler.SPECIAL_SEED;
        nb_captured_seeds = GameControler.capture_seeds(board, hole_index, color);

        Assert.assertEquals(11,nb_captured_seeds);
        Assert.assertArrayEquals(board_to_verify,board);

        //endregion

        //region capture multiple avec graine spéciale en dernière position mais une couleur au départ

        board_to_verify = new Hole[board_size];

        for(int i=0;i<board_size;i++){
            board[i] = new Hole(3,3,0);
            board_to_verify[i] = new Hole(3,3,0);
        }

        board[2] = new Hole(2,4,1);

        board_to_verify[2] = new Hole(0,4,0);
        board_to_verify[1] = new Hole(0,3,0);
        board_to_verify[0] = new Hole(0,3,0);

        hole_index = 2;
        color = GameControler.SPECIAL_SEED;
        nb_captured_seeds = GameControler.capture_seeds(board, hole_index, color);

        Assert.assertEquals(9,nb_captured_seeds);
        Assert.assertArrayEquals(board_to_verify,board);

        //endregion

        //region capture multiple avec graine spéciale en dernière position avec interruption

        board_to_verify = new Hole[board_size];

        for(int i=0;i<board_size;i++){
            board[i] = new Hole(3,3,0);
            board_to_verify[i] = new Hole(3,3,0);
        }

        board[2] = new Hole(2,2,1);
        board[0] = new Hole(4,4,0);

        board_to_verify[2] = new Hole(0,0,0);
        board_to_verify[1] = new Hole(0,0,0);
        board_to_verify[0] = new Hole(4,4,0);

        hole_index = 2;
        color = GameControler.SPECIAL_SEED;
        nb_captured_seeds = GameControler.capture_seeds(board, hole_index, color);

        Assert.assertArrayEquals(board_to_verify,board);
        Assert.assertEquals(11,nb_captured_seeds);

        //endregion

        //region capture multiple avec graine spéciale en dernière position avec changement de couleur et interruption

        board_to_verify = new Hole[board_size];

        for(int i=0;i<board_size;i++){
            board[i] = new Hole(3,3,0);
            board_to_verify[i] = new Hole(3,3,0);
        }

        board[2] = new Hole(2,2,1);
        board[1] = new Hole(4,2,0);
        board[0] = new Hole(3,4,0);

        board_to_verify[2] = new Hole(0,0,0);
        board_to_verify[1] = new Hole(4,0,0);
        board_to_verify[0] = new Hole(3,4,0);

        hole_index = 2;
        color = GameControler.SPECIAL_SEED;
        nb_captured_seeds = GameControler.capture_seeds(board, hole_index, color);

        Assert.assertArrayEquals(board_to_verify,board);
        Assert.assertEquals(7,nb_captured_seeds);

        //endregion

        //endregion
    }

    @Test
    void collect_seeds() {
        Hole hole;
        String[] color = new String[1];
        int nb_captured_seeds;

        hole = new Hole(4,4,0);
        color[0] = GameControler.COLOR_RED;

        nb_captured_seeds = GameControler.collect_seeds(hole, color);

        Assert.assertEquals(0,nb_captured_seeds);
        Assert.assertEquals(4,hole.nb_red_seeds);
        Assert.assertEquals(4,hole.nb_black_seeds);
        Assert.assertEquals(0,hole.nb_special_seeds);

        //---------------------------------------------------

        hole = new Hole(3,3,0);
        color[0] = GameControler.COLOR_RED;

        nb_captured_seeds = GameControler.collect_seeds(hole, color);

        Assert.assertEquals(3,nb_captured_seeds);
        Assert.assertEquals(0,hole.nb_red_seeds);
        Assert.assertEquals(3,hole.nb_black_seeds);
        Assert.assertEquals(0,hole.nb_special_seeds);

        //---------------------------------------------------

        hole = new Hole(4,3,0);
        color[0] = GameControler.COLOR_RED;

        nb_captured_seeds = GameControler.collect_seeds(hole, color);

        Assert.assertEquals(0,nb_captured_seeds);
        Assert.assertEquals(4,hole.nb_red_seeds);
        Assert.assertEquals(3,hole.nb_black_seeds);
        Assert.assertEquals(0,hole.nb_special_seeds);

        //---------------------------------------------------

        hole = new Hole(2,2,1);
        color[0] = GameControler.SPECIAL_SEED;

        nb_captured_seeds = GameControler.collect_seeds(hole, color);

        Assert.assertEquals(5,nb_captured_seeds);
        Assert.assertEquals(0,hole.nb_red_seeds);
        Assert.assertEquals(0,hole.nb_black_seeds);
        Assert.assertEquals(0,hole.nb_special_seeds);
        Assert.assertEquals(color[0],GameControler.SPECIAL_SEED);

        //---------------------------------------------------

        hole = new Hole(2,2,0);
        color[0] = GameControler.SPECIAL_SEED;

        nb_captured_seeds = GameControler.collect_seeds(hole, color);

        Assert.assertEquals(4,nb_captured_seeds);
        Assert.assertEquals(0,hole.nb_red_seeds);
        Assert.assertEquals(0,hole.nb_black_seeds);
        Assert.assertEquals(0,hole.nb_special_seeds);
        Assert.assertEquals(color[0],GameControler.SPECIAL_SEED);

        //---------------------------------------------------

        hole = new Hole(2,3,1);
        color[0] = GameControler.SPECIAL_SEED;

        nb_captured_seeds = GameControler.collect_seeds(hole, color);

        Assert.assertEquals(3,nb_captured_seeds);
        Assert.assertEquals(0,hole.nb_red_seeds);
        Assert.assertEquals(3,hole.nb_black_seeds);
        Assert.assertEquals(0,hole.nb_special_seeds);
        Assert.assertEquals(color[0],GameControler.COLOR_RED);

        //---------------------------------------------------

        hole = new Hole(2,4,0);
        color[0] = GameControler.SPECIAL_SEED;

        nb_captured_seeds = GameControler.collect_seeds(hole, color);

        Assert.assertEquals(2,nb_captured_seeds);
        Assert.assertEquals(0,hole.nb_red_seeds);
        Assert.assertEquals(4,hole.nb_black_seeds);
        Assert.assertEquals(0,hole.nb_special_seeds);
        Assert.assertEquals(color[0],GameControler.COLOR_RED);
    }

    @Test
    void evaluation() {
    }

    @Test
    void finalEvaluation(){

    }

    @Test
    void winner() {
    }

    @Test
    void totalSeeds() {
    }

    @Test
    void fixDepth() {
    }

    @Test
    void containsSpecialSeed() {
    }
}