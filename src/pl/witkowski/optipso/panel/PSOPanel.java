package pl.witkowski.optipso.panel;

import lombok.Getter;
import pl.witkowski.optipso.function.Function;
import pl.witkowski.optipso.function.FunctionInvoker;
import pl.witkowski.optipso.function.FunctionType;
import pl.witkowski.optipso.particle.Particle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.JPanel;

/*
* This is main class responsible for drawing charts.
* In this class is created and updated swarm and stored information about global best particle positions.
*/
public class PSOPanel extends JPanel {

    private FunctionType function;
    private FunctionInvoker functionInvoker;

    private Color[] functionColors;
    private BufferedImage background;

    private Particle[] swarm;
    @Getter
    private double bestPositionX, bestPositionY;
    @Getter
    private double best;   /* Best value for function (minimum) */
    @Getter
    private int step;

    public PSOPanel() {
        this.function = FunctionType.PARABOLA;
        this.functionInvoker = new FunctionInvoker();
        this.functionColors = new Color[256];
        for (int i = 255; i >= 0; i--) {
            this.functionColors[i] = new Color(i / 255.0F, i / 255.0F, i / 255.0F);
        }
        this.background = null;
    }

    public void setFunction(FunctionType type) {
        this.function = type;
        this.swarm = null;
        this.background = null;
        this.repaint();
    }

    public boolean hasSwarm() {
        return this.swarm != null;
    }

    public void createSwarm(int numberOfParticles) {
        double tempBestValue;
        Random rnd = new Random();
        this.step = 0;
        this.best = -Double.MAX_VALUE;
        this.swarm = new Particle[numberOfParticles];

        for (int i = 0; i < numberOfParticles; i++) {
            Particle p = new Particle(2, rnd);
            this.swarm[i] = p;
            tempBestValue = functionInvoker.calc(p.getPositionX()[0], p.getPositionY()[0], function);
            p.setBestValue(tempBestValue);

            if (tempBestValue > this.best) {
                this.best = tempBestValue;
                this.bestPositionX = p.getPositionX()[0];
                this.bestPositionY = p.getPositionY()[0];
            }
        }

        this.repaint();
    }

    public void updateSwarm(double alpha, double beta) {
        if (this.swarm == null) {
            return;
        }

        double omega = Math.pow(++this.step, -0.015); /* little fix for speed for better minimum search */
        for (int i = 0; i < this.swarm.length; i++) {
            Particle particle = this.swarm[i];
            particle.update(omega, alpha, beta, this.bestPositionX, this.bestPositionY);

            int actualNumber = particle.getActualNumber();
            double fValue = functionInvoker.calc(particle.getPositionX()[actualNumber], particle.getPositionY()[actualNumber], function);

            if (fValue > particle.getBestValue()) {
                double fromParticleX = particle.getPositionX()[actualNumber];
                double fromParticleY = particle.getPositionY()[actualNumber];

                particle.setBestValue(fValue);
                particle.setBestPositionX(fromParticleX);
                particle.setBestPositionY(fromParticleY);

                this.best = fValue;
                this.bestPositionX = fromParticleX;
                this.bestPositionY = fromParticleY;
            }
        }
        this.repaint();
    }

    @Override
    public void paint(Graphics g) {
        Dimension d;                   /* ustawiamy wielkosc rysunku */
        int width, height;             /* wielkosc tla */
        int xPixel, yPixel;            /* zmienne iteracyjne dla pixeli */
        Graphics backgroundGraphics;   /* grafika dla tla  */
        double[][] pixelValues;        /* tablica pixeli */
        double min, max, tempFunctionVal;/* minimalna, maxymalna wartosc funkcji i tymczasowa zmienna do kolorow pixeli */

        d = this.getSize();
        width = d.width;
        height = d.height;

        if (isWindowSizeChanged(width, height)) {
            this.background = null;
        }

        if (this.background == null) {
            this.background = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            backgroundGraphics = this.background.getGraphics();
            pixelValues = new double[width][height];
            min = Double.MAX_VALUE;
            max = -Double.MAX_VALUE;
            for (xPixel = width - 1; xPixel >= 0; --xPixel) {
                for (yPixel = height - 1; yPixel >= 0; --yPixel) {
                    tempFunctionVal = functionInvoker.calc((xPixel / (double) (width - 1)) - 0.5, 0.5 - (yPixel / (double) (height - 1)), function);
                    pixelValues[xPixel][yPixel] = tempFunctionVal;
                    if (tempFunctionVal < min) {
                        min = tempFunctionVal;
                    }
                    if (tempFunctionVal > max) {
                        max = tempFunctionVal;
                    }
                }
            }
            paintPixels(width, height, backgroundGraphics, pixelValues, min, max);
        }
        g.drawImage(this.background, 0, 0, null);
        drawSwarm(g, width, height);
        paintBestParticle(g, width, height);
    }

    private void paintPixels(int width, int height, Graphics backgroundGraphics, double[][] pixelValues, double min, double max) {
        double tempFunctionVal;
        int xPixel;
        int yPixel;
        tempFunctionVal = 255.0 / 1.04 / (max - min);
        min -= 0.02 * (max - min);  /* calculate color scaling factor */

        for (xPixel = width - 1; xPixel >= 0; --xPixel) {
            for (yPixel = height - 1; yPixel >= 0; --yPixel) {
                int colorIndex = (int) (tempFunctionVal * (pixelValues[xPixel][yPixel] - min));
                if (colorIndex < 0) {
                    colorIndex = 0;
                } else if (colorIndex > 255) {
                    colorIndex = 255;
                }
                backgroundGraphics.setColor(this.functionColors[colorIndex]);
                backgroundGraphics.fillRect(xPixel, yPixel, 1, 1);
            }
        }
    }

    private void drawSwarm(Graphics g, int width, int height) {
        if (this.swarm == null) {
            return;
        }
        for (int i = 0; i < this.swarm.length; i++) {
            this.swarm[i].draw(g, width, height);
        }
    }

    private void paintBestParticle(Graphics g, int width, int height) {
        int bestPixelX = (int) ((0.5 + this.bestPositionX) * (width - 1));
        int bextPixelY = (int) ((0.5 - this.bestPositionY) * (height - 1));
        g.setColor(Color.black);
        g.fillOval(bestPixelX - 4, bextPixelY - 4, 9, 9);
        g.setColor(Color.blue);
        g.fillOval(bestPixelX - 3, bextPixelY - 3, 7, 7);
    }

    private boolean isWindowSizeChanged(int width, int height) {
        return (this.background != null) && ((width != this.background.getWidth()) || (height != this.background.getHeight()));
    }
}