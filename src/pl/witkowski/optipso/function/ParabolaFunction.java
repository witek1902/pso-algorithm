package pl.witkowski.optipso.function;

class ParabolaFunction implements Function {
    @Override
    public double calc(double x, double y) {
        return -4 * (x * x + y * y);
    }
}
