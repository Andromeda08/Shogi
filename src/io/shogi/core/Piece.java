package io.shogi.core;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * Shogi egység absztrakt osztály.
 */
public abstract class Piece implements Serializable {
    protected int owner;
    protected boolean promoted = false;
    private PieceType type;
    private String symbol;
    /**
     * Létrehoz egy egységet
     * @param owner : Az egység tulajdonosa.
     */
    public Piece(int owner) {
        this.owner = owner;
    }

    /**
     * Ellenőrzi, hogy az egység tud-e egyik mezőről a másikra lépni.
     * @param current A mező, ahol az egység tartózkodik.
     * @param target A mező, ahova szeretnénk lépni.
     * @param board A játéktábla.
     * @return Tud-e lépni az egység.
     */
    public abstract boolean canMove(Field current, Field target, Board board);

    /**
     * Előlépteti az egységet.
     */
    public void promote() {
        if (!promoted) {
            promoted = true;
            setSymbol(symbol + "_P");
        }
    }

    /**
     * Lefokoz egy egységet
     */
    public void demote() {
        promoted = false;
        setSymbol(symbol.split("_")[0]);
    }

    /**
     * Visszaadja, hogy előreléptetett-e az egység.
     * @return Előreléptetett-e?
     */
    public boolean isPromoted() {
        return promoted;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    public void setType(PieceType type) {
        this.type = type;
    }

    public int getOwner() {
        return owner;
    }
    public String getSymbol() {
        return symbol;
    }
    public PieceType getType() {
        return type;
    }
}
