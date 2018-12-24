package GUI;

import javax.swing.*;
import java.awt.*;

public class Plansza extends JButton {
    public Plansza(Okno okno) {
        super.setBorderPainted(false);
        super.setFocusPainted(false);
        super.setContentAreaFilled(false);
        super.setBackground(Color.LIGHT_GRAY);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;

        final int BOX_PADDING = 4;
        final int W = getWidth(), H = getHeight();
        final int DIM = W < H? W : H, BOX_SIZE = DIM / 8;
        final int OFFSET_X = (W - BOX_SIZE * 8) / 2;
        final int OFFSET_Y = (H - BOX_SIZE * 8) / 2;
        final int CHECKER_SIZE = Math.max(0, BOX_SIZE - 2 * BOX_PADDING);

        //  rysowanie planszy
        g.setColor(Color.BLACK);
        g.drawRect(OFFSET_X - 1, OFFSET_Y - 1, BOX_SIZE * 8 + 1, BOX_SIZE * 8 + 1);
        g.setColor(Color.WHITE);
        g.fillRect(OFFSET_X, OFFSET_Y, BOX_SIZE * 8, BOX_SIZE * 8);
        g.setColor(Color.BLACK);
        for (int y = 0; y < 8; y ++) {
            for (int x = (y + 1) % 2; x < 8; x += 2) {
                g.fillRect(OFFSET_X + x * BOX_SIZE, OFFSET_Y + y * BOX_SIZE,
                        BOX_SIZE, BOX_SIZE);
            }
        }


    }
}
