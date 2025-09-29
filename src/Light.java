import javax.swing. *;
import java.awt. *;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//Light component - controls a light with on/off toggle functionality

public class Light extends JPanel {
    private boolean isOn;
    private JButton toggleButton;
    private JLabel statusLabel;
    private JPanel indicatorPanel;

    //constructor
    public Light() {
        this.isOn = false;
        initializeUI();
        updateDisplay();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Light",
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
        indicatorPanel.setPreferredSize(new Dimension(50, 50));
        indicatorPanel.setBorder(BorderFactory.createEtchedBorder());

        //status label
        statusLabel = new JLabel("OFF", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));

        topPanel.add(indicatorPanel);
        topPanel.add(statusLabel);

        //bottom panel
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(Color.WHITE);

        //toggle button
        toggleButton = new JButton("Turn On");
        toggleButton.setFont(new Font("Arial", Font.PLAIN, 12));
        toggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggle();
            }
        });

        bottomPanel.add(toggleButton);

        //layout panels
        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    //toggle light on/off
    public void toggle() {
        isOn = !isOn;
        updateDisplay();
        System.out.println("Light " + (isOn ? "turned ON" : "turned OFF"));
    }

    //turn light off for system updates
    public void turnOff() {
        if (isOn) {
            isOn = false;
            updateDisplay();
            System.out.println("Light turned OFF (System Update)");
        }
    }

    //getter for state of light
    public boolean isOn() {
        return isOn;
    }

    //getter for status as a string for logging/debugging
    public String getStatus() {
        return "Light: "+(isOn ? "ON" : "OFF");
    }

    //update display based on current state
    private void updateDisplay() {
        if (isOn) {
            statusLabel.setText("ON");
            statusLabel.setForeground(Color.decode("#00A63E"));
            indicatorPanel.setBackground(Color.decode("#FFDF20"));
            toggleButton.setText("Turn Off");            
        }
        else {
            statusLabel.setText("OFF");
            statusLabel.setForeground(Color.decode("#E7000B"));
            indicatorPanel.setBackground(Color.decode("#EEF2FF"));
            toggleButton.setText("Turn On");            
        }
        repaint();
    }
}