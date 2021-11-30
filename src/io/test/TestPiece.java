package io.test;

import io.shogi.core.*;
import io.shogi.pieces.*;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestPiece {

    private Piece subject;
    private Piece unpromotable;
    private Piece promotable;

    private Board board;
    private Field source, target, targetPromoted, targetBad;

    @Before
    public void setUp() {
        board = new Board();
        source = board.getField(2, 0);
        target = board.getField(3, 0);
        targetBad = board.getField(1, 0);
        targetPromoted = board.getField(3, 1);

        subject = source.getPiece();
        promotable = new Rook(1);
        unpromotable = new Gold(1);
    }

    @Test
    public void test_Initial_State() {
        assertEquals(1, subject.getOwner());
        assertFalse(subject.isPromoted());
        assertEquals(PieceType.FUHYO, subject.getType());
        assertEquals("P", subject.getSymbol());
    }

    @Test
    public void test_Promotion_Can() {
        promotable.promote();
        assertTrue(promotable.isPromoted());
    }

    @Test
    public void test_Promotion_Cant() {
        unpromotable.promote();
        assertFalse(unpromotable.isPromoted());
    }

    @Test
    public void test_Demotion() {
        promotable.promote();
        promotable.demote();
        assertFalse(promotable.isPromoted());
    }

    @Test
    public void test_Piece_Owner() {
        assertEquals(1, subject.getOwner());
    }

    @Test
    public void test_Change_Owner() {
        subject.setOwner(2);
        assertEquals(2, subject.getOwner());
    }

    @Test
    public void test_Can_Move_True() {
        assertTrue(subject.canMove(source, target, board));
    }

    @Test
    public void test_Can_Move_Promoted() {
        subject.promote();
        assertTrue(subject.canMove(source, targetPromoted, board));
    }

    @Test
    public void test_Can_Move_False() {
        assertFalse(subject.canMove(source, targetBad, board));
    }
}
