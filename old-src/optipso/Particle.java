package optipso;
/*
 * Klasa odpowiadajaca za przetrzymywanie wszystkich informacji o czastkach.
 * Przechowujemy w niej pozycje i predkosc czastki, najlepsza pozycje i najlepsza wartosc czastki oraz jej numer
 * Metody w tej klasie odpowiadaja za inicjalizacje czastki oraz za jej rysowanie na wykresie
*/
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

class Particle {

    double[] x, y;     /* pozycja czastki */
    double vx, vy;    /* predkosc czastki */
    double bx, by;    /* najlepsza pozycja czastki */
    double best;      /* najlepsza wartosc czastki */
    int size;         /* zmienna odpowiadajaca za zapamietywanie wstecz */
    int number;       /* aktualny numer czastki */

    /* inicjalizacja czastki */
    private void init(int size, double x, double y) {
        int i;                  

        this.bx = x;    /* poczatkowa najlepsza pozycja rowna wylosowanym liczbom */
        this.by = y;
        this.vx = 0;    /* poczatkowa predkosc czastki rowna 0 */
        this.vy = 0;   
        this.number = 0;  
        
        this.size = ++size;
        this.x = new double[size];
        this.y = new double[size];
        
        for (i = 0; i < size; i++) {
            this.x[i] = x;
            this.y[i] = y;
        }
    }

    public Particle(int size, double x, double y) {
        this.init(size, x, y);
    }

    public Particle(int size, Random generator) {
        double rand_x, rand_y;              /* wylosowana pozycja czastek */

        do {
            rand_x = (generator.nextDouble() - 0.5) * 0.96;
            rand_y = (generator.nextDouble() - 0.5) * 0.96;
        } while (rand_x * rand_x + rand_y * rand_y < 0.09);  /* generowanie pozycji nie za blisko centrum */
                                                             /* tylko i wylacznie na potrzeby ladnej wizualizacji */
        this.init(size, rand_x, rand_y);
    } 

    /* aktualizacja pozycji i predkosci czastki wzorem PSO */
    public void update(double omega, double alpha, double beta,double gBestX, double gBestY) {
        
        double currentX = this.x[this.number];
        double currentY = this.y[this.number];
        
        this.vx = omega * this.vx + alpha * (this.bx - currentX) + beta * (gBestX - currentX);
        this.vy = omega * this.vy + alpha * (this.by - currentY) + beta * (gBestY - currentY);

        this.x[this.number] = currentX + this.vx;
        this.y[this.number] = currentY + this.vy; 
    } 

    /* rysujemy czastke na wykresie */
    public void drawParticle(Graphics g, int width, int height ){
        int sx = (int) ((0.5 + this.x[this.number]) * (width - 1));
        int sy = (int) ((0.5 - this.y[this.number]) * (height - 1));
        /* rysujemy czerwone kolko z czarnym obramowaniem = czasteczka */
        g.setColor(Color.black); 
        g.fillOval(sx - 4, sy - 4, 9, 9);
        g.setColor(Color.red);    
        g.fillOval(sx - 3, sy - 3, 7, 7);
    }

}