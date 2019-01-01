package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;


public class Okno extends JFrame {
    private KonfSieci konfSieci = new KonfSieci(new UstawPoloczenieListener());
    private int wysokosc = 600;
    private int szerokosc = 500;
    private Plansza plansza;
    private JButton jButton = new JButton("Ustaw polaczenie");
    JPanel layout = new JPanel(new BorderLayout());
    public Okno() {
        super("Warcaby");
        super.setSize(szerokosc, wysokosc);
        super.setLocationByPlatform(true);
        plansza = new Plansza(this, false);
        layout.add(plansza, BorderLayout.CENTER);
        jButton.addActionListener(new UstawPoloczenieListener());
        layout.add(jButton, BorderLayout.SOUTH);
        this.add(layout);



    }

    private void ustawieniePolaczenia(ActionEvent e) {
        KonfSieci konfSieci = (KonfSieci) e.getSource();
        try {
            Socket clientSocket = new Socket(konfSieci.getDestinationHost(), konfSieci.getDestinationPort());
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String serverMessage = reader.readLine();
            layout.remove(plansza);
            if(serverMessage.equals("BIALY")){
                this.plansza=new Plansza(this,false);
            }
            else if(serverMessage.equals("CZARNY")){
                this.plansza=new Plansza(this,true);
            }
            layout.add(plansza,BorderLayout.CENTER);
            this.revalidate();
            this.repaint();
            throw new NullPointerException();

        }
        catch(Exception e1){
            konfSieci.setMessage("dasdssd");
        }
    }

    private class UstawPoloczenieListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //wywolanie trafia tutaj gdy zostanie nacisniety przycisk polacz w konfiguracji polaczenia
            if (e.getSource() instanceof KonfSieci) {
                ustawieniePolaczenia(e);
            }
            //wywolanie trafia tutaj gdy zostaje klikniety przycisk ustaw polaczenie
            else {
                konfSieci.setVisible(true);
            }

        }


    }
}
