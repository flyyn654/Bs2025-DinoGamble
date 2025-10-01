package main.java;
// Paketdeklaration

import org.w3c.dom.css.RGBColor;

import javax.swing.*;
//importierung des Gui package
import javax.swing.Timer;
//import Timer für zeitgesteuerte Aktionen
import java.awt.*;
//import von Grafik und Layout package
import java.util.*;
//import vom package von Zufallszahlen etc.
import java.util.List;
//import vom package von Listend



public class CasinoApp extends JFrame {
    //Klassendeklaration
    CardLayout cardLayout;
    //Layout welches Panels austauschen kann
    JPanel mainPanel;
    //Behälter für alle Panels
    static int credits = 500;
    //Guthaben als Statische Variabel
    static JLabel creditLabel;
    //Statisches Label
    public CasinoApp() {
        setTitle("Casino App - Dino Casino");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        //Erstellt ein Fenster mit Titel, Grösse und Verhalten beim Schliessen

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        //Ermöglicht wechseln zwischen Spielen


        BlackjackPanel blackjackPanel = new BlackjackPanel(this);
        DinoRacePanel dinoRacePanel = new DinoRacePanel(this);
        //Zwei Spielpanels

        mainPanel.add(blackjackPanel, "Blackjack");
        mainPanel.add(dinoRacePanel, "DinoRaces");
        //Beide Panels werden benennt und können somit im Menü aufgerufen werden

        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Games");
        //Menüleiste mit Namen Games

        JMenuItem blackjackItem = new JMenuItem("Blackjack");
        blackjackItem.addActionListener(e -> cardLayout.show(mainPanel, "Blackjack"));
        //Wechselt beim Klick auf das Blackjackpanel

        JMenuItem dinoRaceItem = new JMenuItem("Dino Races");
        dinoRaceItem.addActionListener(e -> cardLayout.show(mainPanel, "DinoRaces"));
        //Wechselt beim Klick auf das Rennpanel

        gameMenu.add(blackjackItem);
        gameMenu.add(dinoRaceItem);
        menuBar.add(gameMenu);
        menuBar.add(Box.createHorizontalStrut(20));
        setJMenuBar(menuBar);
        //Menüleiste Inhalt hinzufügen

        add(mainPanel);
        //Panel wird ins Fenster eingebaut
    }

    public void updateCredits(int change) {
        credits += change;
        creditLabel.setText("Credits: " + credits);
        //Funktion welche nach jedem Spiel das Guthaben anpasst
    }

    // ================= BLACKJACK =================
    static class BlackjackPanel extends JPanel {
        //Panel für Blackjack
        private final JButton hitButton;
        //Ein Knopf mit Namen hitButton
        private final JButton standButton;
        //Ein Knopf mit Namen standButton
        private List<Card> playerCards;
        //Liste mit Karten des Spielers
        private List<Card> dealerCards;
        //Liste mit Karten des Dealers
        private final Random rand = new Random();
        //Zufallszahl fürs Kartenziehen
        private int bet;
        //Zahl wieviel gesetzt wird
        private final JTextField betField;
        //Textfeld für die Eingabe der Menge des Einsatzes
        private boolean gameOver;
        //Variable welhce den Spielstatus zeigt

        private final String[] cardNames = {
                "2", "3", "4", "5", "6", "7", "8", "9", "10",
                "Junge", "Königin", "König", "Ass"
                //String Array welche die Namen aller Karten aufzeigt
        };

        public BlackjackPanel(CasinoApp app) {
            playerCards = new ArrayList<>();
            //Kreiert eine Liste mit den Spielerkarten
            dealerCards = new ArrayList<>();
            //Kreiert eine Liste mid dne Dealerkarten
            setLayout(new BorderLayout());
            //Setzt das Layout des Panels auf Borderlayout, was das Panel in 5 Teile einteilt


            JPanel bottomPanel = new JPanel(new GridLayout(1,3,8,0));
            //Kreiert ein Panel mit dem Namen bottomPanel mit einer Reihe und 3 Spalten
            JPanel left = util.createPanel();
            //Ruft Methode createPanel auf, welche ein Panel mit namen left kreiert
            JPanel middle = util.createPanel();
            //Ruft Methode createPanel auf, welche ein Panel mit namen middle kreiert
            JPanel right = util.createPanel();
            //Ruft Methode createPanel auf, welche ein Panel mit namen right kreiert

            creditLabel = new JLabel("Credits: " + credits);
            //Kreiert ein neues Label mit Namen creditLabel um die Anzahl Credits anzuzeigen
            creditLabel.setBounds(20, 60, 500, 25);
            //Legt den Ort fest
            creditLabel.setFont(new Font("Arial", Font.BOLD, 20));
            //Legt den Font fest
            creditLabel.setForeground(Color.GREEN);
            //Setzt die Farbe auf Grün
            left.add(creditLabel);
            //Fügt das Label dem Panel mit Namen left hinzu

            betField = new JTextField("50", 5);
            //Kreiert ein neues Textfeld mit dem Standard text fest und mit 5 Zeilen
            middle.add(new JLabel("Bet:"));
            //Fügt dem Panel middle ein Label mit Text Bet: hinzu
            middle.add(betField);
            //Fügt dem Panel middle das Textfeld hinzu

            JButton close = new JButton("Quit");
            //Kreiert ein Knopf mit Text Quit und dme Namen close
            hitButton = new JButton("Hit");
            //instanziert ein Knopf mit Text Hit und dem Namen hitbutton
            standButton = new JButton("Stand");
            //instanziert ein Knopf mit Text Stand und dem Namen standbutton
            JButton newGameButton = new JButton("New Game");
            //Kreiert ein Knopf mit Text New Game und dem Namen newGameButton

            middle.add(hitButton);
            middle.add(standButton);
            right.add(newGameButton);
            right.add(close);
            //Fügt alle Knöpfe hinzu

            bottomPanel.add(left);
            bottomPanel.add(middle);
            bottomPanel.add(right);
            //Fügt alle Panels ins Grid ein, das Gridpanel wird immer von links aufgefüllt

            add(bottomPanel, BorderLayout.SOUTH);
            //Fügt das bottomPanel unten hinzu

            disableButtons();
            //Ruft methode disableButtons auf

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
            //Wenn der Knopf geklickt wurde wird der Liste der Spielerkarten eine Karte hinzugefügt
            //Das Frame neugezeichnet
            //Wenn die Spielerkarten einen Wert von über 21 hat wird die Nachricht angezeigt Bustec! Dealer wins.
            //Die Credits werden angepasst und gameOver wird auf true gesetzt
            //Die Knöpfe werden ausgeschaltet

            close.addActionListener(e -> {
                System.exit(0);
            });
            //Wenn der Knopf close gedrückt wird wird das Frame geschlossen

            standButton.addActionListener(e -> {
                while (getTotal(dealerCards) < 17) {
                    dealerCards.add(drawCard());
                }
                //Wenn Knopf stand geklickt wird zieht der dealer Karten solange sein Wert kleiner als 17 ist
                repaint();
                //Das Frame wird neu gezeichnet
                int playerTotal = getTotal(playerCards);
                //Variable playerTotal wird auf den Wet der Karten in der Liste gesetzt
                int dealerTotal = getTotal(dealerCards);
                //Variable dealerTotal wird auf den Wet der Karten in der Liste gesetzt
                String message;
                //Variable message wird deklariert
                if (dealerTotal > 21 || playerTotal > dealerTotal) {
                    message = "You win!";
                    app.updateCredits(bet);
                    //Wenn das Dealertotal grösser als 21 ist oder das Spielertotal grösser als das dealertotal ist
                    //wird die Nachricht auf You win gesetzt und die Credits aktualisiert
                } else if (playerTotal == dealerTotal) {
                    message = "Push (Draw).";
                    //Wenn die Totalen gleich gross sind wird die Nachricht auf Push (Draw) gesetzt
                } else {
                    message = "Dealer wins.";
                    app.updateCredits(-bet);
                    //Wenn der Dealer gewinnt wird die Nachricht auf Dealer wins gesetzt und die Credits aktualisiert
                }
                JOptionPane.showMessageDialog(this, message);
                //Die Nachricht wird angezeigt
                gameOver = true;
                //gameOver wird auf true gesetzt
                disableButtons();
                //Die Knöpfe werden ausgeschaltet
                repaint();
                //Das Frame wird neugezeichnet
            });

            newGameButton.addActionListener(e -> startGame());
            //wenn der newGameButton geklickt wird wird die Methode startGame aufgerufen
        }

        private void startGame() {
            try {
                bet = Integer.parseInt(betField.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid bet!");
                return;
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
            //Versucht die Variable bet auf den Wert des Textfeldes zu setzen,
            //falls das nicht geht weil im Textfeld Buchstaben sind dann,
            //wird die Nachricht Invalid bet! angezeigt
            //Falls der Wert des Textfeldes unter Null ist oder grösser als der Wert des Guthabens ist wird
            //wieder der gleiche Text angezeigt wie vorher
            //die Liste der Spielerkarten wird instanziert sowie die Dealerkarten
            //den beiden Listen werden je 2 Karten hinzugefügt
            //gameOver wird auf false gesetzt, die Knöpfe werden eingeschaltet und das Frame wird neugezeichnet
        }

        private Card drawCard() {
            String name = cardNames[rand.nextInt(cardNames.length)];
            //Zufällige Karte aus Array ziehen
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
            //Setzt den Wert der Karte
            return new Card(name, value);
            //Gibt neue Karte zurück
        }

        private int getTotal(List<Card> cards) {
            int total = cards.stream().mapToInt(c -> c.value).sum();
            //Addiert die Werte aller Karten
            long aceCount = cards.stream().filter(c -> c.name.equals("Ass")).count();
            //Zählt die Anzahl Ass-Karten
            while (total > 21 && aceCount > 0) {
                total -= 10;
                aceCount--;
            }
            //Wenn Gesamtwert > 21 und As vorhanden, wird As als 1 gezählt
            return total;
        }

        private void disableButtons() {
            hitButton.setEnabled(false);
            standButton.setEnabled(false);
            //Schaltet Buttons Hit und Stand aus
        }

        private void enableButtons() {
            hitButton.setEnabled(true);
            standButton.setEnabled(true);
            //Schaltet Buttons Hit und Stand ein
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            //zeichnet Hintergrund und Karten

            java.net.URL url = getClass().getResource("/background.png");
            if (url != null) {
                Image bg = new ImageIcon(url).getImage();
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            } else {
                g.setColor(new Color(240, 250, 240));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
            //Hintergrundbild laden, falls nicht vorhanden hellgrüner Hintergrund

            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.setColor(Color.WHITE);

            g.drawString("Dealer Hand (" + getTotal(dealerCards) + ")", 50, 40);
            drawCards(g, dealerCards, 50, 60);
            //Zeigt Karten und Gesamtwert des Dealers an

            g.drawString("Your Hand (" + getTotal(playerCards) + ")", 50, 350);
            drawCards(g, playerCards, 50, 370);
            //Zeigt Karten und Gesamtwert des Spielers an
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
                //Zeichnet die Karten grafisch, lädt Bild oder zeichnet roten Platzhalter
            }
        }
    }

    static class Card {
        String name;
        int value;
        //Speichert Name und Wert einer Karte

        Card(String name, int value) {
            this.name = name;
            this.value = value;
            //Konstruktor, initialisiert Name und Wert
        }
    }

    // ================= DINO RACES =================
    static class DinoRacePanel extends JPanel {
        private JButton startRaceButton;
        private JButton close;
        private JComboBox<String> betBox;
        private JLabel resultLabel;
        private int finishLine = 800;
        private int[] dinoX;
        private Timer raceTimer;
        private Random rand = new Random();
        private CasinoApp app;
        private JTextField betField;
        private int bet;
        //Deklaration der Variablen für Dino Rennen

        public DinoRacePanel(CasinoApp app) {
            this.app = app;
            setLayout(null);
            setLayout(new BorderLayout());
            //Layout setzen, erst null dann BorderLayout (BorderLayout aktiv)
            JPanel bottomPanel = new JPanel(new GridLayout(1,3,8,0));
            JPanel left = util.createPanel();
            JPanel middle = util.createPanel();
            JPanel right = util.createPanel();
            //Panels für Steuerung

            resultLabel = new JLabel("Place your bet!");
            resultLabel.setBounds(20, 60, 500, 25);
            resultLabel.setFont(new Font("Arial", Font.BOLD, 20));
            resultLabel.setForeground(Color.BLACK);
            middle.add(resultLabel);
            //Anzeige für Ergebnis

            creditLabel = new JLabel("Credits: " + credits);
            creditLabel.setBounds(20, 60, 500, 25);
            creditLabel.setFont(new Font("Arial", Font.BOLD, 20));
            creditLabel.setForeground(Color.GREEN);
            left.add(creditLabel);
            //Anzeige für Credits

            betField = new JTextField("50");
            betField.setColumns(3);
            betField.setBounds(140, 20, 60, 25);
            middle.add(betField);
            //Textfeld für Einsatz

            String[] dinos = {"T-Rex", "Triceratops", "Parasaur"};
            betBox = new JComboBox<>(dinos);
            betBox.setBounds(20, 20, 100, 25);
            middle.add(betBox);
            //Dropdown zur Dino-Auswahl

            startRaceButton = new JButton("Start Race");
            startRaceButton.setBounds(220, 20, 120, 25);
            right.add(startRaceButton);
            //Button für Start

            close = new JButton("Quit");
            right.add(close);
            //Button zum Beenden

            bottomPanel.add(left);
            bottomPanel.add(middle);
            bottomPanel.add(right);
            add(bottomPanel, BorderLayout.SOUTH);
            //Panels im Grid anordnen und unten ins Panel einfügen

            dinoX = new int[dinos.length];
            raceTimer = new Timer(100, e -> moveDinos());
            //Timer initialisieren, bewegt Dinos alle 100ms

            startRaceButton.addActionListener(e -> startRace());
            //Startet das Rennen

            close.addActionListener(e -> System.exit(0));
            //Beendet das Programm
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
            //Setzt Positionen der Dinos, startet Timer und zeigt Nachricht an
        }

        private void moveDinos() {
            for (int i = 0; i < dinoX.length; i++) {
                dinoX[i] += rand.nextInt(20);
                if (dinoX[i] >= finishLine) {
                    raceTimer.stop();
                    String winner;
                    switch (i) {
                        case 0:
                            resultLabel.setText("T-Rex wins! You bet on: " + betBox.getSelectedItem());
                            winner = "T-Rex";
                            break;
                        case 1:
                            resultLabel.setText("Triceratops wins! You bet on: " + betBox.getSelectedItem());
                            winner = "Triceratops";
                            break;
                        case 2:
                            resultLabel.setText("Parasaur wins! You bet on: " + betBox.getSelectedItem());
                            winner = "Parasaur";
                            break;
                        default:
                            winner = " ";
                    }
                    if (betBox.getSelectedItem().equals(winner)) {
                        app.updateCredits(bet);
                    } else {
                        app.updateCredits(-bet);
                    }
                    repaint();
                    return;
                    //Überprüft ob ein Dino die Ziellinie erreicht hat
                    //Bestimmt Gewinner und passt Credits an
                }
            }
            repaint();
            //bewegt Dinos bei jedem Timer-Tick
        }

        static boolean raceStatus = false;
        //Status ob Rennen läuft

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
            //Hintergrund zeichnen

            if (raceStatus) {
                g.setColor(Color.WHITE);
                g.fillRect(finishLine + 95, 0, 20, getHeight());
            }
            //Ziellinie darstellen

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
                //Zeichnet Dinos oder Platzhalter
            }
        }
    }

}
