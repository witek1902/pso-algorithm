package optipso;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;

public class GUI extends JFrame implements Runnable {

    /* invokeAndWait po jej wywołaniu działanie bieżącego wątku zostanie wstrzymane 
     * do chwili wykonania przekazanego kodu przez wątek obsługi zdarzeń. */
    public GUI() {
        try {
            EventQueue.invokeAndWait(this);
        } catch (InterruptedException | InvocationTargetException e) {
        }
    }
    
    /* inicjalizujemy komponenty, dodajemy do GUI panel odpowiadajacy za rysowanie funkcji */
    @Override
    public void run() {

        initComponents();
        DrawPanel.setLayout(new BorderLayout());
        VisualisationPanel.add(DrawPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    /* aktualizacja statusu */
    private String msg() {
 
        DecimalFormat fmt = new DecimalFormat("0.0000");
        return "step: " + this.DrawPanel.getStep()
                + ", best: f(" + fmt.format(this.DrawPanel.getBestX())
                + "," + fmt.format(this.DrawPanel.getBestY())
                + ") = " + fmt.format(this.DrawPanel.getBest());
    }

    /** klasa odpowiedzialna za przerysowywanie czastek w czasie, ktory ustawiamy dzieki obiektowi Timer */
    void PanelUpdateSwarm(int kroki, int opoznienie, final double alpha, final double beta) {

        if (counter >= 0) {
            timer.stop();
            counter = -1;
            return;
        }
        if (opoznienie <= 0) {
            while (kroki >= 0) {
                this.DrawPanel.updateSwarm(alpha, beta); /* aktualizujemy pozycje czastek */
            }
            this.DrawPanel.repaint();     /* przerysowujemy okno */
            opoznienie--;
            return;
        }
        counter = kroki;
        timer = new Timer(opoznienie, new ActionListener () {
      @Override
      public void actionPerformed (ActionEvent e) {
         if (--counter < 0) {
                timer.stop();
                return;
            }
            DrawPanel.updateSwarm(alpha, beta);  /* aktualizuj pozycje czastek */
            DrawPanel.repaint();  /* przerysuj okno z nowymi pozycjami */
            status.setText(msg()); /* ustaw status */
      }});
            this.timer.start();         /* wlaczamy timer */
    }

    /* inicjalizacja wszystkich komponentow GUI i ustawianie ich */
    private void initComponents() {

        DrawPanel = new PSOPanel();
        VisualisationPanel = new javax.swing.JPanel();
        ConfigurationPanel = new javax.swing.JPanel();
        StepsLabel = new javax.swing.JLabel();
        ParticlesLabel = new javax.swing.JLabel();
        FunctionLabel = new javax.swing.JLabel();
        OmegaLabel = new javax.swing.JLabel();
        AlphaLabel = new javax.swing.JLabel();
        BetaLabel = new javax.swing.JLabel();

        FunctionComboBox = new javax.swing.JComboBox<String>();
        StepsSpinner = new javax.swing.JSpinner();
        ParticlesSpinner = new javax.swing.JSpinner();
        OmegaSpinner = new javax.swing.JSpinner();
        AlphaSpinner = new javax.swing.JSpinner();
        BetaSpinner = new javax.swing.JSpinner();

        VisualisationLabel = new javax.swing.JLabel();
        ConfigurationLabel = new javax.swing.JLabel();
        QuitButton = new javax.swing.JButton();
        LoadButton = new javax.swing.JButton();
        SaveButton = new javax.swing.JButton();
        AboutLabel = new javax.swing.JLabel();
        status = new javax.swing.JLabel();
        StartButton = new javax.swing.JButton();


        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Wizualizacja algorytmu PSO v1.1");
        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(750, 530));
        setName("WizualizacjaPSO");

        VisualisationPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
        VisualisationPanel.setLayout(new java.awt.BorderLayout());

        ConfigurationPanel.setBackground(new java.awt.Color(255, 255, 255));
        ConfigurationPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));

        StepsLabel.setFont(new java.awt.Font("Calibri", 1, 14));
        StepsLabel.setText("Ilość kroków:");

        ParticlesLabel.setFont(new java.awt.Font("Calibri", 1, 14));
        ParticlesLabel.setText("Ilość cząstek:");

        FunctionLabel.setFont(new java.awt.Font("Calibri", 1, 14));
        FunctionLabel.setText("Funkcja:");

        OmegaLabel.setFont(new java.awt.Font("Calibri", 1, 14));
        OmegaLabel.setText("Omega:");

        AlphaLabel.setFont(new java.awt.Font("Calibri", 1, 14));
        AlphaLabel.setText("Alpha:");

        BetaLabel.setFont(new java.awt.Font("Calibri", 1, 14));
        BetaLabel.setText("Beta:");

        FunctionComboBox.setFont(new java.awt.Font("Tahoma", 0, 14));
        FunctionComboBox.setModel(new javax.swing.DefaultComboBoxModel<String>(new String[]{"Parabola", "Okregi 1", "Okregi 2", "Jajka 1", "Jajka 2"}));
        FunctionComboBox.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FunkcjaComboBoxActionPerformed(evt);
            }
        });

        StepsSpinner.setFont(new java.awt.Font("Tahoma", 0, 14));
        StepsSpinner.setModel(new javax.swing.SpinnerNumberModel(200, 10, 10000, 10));
        StepsSpinner.setToolTipText("Ustaw ilość kroków iteracyjnych");

        ParticlesSpinner.setFont(new java.awt.Font("Tahoma", 0, 14));
        ParticlesSpinner.setModel(new javax.swing.SpinnerNumberModel(30, 5, 500, 5));
        ParticlesSpinner.setToolTipText("Ustaw ilość cząstek biorących udział w optymalizacji");

        OmegaSpinner.setFont(new java.awt.Font("Tahoma", 0, 14));
        OmegaSpinner.setModel(new javax.swing.SpinnerNumberModel(0.5d, 0.1d, 3.0d, 0.05d));
        OmegaSpinner.setToolTipText("Ustaw parametr odpowiadający za wygaszanie przyspieszenia");

        AlphaSpinner.setFont(new java.awt.Font("Tahoma", 0, 14));
        AlphaSpinner.setModel(new javax.swing.SpinnerNumberModel(0.3d, 0.05d, 3.0d, 0.05d));
        AlphaSpinner.setToolTipText("Ustaw parametr odpowiedzialny za indywidualność cząstek");

        BetaSpinner.setFont(new java.awt.Font("Tahoma", 0, 14));
        BetaSpinner.setModel(new javax.swing.SpinnerNumberModel(0.3d, 0.05d, 3.0d, 0.05d));
        BetaSpinner.setToolTipText("Ustaw parametr odpowiedzialny za stadność roju");

        javax.swing.GroupLayout KonfiguracjaPanelLayout = new javax.swing.GroupLayout(ConfigurationPanel);
        ConfigurationPanel.setLayout(KonfiguracjaPanelLayout);
        KonfiguracjaPanelLayout.setHorizontalGroup(
                KonfiguracjaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(KonfiguracjaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(KonfiguracjaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(FunctionComboBox, 0, 216, Short.MAX_VALUE)
                .addComponent(FunctionLabel)
                .addGroup(KonfiguracjaPanelLayout.createSequentialGroup()
                .addGroup(KonfiguracjaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(StepsLabel)
                .addComponent(AlphaLabel)
                .addComponent(OmegaLabel)
                .addComponent(ParticlesLabel)
                .addComponent(BetaLabel))
                .addGap(20, 20, 20)
                .addGroup(KonfiguracjaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(BetaSpinner, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                .addComponent(ParticlesSpinner, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                .addComponent(StepsSpinner, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                .addComponent(OmegaSpinner, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                .addComponent(AlphaSpinner, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))))
                .addContainerGap()));
        KonfiguracjaPanelLayout.setVerticalGroup(
                KonfiguracjaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(KonfiguracjaPanelLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(FunctionLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(FunctionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addGroup(KonfiguracjaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(StepsLabel)
                .addComponent(StepsSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(KonfiguracjaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(ParticlesSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(ParticlesLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(KonfiguracjaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(OmegaSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(OmegaLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(KonfiguracjaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(AlphaSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(AlphaLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(KonfiguracjaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(BetaSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(BetaLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        VisualisationLabel.setFont(new java.awt.Font("Calibri", 3, 18));
        VisualisationLabel.setText("Wizualizacja");

        ConfigurationLabel.setFont(new java.awt.Font("Calibri", 3, 18));
        ConfigurationLabel.setText("Konfiguracja");

        QuitButton.setText("ZAKOŃCZ");
        QuitButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ZakonczButtonActionPerformed(evt);
            }
        });

        LoadButton.setText("WCZYTAJ");
        LoadButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                WczytajButtonActionPerformed(evt);
            }
        });

        SaveButton.setText("ZAPISZ");
        SaveButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ZapiszButtonActionPerformed(evt);
            }
        });

        AboutLabel.setFont(new java.awt.Font("Tahoma", 1, 20));
        AboutLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        AboutLabel.setText("?");
        AboutLabel.setToolTipText("O programie");
        AboutLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OtworzOknoAbout(evt);
            }
        });

        status.setFont(new java.awt.Font("Tahoma", 0, 10));
        status.setText("Status...");

        StartButton.setText("START");
        StartButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StartButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addComponent(VisualisationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
                .addGap(18, 18, 18))
                .addGroup(layout.createSequentialGroup()
                .addComponent(VisualisationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(404, 404, 404)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(ConfigurationLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(AboutLabel))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(ConfigurationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(status, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(SaveButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(LoadButton))
                .addGroup(layout.createSequentialGroup()
                .addComponent(StartButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(QuitButton)))))))
                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(VisualisationLabel)
                .addComponent(ConfigurationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                .addComponent(AboutLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addComponent(ConfigurationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(LoadButton)
                .addComponent(SaveButton))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(StartButton))
                .addGroup(layout.createSequentialGroup()
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(QuitButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(status))
                .addComponent(VisualisationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE))
                .addContainerGap()));

        pack();
    }

    /* metoda tworzaca okienko JDialog, w ktorym sa informacje dotyczace programu */
    private JDialog createAbout() {

        final JDialog OknoDialogowe = new JDialog(this, "O programie...", true);
        JButton ZamknijButton = new JButton("Zamknij");
        JPanel PanelAbout = new JPanel(new BorderLayout());
        PanelAbout.setBackground(new java.awt.Color(218, 218, 218));
        JTextArea text = new JTextArea("Wizualizacja algorytmu PSO\n"
                + "Wersja 1.0, 14.05.2013\n\n"
                + "Robert Witkowski\n"
                + "Politechnika Warszawska\n"
                + "Wydział Elektryczny, Informatyka\n\n"
                + "mail: witek1902@gmail.com\n");
        text.setEditable(false);
        text.setBackground(new java.awt.Color(218, 218, 218));
        ZamknijButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OknoDialogowe.setVisible(false);
                status.setText("Zamknięto okienko O programie...");
            }
        });
        PanelAbout.add(ZamknijButton, BorderLayout.SOUTH);
        PanelAbout.add(text,BorderLayout.NORTH);
        PanelAbout.setVisible(true);
        OknoDialogowe.add(PanelAbout);
        OknoDialogowe.setLocationRelativeTo(this);
        OknoDialogowe.pack();
        OknoDialogowe.setResizable(false);
        return OknoDialogowe;
    }

    /* otwarcie okienka, zmiana statusu */
    private void OtworzOknoAbout(java.awt.event.MouseEvent evt) {

        status.setText("Otwarto okienko O programie...");
        this.about = this.createAbout();
        this.about.setVisible(true);

    }

    /* rysujemy funkcje w panelu */
    private void FunkcjaComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
    
        int function_number = FunctionComboBox.getSelectedIndex();
        
                if (function_number == 1) {
                    DrawPanel.setFunctionID(1);
                } else if (function_number == 2) {
                    DrawPanel.setFunctionID(2);
                } else if (function_number == 3) {
                    DrawPanel.setFunctionID(3);
                } else if (function_number == 4) {
                    DrawPanel.setFunctionID(4);
                } else { 
                DrawPanel.setFunctionID(0);}
    
    }

    private void WczytajButtonActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(savePath);
        if (fc.showDialog(LoadButton, "Wczytaj") == JFileChooser.APPROVE_OPTION) {
            try {
                /* WCZYTUJEMY WARTOSCI DO KONKRETNYCH ZMIENNYCH */
                Configuration.LoadConfig(fc.getSelectedFile().getPath());

                /* DPOWIEDNIO USTAWIAMY SPINNERY/COMBOBOXY */
                int function_number = Configuration.GetFunctionNumber();
                if (function_number == 1) {
                    FunctionComboBox.setSelectedIndex(0);
                } else if (function_number == 2) {
                    FunctionComboBox.setSelectedIndex(1);
                } else if (function_number == 3) {
                    FunctionComboBox.setSelectedIndex(2);
                } else if (function_number == 4) {
                    FunctionComboBox.setSelectedIndex(3);
                }
                StepsSpinner.setValue(Configuration.GetSteps());
                ParticlesSpinner.setValue(Configuration.GetParticles());
                OmegaSpinner.setValue(Configuration.GetOmega());
                AlphaSpinner.setValue(Configuration.GetAlpha());
                BetaSpinner.setValue(Configuration.GetBeta());

            } catch (Exception ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            /* USTAWIAMY STATUS I ZAPAMIETUJEMY OSTATNIO WYBRANA SCIEZKE */
            status.setText("Wczytano plik: " + fc.getSelectedFile().getPath());
            notSaved = false;
            savePath = new File(fc.getSelectedFile().getParent());
        }
    }

    private void ZapiszButtonActionPerformed(java.awt.event.ActionEvent evt) {

        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(savePath);
        if (fc.showDialog(SaveButton, "Zapisz") == JFileChooser.APPROVE_OPTION) {
            try {
                /* USTAWIAMY POSZCZGOLNE WARTOSCI,KTORE SA AKTUALNIE NA SPINNERACH DO KONKRETNYCH ZMIENNYCH */
                Configuration.SetFunctionNumber((int) FunctionComboBox.getSelectedIndex());
                Configuration.SetSteps((int) StepsSpinner.getValue());
                Configuration.SetParticles((int) ParticlesSpinner.getValue());
                Configuration.SetOmega((double) OmegaSpinner.getValue());
                Configuration.SetAlpha((double) AlphaSpinner.getValue());
                Configuration.SetBeta((double) BetaSpinner.getValue());
                /* ZAPISUJEMY JE DO PLIKU */
                Configuration.SaveConfig(fc.getSelectedFile().getPath());

            } catch (Exception ex) {
            }
            /* USTAWIAMY STATUS I ZAPAMIETUJEMY OSTATNIO SCIEZKE */
            status.setText("Plik zapisano w: " + fc.getSelectedFile().getPath());
            notSaved = false;
            savePath = new File(fc.getSelectedFile().getParent());
        }
    }

    /* ODBLOKOWYWANIE/BLOKOWANIE SPINNEROW */
    private void setEnabledSpinner(boolean blok) {
        StepsSpinner.setEnabled(blok);
        ParticlesSpinner.setEnabled(blok);
        BetaSpinner.setEnabled(blok);
        OmegaSpinner.setEnabled(blok);
        AlphaSpinner.setEnabled(blok);
        FunctionComboBox.setEnabled(blok);
        LoadButton.setEnabled(blok);
        SaveButton.setEnabled(blok);
    }

    /* odblokowujemy spinner i zatrzymujemy rysowanie */
   private void ZakonczButtonActionPerformed(java.awt.event.ActionEvent evt) {

        setEnabledSpinner(true);
        DrawPanel.setFunctionID(Configuration.GetFunctionNumber());
    }
    
   /* zczytujemy wartosci ze spinnerow do zmiennych, blokujemy je, rysujemy funkcje, roj i wykonujemy algorytm */
    private void StartButtonActionPerformed(java.awt.event.ActionEvent evt) {

        Configuration.SetFunctionNumber((int) FunctionComboBox.getSelectedIndex());
        Configuration.SetSteps((int) StepsSpinner.getValue());
        Configuration.SetParticles((int) ParticlesSpinner.getValue());
        Configuration.SetOmega((double) OmegaSpinner.getValue());
        Configuration.SetAlpha((double) AlphaSpinner.getValue());
        Configuration.SetBeta((double) BetaSpinner.getValue());
        setEnabledSpinner(false);
        
        DrawPanel.setFunctionID(Configuration.GetFunctionNumber());
        DrawPanel.createSwarm(Configuration.GetParticles());
        PanelUpdateSwarm(Configuration.GetSteps(), 150, Configuration.GetAlpha(), Configuration.GetBeta());
        
    }

    public static void main(String args[]) {
    new GUI();
    }
    
    private javax.swing.JLabel AboutLabel;
    private javax.swing.JLabel AlphaLabel;
    private javax.swing.JSpinner AlphaSpinner;
    private javax.swing.JLabel BetaLabel;
    private javax.swing.JSpinner BetaSpinner;
    private javax.swing.JComboBox<String> FunctionComboBox;
    private javax.swing.JLabel FunctionLabel;
    private javax.swing.JLabel ParticlesLabel;
    private javax.swing.JSpinner ParticlesSpinner;
    private javax.swing.JLabel StepsLabel;
    private javax.swing.JSpinner StepsSpinner;
    private javax.swing.JLabel ConfigurationLabel;
    private javax.swing.JPanel ConfigurationPanel;
    private javax.swing.JLabel OmegaLabel;
    private javax.swing.JSpinner OmegaSpinner;
    private javax.swing.JButton StartButton;
    private javax.swing.JButton LoadButton;
    private javax.swing.JLabel VisualisationLabel;
    private javax.swing.JPanel VisualisationPanel;
    private javax.swing.JButton QuitButton;
    private javax.swing.JButton SaveButton;
    public static javax.swing.JLabel status;
    private PSOPanel DrawPanel; /* panel rysujacy wykresy funkcji i roj */
    private File savePath = null; /* zmienna odpowiedzialna za zapamietywanie ostatniej lokalizacji */
    private boolean notSaved = false; /* pomocnicza zmienna przy zapamietywaniu ostatniej lokalizacji */
    private javax.swing.JDialog about; /* JDialog odpowiedzialny za wyswietlanie okienka about */
    Timer timer = null;  /* Zegar do aktualizacji */
    int counter = -1;    /* licznik krokow, zainicjalizowany -1 na potrzeby konfiguracji Timera*/

}
