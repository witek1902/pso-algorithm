package pl.witkowski.optipso.function;

class EggOneFunction implements Function {

    @Override
    public double calc(double x, double y) {
        return 0.5 * (Math.cos(31.4 * x) + Math.cos(31.4 * y)) - 4 * (x * x + y * y);
    }
}
