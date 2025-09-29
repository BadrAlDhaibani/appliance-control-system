import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

//Fan component - controls a fan with speed settings: 0(off), 1(low), 2(high)

public class Fan extends JPanel {
    private int speed;
    private JSlider speedSlider;
    private JLabel statusLabel;
    private JLabel speedLabel;
    private JPanel indicatorPanel;
    private JButton turnOffButton;

    private static final String[] SPEED_NAMES = {"OFF", "LOW", "HIGH"};
    private static final Color[] SPEED_COLORS = {Color.decode("#EEF2FF"), Color.decode("#BEDBFF"), Color.decode("#51A2FF")};

    //constructor
    public Fan() {
        this.speed = 0;
        initializeUI();
        updateDisplay();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Fan",
            0,
            0,
            new Font("Arial", Font.BOLD, 14)
        ));
        setBackground(Color.WHITE);

        //top panel
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setBackground(Color.WHITE);

        //indicator panel
        indicatorPanel = new JPanel();
        indicatorPanel.setBackground(Color.WHITE);
        indicatorPanel.setPreferredSize(new Dimension(40, 40));
        indicatorPanel.setBorder(BorderFactory.createEtchedBorder());

        //status label
        statusLabel = new JLabel("OFF", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));

        //speed label
        speedLabel = new JLabel("Speed: 0", SwingConstants.CENTER);
        speedLabel.setFont(new Font("Arial", Font.PLAIN, 10));

        //layout the labels
        JPanel labelPanel = new JPanel(new GridLayout(2, 1));
        labelPanel.setBackground(Color.WHITE);
        labelPanel.add(statusLabel);
        labelPanel.add(speedLabel);

        topPanel.add(indicatorPanel);
        topPanel.add(labelPanel);

        //center panel
        JPanel centerPanel = new JPanel(new FlowLayout());
        centerPanel.setBackground(Color.WHITE);

        //speed slider
        speedSlider = new JSlider(0, 2, 0);
        speedSlider.setBackground(Color.WHITE);
        speedSlider.setMajorTickSpacing(1);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        speedSlider.setSnapToTicks(true);

        //custom labels for slider
        speedSlider.setLabelTable(speedSlider.createStandardLabels(1));
        speedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e){
                if (!speedSlider.getValueIsAdjusting()) {
                    setSpeed(speedSlider.getValue());
                }
            }
        });

        centerPanel.add(new JLabel("Speed"));
        centerPanel.add(speedSlider);

        //bottom panel
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(Color.WHITE);

        //turn off button
        turnOffButton = new JButton("Turn Off");
        turnOffButton.setFont(new Font("Arial", Font.PLAIN, 12));
        turnOffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setSpeed(0);
            }
        });

        bottomPanel.add(turnOffButton);

        //layout panels
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    //set the fan speed
    public void setSpeed(int newSpeed) {
        if (newSpeed >= 0 && newSpeed <= 2) {
            this.speed = newSpeed;
            speedSlider.setValue(newSpeed);
            updateDisplay();
            System.out.println("Fan speed set to: " + SPEED_NAMES[speed] + " (" + speed + ")");
        }
    }

    //turn off fan for system updates
    public void turnOff() {
        if (speed != 0) {
            setSpeed(0);
            System.out.println("Fan turned OFF (system update)");
        }
    }

    //getter for current speed of the fan
    public int getSpeed() {
        return speed;
    }

    //getter status as a string for logging/debugging
    public String getStatus() {
        return "Fan: " + SPEED_NAMES[speed] + " (Speed " + speed + ")";
    }

    //check if fan is currently running
    public boolean isOn() {
        return speed > 0;
    }

    //update display based on current speed
    private void updateDisplay() {
        //update labels
        statusLabel.setText(SPEED_NAMES[speed]);
        speedLabel.setText("Speed: " + speed);
        //update colors
        statusLabel.setForeground(speed == 0 ? Color.decode("#E7000B") : Color.decode("#00A63E"));
        indicatorPanel.setBackground(SPEED_COLORS[speed]);

        repaint();
    }
}
