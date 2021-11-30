package io.shogi.core;

import java.io.Serializable;

/**
 * Shogi Tábla Mező
 */
public class Field implements Serializable {
    private final int row, col;
    private Piece piece = null;

    /**
     * Field class constructor.
     * Létrehoz egy táblamezőt.
     * @param r Sor
     * @param c Oszlop
     */
    public Field(int r, int c) {
        this.row = r;
        this.col = c;
    }

    /**
     * A mezőn tartózkodó egységet adja vissza.
     * @return A mezőn tartózkodó egység.
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Átállítja a mezőn tartózkodó egységet.
     * @param piece A mezőre helyezendő egység.
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    /**
     * @return A mező sora.
     */
    public int row() {
        return row;
    }

    /**
     * @return A mező oszlopa.
     */
    public int col() {
        return col;
    }
}
