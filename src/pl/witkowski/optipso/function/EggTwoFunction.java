package pl.witkowski.optipso.function;

class EggTwoFunction implements Function {

    @Override
    public double calc(double x, double y) {
        return 0.5 * (Math.cos(62.8 * x) + Math.cos(62.8 * y)) - 4 * (x * x + y * y);
    }
}
