package io.shogi.pieces;

import io.shogi.core.Board;
import io.shogi.core.Field;
import io.shogi.core.Piece;
import io.shogi.core.PieceType;

/**
 * Ezüsttábornok
 */
public class Silver extends Piece {
    /**
     * Ezüst contstructor
     * @param owner Tulajdonos
     */
    public Silver(int owner) {
        super(owner);
        setSymbol("S");
        setType(PieceType.GINSHO);
    }

    /**
     * Silver movement code
     * @param current A mező, ahol az egység tartózkodik.
     * @param target A mező, ahova szeretnénk lépni.
     * @param board A játéktábla.
     * @return Tud-e lépni az egység.
     */
    @Override
    public boolean canMove(Field current, Field target, Board board) {
        if (promoted) {
            if (Math.abs(current.row() - target.row()) <= 1 && Math.abs(current.col() - target.col()) <= 1) {

                if (owner == 1) {
                    if (current.row() - target.row() == 1 && current.col() != target.col())
                        return false;
                }

                if (owner == 2) {
                    if (current.row() - target.row() == -1 && current.col() != target.col())
                        return false;
                }

                if (target.getPiece() != null && current.getPiece().getOwner() == target.getPiece().getOwner()) {
                    return false;
                }

                return true;
            }

            return false;
        }

        if ((Math.abs(current.row() - target.row())) <= 1 && (Math.abs(current.col() - target.col())) <= 1) {

            if (owner == 1) {
                if (current.row() - target.row() == 1)
                    if (current.col() == target.col())
                        return false;
            }

            if (owner == 2) {
                if (current.row() - target.row() == -1)
                    if (current.col() == target.col())
                        return false;
            }

            if (current.row() == target.row()) {
                return false;
            }

            if (target.getPiece() != null && current.getPiece().getOwner() == target.getPiece().getOwner()) {
                return false;
            }

            return true;
        }
        return false;
    }
}
