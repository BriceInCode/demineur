package org.example;


import org.example.models.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MinesweeperGUI extends JFrame {
    private Board board;
    private JPanel mineFieldPanel;
    private JButton[][] buttons;
    private boolean debugMode = false;
    private Timer timer;
    private int timeElapsed;
    private JLabel timeLabel;
    private JLabel scoreLabel;
    private JButton startButton;
    private JButton pauseButton;
    private JButton resetButton;
    private boolean gamePaused;
    private int score;
    private boolean gameOver = false; // Ajouté pour savoir si la partie est terminée

    /**
     * Constructeur de la classe MinesweeperGUI. Initialise la fenêtre du jeu, le panneau de jeu,
     * les boutons et le timer.
     *
     * @param level Le niveau du jeu, qui détermine les dimensions du champ de mines et le nombre de mines.
     */
    public MinesweeperGUI(Level level) {
        board = new Board(level.getWidth(), level.getHeight(), generateMines(level.getMineCount(), level.getWidth(), level.getHeight()));
        setTitle("Démineur - " + level.getLabel());
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mineFieldPanel = new JPanel(new GridLayout(level.getHeight(), level.getWidth()));
        buttons = new JButton[level.getWidth()][level.getHeight()];

        // Création des boutons pour chaque cellule du champ de mines
        for (int x = 0; x < level.getWidth(); x++) {
            for (int y = 0; y < level.getHeight(); y++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(30, 30));
                button.addMouseListener(new FieldMouseListener(x, y));
                buttons[x][y] = button;
                mineFieldPanel.add(button);
            }
        }

        add(mineFieldPanel, BorderLayout.CENTER);

        // Ajout du minuteur et des boutons de contrôle
        timeLabel = new JLabel("Temps écoulé: 0 s");
        scoreLabel = new JLabel("Score: 0");
        startButton = new JButton("Démarrer");
        pauseButton = new JButton("Pause");
        resetButton = new JButton("Réinitialiser");

        JPanel controlPanel = new JPanel();
        controlPanel.add(timeLabel);
        controlPanel.add(scoreLabel);
        controlPanel.add(startButton);
        controlPanel.add(pauseButton);
        controlPanel.add(resetButton);

        add(controlPanel, BorderLayout.SOUTH);

        // Initialisation du minuteur
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeElapsed++;
                timeLabel.setText("Temps écoulé: " + timeElapsed + " s");
            }
        });

        // Ajout des écouteurs pour les boutons de contrôle
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!timer.isRunning()) {
                    timer.start();
                    gamePaused = false;
                    enableFieldButtons(true);
                    gameOver = false; // Réinitialiser l'état du jeu
                }
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timer.isRunning()) {
                    timer.stop();
                    gamePaused = true;
                    enableFieldButtons(false);
                }
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                timeElapsed = 0;
                timeLabel.setText("Temps écoulé: 0 s");
                score = 0;
                scoreLabel.setText("Score: 0");
                resetGame();
                enableFieldButtons(true);
                gameOver = false; // Réinitialiser l'état du jeu
            }
        });
    }

    /**
     * Génère un ensemble de mines à des positions aléatoires sur le champ de mines.
     *
     * @param mineCount Le nombre de mines à générer.
     * @param width     La largeur du champ de mines.
     * @param height    La hauteur du champ de mines.
     * @return Un ensemble de coordonnées représentant les positions des mines.
     */
    private Collection<Coordinate> generateMines(int mineCount, int width, int height) {
        Set<Coordinate> mines = new HashSet<>();
        while (mines.size() < mineCount) {
            int x = (int) (Math.random() * width);
            int y = (int) (Math.random() * height);
            mines.add(new Coordinate(x, y));
        }
        return mines;
    }

    /**
     * Met à jour l'affichage d'un bouton en fonction de l'état du champ de mines associé.
     * Si le champ a été ouvert, affiche le nombre de mines voisines ou une mine si présente.
     * Si le champ est marqué, affiche un drapeau.
     *
     * @param button Le bouton à mettre à jour.
     * @param field Le champ correspondant à ce bouton.
     */
    private void updateButton(JButton button, Field field) {
        if (gameOver) {
            return; // Empêche l'incrémentation du score et toute action supplémentaire après la fin de la partie
        }

        if (field.isOpened()) {
            if (field.hasMine()) {
                button.setText("💣");
            } else {
                int neighbourMines = field.getNeighbourMineCount();
                button.setText(neighbourMines > 0 ? String.valueOf(neighbourMines) : "");
                score++; // Incrémenter le score seulement si la partie n'est pas terminée
                scoreLabel.setText("Score: " + score);
            }
            button.setEnabled(false);
        } else if (field.hasFlag()) {
            button.setText("🏴");
        } else {
            button.setText("");
        }

        if (debugMode && field.hasMine()) {
            button.setText("💣");
        }
    }

    /**
     * Classe interne qui gère les événements de souris sur un champ de mines.
     * Permet de gérer l'action de clic gauche pour ouvrir un champ, et de clic droit pour marquer un champ.
     */
    private class FieldMouseListener extends MouseAdapter {
        private int x;
        private int y;

        /**
         * Constructeur de FieldMouseListener.
         *
         * @param x La position horizontale du champ.
         * @param y La position verticale du champ.
         */
        public FieldMouseListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Cette méthode est appelée lors du clic sur un bouton.
         * Si le jeu n'est pas en pause, elle révèle le champ ou marque le champ avec un drapeau.
         * Si le joueur touche une mine, la partie se termine.
         *
         * @param e L'événement de la souris.
         */
        @Override
        public void mousePressed(MouseEvent e) {
            if (gamePaused || gameOver) return; // Si la partie est en pause ou terminée, ne rien faire

            if (e.getButton() == MouseEvent.BUTTON1) {
                RevealFieldsResult result = board.revealFields(new Coordinate(x, y));
                for (Field field : result.getRevealedFields()) {
                    updateButton(buttons[field.getCoordinate().getX()][field.getCoordinate().getY()], field);
                }

                if (result.getState() == RevealFieldsResult.RevealFieldState.FOUND_MINE) {
                    // Partie terminée, on affiche un message et on dévoile toutes les mines
                    JOptionPane.showMessageDialog(MinesweeperGUI.this, "Partie terminée ! Vous avez touché une mine.\nDurée: " + timeElapsed + " s\nScore: " + score);
                    revealAllMines();
                    gameOver = true; // Marque la fin du jeu
                    timer.stop();
                } else if (board.hasWon()) {
                    // Partie gagnée, on affiche un message et on dévoile toutes les cases
                    JOptionPane.showMessageDialog(MinesweeperGUI.this, "Félicitations ! Vous avez gagné !\nDurée: " + timeElapsed + " s\nScore: " + score);
                    revealAllFields(); // Cette méthode dévoile toutes les cases
                    gameOver = true; // Marque la fin du jeu
                    timer.stop();
                }
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                // Gestion du clic droit pour poser un drapeau
                board.flagField(new Coordinate(x, y));
                updateButton(buttons[x][y], board.getFields().stream()
                        .filter(f -> f.getCoordinate().equals(new Coordinate(x, y)))
                        .findFirst()
                        .orElse(null));
            }
        }
    }

    /**
     * Révèle toutes les mines sur le champ de jeu, utilisé lorsqu'une mine est touchée.
     */
    private void revealAllMines() {
        for (Field field : board.getMines()) {
            JButton button = buttons[field.getCoordinate().getX()][field.getCoordinate().getY()];
            button.setText("💣");
            button.setEnabled(false);
        }
    }

    /**
     * Révèle toutes les cases à la fin du jeu (gagné ou perdu).
     */
    private void revealAllFields() {
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                int finalX = x;
                int finalY = y;
                Field field = board.getFields().stream()
                        .filter(f -> f.getCoordinate().getX() == finalX && f.getCoordinate().getY() == finalY)
                        .findFirst().orElse(null);
                updateButton(buttons[x][y], field); // Révèle toutes les cellules
            }
        }
    }

    /**
     * Réinitialise le jeu, recréant un nouveau tableau de mines et réinitialisant les boutons.
     */
    private void resetGame() {
        board = new Board(board.getWidth(), board.getHeight(), generateMines(board.getMineCount(), board.getWidth(), board.getHeight()));
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                buttons[x][y].setText("");
                buttons[x][y].setEnabled(true);
            }
        }
    }


    private void enableFieldButtons(boolean enable) {
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                buttons[x][y].setEnabled(enable);
            }
        }
    }

    /**
     * Méthode principale pour lancer le jeu avec un niveau par défaut (débutant).
     *
     * @param args Les arguments de ligne de commande (non utilisés ici).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Level level = Level.getBeginner();
            MinesweeperGUI gui = new MinesweeperGUI(level);
            gui.setVisible(true);
        });
    }
}
