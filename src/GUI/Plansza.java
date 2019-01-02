package GUI;

import logika.Pozycje;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import static java.lang.StrictMath.abs;

public class Plansza extends JButton {
    private Pozycje pozycje;
    private int zaznaczoneX, zaznaczoneY;
    private static boolean BIALY = false;
    private static boolean CZARNY = true;
    private boolean czyMojRuch;
    private Okno okno;
    private boolean czyCzekamNaOdpowiedz;
    //oznaczenie czy grac gra bialymi czy czarnymi
    private boolean kolor;
    private boolean koniec;

     Plansza(Okno okno, boolean kolor) {
        this.okno = okno;
        koniec=false;
        this.kolor = kolor;
        this.czyMojRuch = !kolor;
        this.czyCzekamNaOdpowiedz=false;
        super.setBorderPainted(false);
        super.setFocusPainted(false);
        super.setContentAreaFilled(false);
        super.setBackground(Color.LIGHT_GRAY);
        this.pozycje = new Pozycje();
        this.addActionListener(new ClickListener());
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;

        final int BOX_PADDING = 4;
        final int W = getWidth(), H = getHeight();
        final int DIM = W < H ? W : H, BOX_SIZE = DIM / 8;
        final int OFFSET_X = (W - BOX_SIZE * 8) / 2;
        final int OFFSET_Y = (H - BOX_SIZE * 8) / 2;
        final int CHECKER_SIZE = Math.max(0, BOX_SIZE - 2 * BOX_PADDING);

        //  rysowanie planszy
        if (czyMojRuch) g.drawString("twoj ruch", W / 2-10, OFFSET_Y + 8 * BOX_SIZE + 13);
        else g.drawString("ruch przeciwnika(kliknij w plansze, aby odświeżyć)", W / 2-140, OFFSET_Y + 8 * BOX_SIZE + 13);
        g.setColor(Color.BLACK);
        g.drawRect(OFFSET_X - 1, OFFSET_Y - 1, BOX_SIZE * 8 + 1, BOX_SIZE * 8 + 1);
        g.setColor(Color.WHITE);
        g.fillRect(OFFSET_X, OFFSET_Y, BOX_SIZE * 8, BOX_SIZE * 8);
        g.setColor(Color.BLACK);
        for (int y = 0; y < 8; y++) {
            for (int x = (y + 1) % 2; x < 8; x += 2) {
                g.fillRect(OFFSET_X + x * BOX_SIZE, OFFSET_Y + y * BOX_SIZE,
                        BOX_SIZE, BOX_SIZE);
            }
        }
        //rysowanie pionkow
        for (int y = 0; y < 8; y++) {
            int cy = OFFSET_Y + y * BOX_SIZE + BOX_PADDING;
            for (int x = (y + 1) % 2; x < 8; x += 2) {
                int id = pozycje.getIndeks(x, y);

                // puste
                if (id == pozycje.getPusteId()) {
                    continue;
                }

                int cx = OFFSET_X + x * BOX_SIZE + BOX_PADDING;

                // czarne
                if (id == pozycje.getCzarneId()) {
                    g.setColor(Color.DARK_GRAY);
                    g.fillOval(cx + 1, cy + 2, CHECKER_SIZE, CHECKER_SIZE);
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawOval(cx + 1, cy + 2, CHECKER_SIZE, CHECKER_SIZE);
                    g.setColor(Color.BLACK);
                    g.fillOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
                } else if (id == pozycje.getBialeId()) {
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillOval(cx + 1, cy + 2, CHECKER_SIZE, CHECKER_SIZE);
                    g.setColor(Color.DARK_GRAY);
                    g.drawOval(cx + 1, cy + 2, CHECKER_SIZE, CHECKER_SIZE);
                    g.setColor(Color.WHITE);
                    g.fillOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
                    g.setColor(Color.DARK_GRAY);
                    g.drawOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
                } else if (id == pozycje.getBialeKrolId()) {
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillOval(cx + 1, cy + 2, CHECKER_SIZE, CHECKER_SIZE);
                    g.setColor(Color.DARK_GRAY);
                    g.drawOval(cx + 1, cy + 2, CHECKER_SIZE, CHECKER_SIZE);
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
                    g.setColor(Color.DARK_GRAY);
                    g.drawOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
                    g.setColor(Color.WHITE);
                    g.fillOval(cx - 1, cy - 2, CHECKER_SIZE, CHECKER_SIZE);
                } else if (id == pozycje.getCzarneKrolId()) {
                    g.setColor(Color.DARK_GRAY);
                    g.fillOval(cx + 1, cy + 2, CHECKER_SIZE, CHECKER_SIZE);
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawOval(cx + 1, cy + 2, CHECKER_SIZE, CHECKER_SIZE);
                    g.setColor(Color.DARK_GRAY);
                    g.fillOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
                    g.setColor(Color.BLACK);
                    g.fillOval(cx - 1, cy - 2, CHECKER_SIZE, CHECKER_SIZE);
                }


            }
        }
    }

    private boolean jestPoprawny(int x, int y) {

        // sprawdzenie czy zaznaczony punkt jest na planszy
        if (x < 0 || x > 7 || y < 0 || y > 7) {
            return false;
        }

        // sprawdzenie czy zaznaczony punkt jest czarnym punktem
        if (x % 2 == y % 2) {
            return false;
        }


        return true;
    }

    private boolean jestPoprawnyRuch(int x, int y) {
        return jestPoprawnyRuch(zaznaczoneX,zaznaczoneY,x,y);
    }
    //zaznaczoneY i zaznaczoneX to wspolrzedne pola z ktorego dokonywany jest ruch, x i y to wspolrzedne pola na ktory chcemy przestawic pionka
    private boolean jestPoprawnyRuch(int zaznaczoneX,int zaznaczoneY,int x, int y) {
        return jestPoprawnyRuch(zaznaczoneX,zaznaczoneY,x,y,kolor);

    }
    private boolean jestPoprawnyRuch(int zaznaczoneX,int zaznaczoneY,int x, int y,boolean kolor) {
        if (x == zaznaczoneX || y == zaznaczoneY) {
            return false;
        }
        if (pozycje.getIndeks(x, y) != pozycje.getPusteId()) {
            return false;
        }
        if (pozycje.getIndeks(zaznaczoneX, zaznaczoneY) == pozycje.getCzarneId()&&y<zaznaczoneY) {
            return false;
        }
        if (pozycje.getIndeks(zaznaczoneX, zaznaczoneY) == pozycje.getBialeId()&&y>zaznaczoneY) {
            return false;
        }
        if (kolor == BIALY && (pozycje.getIndeks(zaznaczoneX, zaznaczoneY) != pozycje.getBialeId() &&
                pozycje.getIndeks(zaznaczoneX, zaznaczoneY) != pozycje.getBialeKrolId())) {
            return false;
        }
        if (kolor == BIALY && (x == zaznaczoneX + 2 || x == zaznaczoneX - 2) && (y == zaznaczoneY + 2 || y == zaznaczoneY - 2) &&
                pozycje.getIndeks(zaznaczoneX+(-1)*(zaznaczoneX - x)/2, zaznaczoneY+(-1)*(zaznaczoneY - y)/2) != pozycje.getCzarneId()) {
            return false;
        }
        if ( (x > zaznaczoneX + 2 || x < zaznaczoneX - 2) || (y > zaznaczoneY + 2 || y < zaznaczoneY - 2)) {
            return false;
        }
        if (kolor == CZARNY && (x == zaznaczoneX + 2 || x == zaznaczoneX - 2) && (y == zaznaczoneY + 2 || y == zaznaczoneY - 2) &&
                pozycje.getIndeks(zaznaczoneX+(-1)*(zaznaczoneX - x)/2, zaznaczoneY+(-1)*(zaznaczoneY - y)/2) != pozycje.getBialeId()) {
            return false;
        }
        if (kolor == CZARNY && (pozycje.getIndeks(zaznaczoneX, zaznaczoneY) != pozycje.getCzarneId() &&
                pozycje.getIndeks(zaznaczoneX, zaznaczoneY) != pozycje.getCzarneKrolId())) {
            return false;
        }
        return true;

    }
    private boolean czyPrzegralem(){
        if(kolor==BIALY){
            for(int i=0;i<8;i++){
                for(int j=0;j<8;j++){
                    if(pozycje.getIndeks(i,j)==pozycje.getBialeId() || pozycje.getIndeks(i,j)==pozycje.getBialeKrolId()){
                       for(int o=i-2;o<i+3;o++){
                           for(int p=j-2;p<j+3;p++){
                                if(jestPoprawny(o,p)){
                                    if(jestPoprawnyRuch(i,j,o,p)){
                                        return false;
                                    }
                                }
                           }
                       }
                    }
                }
            }
        }
        if(kolor==CZARNY){
            for(int i=0;i<8;i++){
                for(int j=0;j<8;j++){
                    if(pozycje.getIndeks(i,j)==pozycje.getCzarneId() || pozycje.getIndeks(i,j)==pozycje.getCzarneKrolId()){
                        for(int o=i-2;o<i+3;o++){
                            for(int p=j-2;p<j+3;p++){
                                if(jestPoprawny(o,p)){
                                    if(jestPoprawnyRuch(i,j,o,p)){
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    private boolean czyWygralem(){
        if(kolor==BIALY){
            for(int i=0;i<8;i++){
                for(int j=0;j<8;j++){
                    if(pozycje.getIndeks(i,j)==pozycje.getCzarneId() || pozycje.getIndeks(i,j)==pozycje.getCzarneKrolId()){
                        for(int o=i-2;o<i+3;o++){
                            for(int p=j-2;p<j+3;p++){
                                if(jestPoprawny(o,p)){
                                    if(jestPoprawnyRuch(i,j,o,p,CZARNY)){
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if(kolor==CZARNY){
            for(int i=0;i<8;i++){
                for(int j=0;j<8;j++){
                    if(pozycje.getIndeks(i,j)==pozycje.getBialeId() || pozycje.getIndeks(i,j)==pozycje.getBialeKrolId()){
                        for(int o=i-2;o<i+3;o++){
                            for(int p=j-2;p<j+3;p++){
                                if(jestPoprawny(o,p)){
                                    if(jestPoprawnyRuch(i,j,o,p,BIALY)){
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    private void ustawRuch(int x, int y, int x2, int y2) {
        if ((x2 == x + 2 || x2 == x - 2) && (y2 == y + 2 || y2 == y - 2)) {
            pozycje.setIndeks(x+(-1)*(x - x2)/2, y+(-1)*(y - y2)/2,pozycje.getPusteId());
        }
        pozycje.setIndeks(x2, y2, pozycje.getIndeks(x, y));
        pozycje.setIndeks(x, y, pozycje.getPusteId());
        repaint();
        if(czyWygralem()){
            JOptionPane.showMessageDialog(this, "Wygrales!!!!!!!!!");
            koniec=true;
        }
        else if(czyPrzegralem()){
            JOptionPane.showMessageDialog(this, "Przegrales!!!!!!!");
            koniec=true;
        }
    }

    private void wyslijRuchDoSerwera(int x, int y, int x2, int y2) {
        PrintWriter printWriter = okno.getWriter();
        char[] buf=new char[5];
        buf[0]=(char)(x+'0');
        buf[1]=(char)(y+'0');
        buf[2]=(char)(x2+'0');
        buf[3]=(char)(y2+'0');
        buf[4]='\n';
        System.setProperty("line.separator", "\n");
        printWriter.println(Integer.toString(x) + Integer.toString(y) + Integer.toString(x2) + Integer.toString(y2));

    }

    public void ruchPrzeciwnika() {
        ReaderThread readerThread = new ReaderThread();
        readerThread.start();
    }

    private char[] ruchPrzeciwnikaZSerwera() {
        BufferedReader bufferedReader = okno.getReader();
        String response = null;
        char[] buf=new char[5];
        try {
            bufferedReader.read(buf);
        } catch (Exception e) {
            System.out.println("blad odczytu ruchu przeciwnika");
        }
        return buf;
    }

    private void klikniecie(int x, int y) {
        final int W = getWidth(), H = getHeight();
        final int DIM = W < H ? W : H, BOX_SIZE = DIM / 8;
        final int OFFSET_X = (W - BOX_SIZE * 8) / 2;
        final int OFFSET_Y = (H - BOX_SIZE * 8) / 2;
        x = (x - OFFSET_X) / BOX_SIZE;
        y = (y - OFFSET_Y) / BOX_SIZE;
        if (jestPoprawny(x, y) && jestPoprawny(zaznaczoneX, zaznaczoneY)) {
            if (jestPoprawnyRuch(x, y) && czyMojRuch && okno.getWriter() != null) {
                czyMojRuch = false;
                ustawRuch(zaznaczoneX, zaznaczoneY, x, y);
                wyslijRuchDoSerwera(zaznaczoneX, zaznaczoneY, x, y);
                if(koniec){
                    wyslijRuchDoSerwera(0,0,0,0);
                }

            } else {
                zaznaczoneY = y;
                zaznaczoneX = x;
            }
        } else {
            zaznaczoneY = y;
            zaznaczoneX = x;
        }
    }

    private class ClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Point2D m = Plansza.this.getMousePosition();
            if (m != null) {
                if(!czyCzekamNaOdpowiedz && czyMojRuch)
                klikniecie((int) m.getX(), (int) m.getY());
                else if(!czyCzekamNaOdpowiedz && !czyMojRuch){
                    ReaderThread readerThread=new ReaderThread();
                    readerThread.run();
                }
            }
        }
    }

    private class ReaderThread extends Thread {
        @Override
        public void run() {
            char[] ruch = ruchPrzeciwnikaZSerwera();
            czyMojRuch = true;
            ustawRuch(ruch[0]-48, ruch[1]-48,ruch[2]-48,ruch[3]-48);
            if(koniec){
                wyslijRuchDoSerwera(0,0,0,0);
            }
            czyCzekamNaOdpowiedz=false;
        }

    }

}