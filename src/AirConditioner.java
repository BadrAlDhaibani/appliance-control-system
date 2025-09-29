import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.Timer;
import java.util.TimerTask;

//Air Conditioner component - controls an AC unit with on/off and automatic temperature control

public class AirConditioner extends JPanel {
    private boolean isOn;
    private int targetTemperature;
    private int currentTemperature;

    //ui components
    private JSpinner temperatureSpinner;
    private JLabel statusLabel;
    private JLabel currentTempLabel;
    private JLabel targetTempLabel;
    private JButton powerButton;

    //temperature adjustment timer
    private Timer temperatureTimer;

    private static final int MIN_TEMP = 16;
    private static final int MAX_TEMP = 30;
    private static final int DEFAULT_TEMP = 21;

    //constructor
    public AirConditioner() {
        this.isOn = false;
        this.targetTemperature = DEFAULT_TEMP;
        this.currentTemperature = DEFAULT_TEMP;
        initializeUI();
        updateDisplay();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Air Conditioner Control",
            0,
            0,
            new Font("Arial", Font.BOLD, 14)
        ));
        setBackground(Color.WHITE);

        //top panel
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setBackground(Color.WHITE);

        //status label
        statusLabel = new JLabel("OFF", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));

        topPanel.add(statusLabel);

        //center panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);

        //layout setup
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        //target label
        gbc.gridx = 0; gbc.gridy = 0;

        centerPanel.add(new JLabel("Target:"), gbc);

        //target panel
        gbc.gridx = 1; gbc.gridy = 0;
        temperatureSpinner = new JSpinner(new SpinnerNumberModel(DEFAULT_TEMP, MIN_TEMP, MAX_TEMP, 1));
        temperatureSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int newTemp = (Integer) temperatureSpinner.getValue();
                setTargetTemperature(newTemp);
            }
        });
        JPanel tempPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tempPanel.setBackground(Color.WHITE);
        tempPanel.add(temperatureSpinner);
        tempPanel.add(new JLabel("°C"));

        centerPanel.add(tempPanel, gbc);

        //current label
        gbc.gridx = 0; gbc.gridy = 1;
        centerPanel.add(new JLabel("Current:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        currentTempLabel = new JLabel(currentTemperature + "°C");
        currentTempLabel.setFont(new Font("Arial", Font.BOLD, 12));

        centerPanel.add(currentTempLabel, gbc);

        //set to label
        gbc.gridx = 0; gbc.gridy = 2;
        centerPanel.add(new JLabel("Set to:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        targetTempLabel = new JLabel(targetTemperature + "°C");
        targetTempLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        centerPanel.add(targetTempLabel, gbc);

        //bottom panel
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(Color.WHITE);

        //power button
        powerButton = new JButton("Turn On");
        powerButton.setFont(new Font("Arial", Font.PLAIN, 12));
        powerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                togglePower();
            }
        });

        bottomPanel.add(powerButton);

        //layout panels
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    //set target temperature
    public void setTargetTemperature(int temperature) {
        if (temperature >= MIN_TEMP && temperature <= MAX_TEMP) {
            this.targetTemperature = temperature;
            temperatureSpinner.setValue(temperature);
            updateDisplay();
            System.out.println("AC target temperature set to: " + temperature + "°C");
        }
    }

    //toggle power state
    public void togglePower() {
        isOn = !isOn;
        if (isOn) {
            startTemperatureAdjustment();
            System.out.println("AC turned ON");
        }
        else {
            stopTemperatureAdjustment();
            System.out.println("AC turned OFF");
        }
        updateDisplay();
    }

    //turn off AC for system updates
    public void turnOff() {
        if (isOn) {
            isOn = false;
            stopTemperatureAdjustment();
            updateDisplay();
            System.out.println("AC turned OFF (system update)");
        }
    }

    //start the automatic temperature adjustment timer
    private void startTemperatureAdjustment() {
        if (temperatureTimer != null) {
            temperatureTimer.cancel();
        }
        temperatureTimer = new Timer(true);
        temperatureTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    adjustTemperature();
                });
            }
        }, 2000, 2000); //adjust every 2 seconds
    }

    //stop the automatic temperature adjustment timer
    private void stopTemperatureAdjustment() {
        if (temperatureTimer != null) {
            temperatureTimer.cancel();
            temperatureTimer = null;
        }
    }

    //adjust current temperature toward target by 1°C
    private void adjustTemperature() {
        if (!isOn) return;
        if (currentTemperature < targetTemperature) {
            currentTemperature++;
            System.out.println("AC heating: " + currentTemperature + "°C");
        }
        else if (currentTemperature > targetTemperature) {
            currentTemperature--;
            System.out.println("AC cooling: " + currentTemperature + "°C");
        }
        updateDisplay();
    }

    //check if AC is currently on
    public boolean isOn() {
        return isOn;
    }

    //getter for target temperature
    public int getTargetTemperature() {
        return targetTemperature;
    }

    //getter for current temperature
    public int getCurrentTemperature() {
        return currentTemperature;
    }

    //update display based on current state
    private void updateDisplay() {
        //update status label
        statusLabel.setText(isOn ? "ON" : "OFF");
        statusLabel.setForeground(isOn ? Color.decode("#00A63E") : Color.decode("#E7000B"));
        
        //update temperature labels
        currentTempLabel.setText(currentTemperature + "°C");
        targetTempLabel.setText(targetTemperature + "°C");

        //update power button
        powerButton.setText(isOn ? "Turn Off" : "Turn On");

        //enable/disable temperature control based on power state
        temperatureSpinner.setEnabled(isOn);

        repaint();
    }

    //getter status as a string for logging/debugging
    public String getStatus() {
        return String.format("AC: %s | Current: %d°C | Target: %d°C", isOn ? "AUTO" : "OFF", currentTemperature, targetTemperature);
    }

    //clean up resources when component is destroyed
    public void cleanup() {
        stopTemperatureAdjustment();
    }
}