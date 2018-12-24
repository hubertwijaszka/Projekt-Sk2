package GUI;

import javax.swing.*;
import java.awt.*;


public class Okno extends JFrame {
    private int wysokosc=600;
    private int szerokosc=500;
    private Plansza plansza;
public Okno(){
    super("Warcaby");
    super.setSize(szerokosc, wysokosc);
    super.setLocationByPlatform(true);
    plansza = new Plansza(this);
    this.add(plansza);





}
}
