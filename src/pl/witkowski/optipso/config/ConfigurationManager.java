package pl.witkowski.optipso.config;

import lombok.Getter;
import lombok.Setter;
import pl.witkowski.optipso.function.FunctionType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ConfigurationManager {

    @Setter
    @Getter
    private Configuration config;

    public ConfigurationManager() {
        this.config = new Configuration();
    }

    public void loadFromFile(String fileName) {
        try {
            FileReader reader = new FileReader(fileName);
            BufferedReader br = new BufferedReader(reader);

            setConfig(Configuration.builder()
                    .functionType(FunctionType.valueOf(br.readLine()))
                    .particles(Integer.parseInt(br.readLine()))
                    .steps(Integer.parseInt(br.readLine()))
                    .omega(Double.parseDouble(br.readLine()))
                    .alpha(Double.parseDouble(br.readLine()))
                    .beta(Double.parseDouble(br.readLine()))
                    .build());
        } catch (IOException e) {
            Logger.getLogger(ConfigurationManager.class.getName()).log(Level.WARNING, "Load config exception", e);
        }

    }

    public void saveInFile(String fileName) {
        try {
            PrintWriter out = new PrintWriter(fileName);
            printConfigValues(out);
            printInstruction(out);
            out.close();
        } catch (IOException e) {
            Logger.getLogger(ConfigurationManager.class.getName()).log(Level.WARNING, "Save config exception", e);
        }
    }

    private void printConfigValues(PrintWriter out) {
        out.println(config.getFunctionType().name());
        out.println(config.getParticles());
        out.println(config.getSteps());
        out.println(config.getOmega());
        out.println(config.getAlpha());
        out.println(config.getBeta());
    }

    private void printInstruction(PrintWriter out) {
        out.println();
        out.println();
        out.println("1. Function (one of: PARABOLA, CIRCLE_ONE, CIRCLE_TWO, EGG_ONE, EGG_TWO). By default = PARABOLA.");
        out.println("2. Number of particles");
        out.println("3. Number of steps");
        out.println("4. Omega");
        out.println("5. Alpha");
        out.println("6. Beta");
    }
}
