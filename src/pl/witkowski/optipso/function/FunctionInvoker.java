package pl.witkowski.optipso.function;

import java.util.HashMap;
import java.util.Map;

public class FunctionInvoker {

    private static final Map<FunctionType, Function> functions = createMap();
    private static Map<FunctionType, Function> createMap() {
        Map<FunctionType, Function> functions = new HashMap<>();

        functions.put(FunctionType.CIRCLE_ONE, new CircleOneFunction());
        functions.put(FunctionType.CIRCLE_TWO, new CircleTwoFunction());
        functions.put(FunctionType.EGG_ONE, new EggOneFunction());
        functions.put(FunctionType.EGG_TWO, new EggTwoFunction());
        functions.put(FunctionType.PARABOLA, new ParabolaFunction());

        return functions;
    }

    public double calc(double x, double y, FunctionType type) {
        return functions.get(type).calc(x, y);
    }
}
