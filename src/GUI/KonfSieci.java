package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KonfSieci extends JFrame {


    public static final int SZEROKOSC = 500;


    public static final int WYSOKOSC = 200;

    public static final String TYTUL = "Konfiguracja Polaczenia";


    private JTextField host;


    private JTextField port;


    private JButton polacz;


    private JPanel jPanel;

    /**
     * The label to display the message on the window.
     */
    private JLabel wiadomosc;
    private ActionListener actionListener;


    public KonfSieci(ActionListener actionListener) {
        super.setTitle(TYTUL);
        super.setSize(SZEROKOSC, WYSOKOSC);
        super.setLocationByPlatform(true);
        this.actionListener = actionListener;
        init();
    }


    private void init() {
        this.getContentPane().setLayout(new GridLayout(3, 1));
        this.host = new JTextField(11);
        this.host.setText("127.0.0.1");
        this.port = new JTextField(4);
        this.polacz = new JButton("Connect");
        this.polacz.addActionListener(new ConnectionListener());
        this.jPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        this.wiadomosc = new JLabel();
        this.jPanel.add(new JLabel("Destination host/port:"));
        this.jPanel.add(host);
        this.jPanel.add(port);
        this.jPanel.add(polacz);
        stworzLayout(null);
    }

    //funkcja wywyolywana przy ustawianiu wiadomosci i w konstruktorze
    private void stworzLayout(String msg) {

        this.getContentPane().removeAll();
        this.getContentPane().add(jPanel);
        this.wiadomosc.setText(msg);
        this.getContentPane().add(this.wiadomosc);
        //bez ponizszej linijki wiadomość sie nie wyswietla
        this.wiadomosc.setVisible(false);
        this.wiadomosc.setVisible(true);
    }


    public String getDestinationHost() {
        return host.getText();
    }


    public void setDestinationHost(String host) {
        this.host.setText(host);
    }


    public int getDestinationPort() {
        return PobierzInt(port);
    }


    public void setDestinationPort(int port) {
        this.port.setText("" + port);
    }


    public String getMessage() {
        return wiadomosc.getText();
    }


    public void setMessage(String message) {
        stworzLayout(message);
    }

    //pobieranie numeru portu oraz adresu ip
    private static int PobierzInt(JTextField tf) {

        if (tf == null) {
            return 0;
        }
        int val = 0;
        try {
            val = Integer.parseInt(tf.getText());
        } catch (NumberFormatException e) {


        }

        return val;
    }

    // listener dla guzika polacz odpala metode ustawieniePolaczenia w klasie Okno
    private class ConnectionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            ActionEvent event = new ActionEvent(KonfSieci.this,
                    1, null);

            actionListener.actionPerformed(event);
        }

    }
}