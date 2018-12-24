package GUI;

import logika.Pozycje;

import javax.swing.*;
import java.awt.*;

public class Plansza extends JButton {
    private Pozycje pozycje;


    public Plansza(Okno okno) {
        super.setBorderPainted(false);
        super.setFocusPainted(false);
        super.setContentAreaFilled(false);
        super.setBackground(Color.LIGHT_GRAY);
        this.pozycje = new Pozycje();
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

                // Empty, just skip
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
                }
                else if (id == pozycje.getBialeId()) {
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillOval(cx + 1, cy + 2, CHECKER_SIZE, CHECKER_SIZE);
                    g.setColor(Color.DARK_GRAY);
                    g.drawOval(cx + 1, cy + 2, CHECKER_SIZE, CHECKER_SIZE);
                    g.setColor(Color.WHITE);
                    g.fillOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
                    g.setColor(Color.DARK_GRAY);
                    g.drawOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
                }
                else if (id == pozycje.getBialeKrolId()) {
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
                }
                else if (id == pozycje.getCzarneKrolId()) {
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
}
