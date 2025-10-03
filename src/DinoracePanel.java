import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

class DinoracePanel extends JPanel {
    private final JComboBox<String> betBox;
    private final JLabel resultLabel;
    private final JLabel creditLabel;
    private final int FINISHLINE = 800;
    private final int[] dinoX;
    private final Timer raceTimer;
    private final Random rand = new Random();
    private final JTextField betField;
    private int bet;
    //Deklaration der Variablen für Dino Rennen

    public DinoracePanel() {

        setLayout(null);
        setLayout(new BorderLayout());
        //Layout setzen, erst null dann BorderLayout (BorderLayout aktiv)
        JPanel bottomPanel = new JPanel(new GridLayout(1,2,8,0));
        JPanel left = util.createPanel();
        JPanel right = util.createPanel();
        //Panels für Steuerung

        creditLabel = new JLabel("Credits: " + CasinoApp.credits);
        creditLabel.setBounds(20, 60, 500, 25);
        creditLabel.setFont(new Font("Arial", Font.BOLD, 15));
        left.add(creditLabel);
        //Anzeige für Credits

        resultLabel = new JLabel("Place your bet!");
        resultLabel.setBounds(20, 60, 500, 25);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 15));
        resultLabel.setForeground(Color.BLACK);
        left.add(resultLabel);
        //Anzeige für Ergebnis

        betField = new JTextField("50");
        betField.setColumns(3);
        betField.setBounds(140, 20, 60, 25);
        betField.setFont(new Font("Arial", Font.BOLD, 15));
        left.add(betField);
        //Textfeld für Einsatz

        String[] dinos = {"T-Rex", "Triceratops", "Parasaur"};
        betBox = new JComboBox<>(dinos);
        betBox.setBounds(20, 20, 100, 25);
        betBox.setFont(new Font("Arial", Font.BOLD, 15));
        right.add(betBox);
        //Dropdown zur Dino-Auswahl

        JButton startRaceButton = new JButton("Start Race");
        startRaceButton.setBounds(220, 20, 120, 25);
        right.add(startRaceButton);
        //Button für Start

        JButton close = new JButton("Quit");
        right.add(close);
        //Button zum Beenden

        bottomPanel.add(left);
        bottomPanel.add(right);
        add(bottomPanel, BorderLayout.SOUTH);
        //Panels im Grid anordnen und unten ins Panel einfügen

        dinoX = new int[dinos.length];
        raceTimer = new Timer(100, _ -> moveDinos());
        //Timer initialisieren, bewegt Dinos alle 100ms

        startRaceButton.addActionListener(_ -> startRace());
        //Startet das Rennen

        close.addActionListener(_ -> System.exit(0));
        //Beendet das Programm
    }

    public void updateCreditLabel(){
        util.updateCredits(0, creditLabel);
    }

    private void startRace() {
        raceStatus = true;
        try {
            bet = Integer.parseInt(betField.getText());
        } catch (NumberFormatException e) {
            resultLabel.setText("Invalid bet!");
            return;        }
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
            if (dinoX[i] >= FINISHLINE) {
                raceTimer.stop();
                String winner = switch (i) {
                    case 0 -> {
                        resultLabel.setText("T-Rex wins! You bet on: " + betBox.getSelectedItem());
                        yield "T-Rex";
                    }
                    case 1 -> {
                        resultLabel.setText("Triceratops wins! You bet on: " + betBox.getSelectedItem());
                        yield "Triceratops";
                    }
                    case 2 -> {
                        resultLabel.setText("Parasaur wins! You bet on: " + betBox.getSelectedItem());
                        yield "Parasaur";
                    }
                    default -> " ";
                };
                if (Objects.equals(betBox.getSelectedItem(), winner)) {
                    util.updateCredits(bet, creditLabel);
                } else {
                    util.updateCredits(-bet,creditLabel);
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
            g.fillRect(FINISHLINE + 95, 0, 20, getHeight());
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