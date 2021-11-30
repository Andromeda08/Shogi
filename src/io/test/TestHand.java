package io.test;

import io.shogi.core.*;
import io.shogi.pieces.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestHand {

    private Hand hand;
    private Hand emptyHand;
    private Piece rook, testPiece;

    @Before
    public void setUp() {
        hand = new Hand(1);
        rook = new Rook(1);
        testPiece = new Pawn(1);

        hand.addPiece(rook);
    }

    @Test
    public void test_New_Hand_Is_Empty() {
        emptyHand = new Hand(1);
        ArrayList<Piece> list = emptyHand.getList();
        for (Piece p : list)
            assertNull(p);
    }

    @Test
    public void test_Add_Piece_To_Hand() {
        hand.addPiece(testPiece);
        assertEquals(testPiece, hand.getPiece(1));
    }

    @Test
    public void test_Remove_Piece_From_Hand() {
        hand.removePiece(rook);
        assertNull(hand.getPiece(0));
    }

    @Test
    public void test_Get_Piece_From_Hand() {
        Piece expected = hand.getPiece(0);
        assertEquals(expected, rook);
    }

    @Test
    public void test_Hand_Owner() {
        assertEquals(1, hand.getOwner());
    }
}
