package logika;

public class Pozycje {
    private int pusteId = 0;
    private int bialeId = 1;
    private int czarneId = 2;
    private int bialeKrolId = 3;
    private int czarneKrolId = 4;
    int[][] wspolrzedne = new int[8][8];

    public Pozycje() {
        for (int y = 0; y < 8; y++) {
            for (int x = (y + 1) % 2; x < 8; x += 2) {
                if(y<3) wspolrzedne[x][y]=czarneId;
                else if(y>4) wspolrzedne[x][y]= bialeId;
                else wspolrzedne[x][y]=pusteId;
            }
        }
    }

    public int getIndeks(int x, int y) {
        return wspolrzedne[x][y];
    }

    public int getPusteId() {
        return pusteId;
    }

    public int getBialeId() {
        return bialeId;
    }

    public int getCzarneId() {
        return czarneId;
    }

    public int getBialeKrolId() {
        return bialeKrolId;
    }

    public int getCzarneKrolId() {
        return czarneKrolId;
    }
}
