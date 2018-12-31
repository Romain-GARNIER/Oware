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
    GameControler gameControlerForTest;
    Position positionForTest;

    @BeforeEach
    void initialize(){
        gameControlerForTest = new GameControler();
        positionForTest = new Position();

        positionForTest.initDefault();
        positionForTest.defineSpecialSeed(true, 0);
        positionForTest.defineSpecialSeed(false, 0);
    }

    @Test
    void validMove() {
        String move;

        //----------------Premier trou avec graines spéciale--------------------------------------

        //Cas avec graine spéciale
        move = "1-R-1";
        Assert.assertTrue(GameControler.validMove(positionForTest, true, move));
        Assert.assertTrue(GameControler.validMove(positionForTest, false, move));

        move = "1-R";
        Assert.assertFalse(GameControler.validMove(positionForTest, true, move));
        Assert.assertFalse(GameControler.validMove(positionForTest, false, move));

        //Cas impossible avec 3 graines de chaque couleur
        move = "1-R-8";
        Assert.assertFalse(GameControler.validMove(positionForTest, true, move));
        Assert.assertFalse(GameControler.validMove(positionForTest, false, move));

        //----------------Dernier trou sans graines spéciale--------------------------------------

        move = "6-R";
        Assert.assertTrue(GameControler.validMove(positionForTest, true, move));
        Assert.assertTrue(GameControler.validMove(positionForTest, false, move));

        move = "6-R-1";
        Assert.assertFalse(GameControler.validMove(positionForTest, true, move));
        Assert.assertFalse(GameControler.validMove(positionForTest, false, move));

        //----------------Premier trou camp averse avec graines spéciale--------------------------------------

        move = "7-R-2";
        Assert.assertTrue(GameControler.validMove(positionForTest, true, move));
        Assert.assertTrue(GameControler.validMove(positionForTest, false, move));

        move = "7-R";
        Assert.assertFalse(GameControler.validMove(positionForTest, true, move));
        Assert.assertFalse(GameControler.validMove(positionForTest, false, move));

        //----------------Dernier trou camp adverse sans graines spéciale--------------------------------------

        move = "12-R";
        Assert.assertTrue(GameControler.validMove(positionForTest, true, move));
        Assert.assertTrue(GameControler.validMove(positionForTest, false, move));

        move = "12-R-1";
        Assert.assertFalse(GameControler.validMove(positionForTest, true, move));
        Assert.assertFalse(GameControler.validMove(positionForTest, false, move));

        //----------------Trou impossible--------------------------------------

        move = "13-R-1";
        Assert.assertFalse(GameControler.validMove(positionForTest, true, move));
        Assert.assertFalse(GameControler.validMove(positionForTest, false, move));

        move = "13-R";
        Assert.assertFalse(GameControler.validMove(positionForTest, true, move));
        Assert.assertFalse(GameControler.validMove(positionForTest, false, move));

        move = "0-R-1";
        Assert.assertFalse(GameControler.validMove(positionForTest, true, move));
        Assert.assertFalse(GameControler.validMove(positionForTest, false, move));

        move = "0-R";
        Assert.assertFalse(GameControler.validMove(positionForTest, true, move));
        Assert.assertFalse(GameControler.validMove(positionForTest, false, move));

        //----------------Chaine incorrecte--------------------------------------
        move = "0R";
        Assert.assertFalse(GameControler.validMove(positionForTest, true, move));
    }

    @Test
    void playMove() {
    }

    @Test
    void finalPosition() {
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
    void collect_seeds() {
    }

    @Test
    void evaluation() {
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