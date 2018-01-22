package pl.witkowski.optipso.function;

class CircleTwoFunction implements Function {

    @Override
    public double calc(double x, double y) {
        return Math.cos(96 * Math.sqrt(x * x + y * y)) - 4 * (x * x + y * y);
    }
}
