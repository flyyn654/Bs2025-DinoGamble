package main.java;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.util.*;
import java.util.List;

public class CasinoApp extends JFrame {
    CardLayout cardLayout;
    JPanel mainPanel;
    static int credits = 500;
    JLabel creditLabel;

    public CasinoApp() {
        setTitle("Casino App - Dino Casino");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        creditLabel = new JLabel("Credits: " + credits);
        creditLabel.setFont(new Font("Arial", Font.BOLD, 16));

        BlackjackPanel blackjackPanel = new BlackjackPanel(this);
        DinoRacePanel dinoRacePanel = new DinoRacePanel(this);

        mainPanel.add(blackjackPanel, "Blackjack");
        mainPanel.add(dinoRacePanel, "DinoRaces");

        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Games");

        JMenuItem blackjackItem = new JMenuItem("Blackjack");
        blackjackItem.addActionListener(e -> cardLayout.show(mainPanel, "Blackjack"));

        JMenuItem dinoRaceItem = new JMenuItem("Dino Races");
        dinoRaceItem.addActionListener(e -> cardLayout.show(mainPanel, "DinoRaces"));

        gameMenu.add(blackjackItem);
        gameMenu.add(dinoRaceItem);
        menuBar.add(gameMenu);
        menuBar.add(Box.createHorizontalStrut(20));
        menuBar.add(creditLabel);
        setJMenuBar(menuBar);

        add(mainPanel);
    }

    public void updateCredits(int change) {
        credits += change;
        creditLabel.setText("Credits: " + credits);
    }

    // ================= BLACKJACK =================
    static class BlackjackPanel extends JPanel {
        private final JButton hitButton;
        private final JButton standButton;
        private List<Card> playerCards;
        private List<Card> dealerCards;
        private final Random rand = new Random();
        private int bet;
        private final JTextField betField;
        private boolean gameOver;

        private final String[] cardNames = {
                "2", "3", "4", "5", "6", "7", "8", "9", "10",
                "Junge", "Königin", "König", "Ass"
        };

        public BlackjackPanel(CasinoApp app) {
            playerCards = new ArrayList<>();
            dealerCards = new ArrayList<>();
            setLayout(new BorderLayout());

            JPanel bottomPanel = new JPanel();
            betField = new JTextField("50", 5);
            bottomPanel.add(new JLabel("Bet:"));
            bottomPanel.add(betField);

            hitButton = new JButton("Hit");
            standButton = new JButton("Stand");
            JButton newGameButton = new JButton("New Game");

            bottomPanel.add(hitButton);
            bottomPanel.add(standButton);
            bottomPanel.add(newGameButton);
            add(bottomPanel, BorderLayout.SOUTH);

            disableButtons();

            hitButton.addActionListener(e -> {
                playerCards.add(drawCard());
                repaint();
                if (getTotal(playerCards) > 21) {
                    JOptionPane.showMessageDialog(this, "Busted! Dealer wins.");
                    app.updateCredits(-bet);
                    gameOver = true;
                    disableButtons();
                }
            });

            standButton.addActionListener(e -> {
                while (getTotal(dealerCards) < 17) {
                    dealerCards.add(drawCard());
                }
                repaint();
                int playerTotal = getTotal(playerCards);
                int dealerTotal = getTotal(dealerCards);
                String message;
                if (dealerTotal > 21 || playerTotal > dealerTotal) {
                    message = "You win!";
                    app.updateCredits(bet);
                } else if (playerTotal == dealerTotal) {
                    message = "Push (Draw).";
                } else {
                    message = "Dealer wins.";
                    app.updateCredits(-bet);
                }
                JOptionPane.showMessageDialog(this, message);
                gameOver = true;
                disableButtons();
                repaint();
            });

            newGameButton.addActionListener(e -> startGame());
        }

        private void startGame() {
            try {
                bet = Integer.parseInt(betField.getText());
            } catch (NumberFormatException e) {
                bet = 50;
            }
            if (bet <= 0 || bet > CasinoApp.credits) {
                JOptionPane.showMessageDialog(this, "Invalid bet!");
                return;
            }
            playerCards = new ArrayList<>();
            dealerCards = new ArrayList<>();
            playerCards.add(drawCard());
            playerCards.add(drawCard());
            dealerCards.add(drawCard());
            dealerCards.add(drawCard());
            gameOver = false;
            enableButtons();
            repaint();
        }

        private Card drawCard() {
            String name = cardNames[rand.nextInt(cardNames.length)];
            int value;
            switch (name) {
                case "Junge":
                case "Königin":
                case "König":
                    value = 10;
                    break;
                case "Ass":
                    value = 11;
                    break;
                default:
                    value = Integer.parseInt(name);
            }
            return new Card(name, value);
        }

        private int getTotal(List<Card> cards) {
            int total = cards.stream().mapToInt(c -> c.value).sum();
            long aceCount = cards.stream().filter(c -> c.name.equals("Ass")).count();
            while (total > 21 && aceCount > 0) {
                total -= 10;
                aceCount--;
            }
            return total;
        }

        private void disableButtons() {
            hitButton.setEnabled(false);
            standButton.setEnabled(false);
        }

        private void enableButtons() {
            hitButton.setEnabled(true);
            standButton.setEnabled(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Hintergrund laden
            java.net.URL url = getClass().getResource("/background.png");
            if (url != null) {
                Image bg = new ImageIcon(url).getImage();
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            } else {
                g.setColor(new Color(240, 250, 240));
                g.fillRect(0, 0, getWidth(), getHeight());
            }

            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.setColor(Color.WHITE);

            g.drawString("Dealer Hand (" + getTotal(dealerCards) + ")", 50, 40);
            drawCards(g, dealerCards, 50, 60);

            g.drawString("Your Hand (" + getTotal(playerCards) + ")", 50, 350);
            drawCards(g, playerCards, 50, 370);
        }

        private void drawCards(Graphics g, List<Card> cards, int x, int y) {
            if (cards == null) return;
            for (int i = 0; i < cards.size(); i++) {
                Card c = cards.get(i);
                int cardX = x + i * 110;

                java.net.URL url = getClass().getResource("/cards/" + c.name + ".png");
                if (url != null) {
                    Image img = new ImageIcon(url).getImage();
                    g.drawImage(img, cardX, y, 100, 140, this);
                } else {
                    System.out.println("Bild für Karte '" + c.name + "' nicht gefunden!");
                    g.setColor(Color.RED);
                    g.fillRect(cardX, y, 100, 140);
                    g.setColor(Color.BLACK);
                    g.drawString(c.name, cardX + 10, y + 70);
                }
            }
        }
    }

    static class Card {
        String name;
        int value;

        Card(String name, int value) {
            this.name = name;
            this.value = value;
        }
    }

    // ================= DINO RACES =================
    static class DinoRacePanel extends JPanel {
        private JButton startRaceButton;
        private JComboBox<String> betBox;
        private JLabel resultLabel;
        private int finishLine = 800;
        private int[] dinoX;
        private Timer raceTimer;
        private Random rand = new Random();
        private CasinoApp app;
        private JTextField betField;
        private int bet;

        public DinoRacePanel(CasinoApp app) {
            this.app = app;
            setLayout(null);

            String[] dinos = {"Dino 1", "Dino 2", "Dino 3"};
            betBox = new JComboBox<>(dinos);
            betBox.setBounds(20, 20, 100, 30);
            add(betBox);

            betField = new JTextField("50");
            betField.setBounds(140, 20, 60, 30);
            add(betField);

            startRaceButton = new JButton("Start Race");
            startRaceButton.setBounds(220, 20, 120, 30);
            add(startRaceButton);

            resultLabel = new JLabel("Place your bet!");
            resultLabel.setBounds(20, 60, 500, 30);
            resultLabel.setFont(new Font("Arial", Font.BOLD, 16));
            resultLabel.setForeground(Color.WHITE);

            add(resultLabel);

            dinoX = new int[dinos.length];
            raceTimer = new Timer(100, e -> moveDinos());

            startRaceButton.addActionListener(e -> startRace());
        }

        private void startRace() {
            raceStatus = true;
            try {
                bet = Integer.parseInt(betField.getText());
            } catch (NumberFormatException e) {
                bet = 50;
            }
            if (bet <= 0 || bet > CasinoApp.credits) {
                resultLabel.setText("Invalid bet!");
                return;
            }

            Arrays.fill(dinoX,50);
            resultLabel.setText("Race started! You bet on: " + betBox.getSelectedItem());
            raceTimer.start();
            repaint();
        }

        private void moveDinos() {
            for (int i = 0; i < dinoX.length; i++) {
                dinoX[i] += rand.nextInt(20);
                if (dinoX[i] >= finishLine) {
                    raceTimer.stop();
                    String winner = "Dino " + (i + 1);
                    resultLabel.setText(winner + " wins! You bet on: " + betBox.getSelectedItem());
                    if (betBox.getSelectedItem().equals(winner)) {
                        app.updateCredits(bet);
                    } else {
                        app.updateCredits(-bet);
                    }
                    repaint();
                    return;
                }
            }
            repaint();
        }
        static boolean raceStatus = false;
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            java.net.URL url = getClass().getResource("/background.png");
            if (url != null) {
                Image bg = new ImageIcon(url).getImage();
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            } else {
                g.setColor(new Color(240, 250, 240));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
            if (raceStatus) {
                g.setColor(Color.WHITE);
                g.fillRect(finishLine + 95, 0, 20, getHeight());
            }


            for (int i = 0; i < dinoX.length; i++) {
                java.net.URL dinoUrl = getClass().getResource("/racedesigns/dino" + (i + 1) + ".png");
                if (dinoUrl != null) {
                    Image dinoImg = new ImageIcon(dinoUrl).getImage();
                    g.drawImage(dinoImg, dinoX[i], 150 + i * 120, 100, 60, this);
                } else {
                    g.setColor(Color.BLUE);
                    g.fillRect(dinoX[i], 150 + i * 120, 100, 60);
                    g.setColor(Color.WHITE);
                    g.drawString("Dino " + (i + 1), dinoX[i] + 10, 150 + i * 120 + 30);
                }
            }


        }
    }

}
