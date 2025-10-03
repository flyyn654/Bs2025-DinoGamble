import javax.swing.*;
import java.awt.*;

public class util {

    public static JPanel createPanel(){
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(200,40));
        return p;
    }

    public static void updateCredits(int change, JLabel creditLabel) {
        CasinoApp.credits += change;
        creditLabel.setText("Credits: " + CasinoApp.credits);
        if (CasinoApp.credits + change < CasinoApp.STARTINGVALUE) {
            creditLabel.setForeground(Color.RED);
        } else {
            creditLabel.setForeground(Color.decode("#2a6e20"));
        }
    }

}
