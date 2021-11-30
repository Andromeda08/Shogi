package io.test;

import io.shogi.core.*;
import io.shogi.core.PieceType;

import io.shogi.pieces.Knight;
import io.shogi.pieces.Lance;
import io.shogi.pieces.Pawn;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestBoard {

    private Board board;

    @Before()
    public void setUp() {
        board = new Board();
    }

    @Test()
    public void test_Move_Piece_Normal() {
        Field from = board.getField(2, 0);
        Field to = board.getField(3, 0);

        assertTrue(board.movePiece(from, to, 1));
        assertFalse(board.movePiece(from, to, 2));
    }

    @Test
    public void test_Move_Into_Promotion() {
        Field from = board.getField(5, 0);
        from.setPiece(board.getField(2, 0).getPiece());
        Field to = board.getField(6, 0);
        to.setPiece(null);
        board.movePiece(from, to, 1);
        assertTrue(to.getPiece().isPromoted());
    }

    @Test
    public void test_Move_Captures() {
        Piece expected = null;
        Piece captured = null;
        if (board.getField(2, 0).getPiece() != null) {
            expected = board.getField(2, 0).getPiece();
            captured = board.getField(6, 0).getPiece();
        }
        assertNotNull(expected);
        assertNotNull(captured);

        board.getField(5, 0).setPiece(expected);

        board.movePiece(board.getField(5, 0), board.getField(6, 0), 1);
        assertEquals(expected, board.getField(6, 0).getPiece());
    }

    @Test
    public void test_Move_Piece_Drop_Legal() {
        Piece expected = null;
        Field from = new Field(99, 99);

        if (board.getField(6, 0).getPiece() != null) {
            expected = board.getField(6, 0).getPiece();
        }

        assertNotNull(expected);
        from.setPiece(expected);
        board.getField(6, 0).setPiece(null);
        board.getField(2, 0).setPiece(null);

        board.movePiece(from, board.getField(2, 0), 1);

        assertEquals(expected, board.getField(2, 0).getPiece());
    }

    @Test
    public void test_Bad_Row_Drops() {
        Field dropper = new Field(99, 99);
        Piece pawn = new Pawn(1);
        Piece lance = new Lance(2);
        Piece knight1 = new Knight(1);
        Piece knight2 = new Knight(2);
        board.getField(0, 0).setPiece(null);
        board.getField(8, 0).setPiece(null);

        dropper.setPiece(pawn);
        assertFalse(board.movePiece(dropper, board.getField(8, 0), 1));
        dropper.setPiece(lance);
        assertFalse(board.movePiece(dropper, board.getField(0, 0), 2));
        dropper.setPiece(knight1);
        assertFalse(board.movePiece(dropper, board.getField(7, 0), 1));
        dropper.setPiece(knight2);
        assertFalse(board.movePiece(dropper, board.getField(1, 0), 2));
    }

    @Test
    public void test_Move_Piece_Drop_Pawn() {
        Piece expected = new Pawn(1);
        Field from = new Field(99, 99);
        from.setPiece(expected);

        board.movePiece(from, board.getField(3, 0), 1);

        assertNull(board.getField(3, 0).getPiece());
    }

    @Test
    public void test_Move_Piece_Drop_Check() {
        Piece expected = new Pawn(1);
        Field from = new Field(99, 99);
        from.setPiece(expected);

        board.getField(2, 4).setPiece(null);
        board.movePiece(from, board.getField(7, 4), 1);

        assertNull(board.getField(7, 4).getPiece());
    }

    @Test
    public void test_Move_Piece_Drop_Occupied() {
        Piece drop = new Pawn(1);
        Field from = new Field(99, 99);
        from.setPiece(drop);

        Piece expected = null;
        if (board.getField(2, 0).getPiece() != null)
            expected = board.getField(2, 0).getPiece();

        assertNotNull(expected);
        board.movePiece(from, board.getField(2, 0), 1);

        assertEquals(expected, board.getField(2, 0).getPiece());
    }

    @Test
    public void test_Check_Force_Move() {
        board.getField(2, 4).setPiece(null);
        board.getField(6, 4).setPiece(null);
        board.movePiece(board.getField(7, 7), board.getField(7, 4), 2);

        assertFalse(board.movePiece(board.getField(0, 4), board.getField(1, 4), 1));
        assertNull(board.getField(1, 4).getPiece());
        assertFalse(board.movePiece(board.getField(2, 0), board.getField(3, 0), 1));
        assertNull(board.getField(3, 0).getPiece());
        assertTrue(board.movePiece(board.getField(0, 4), board.getField(1, 3), 1));
        assertNotNull(board.getField(1, 3).getPiece());
    }

    /*@Test
    public void test_Mate_Detection() {
        board.getField(0, 3).setPiece(new Pawn(1));
        board.getField(0, 5).setPiece(new Pawn(1));
        board.getField(6, 4).setPiece(null);
        board.movePiece(board.getField(7, 7), board.getField(7, 4), 2);
        board.movePiece(board.getField(7, 4), board.getField(2, 4), 2);
    }*/

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
    public void test_Get_Field() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                assertNotNull(board.getField(i, j));
            }
        }
    }

    @Test
    public void test_Field_Out_Of_Bounds_Null() {
        assertNull(board.getField(1000, 1000));
    }

    @Test
    public void test_Hand_Out_Of_Bounds_Null() {
        assertNull(board.getHand(1000));
    }

    @Test()
    public void test_Correct_Initial_Setup() {
        for (int i = 0; i < 9; i++) {
            switch (i) {
                case 0:
                case 8:
                    assertEquals(PieceType.KYOUSHA, board.getField(i, 0).getPiece().getType());
                    assertEquals(PieceType.KEIMA, board.getField(i, 1).getPiece().getType());
                    assertEquals(PieceType.GINSHO, board.getField(i, 2).getPiece().getType());
                    assertEquals(PieceType.KINSHO, board.getField(i, 3).getPiece().getType());
                    assertEquals(PieceType.OSHO, board.getField(i, 4).getPiece().getType());
                    assertEquals(PieceType.KINSHO, board.getField(i, 5).getPiece().getType());
                    assertEquals(PieceType.GINSHO, board.getField(i, 6).getPiece().getType());
                    assertEquals(PieceType.KEIMA, board.getField(i, 7).getPiece().getType());
                    assertEquals(PieceType.KYOUSHA, board.getField(i, 8).getPiece().getType());
                    break;
                case 1:
                    assertEquals(PieceType.KAKUGYO, board.getField(i, 7).getPiece().getType());
                    assertEquals(PieceType.HISHA, board.getField(i, 1).getPiece().getType());
                    for (int j = 0; j < 9; j++)
                        if (j != 1 && j != 7)
                            assertNull(board.getField(i, j).getPiece());
                    break;
                case 7:
                    assertEquals(PieceType.KAKUGYO, board.getField(i, 1).getPiece().getType());
                    assertEquals(PieceType.HISHA, board.getField(i, 7).getPiece().getType());
                    for (int j = 0; j < 9; j++)
                        if (j != 1 && j != 7)
                            assertNull(board.getField(i, j).getPiece());
                    break;
                case 2:
                case 6:
                    for (int j = 0; j < 9; j++)
                        assertEquals(PieceType.FUHYO, board.getField(i, j).getPiece().getType());
                    break;
                default:
                    for (int j = 0; j < 9; j++)
                        assertNull(board.getField(i, j).getPiece());
                    break;
            }
        }
    }
}
