import javax.swing.*;
import java.awt.*;
import java.awt.event.HierarchyEvent;

public class CasinoApp extends JFrame {
    //Klassendeklaration
    CardLayout cardLayout;
    //Layout welches Panels austauschen kann
    JPanel mainPanel;
    //Behälter für alle Panels

    static int credits = 500;

    //Guthaben als Statische Variabel
    public static final int STARTINGVALUE = 500;
    public CasinoApp() {
        setTitle("Casino App - Dino Casino");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        //Erstellt ein Fenster mit Titel, Grösse und Verhalten beim Schliessen

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        //Ermöglicht wechseln zwischen Spielen


        BlackjackPanel blackjackPanel = new BlackjackPanel();
        DinoracePanel dinoracePanel = new DinoracePanel();
        //Zwei Spielpanels

        dinoracePanel.addHierarchyListener(e -> {
            if((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 && dinoracePanel.isShowing()){
                dinoracePanel.updateCreditLabel();
            }
        });

        blackjackPanel.addHierarchyListener(e -> {
            if((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 && blackjackPanel.isShowing()){
                blackjackPanel.updateCreditLabel();
            }
        });

        mainPanel.add(blackjackPanel, "Blackjack");
        mainPanel.add(dinoracePanel, "DinoRaces");
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


}
