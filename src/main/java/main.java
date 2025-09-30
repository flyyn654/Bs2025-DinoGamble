import main.java.CasinoApp;

import javax.swing.*;

public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        CasinoApp app = new CasinoApp();
        app.setVisible(true);
    });
}
