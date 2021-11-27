package io.test;

import io.shogi.core.Hand;
import io.shogi.core.Piece;
import io.shogi.pieces.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestHandClass {

    private Hand hand;
    private Piece testPiece;
    private Piece firstPiece;

    @Before
    public void setUp() {
        hand = new Hand(1);
        firstPiece = new Rook(1);
        testPiece = new Pawn(1);

        hand.addPiece(firstPiece);
    }

    @Test
    public void test_Add_Piece_To_Hand() {
        hand.addPiece(testPiece);

        assertEquals(testPiece, hand.getPiece(1));
    }

    @Test
    public void test_Remove_Piece_From_Hand() {
        hand.addPiece(testPiece);
        hand.removePiece(testPiece);

        assertNull(hand.getPiece(1));
    }

    @Test
    public void test_Hand_Set_Piece() {
        hand.setPiece(0, testPiece);
        assertEquals(testPiece, hand.getPiece(0));
    }

    @Test
    public void test_Hand_Owner() {
        assertEquals(1, hand.getOwner());
    }
}
