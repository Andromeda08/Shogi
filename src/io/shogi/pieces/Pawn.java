package io.shogi.pieces;

import io.shogi.core.Board;
import io.shogi.core.Field;
import io.shogi.core.Piece;
import io.shogi.core.PieceType;

/**
 * Gyalog
 */
public class Pawn extends Piece {
    /**
     * Gyalog constructor
     * @param owner Tulajdonos
     */
    public Pawn(int owner) {
        super(owner);
        setSymbol("P");
        setType(PieceType.FUHYO);
    }

    /**
     * Pawn movement code
     * @param current A mező, ahol az egység tartózkodik.
     * @param target A mező, ahova szeretnénk lépni.
     * @param board A játéktábla.
     * @return Tud-e lépni az egység.
     */
    public boolean canMove(Field current, Field target, Board board) {
        // Tokin movement
        if (promoted) {
            if (Math.abs(current.row() - target.row()) <= 1 &&
                    Math.abs(current.col() - target.col()) <= 1) {
                // Don't allow backwards diagonal movement
                if (owner == 1) {
                    if (current.row() - target.row() == 1 &&
                            current.col() != target.col()) {
                        return false;
                    }
                }
                if (owner == 2) {
                    if (current.row() - target.row() == -1 &&
                            current.col() != target.col()) {
                        return false;
                    }
                }
                if (target.getPiece() != null && current.getPiece().getOwner() == target.getPiece().getOwner()) {
                    return false;
                }
                return true;
            }
            return false;
        }

        // Fuhyou movement
        if (owner == 1 && current.row() - target.row() == -1 ||
                owner == 2 && current.row() - target.row() == 1) {
            if (current.col() == target.col()) {
                if (target.getPiece() != null)
                    if (current.getPiece().getOwner() == target.getPiece().getOwner())
                        return false;
                return true;
            }
        }

        return false;
    }

    @Override
    public void promote() {
        setType(PieceType.TOKIN);
        setSymbol("P!");
        promoted = true;
    }

    @Override
    public void demote() {
        setType(PieceType.FUHYO);
        setSymbol("P");
        promoted = false;
    }
}
