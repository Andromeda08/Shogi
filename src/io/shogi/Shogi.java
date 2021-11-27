package io.shogi;

import io.shogi.core.Board;
import io.shogi.core.Field;
import io.shogi.core.Piece;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

public class Shogi implements Serializable {
    // Dimension Constants
    private final Dimension DIM_FRAME = new Dimension(630, 730);
    private final Dimension DIM_BOARD = new Dimension(630, 630);
    private final Dimension DIM_HAND  = new Dimension(630, 50);

    // Color Constants
    private final Color COLOR_BOARD  = Color.decode("#F8C68B");
    private final Color COLOR_HLIGHT = Color.decode("#DEAE78");

    public JFrame frame;

    public JPanel boardPanel = new JPanel(new GridLayout(9, 9));
    public JButton[][] fields = new JButton[9][9];

    public JPanel[] handPanels = { new JPanel(), new JPanel() };
    public JButton[][] handBtns = { new JButton[40], new JButton[40] };

    public Field selected = null;

    // Game state
    public Board board = new Board();
    public int turn = 2;

    // Quick launch for debug
    public static void main(String[] args) {
        Shogi shogi = new Shogi();
    }

    public Shogi() {
        initializeFrame();
        initializeGame();
        updateBoard();
        frame.setVisible(true);
    }

    // Used when loading a saved game
    public Shogi(Board b, int t) {
        initializeFrame();
        initializeGame();
        updateBoard();
        frame.setVisible(true);

        this.board = b;
        this.turn = t;
        updateBoard();
    }

    private void updateBoard() {
        if (turn == 1) frame.setTitle("将棋 - Top Turn");
        else frame.setTitle("将棋 - Bottom Turn");

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board.getField(i, j).getPiece() != null) {
                    fields[i][j].setText(board.getField(i, j).getPiece().getSymbol());
                    if (board.getField(i, j).getPiece().getOwner() == 1)
                        fields[i][j].setForeground(Color.RED);
                    else
                        fields[i][j].setForeground(Color.BLUE);
                }
                else {
                    fields[i][j].setText("");
                    fields[i][j].setForeground(Color.BLACK);
                    fields[i][j].setBackground(COLOR_BOARD);
                }
            }
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 40; j++) {
                if (board.getHand(i).getPiece(j) != null) {
                    board.getHand(i).getPiece(j).demote();
                    handBtns[i][j].setText(board.getHand(i).getPiece(j).getSymbol());
                    handBtns[i][j].setVisible(true);
                }
                else {
                    handBtns[i][j].setVisible(false);
                }
            }
        }

        frame.revalidate();
        frame.repaint();
    }

    private void passTurn() {
        if (turn == 1) {
            frame.setTitle("Shogi - Bottom Turn");
            turn = 2;
        }
        else {
            frame.setTitle("Shogi - Top Turn");
            turn = 1;
        }
    }

    private void initializeFrame() {
        frame = new JFrame("将棋 - Bottom Turn");
        frame.setLayout(new BorderLayout());
        frame.setSize(DIM_FRAME);

        boardPanel.setSize(DIM_BOARD);

        for (int i = 0; i < 2; i++) {
            handPanels[i].setSize(DIM_HAND);
            handPanels[i].setPreferredSize(DIM_HAND);
            handPanels[i].setLayout(new FlowLayout());
        }

        frame.add(boardPanel, BorderLayout.CENTER);
        frame.add(handPanels[0], BorderLayout.PAGE_START);
        frame.add(handPanels[1], BorderLayout.PAGE_END);

        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(frame, "Do you want to save the game?", "Save?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    saveGame();
                }
                System.exit(0);
            }
        });
    }

    private void initializeGame() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 40; j++) {
                final int o = i; // Hand Owner
                JButton b = new JButton("");
                b.setOpaque(true);
                b.setSize(50, 50);
                b.setBorder(new LineBorder(Color.BLACK));
                b.setBackground(Color.WHITE);
                b.setVisible(false);

                b.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        for (int k = 0; k < 40; k++) {
                            if (e.getSource() == handBtns[o][k]) {
                                if (board.getHand(o).getPiece(k).getOwner() != turn) {
                                    Field tempField = new Field(99, 99);
                                    Piece dropPiece = board.getHand(o).getPiece(k);
                                    dropPiece.setOwner(o + 1);
                                    tempField.setPiece(dropPiece);

                                    selected = tempField;
                                    System.out.println(selected.col() + "," + selected.row());

                                    handBtns[o][k].setText("");
                                    handBtns[o][k].setVisible(false);
                                    board.getHand(o).setPiece(k, null);
                                }
                            }
                        }
                    }
                });

                handBtns[i][j] = b;
                handPanels[i].add(handBtns[i][j]);
            }
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JButton b = new JButton();
                b.setOpaque(true);
                b.setSize(70, 70);;
                b.setBorder(new LineBorder(Color.BLACK));
                b.setBackground(COLOR_BOARD);

                b.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // TODO: Allow the deselection of pieces, this fixes related bg color issue.
                        for (int i = 0; i < 9; i++) {
                            for (int j = 0; j < 9; j++) {
                                if (e.getSource() == fields[i][j]) {
                                    int owner = 0;
                                    if (board.getField(i, j).getPiece() != null) {
                                        owner = board.getField(i, j).getPiece().getOwner();
                                    }
                                    if (selected == null && owner == turn) {
                                        if(board.getField(i, j).getPiece() != null) {
                                            selected = board.getField(i, j);
                                            fields[i][j].setBackground(COLOR_HLIGHT);
                                            for (int r = 0; r < 9; r++) {
                                                for (int c = 0; c < 9; c++) {
                                                    if (board.getField(i, j).getPiece().canMove(board.getField(i, j), board.getField(r, c), board)) {
                                                        fields[r][c].setText(fields[r][c].getText() + ".");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    else {
                                        if (selected != null) {
                                            board.movePiece(selected, board.getField(i, j), turn);
                                            passTurn();
                                        }
                                        selected = null;
                                        updateBoard();
                                    }
                                }
                            }
                        }
                    }
                });

                fields[i][j] = b;
                boardPanel.add(fields[i][j]);
            }
        }
    }

    private void saveGame() {
        try {
            FileOutputStream fos = new FileOutputStream("game.save");
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(board);
            out.writeObject(turn);
            out.close();
            fos.close();
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "An error occurred while trying to save the game.\nExiting without saving on OK.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

}
