package pl.witkowski.optipso.function;

class CircleOneFunction implements Function {

    @Override
    public double calc(double x, double y) {
        return Math.cos(48 * Math.sqrt(x * x + y * y)) - 4 * (x * x + y * y);
    }
}
