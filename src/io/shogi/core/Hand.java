package io.shogi.core;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Egy játékos keze
 */
public class Hand implements Serializable {
    private ArrayList<Piece> pieces = new ArrayList<Piece>();
    private final int owner;

    /**
     * Létrehozza egy játékos "kezét".
     * @param owner Kéz tulajdonosa
     */
    public Hand(int owner) {
        this.owner = owner;
        for (int i = 0; i < 40; i++) {
            pieces.add(null);
        }
    }

    /**
     * Hozzáad egy egységet a játékos kezéhez.
     * @param piece A hozzáadandó egység.
     */
    public void addPiece(Piece piece) {
        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i) == null) {
                pieces.set(i, piece);
                return;
            }
        }
    }

    /**
     * Eltávolít egy egységet a játékos kezéből.
     * @param piece Az eltávolítandó egység.
     */
    public void removePiece(Piece piece) {
        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i) == piece) {
                pieces.set(i, null);
                return;
            }
        }
    }

    /**
     * Valamilyen indexben talált egységet visszaadja.
     * @param idx Index
     * @return Indexbeli egység.
     */
    public Piece getPiece(int idx) {
        return pieces.get(idx);
    }

    /**
     * Visszaadja a kéz tulajdonosát.
     * @return Tulajdonos
     */
    public int getOwner() {
        return owner;
    }

    /**
     * Teszteléshez használt függvény
     * @return A kézben lévő egységek listája
     */
    public ArrayList<Piece> getList() {
        return pieces;
    }
}
