import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

//Appliance Control System Dashboard - main application that manages all home appliances and handles system updates

public class Dashboard extends JFrame {
    //appliance components
    private Light light;
    private Fan fan;
    private AirConditioner airConditioner;

    //ui components
    private JLabel statusBar;
    private JLabel timeLabel;

    //system management
    private Timer systemTimer;
    private Timer clockTimer;
    private boolean systemUpdateMode = false;

    //constructor
    public Dashboard() {
        initializeComponents();
        setupUI();
        startSystemServices();
        System.out.println("Appliance Control System intialized successfully");
    }

    //initialize al appliance components
    private void initializeComponents() {
        light = new Light();
        fan = new Fan();
        airConditioner = new AirConditioner();
    }

    //setup main ui
    private void setupUI(){
        setTitle("Appliance Control System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //main panels
        setupHeaderPanel();
        setupAppliancePanel();
        setupFooterPanel();

        //window properties
        setResizable(false);
        pack();
        setLocationRelativeTo(null); //center

        //minimum size
        setMinimumSize(new Dimension(800, 300));
    }

    //setup header panel with title and time
    private void setupHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        headerPanel.setBackground(Color.decode("#C6D2FF"));

        //title
        JLabel titleLabel = new JLabel("Appliance Control System Dashboard", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        //current time
        timeLabel = new JLabel("", SwingConstants.RIGHT);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        timeLabel.setForeground(Color.WHITE);

        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(timeLabel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);
    }

    //setup main appliance control panel
    private void setupAppliancePanel() {
        JPanel appliancePanel = new JPanel(new GridLayout(1, 3, 20, 20));
        appliancePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        appliancePanel.setBackground(Color.WHITE);

        //add appliance components
        appliancePanel.add(light);
        appliancePanel.add(fan);
        appliancePanel.add(airConditioner);

        add(appliancePanel, BorderLayout.CENTER);
    }

    //setup footer status bar
    private void setupFooterPanel() {
        statusBar = new JLabel("System Status: ONLINE | All devices operational");
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusBar.setBackground(Color.LIGHT_GRAY);
        statusBar.setOpaque(true);
        statusBar.setFont(new Font("Arial", Font.PLAIN, 11));
        
        add(statusBar, BorderLayout.SOUTH);
    }

    //start system services including timers and update scheduler
    private void startSystemServices() {
        //start clock timer for time display
        startClockTimer();
        
        //start system update scheduler
        startSystemUpdateScheduler();
        
        System.out.println("System services started successfully");
    }

    //start clock timer for local time display
        private void startClockTimer() {
        clockTimer = new Timer(true);
        clockTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    LocalDateTime now = LocalDateTime.now();
                    String timeString = now.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm:ss"));
                    timeLabel.setText(timeString);
                });
            }
        }, 0, 1000); //update every second
    }

    //start system update scheduler for January 1st updates
    private void startSystemUpdateScheduler() {
        systemTimer = new Timer(true);
        systemTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkForSystemUpdate();
            }
        }, 0, 1000); //check every second
    }

    //check for update on January 1st, at 1:00am
    private void checkForSystemUpdate() {
        LocalDateTime now = LocalDateTime.now();
        if (now.getMonthValue() == 1 &&
            now.getDayOfMonth() == 1 &&
            now.getHour() == 1 && 
            now.getMinute() == 0 &&
            now.getSecond() == 0 &&
            !systemUpdateMode) {
            SwingUtilities.invokeLater(() -> {
                performSystemUpdate();
            });
        }
    }

    //perform the update
    private void performSystemUpdate() {
        systemUpdateMode = true;
        System.out.println("--- ANNUAL SYSTEM UPDATE INITIATED ---");
        
        //turn off all devices
        light.turnOff();
        fan.turnOff();
        airConditioner.turnOff();
        
        System.out.println("All devices turned OFF for system update");
        
        //update status bar
        statusBar.setText("System Status: UPDATING | Annual maintenance in progress...");
        statusBar.setBackground(Color.decode("#FFDF20"));
        
        //simulate update process
        Timer updateTimer = new Timer();
        updateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    completeSystemUpdate();
                });
            }
        }, 2000); //simulate 2 second update process
    }

    //complete system update
    private void completeSystemUpdate() {
        System.out.println("System update completed successfully");
        System.out.println("All devices remain OFF");
        System.out.println("--- SYSTEM UPDATE COMPLETE ---");
        
        // Set status bar to green for 10 seconds
        statusBar.setText("System Status: UPDATE COMPLETE | All devices turned OFF");
        statusBar.setBackground(Color.decode("#00A63E"));
        
        systemUpdateMode = false;
        
        //show completion dialog
        JOptionPane.showMessageDialog(this,
            "Annual system update completed successfully!\n" +
            "All devices have been turned OFF.",
            "System Update Complete",
            JOptionPane.INFORMATION_MESSAGE);
        
        //set timer to return status bar to normal 
        Timer resetTimer = new Timer();
        resetTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    statusBar.setText("System Status: ONLINE | All devices operational");
                    statusBar.setBackground(Color.LIGHT_GRAY);
                });
            }
        }, 5000);//5 second delay
    }

    //clean up resources when application closes
    private void cleanup() {
        if (clockTimer != null) {
            clockTimer.cancel();
        }
        if (systemTimer != null) {
            systemTimer.cancel();
        }
        System.out.println("System shutdown complete");
    }

    //main method
    public static void main(String[] args) {
        //set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Failed to set system look and feel: " + e.getMessage());
        }
        
        //create and show the dashboard
        SwingUtilities.invokeLater(() -> {
            Dashboard dashboard = new Dashboard();
            
            //add shutdown hook for cleanup
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                dashboard.cleanup();
            }));
            
            dashboard.setVisible(true);
        });
    }
}

