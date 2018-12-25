package GUI;

import logika.Pozycje;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;

public class Plansza extends JButton {
    private Pozycje pozycje;
    private int zaznaczoneX, zaznaczoneY;
    private static boolean BIALY = false;
    private static boolean CZARNY = true;
    //oznaczenie czy grac gra bialymi czy czarnymi
    private boolean kolor;

    public Plansza(Okno okno, boolean kolor) {
        this.kolor = kolor;
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

    public  boolean jestPoprawny(int x, int y) {

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
        if (x == zaznaczoneX || y == zaznaczoneY) {
            return false;
        }
        if (pozycje.getIndeks(x, y) != pozycje.getPusteId()) {
            return false;
        }
        if(kolor==BIALY && (pozycje.getIndeks(zaznaczoneX,zaznaczoneY) != pozycje.getBialeId()&&
                pozycje.getIndeks(zaznaczoneX,zaznaczoneY) != pozycje.getBialeKrolId() )){
            return false;
        }
        if(kolor==CZARNY && (pozycje.getIndeks(zaznaczoneX,zaznaczoneY) != pozycje.getCzarneId()&&
                pozycje.getIndeks(zaznaczoneX,zaznaczoneY) != pozycje.getCzarneKrolId() )){
            return false;
        }
    return true;

    }

    private void klikniecie(int x, int y) {
        final int W = getWidth(), H = getHeight();
        final int DIM = W < H ? W : H, BOX_SIZE = DIM / 8;
        final int OFFSET_X = (W - BOX_SIZE * 8) / 2;
        final int OFFSET_Y = (H - BOX_SIZE * 8) / 2;
        x = (x - OFFSET_X) / BOX_SIZE;
        y = (y - OFFSET_Y) / BOX_SIZE;
        if (jestPoprawny(x, y) && jestPoprawny(zaznaczoneX, zaznaczoneY)) {
            if (jestPoprawnyRuch(x, y)) {
                pozycje.setIndeks(x,y,pozycje.getIndeks(zaznaczoneX,zaznaczoneY));
                pozycje.setIndeks(zaznaczoneX,zaznaczoneY,pozycje.getPusteId());
                repaint();

            }
            else{
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
                klikniecie((int)m.getX(), (int)m.getY());
            }
        }
    }

}

