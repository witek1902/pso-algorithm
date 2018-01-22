package optipso;
/*
* Glowna klasa odpowiadajaca za rysowanie wykresow funkcji.
* Tutaj tworzony jest i aktualizowany roj oraz przechowywane sa najlepsze globalne pozycje czastek 
* Jednak glowna metoda tej klasy jest metoda paint odpowiadajaca za ustalanie kolorow pixeli
* oraz piekne rysowanie wykresow zaimplementowanych w programie funkcji
*/
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.JPanel;

public class PSOPanel extends JPanel {

    private int function_id;   /* id funkcji */

    private Color[] function_color;  /* kolory  */

    private BufferedImage background;  /* tlo */

    /* Zmienne potrzebne do inicjalizacji roju */
    
    private Particle[] swarm;  /* Tablica czastek */

    private Random rand;   /* generator liczb */

    private double bestX, bestY; /* najlepsza globalna pozycja X i Y */

    private double best;   /* najlepsza wartosc funkcji (minimum) */

    private int step;   /* kroki */

    /* Inicjalizujemy kolory, wybieramy pierwsza funkcje (przykladowa) */
    public PSOPanel() {
        this.function_id = 0;
        this.function_color = new Color[256];
        for (int i = 255; i >= 0; i--) {
            this.function_color[i] = new Color(i / 255.0F, i / 255.0F, i / 255.0F);
        }
        this.background = null;
    }

    /* metoda zabezpieczajaca */
    public boolean hasSwarm() {
        return (this.swarm != null);
    }

    /* wybieramy funkcje i przerysowujemy */
    public void setFunctionID(int id) {

        this.function_id = id;
        this.swarm = null;
        this.background = null;
        this.repaint();
    }

    
    
    /* tworzymy roj */
    public void createSwarm(int liczba_czastek) {
        int i;        /* zmienna iteracyjna */
        Particle p;   /* czasteczka do chodzenia po tablicy */
        double bufBestValue;   /* zmienna tymczasowa najlepszej wartosci */

        rand = new Random();
        this.step = 0;
        this.best = -Double.MAX_VALUE;
        this.swarm = new Particle[liczba_czastek];

        /* dokladna inicjalizacja roju, czastki przetrzymywane w tablicy */
        for (i = 0; i < liczba_czastek; i++) {
            this.swarm[i] = p = new Particle(2, rand);
            /* obliczamy wartosc funkcji i ustawiamy pierwsze najlepsze wartosci jako wartosci */
            bufBestValue = Function.functionValue(p.x[0], p.y[0], function_id); 
            p.best = bufBestValue;              
            /* najlepsza globalna wartosc = najlepsza wartosc pierwszej czasteczki */
            if (bufBestValue > this.best) { 
                this.best = bufBestValue;
                this.bestX = p.x[0];
                this.bestY = p.y[0];
            }
        }

        this.repaint();
    }

    /* aktualizujemy pozycje roju i przerysowujemy ja na panelu */
    public void updateSwarm(double alpha, double beta) {

        int i, k;
        Particle particle;        /* do chodzenia po tablicy czastek */
        double omega;             /* zmniejszanie predkosci */
        double FunctionValue;     /* zmienna tymczasowa dla zawrtosci funkcji*/

        // jesli roj jest pusty to do widzenia
        if (this.swarm == null) {
            return;
        }

        /* zmniejszamy parametr, by lepiej znajdowac minimum */
        omega = Math.pow(++this.step, -0.015);

        for (i = 0; i < this.swarm.length; i++) {

            particle = this.swarm[i];  /* zapisujemy kolejne czasteczki z tablicy */

            /* omega, alpha, beta */
            particle.update(omega, alpha, beta, this.bestX, this.bestY);

            k = particle.number;               /* k = numer czasteczki */
            /* obliczamy wartosci funkcji */
            FunctionValue = Function.functionValue(particle.x[k], particle.y[k], function_id);
            /* zapisujemy najlepsza pozycje lokalna */
            if (FunctionValue > particle.best) {
                particle.best = FunctionValue;
                particle.bx = particle.x[k];
                particle.by = particle.y[k];
            }
            /* zapisujemy najlepsza pozycje globalna */
            if (FunctionValue > this.best) {
                this.best = FunctionValue;
                this.bestX = particle.x[k];
                this.bestY = particle.y[k];
            }
        }
        this.repaint();     /* przerysowujemy roj */
    }
  
  /* Metody dostepowe stworzone na potrzebny metody aktualizujacej status w GUI */  
  /* zwraca aktualny krok */
  public int     getStep  () { return this.step; }
  /* zwraca najlepsza wartosc */
  public double  getBest  () { return this.best; }
  /* zwraca najlesza pozycje x */
  public double  getBestX () { return this.bestX; }
  /* zwraca najlepsza pozycje y */
  public double  getBestY () { return this.bestY; }
  
    /* glowna metoda tej klasy, odpowiadajaca za ustalanie kolorow pixeli i rysowanie roju czastek */
    @Override
    public void paint(Graphics g) {
        int i;
        Dimension d;                   /* ustawiamy wielkosc rysunku */
        int width, height;             /* wielkosc tla */
        int xPixel, yPixel;            /* zmienne iteracyjne dla pixeli */
        int color_index;               /* color index dla pixeli */
        Graphics backgr;               /* grafika dla tla  */
        double[][] pixelValues;        /* tablica pixeli */
        double min, max, FunctionValue;/* minimalna, maxymalna wartosc funkcji i tymczasowa zmienna do kolorow pixeli */

        /* odczytaj wielkosc panelu */
        d = this.getSize();  
        width = d.width;
        height = d.height;
        /* kiedy okno zmienia rozmiar robimy je nullem by na nowo wygenerowac obraz */
        if ((this.background != null) && ((width != this.background.getWidth()) || (height != this.background.getHeight()))) {
            this.background = null;
        }
        /* kiedy tlo nie jest null */
        if (this.background == null) {
            this.background = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            backgr = this.background.getGraphics();
            pixelValues = new double[width][height];
            min = Double.MAX_VALUE;
            max = -Double.MAX_VALUE;
            for (xPixel = width - 1; xPixel >= 0; --xPixel) {
                for (yPixel = height - 1; yPixel >= 0; --yPixel) {
                    /* obliczamy wartosc dla pixeli */
                    FunctionValue = Function.functionValue((xPixel / (double) (width - 1)) - 0.5, 0.5 - (yPixel / (double) (height - 1)), function_id);
                    pixelValues[xPixel][yPixel] = FunctionValue;
                    /* szukamy minimum i maksimum do skalowania kolorow */
                    if (FunctionValue < min) {
                        min = FunctionValue;
                    }
                    if (FunctionValue > max) {
                        max = FunctionValue;
                    }
                }
            }
            FunctionValue = 255.0 / 1.04 / (max - min);
            min -= 0.02 * (max - min);  /* obliczamy wspolczynnik skalowania kolorow */
            for (xPixel = width - 1; xPixel >= 0; --xPixel) { /* chodzimy znow po pixelach */
                for (yPixel = height - 1; yPixel >= 0; --yPixel) {
                    /* obliczamy kolor pixela na podstawie wartosci funkcji */
                    color_index = (int) (FunctionValue * (pixelValues[xPixel][yPixel] - min));
                    if (color_index < 0) {
                        color_index = 0;
                    } else if (color_index > 255) {
                        color_index = 255;
                    }
                    backgr.setColor(this.function_color[color_index]);
                    backgr.fillRect(xPixel, yPixel, 1, 1);
                }
            }
        }
        g.drawImage(this.background, 0, 0, null);
  
        if (this.swarm == null) {
            return;
        }
        /* rysowanie roju czastek */
        for (i = 0; i < this.swarm.length; i++) {
            this.swarm[i].drawParticle(g, width, height);
        }

        /* rysowanie najlepszej czastki innym kolorem */
        xPixel = (int) ((0.5 + this.bestX) * (width - 1));
        yPixel = (int) ((0.5 - this.bestY) * (height - 1));
        g.setColor(Color.black);
        g.fillOval(xPixel - 4, yPixel - 4, 9, 9);
        g.setColor(Color.blue);
        g.fillOval(xPixel - 3, yPixel - 3, 7, 7);
    }
}