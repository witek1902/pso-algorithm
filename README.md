# Particle Swarm Optimization (PSO) with visualisation
![Program OptiPSO](https://i.imgur.com/QnNhi5c.gif)

## About
This project was created in 2013. In January 2018 I decided to little refactoring and publishing it for open source.
In folder**old-src**you can see the old code written in**student times**with comment/code in polish language :)
In folder**algorithm-description**is located base description Particle Swarm Optimization in polish language (LaTeX version and PDF).

## Requirements
 - Java 8
 - Maven 3
## How to use
In root folder with project:

    mvn package

After, in the*target*folder will be executable jar**OptiPSO.jar**. <br/>
That's all!

## Algorithm parameters
User can change algorithm parameters from UI or load from file.
Example file with configuration:

    CIRCLE_ONE
    300
    2000
    0.3
    0.2
    0.4
    
    
    1. Function (one of: CIRCLE_ONE, CIRCLE_TWO, EGG_ONE, EGG_TWO, PARABOLA). By default = PARABOLA.
    2. Number of particles
    3. Number of steps
    4. Omega
    5. Alpha
    6. Beta
    
## Available function
    CIRCLE_ONE -> 0.5 * (cos(62.8 * x) + cos(62.8 * y)) - 4 * (x * x + y * y
    CIRCLE_TWO -> cos(96 * sqrt(x * x + y * y)) - 4 * (x * x + y * y)
    EGG_ONE -> 0.5 * (cos(31.4 * x) + cos(31.4 * y)) - 4 * (x * x + y * y)
    EGG_TWO -> 0.5 * (cos(62.8 * x) + cos(62.8 * y)) - 4 * (x * x + y * y)
    PARABOLA -> -4 * (x * x + y * y)

If you want add custom function:
1. Add new class which implements**Function**interface,
2. Add value to**FunctionType**enum,
3. Add entry to Map in to**FunctionInvoker** class.

## References
 - https://en.wikipedia.org/wiki/Particle_swarm_optimization
 - http://www.swarmintelligence.org/index.php