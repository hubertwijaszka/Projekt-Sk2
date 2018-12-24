import GUI.Okno;

import javax.swing.*;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class Main {

    public static void main(String[] args) {
        Okno window = new Okno();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setVisible(true);

    }
}
