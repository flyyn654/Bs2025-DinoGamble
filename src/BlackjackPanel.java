import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class BlackjackPanel extends JPanel {
    //Panel für Blackjack
    private final JButton hitButton;
    //Ein Knopf mit Namen hitButton
    private final JButton standButton;
    //Ein Knopf mit Namen standButton
    private final JButton newGameButton;
    //Ein Knopf mit Namen newGameButton
    private final JLabel creditLabel;

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

    private final String[] cardNames = {
            "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "Junge", "Königin", "König", "Ass"
            //String Array welche die Namen aller Karten aufzeigt
    };

    public void updateCreditLabel(){
        util.updateCredits(0, creditLabel);
    }

    public BlackjackPanel() {
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

        creditLabel = new JLabel();
        creditLabel.setText("Credits: " + CasinoApp.credits);
        //Kreiert ein neues Label mit Namen creditLabel um die Anzahl Credits anzuzeigen
        creditLabel.setBounds(20, 800, 500, 25);
        //Legt den Ort fest
        creditLabel.setFont(new Font("Arial", Font.BOLD, 15));
        //Legt den Font fest
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
        newGameButton = new JButton("New Game");
        //Instanziert ein Knopf mit Text New Game und dem Namen newGameButton

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

        changeButtons(false);
        //Ruft methode disableButtons auf

        hitButton.addActionListener(e -> {
            playerCards.add(drawCard());
            repaint();
            if (getTotal(playerCards) > 21) {
                JOptionPane.showMessageDialog(this, "Busted! Dealer wins.");
                util.updateCredits(-bet, creditLabel);
                changeButtons(false);
            }
        });
        //Wenn der Knopf geklickt wurde wird der Liste der Spielerkarten eine Karte hinzugefügt
        //Das Frame neugezeichnet
        //Wenn die Spielerkarten einen Wert von über 21 hat wird die Nachricht angezeigt Bustec! Dealer wins.
        //Die Credits werden angepasst
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
                util.updateCredits(bet, creditLabel);
                //Wenn das Dealertotal grösser als 21 ist oder das Spielertotal grösser als das dealertotal ist
                //wird die Nachricht auf You win gesetzt und die Credits aktualisiert
            } else if (playerTotal == dealerTotal) {
                message = "Push (Draw).";
                //Wenn die Totalen gleich gross sind wird die Nachricht auf Push (Draw) gesetzt
            } else {
                message = "Dealer wins.";
                util.updateCredits(-bet, creditLabel);
                //Wenn der Dealer gewinnt wird die Nachricht auf Dealer wins gesetzt und die Credits aktualisiert
            }
            JOptionPane.showMessageDialog(this, message);
            //Die Nachricht wird angezeigt
            changeButtons(false);
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
        changeButtons(true);
        repaint();
        //Versucht die Variable bet auf den Wert des Textfeldes zu setzen,
        //falls das nicht geht weil im Textfeld Buchstaben sind dann,
        //wird die Nachricht Invalid bet! angezeigt
        //Falls der Wert des Textfeldes unter Null ist oder grösser als der Wert des Guthabens ist wird
        //wieder der gleiche Text angezeigt wie vorher
        //die Liste der Spielerkarten wird instanziert sowie die Dealerkarten
        //den beiden Listen werden je 2 Karten hinzugefügt
        //Die Knöpfe werden eingeschaltet und das Frame wird neugezeichnet
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

    private void changeButtons(boolean status){
        hitButton.setEnabled(status);
        standButton.setEnabled(status);
        newGameButton.setEnabled(!status);
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