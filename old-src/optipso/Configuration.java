package optipso;

import java.io.*;

public class Configuration {

    private static int steps;
    private static int particles;
    private static double omega;
    private static double alpha;
    private static double beta;
    private static int function_number;

    /* konstruktor z domyslnymi wartosciami (takie same ustawione w GUI) */
    public Configuration() {

        particles = 30;
        steps = 200;
        omega = 0.5;
        alpha = 0.3;
        beta = 0.3;
        function_number = 1;
    }

    /* konstruktor z parametrami */
    public Configuration(int function_number, int steps, int particles, double omega, double alpha, double beta) {

        Configuration.function_number = function_number;
        Configuration.steps = steps;
        Configuration.particles = particles;
        Configuration.omega = omega;
        Configuration.alpha = alpha;
        Configuration.beta = beta;
    }

    /* Metody dostepowe */
    public static double GetOmega() {
        return omega;
    }

    
    public static double GetAlpha() {
        return beta;
    }

    
    public static double GetBeta() {
        return alpha;
    }

    
    public static int GetSteps() {
        return steps;
    }

    
    public static int GetParticles() {
        return particles;
    }

    
    public static int GetFunctionNumber() {
        return function_number;
    }

    
    public static void SetOmega(double om) {
        omega = om;
    }

    
    public static void SetAlpha(double fi_cz) {
        beta = fi_cz;
    }

    
    public static void SetBeta(double fi_r) {
        alpha = fi_r;
    }

    
    public static void SetSteps(int kr) {
        steps = kr;
    }

    
    public static void SetParticles(int cz) {
        particles = cz;
    }

    
    public static void SetFunctionNumber(int nrf) {
        function_number = nrf;
    }
    
    /* Funkcja wczytujaca konfiguracje z pliku */
    public static void LoadConfig(String FileName) {
        try {
            FileReader konfig = new FileReader(FileName);
            BufferedReader br = new BufferedReader(konfig);
            String tekst;

            tekst = br.readLine();
            function_number = Integer.parseInt(tekst);
            tekst = br.readLine();
            particles = Integer.parseInt(tekst);
            tekst = br.readLine();
            steps = Integer.parseInt(tekst);
            tekst = br.readLine();
            omega = Double.parseDouble(tekst);
            tekst = br.readLine();
            alpha = Double.parseDouble(tekst);
            tekst = br.readLine();
            beta = Double.parseDouble(tekst);
        } catch (IOException e) {
        }

    }

    /* funkcja zapisujaca konfiguracje do pliku */
    public static void SaveConfig(String fileName) {
        try {
            PrintWriter out = new PrintWriter(fileName);
            out.println(function_number+1);
            out.println(particles);
            out.println(steps);
            out.println(omega);
            out.println(alpha);
            out.println(beta);
            out.println();
            out.println();
            out.println("1.Numer funkcji");
            out.println("2.Liczba cząstek");
            out.println("3.Liczba kroków");
            out.println("4.Omega");
            out.println("5. Alpha");
            out.println("6. Beta");
            out.close();
        } catch (IOException ex) {
        }
    }
}
