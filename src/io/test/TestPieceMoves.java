package io.test;

import io.shogi.core.Board;
import io.shogi.core.Field;
import io.shogi.core.Piece;
import io.shogi.pieces.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestPieceMoves {

    private Board board;

    /* Create board before tests */

    @Before
    public void setUp() {
        board = new Board();
    }

    /* General Move Tests */

    @Test
    public void test_Move_To_Free_Field() {
        Field source = board.getField(6, 0);
        Field target = board.getField(5, 0);
        Piece expected = source.getPiece();

        board.movePiece(source, target, 2);

        assertEquals(expected, target.getPiece());
    }

    @Test
    public void test_Move_To_Enemy_Occupied_Field() {
        board.getField(6, 0).setPiece(null);
        board.getField(4, 0).setPiece(new Pawn(2));

        Field source = board.getField(4, 0);
        Field target = board.getField(3, 0);
        Piece expected = source.getPiece();

        board.movePiece(source, target, 2);

        assertEquals(expected, target.getPiece());
    }

    @Test
    public void test_Move_To_Ally_Occupied_Field() {
        Field source = board.getField(7, 1);
        Field target = board.getField(6, 1);
        Piece expected = target.getPiece();

        board.movePiece(source, target, 2);

        assertEquals(expected, target.getPiece());
    }

    /* Test canMove */

    @Test
    public void test_Legal_Move() {
        Field source = board.getField(2, 0);
        Field target = board.getField(3, 0);
        Piece piece = source.getPiece();
        assertTrue(piece.canMove(source, target, board));
    }

    @Test
    public void test_Illegal_Move() {
        Field source = board.getField(2, 0);
        Field target = board.getField(4, 0);
        Piece piece = source.getPiece();
        assertFalse(piece.canMove(source, target, board));
    }

    /* Test Legal Piece Moves */

    @Test
    public void test_Lance_Legal_Move() {
        Field source = board.getField(0, 0);
        Field target = board.getField(1, 0);
        Piece expected = source.getPiece();

        board.movePiece(source, target, 1);

        assertEquals(expected, target.getPiece());
    }

    @Test
    public void test_Knight_Legal_Move() {
        Field source = board.getField(0, 1);
        Field target = board.getField(2, 0);
        Piece expected = source.getPiece();
        target.setPiece(null);

        board.movePiece(source, target, 1);

        assertEquals(expected, target.getPiece());
    }

    @Test
    public void test_Silver_Legal_Move() {
        Field source = board.getField(0, 2);
        Field target = board.getField(1, 3);
        Piece expected = source.getPiece();

        board.movePiece(source, target, 1);

        assertEquals(expected, target.getPiece());
    }

    @Test
    public void test_Gold_Legal_Move() {
        Field source = board.getField(0, 3);
        Field target = board.getField(1, 4);
        Piece expected = source.getPiece();

        board.movePiece(source, target, 1);

        assertEquals(expected, target.getPiece());
    }

    @Test
    public void test_King_Legal_Move() {
        Field source = board.getField(0, 4);
        Field target = board.getField(1, 4);
        Piece expected = source.getPiece();

        board.movePiece(source, target, 1);

        assertEquals(expected, target.getPiece());
    }

    @Test
    public void test_Rook_Legal_Move() {
        Field source = board.getField(1, 1);
        Field target = board.getField(1, 4);
        Piece expected = source.getPiece();

        board.movePiece(source, target, 1);

        assertEquals(expected, target.getPiece());
    }

    @Test
    public void test_Bishop_Legal_Move() {
        Field source = board.getField(1, 7);
        Field target = board.getField(4, 4);
        Piece expected = source.getPiece();
        board.getField(2, 6).setPiece(null);

        board.movePiece(source, target, 1);

        assertEquals(expected, target.getPiece());
    }

    /* Test Illegal Piece Moves */

    @Test
    public void test_Lance_Illegal_Move() {
        Field base = board.getField(0, 0);
        Field source = board.getField(1, 0);
        Field target = board.getField(0, 0);
        Piece piece = base.getPiece();

        base.setPiece(null);
        source.setPiece(piece);

        assertFalse(piece.canMove(source, target, board));
    }

    @Test
    public void test_Knight_Illegal_Move() {
        Field base = board.getField(0, 1);
        Field source = board.getField(2, 0);
        Piece piece = base.getPiece();

        base.setPiece(null);
        source.setPiece(piece);

        assertFalse(piece.canMove(source, base, board));
    }

    @Test
    public void test_Silver_Illegal_Move() {
        Field base = board.getField(0, 2);
        Field source = board.getField(1, 2);
        Piece piece = base.getPiece();

        base.setPiece(null);
        source.setPiece(piece);

        assertFalse(piece.canMove(source, base, board));
    }

    @Test
    public void test_Gold_Illegal_Move() {
        Field base = board.getField(0, 3);
        Field source = board.getField(1, 4);
        Piece piece = base.getPiece();

        base.setPiece(null);
        source.setPiece(piece);

        assertFalse(piece.canMove(source, base, board));
    }

    @Test
    public void test_King_Illegal_Move() {
        Field source = board.getField(0, 4);
        Field target = board.getField(2, 4);
        Piece piece = source.getPiece();

        assertFalse(piece.canMove(source, target, board));
    }

    @Test
    public void test_Rook_Illegal_Move() {
        Field source = board.getField(1, 1);
        Field target = board.getField(2, 2);
        Piece piece = source.getPiece();

        target.setPiece(null);

        assertFalse(piece.canMove(source, target, board));
    }

    @Test
    public void test_Bishop_Illegal_Move() {
        Field source = board.getField(1, 7);
        Field target = board.getField(1, 5);
        Piece piece = source.getPiece();

        assertFalse(piece.canMove(source, target, board));
    }

    // TODO: Promoted moves

    /* Test Promotion */

    @Test
    public void test_Promote_Piece() {
        Field source = board.getField(5, 0);
        Field target = board.getField(6, 0);
        Piece piece = board.getField(2, 0).getPiece();

        board.getField(0, 2).setPiece(null);
        source.setPiece(piece);

        board.movePiece(source, target, 1);

        assertTrue(piece.isPromoted());
    }
}
