package io.test;

import io.shogi.pieces.*;
import io.shogi.core.Piece;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestPieces {
    private Piece piece;

    @Before
    public void setUp() {
        piece = new Pawn(1);
    }

    @Test
    public void test_Create_Piece() {
        assertEquals("Pawn", piece.getType());
    }

    @Test
    public void test_Promote() {
        piece.promote();
        assertTrue(piece.isPromoted());
    }

    @Test
    public void test_Demote() {
        piece.promote();
        piece.demote();
        assertFalse(piece.isPromoted());
    }

    @Test
    public void test_Set_Owner() {
        piece.setOwner(2);
        assertEquals(2, piece.getOwner());
    }

    @Test
    public void test_Set_Type() {
        piece.setType("Tokin");
        assertEquals("Tokin", piece.getType());
    }

    @Test
    public void test_Get_Symbol() {
        assertEquals("P", piece.getSymbol());
    }
}
