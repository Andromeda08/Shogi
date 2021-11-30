package io.test;

import io.shogi.core.*;
import io.shogi.pieces.Bishop;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestField {

    private Field field;
    private Piece piece;
    int row = 3;
    int col = 9;

    @Before
    public void setUp() {
        field = new Field(row, col);
        piece = new Bishop(1);
    }

    @Test
    public void test_Set_Piece() {
        field.setPiece(piece);
        assertEquals(field.getPiece(), piece);
    }

    @Test
    public void test_Get_Piece() {
        assertNull(field.getPiece());
    }

    @Test
    public void test_Row() {
        assertEquals(row, field.row());
    }

    @Test
    public void test_Column() {
        assertEquals(col, field.col());
    }
}
