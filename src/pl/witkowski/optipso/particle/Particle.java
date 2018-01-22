package pl.witkowski.optipso.particle;

import lombok.Getter;
import lombok.Setter;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

@Getter
@Setter
public class Particle {

    double[] positionX, positionY;
    double speedX, speedY;
    double bestPositionX, bestPositionY;
    double bestValue;
    int size;
    int actualNumber;

    public Particle(int size, Random generator) {
        double randomPositionX, randomPositionY;

        do {
            randomPositionX = (generator.nextDouble() - 0.5) * 0.96;
            randomPositionY = (generator.nextDouble() - 0.5) * 0.96;
        } while (isNotNearInCentrum(randomPositionX, randomPositionY));
        this.init(size, randomPositionX, randomPositionY);
    }

    private boolean isNotNearInCentrum(double randomPositionX, double randomPositionY) {
        return randomPositionX * randomPositionX + randomPositionY * randomPositionY < 0.09;
    }

    private void init(int size, double x, double y) {
        int i;

        this.bestPositionX = x;
        this.bestPositionY = y;
        this.speedX = 0;
        this.speedY = 0;
        this.actualNumber = 0;

        this.size = ++size;
        this.positionX = new double[size];
        this.positionY = new double[size];

        for (i = 0; i < size; i++) {
            this.positionX[i] = x;
            this.positionY[i] = y;
        }
    }

    /**
     * Update particle position based on PSO algorithm
     *
     * @param omega
     * @param alpha
     * @param beta
     * @param globalBestX
     * @param globalBestY
     */
    public void update(double omega, double alpha, double beta, double globalBestX, double globalBestY) {
        double currentX = this.positionX[this.actualNumber];
        double currentY = this.positionY[this.actualNumber];

        this.speedX = omega * this.speedX + alpha * (this.bestPositionX - currentX) + beta * (globalBestX - currentX);
        this.speedY = omega * this.speedY + alpha * (this.bestPositionY - currentY) + beta * (globalBestY - currentY);

        this.positionX[this.actualNumber] = currentX + this.speedX;
        this.positionY[this.actualNumber] = currentY + this.speedY;
    }

    /**
     * Draw red circle with black border (particle visualisation
     *
     * @param graphics
     * @param width
     * @param height
     */
    public void draw(Graphics graphics, int width, int height) {
        int sx = (int) ((0.5 + this.positionX[this.actualNumber]) * (width - 1));
        int sy = (int) ((0.5 - this.positionY[this.actualNumber]) * (height - 1));

        graphics.setColor(Color.black);
        graphics.fillOval(sx - 4, sy - 4, 9, 9);
        graphics.setColor(Color.red);
        graphics.fillOval(sx - 3, sy - 3, 7, 7);
    }

}