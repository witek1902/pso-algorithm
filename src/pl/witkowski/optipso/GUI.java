package pl.witkowski.optipso;

import pl.witkowski.optipso.config.Configuration;
import pl.witkowski.optipso.config.ConfigurationManager;
import pl.witkowski.optipso.function.FunctionType;
import pl.witkowski.optipso.panel.PSOPanel;

import java.awt.*;
import java.io.*;
import java.util.EnumSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.stream.Collectors;

import static pl.witkowski.optipso.function.FunctionType.valueOf;

public class GUI extends JFrame implements Runnable {

    private static final String DEFAULT_LABEL_FONT = "Calibri";
    private static final int DEFAULT_LABEL_FONT_SIZE = 14;
    private static final int DEFAULT_LABEL_FONT_STYLE = Font.BOLD;

    private static final String DEFAULT_INPUT_FONT = "Tahoma";
    private static final int DEFAULT_INPUT_FONT_STYLE = Font.PLAIN;

    private PSOPanel drawPanel;
    private ConfigurationManager configurationManager;
    private File lastPath;

    private Timer timer;
    private int counter = -1;

    private JLabel aboutLabel = new JLabel();
    private JLabel alphaLabel = new JLabel();
    private JSpinner alphaSpinner = new JSpinner();
    private JLabel betaLabel = new JLabel();
    private JSpinner betaSpinner = new JSpinner();
    private JComboBox<String> functionComboBox = new JComboBox<>();
    private JLabel functionLabel = new JLabel();
    private JLabel particlesLabel = new JLabel();
    private JSpinner particlesSpinner = new JSpinner();
    private JLabel stepsLabel = new JLabel();
    private JSpinner stepsSpinner = new JSpinner();
    private JLabel configurationLabel = new JLabel();
    private JPanel configurationPanel = new JPanel();
    private JLabel omegaLabel = new JLabel();
    private JSpinner omegaSpinner = new JSpinner();
    private JButton startButton= new JButton();
    private JButton loadButton= new JButton();
    private JLabel visualisationLabel = new JLabel();
    private JPanel visualisationPanel = new JPanel();
    private JButton quitButton = new JButton();
    private JButton saveButton= new JButton();
    private static JLabel status = new JLabel();

    public static void main(String args[]) {
        new GUI();
    }

    private GUI() {
        try {
            EventQueue.invokeAndWait(this);
        } catch (InterruptedException | InvocationTargetException e) {
        }
    }

    @Override
    public void run() {
        initComponents();
        drawPanel.setLayout(new BorderLayout());
        visualisationPanel.add(drawPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private String updateStatusMessage() {
        DecimalFormat fmt = new DecimalFormat("0.0000");
        return "Step: " + drawPanel.getStep()
                + ", best: f(" + fmt.format(drawPanel.getBestPositionX())
                + "," + fmt.format(drawPanel.getBestPositionY())
                + ") = " + fmt.format(drawPanel.getBest());
    }

    private void updatePanelSwarm(int steps, int delay, final double alpha, final double beta) {
        if (counter >= 0) {
            timer.stop();
            counter = -1;
            return;
        }

        if (delay <= 0) {
            while (steps >= 0) {
                drawPanel.updateSwarm(alpha, beta);
            }
            drawPanel.repaint();
            delay--;
            return;
        }

        counter = steps;
        timer = new Timer(delay, e -> {
            if (--counter < 0) {
                timer.stop();
                return;
            }
            drawPanel.updateSwarm(alpha, beta);
            drawPanel.repaint();
            status.setText(updateStatusMessage());
        });
        timer.start();
    }

    private void initComponents() {
        drawPanel = new PSOPanel();
        configurationManager = new ConfigurationManager();

        initBaseWindow();
        initVisualisationPanel();
        initConfigurationPanel();

        initStepsLabel();
        initParticlesLabel();
        initFunctionLabel();
        initOmegaLabel();
        initAlphaLabel();
        initBetaLabel();

        initFunctionComboBox();
        initStepsSpinner();
        initParticlesSpinner();
        initOmegaSpinner();
        initAlphaSpinner();
        initBetaSpinner();

        initConfigLayout();

        initVisualisationLabel();
        initConfigurationLabel();

        initStopButton();
        initLoadButton();
        initSaveButton();
        initStartButton();

        initStatusMessage();
        initAboutLabel();

        initMainLayout();

        pack();
    }

    private void initMainLayout() {
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(visualisationPanel, GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
                                                .addGap(18, 18, 18))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(visualisationLabel, GroupLayout.PREFERRED_SIZE, 122, GroupLayout.PREFERRED_SIZE)
                                                .addGap(404, 404, 404)))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(configurationLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(aboutLabel))
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addComponent(configurationPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addGap(10, 10, 10)
                                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                .addComponent(status, GroupLayout.PREFERRED_SIZE, 230, GroupLayout.PREFERRED_SIZE)
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                                .addComponent(saveButton)
                                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                .addComponent(loadButton))
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(startButton)
                                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                .addComponent(quitButton)))))))
                                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(2, 2, 2)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(visualisationLabel)
                                                        .addComponent(configurationLabel, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED))
                                        .addComponent(aboutLabel))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(configurationPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(loadButton)
                                                        .addComponent(saveButton))
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(startButton))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(quitButton)))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(status))
                                        .addComponent(visualisationPanel, GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE))
                                .addContainerGap()));
    }

    private void initConfigLayout() {
        GroupLayout configPanelLayout = new GroupLayout(configurationPanel);
        configurationPanel.setLayout(configPanelLayout);
        configPanelLayout.setHorizontalGroup(
                configPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(configPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(configPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(functionComboBox, 0, 216, Short.MAX_VALUE)
                                        .addComponent(functionLabel)
                                        .addGroup(configPanelLayout.createSequentialGroup()
                                                .addGroup(configPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(stepsLabel)
                                                        .addComponent(alphaLabel)
                                                        .addComponent(omegaLabel)
                                                        .addComponent(particlesLabel)
                                                        .addComponent(betaLabel))
                                                .addGap(20, 20, 20)
                                                .addGroup(configPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                        .addComponent(betaSpinner, GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                                                        .addComponent(particlesSpinner, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                                                        .addComponent(stepsSpinner, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                                                        .addComponent(omegaSpinner, GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                                                        .addComponent(alphaSpinner, GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))))
                                .addContainerGap()));
        configPanelLayout.setVerticalGroup(
                configPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(configPanelLayout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(functionLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(functionComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(25, 25, 25)
                                .addGroup(configPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(stepsLabel)
                                        .addComponent(stepsSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(configPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(particlesSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(particlesLabel))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(configPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(omegaSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(omegaLabel))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(configPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(alphaSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(alphaLabel))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(configPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(betaSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(betaLabel))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    }

    private void initAboutLabel() {
        aboutLabel.setFont(new Font(DEFAULT_INPUT_FONT, Font.BOLD, 20));
        aboutLabel.setHorizontalAlignment(SwingConstants.CENTER);
        aboutLabel.setText("?");
    }

    private void initStartButton() {
        startButton.setText("START");
        startButton.addActionListener(this::startButtonActionPerformed);
    }

    private void initStatusMessage() {
        status.setFont(new Font(DEFAULT_INPUT_FONT, Font.PLAIN, 10));
        status.setText("Status...");
    }

    private void initSaveButton() {
        saveButton.setText("SAVE");
        saveButton.addActionListener(this::saveButtonActionPerformed);
    }

    private void initLoadButton() {
        loadButton.setText("LOAD");
        loadButton.addActionListener(this::loadButtonActionPerformed);
    }

    private void initStopButton() {
        quitButton.setText("STOP");
        quitButton.addActionListener(this::stop);
    }

    private void initConfigurationLabel() {
        configurationLabel.setFont(new Font(DEFAULT_LABEL_FONT, DEFAULT_LABEL_FONT_STYLE, 18));
        configurationLabel.setText("Configuration");
    }

    private void initVisualisationLabel() {
        visualisationLabel.setFont(new Font(DEFAULT_LABEL_FONT, DEFAULT_LABEL_FONT_STYLE, 18));
        visualisationLabel.setText("Visualisation");
    }

    private void initConfigurationPanel() {
        configurationPanel.setBackground(new Color(255, 255, 255));
        configurationPanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(0, 0, 0)));
    }

    private void initVisualisationPanel() {
        visualisationPanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(0, 0, 0)));
        visualisationPanel.setLayout(new BorderLayout());
    }

    private void initBaseWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("OptiPSO 1.0");
        setBackground(new Color(255, 255, 255));
        setMinimumSize(new Dimension(750, 530));
        setName("OptiPSO");
    }

    private void initBetaSpinner() {
        betaSpinner.setFont(new Font(DEFAULT_INPUT_FONT, DEFAULT_INPUT_FONT_STYLE, DEFAULT_LABEL_FONT_SIZE));
        betaSpinner.setModel(new SpinnerNumberModel(0.3d, 0.05d, 3.0d, 0.05d));
    }

    private void initAlphaSpinner() {
        alphaSpinner.setFont(new Font(DEFAULT_INPUT_FONT, DEFAULT_INPUT_FONT_STYLE, DEFAULT_LABEL_FONT_SIZE));
        alphaSpinner.setModel(new SpinnerNumberModel(0.3d, 0.05d, 3.0d, 0.05d));
    }

    private void initOmegaSpinner() {
        omegaSpinner.setFont(new Font(DEFAULT_INPUT_FONT, DEFAULT_INPUT_FONT_STYLE, DEFAULT_LABEL_FONT_SIZE));
        omegaSpinner.setModel(new SpinnerNumberModel(0.5d, 0.1d, 3.0d, 0.05d));
    }

    private void initParticlesSpinner() {
        particlesSpinner.setFont(new Font(DEFAULT_INPUT_FONT, DEFAULT_INPUT_FONT_STYLE, DEFAULT_LABEL_FONT_SIZE));
        particlesSpinner.setModel(new SpinnerNumberModel(30, 5, 500, 5));
    }

    private void initStepsSpinner() {
        stepsSpinner.setFont(new Font(DEFAULT_INPUT_FONT, DEFAULT_INPUT_FONT_STYLE, DEFAULT_LABEL_FONT_SIZE));
        stepsSpinner.setModel(new SpinnerNumberModel(200, 10, 10000, 10));
    }

    private void initFunctionComboBox() {
        functionComboBox.setFont(new Font(DEFAULT_INPUT_FONT, DEFAULT_INPUT_FONT_STYLE, DEFAULT_LABEL_FONT_SIZE));
        functionComboBox.setModel(new DefaultComboBoxModel<>(EnumSet.allOf(FunctionType.class).stream()
                .map(FunctionType::toString)
                .collect(Collectors.toList())
                .toArray(new String[0])));
        functionComboBox.addActionListener(this::functionComboBoxActionPerformed);
    }

    private void initBetaLabel() {
        betaLabel.setFont(new Font(DEFAULT_LABEL_FONT, DEFAULT_LABEL_FONT_STYLE, DEFAULT_LABEL_FONT_SIZE));
        betaLabel.setText("Beta: ");
    }

    private void initAlphaLabel() {
        alphaLabel.setFont(new Font(DEFAULT_LABEL_FONT, DEFAULT_LABEL_FONT_STYLE, DEFAULT_LABEL_FONT_SIZE));
        alphaLabel.setText("Alpha: ");
    }

    private void initOmegaLabel() {
        omegaLabel.setFont(new Font(DEFAULT_LABEL_FONT, DEFAULT_LABEL_FONT_STYLE, DEFAULT_LABEL_FONT_SIZE));
        omegaLabel.setText("Omega: ");
    }

    private void initFunctionLabel() {
        functionLabel.setFont(new Font(DEFAULT_LABEL_FONT, DEFAULT_LABEL_FONT_STYLE, DEFAULT_LABEL_FONT_SIZE));
        functionLabel.setText("Function: ");
    }

    private void initParticlesLabel() {
        particlesLabel.setFont(new Font(DEFAULT_LABEL_FONT, DEFAULT_LABEL_FONT_STYLE, DEFAULT_LABEL_FONT_SIZE));
        particlesLabel.setText("Number of particles: ");
    }

    private void initStepsLabel() {
        stepsLabel.setFont(new Font(DEFAULT_LABEL_FONT, DEFAULT_LABEL_FONT_STYLE, DEFAULT_LABEL_FONT_SIZE));
        stepsLabel.setText("Number of steps: ");
    }

    private void functionComboBoxActionPerformed(ActionEvent evt) {
        drawPanel.setFunction(valueOf((String) functionComboBox.getSelectedItem()));
    }

    private void loadButtonActionPerformed(ActionEvent evt) {
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(lastPath);
        if (fc.showDialog(loadButton, "LOAD") == JFileChooser.APPROVE_OPTION) {
            try {
                configurationManager.loadFromFile(fc.getSelectedFile().getPath());
                functionComboBox.setSelectedIndex(configurationManager.getConfig().getFunctionType().ordinal());
                stepsSpinner.setValue(configurationManager.getConfig().getSteps());
                particlesSpinner.setValue(configurationManager.getConfig().getParticles());
                omegaSpinner.setValue(configurationManager.getConfig().getOmega());
                alphaSpinner.setValue(configurationManager.getConfig().getAlpha());
                betaSpinner.setValue(configurationManager.getConfig().getBeta());
            } catch (Exception ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.WARNING, "Load config exception", ex);
            }
            status.setText("Loaded file: " + fc.getSelectedFile().getPath());
            lastPath = new File(fc.getSelectedFile().getParent());
        }
    }

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(lastPath);
        if (fc.showDialog(saveButton, "SAVE") == JFileChooser.APPROVE_OPTION) {
            try {
                configurationManager.getConfig().setFunctionType(valueOf(String.valueOf(functionComboBox.getSelectedItem())));
                configurationManager.getConfig().setSteps((int) stepsSpinner.getValue());
                configurationManager.getConfig().setParticles((int) particlesSpinner.getValue());
                configurationManager.getConfig().setOmega((double) omegaSpinner.getValue());
                configurationManager.getConfig().setAlpha((double) alphaSpinner.getValue());
                configurationManager.getConfig().setBeta((double) betaSpinner.getValue());
                configurationManager.saveInFile(fc.getSelectedFile().getPath());
            } catch (Exception ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.WARNING, "Save config exception", ex);
            }
            status.setText("File saved in: " + fc.getSelectedFile().getPath());
            lastPath = new File(fc.getSelectedFile().getParent());
        }
    }

    private void setEnabledSpinner(boolean block) {
        stepsSpinner.setEnabled(block);
        particlesSpinner.setEnabled(block);
        betaSpinner.setEnabled(block);
        omegaSpinner.setEnabled(block);
        alphaSpinner.setEnabled(block);
        functionComboBox.setEnabled(block);
        loadButton.setEnabled(block);
        saveButton.setEnabled(block);
    }

    private void stop(ActionEvent evt) {
        setEnabledSpinner(true);
        drawPanel.setFunction(configurationManager.getConfig().getFunctionType());
    }

    private void startButtonActionPerformed(ActionEvent evt) {
        Configuration config = configurationManager.getConfig();

        config.setFunctionType(valueOf(String.valueOf(functionComboBox.getSelectedItem())));
        config.setSteps((int) stepsSpinner.getValue());
        config.setParticles((int) particlesSpinner.getValue());
        config.setOmega((double) omegaSpinner.getValue());
        config.setAlpha((double) alphaSpinner.getValue());
        config.setBeta((double) betaSpinner.getValue());
        setEnabledSpinner(false);

        drawPanel.setFunction(config.getFunctionType());
        drawPanel.createSwarm(config.getParticles());
        updatePanelSwarm(config.getSteps(), 150, config.getAlpha(), config.getBeta());
    }
}
