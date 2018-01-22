package pl.witkowski.optipso.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.witkowski.optipso.function.FunctionType;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Configuration {

    private int steps;
    private int particles;
    private double omega;
    private double alpha;
    private double beta;
    private FunctionType functionType;

    /**
     * Initial values for algorithm configuration
     */
    public Configuration() {
        particles = 30;
        steps = 200;
        omega = 0.5;
        alpha = 0.3;
        beta = 0.3;
        functionType = FunctionType.PARABOLA;
    }
}
