package io.test;

import io.shogi.core.Board;

import io.shogi.core.Piece;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestBoardInitials {

    private Board board;

    @Before
    public void setUp() {
        board = new Board();
    }

    @Test
    public void test_Board_Pieces_Setup() {
        for (int i = 0; i < 9; i++) {
            switch (i) {
                case 0:
                case 8:
                    assertEquals("Lance", board.getField(i, 0).getPiece().getType());
                    assertEquals("Knight", board.getField(i, 1).getPiece().getType());
                    assertEquals("Silver", board.getField(i, 2).getPiece().getType());
                    assertEquals("Gold", board.getField(i, 3).getPiece().getType());
                    assertEquals("King", board.getField(i, 4).getPiece().getType());
                    assertEquals("Gold", board.getField(i, 5).getPiece().getType());
                    assertEquals("Silver", board.getField(i, 6).getPiece().getType());
                    assertEquals("Knight", board.getField(i, 7).getPiece().getType());
                    assertEquals("Lance", board.getField(i, 8).getPiece().getType());
                    break;
                case 1:
                    assertEquals("Bishop", board.getField(i, 7).getPiece().getType());
                    assertEquals("Rook", board.getField(i, 1).getPiece().getType());
                    for (int j = 0; j < 9; j++)
                        if (j != 1 && j != 7)
                            assertNull(board.getField(i, j).getPiece());
                    break;
                case 7:
                    assertEquals("Bishop", board.getField(i, 1).getPiece().getType());
                    assertEquals("Rook", board.getField(i, 7).getPiece().getType());
                    for (int j = 0; j < 9; j++)
                        if (j != 1 && j != 7)
                            assertNull(board.getField(i, j).getPiece());
                    break;
                case 2:
                case 6:
                    for (int j = 0; j < 9; j++)
                        assertEquals("Pawn", board.getField(i, j).getPiece().getType());
                    break;
                default:
                    for (int j = 0; j < 9; j++)
                        assertNull(board.getField(i, j).getPiece());
                    break;
            }
        }
    }

    @Test
    public void test_Default_Hands() {
        for (int i = 0; i < 2; i ++) {
            ArrayList<Piece> list = board.getHand(i).getList();
            for (Piece p : list) {
                assertNull(p);
            }
        }
    }

    @Test
    public void test_Print() {
        board.print();
    }
}
