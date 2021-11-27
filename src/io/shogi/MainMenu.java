package io.shogi;

import io.shogi.core.Board;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class MainMenu extends JFrame {

    private JPanel panel;
    private JLabel titleLabel;
    private JButton playBtn, continueBtn, exitBtn;

    public static void main(String[] args) {
        MainMenu mainMenu = new MainMenu();
    }

    public MainMenu() {
        initialize();
        initComponents();
        addListeners();
        this.setVisible(true);
    }

    // Checks if there's an unfinished game saved
    private boolean checkForSavedGame() {
        File gameSave = new File("game.save");
        return gameSave.exists();
    }

    // Initializes the JFrame and JPanel
    private void initialize() {
        this.setTitle("将棋");
        this.setLayout(new BorderLayout());
        this.setSize(200, 200);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        ImageIcon imgIcon = new ImageIcon("res/icon.png");
        this.setIconImage(imgIcon.getImage());

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setSize(200, 200);

        this.add(panel);
    }

    // Creates then adds components to the JPanel
    private void initComponents() {
        titleLabel = new JLabel("Shogi");
        playBtn = new JButton("Play");
        continueBtn = new JButton("Continue");
        exitBtn = new JButton("Exit");

        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        playBtn.setAlignmentX(CENTER_ALIGNMENT);
        continueBtn.setAlignmentX(CENTER_ALIGNMENT);
        exitBtn.setAlignmentX(CENTER_ALIGNMENT);

        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));

        continueBtn.setEnabled(checkForSavedGame());

        panel.add(Box.createVerticalGlue());
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(playBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(continueBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(exitBtn);
        panel.add(Box.createVerticalGlue());
    }

    // Adds listeners to components
    private void addListeners() {
        playBtn.addActionListener((e) -> {
            Shogi shogi = new Shogi();
            this.dispose();
        });
        continueBtn.addActionListener((e) -> {
            Shogi shogi = loadSave();
            this.dispose();
        });
        exitBtn.addActionListener((e) -> System.exit(0));
    }

    // Load saved game from file
    private Shogi loadSave() {
        try {
            FileInputStream fis = new FileInputStream("game.save");
            ObjectInputStream in = new ObjectInputStream(fis);
            Board savedBoard = (Board) in.readObject();
            int savedTurn = (int) in.readObject();
            in.close();
            fis.close();
            return new Shogi(savedBoard, savedTurn);
        } catch (IOException | ClassNotFoundException e) {
            int result = JOptionPane.showConfirmDialog(null, "Failed to load saved game.\nStart new game?", "Fatal error :)", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
            switch (result) {
                case JOptionPane.YES_OPTION -> {
                    Shogi shogi = new Shogi();
                }
                case JOptionPane.NO_OPTION -> {
                    System.exit(0);
                }
            }
        }
        return null;
    }
}
