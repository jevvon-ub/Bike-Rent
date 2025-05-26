import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

// Main GUI application class
public class App extends JFrame {
    private RentalManager rentalManager;
    private JPanel contentPane;
    private CardLayout cardLayout;
    private JPanel cardsPanel;

    // Color constants
    private static final Color PRIMARY_COLOR = new Color(70, 130, 180); // Steel blue
    private static final Color SECONDARY_COLOR = new Color(240, 248, 255); // Alice blue
    private static final Color PRI = new Color(255, 69, 0); // Orange-red

    // Constructor
    public App() {
        setTitle("Bike Rental System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Initialize the rental manager
        rentalManager = new RentalManager();

        // Add some sample bikes
        rentalManager.addBike(new MountainBike("Mountain Explorer", 10000));
        rentalManager.addBike(new FoldingBike("City Folder", 8000));
        rentalManager.addBike(new ElectricBike("Electric Cruiser", 15000));

        // Setup the layout
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);

        // Create sidebar menu
        JPanel sidebarPanel = createSidebarPanel();
        contentPane.add(sidebarPanel, BorderLayout.WEST);

        // Create main content area with card layout
        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);
        contentPane.add(cardsPanel, BorderLayout.CENTER);

        // Add cards for different sections
        cardsPanel.add(createWelcomePanel(), "Welcome");
        cardsPanel.add(createAddBikePanel(), "AddBike");
        cardsPanel.add(createRentBikePanel(), "RentBike");
        cardsPanel.add(createListBikesPanel(), "ListBikes");
        cardsPanel.add(createRentalHistoryPanel(), "RentalHistory");
        cardsPanel.add(createReturnBikePanel(), "ReturnBike"); // Add this line

        // Show welcome panel by default
        cardLayout.show(cardsPanel, "Welcome");
    }

    // Create sidebar navigation panel
    private JPanel createSidebarPanel() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(PRIMARY_COLOR);
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        sidebar.setPreferredSize(new Dimension(200, getHeight()));

        // App title
        JLabel titleLabel = new JLabel("Bike Rental");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(titleLabel);

        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        // Navigation buttons
        String[] buttonLabels = { "Home", "Add Bike", "Rent Bike", "Available Bikes", "Rental History", "Return Bike" };
        String[] cardNames = { "Welcome", "AddBike", "RentBike", "ListBikes", "RentalHistory", "ReturnBike" };

        for (int i = 0; i < buttonLabels.length; i++) {
            JButton button = createMenuButton(buttonLabels[i], cardNames[i]);
            sidebar.add(button);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        return sidebar;
    }

    // Create styled menu button
    private JButton createMenuButton(String text, String cardName) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(160, 40));
        button.setFocusPainted(false);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(PRI);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }
        });

        // Add click listener
        button.addActionListener(e -> cardLayout.show(cardsPanel, cardName));

        return button;
    }

    private JPanel createReturnBikePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(SECONDARY_COLOR);

        JLabel titleLabel = new JLabel("Return a Rented Bike");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(SECONDARY_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel selectBikeLabel = new JLabel("Select Rented Bike:");
        selectBikeLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        DefaultComboBoxModel<String> rentedBikeComboModel = new DefaultComboBoxModel<>();
        JComboBox<String> rentedBikeComboBox = new JComboBox<>(rentedBikeComboModel);
        rentedBikeComboBox.setFont(new Font("Arial", Font.PLAIN, 16));

        // Populate the combo box with currently rented bikes
        updateRentedBikeComboBox(rentedBikeComboModel);

        JButton returnButton = new JButton("Return Bike");
        returnButton.setFont(new Font("Arial", Font.BOLD, 16));
        returnButton.setBackground(PRIMARY_COLOR);
        returnButton.setForeground(Color.WHITE);
        returnButton.setFocusPainted(false);

        JLabel statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        statusLabel.setHorizontalAlignment(JLabel.CENTER);

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (rentedBikeComboBox.getSelectedIndex() == -1) {
                    statusLabel.setText("Please select a bike to return.");
                    statusLabel.setForeground(Color.RED);
                    return;
                }

                String selectedItem = (String) rentedBikeComboBox.getSelectedItem();
                String bikeId = selectedItem.split(" - ")[0];

                boolean success = rentalManager.returnBike(bikeId);

                if (success) {
                    statusLabel.setText("Bike returned successfully!");
                    statusLabel.setForeground(new Color(0, 128, 0)); // Dark green
                    updateRentedBikeComboBox(rentedBikeComboModel); // Refresh the list

                    // Update the list of bikes panel
                    cardsPanel.remove(3); // Remove current ListBikes panel
                    cardsPanel.add(createListBikesPanel(), "ListBikes", 3);

                    // Update rental history panel (optional, as return doesn't change history
                    // directly)
                    cardsPanel.remove(4);
                    cardsPanel.add(createRentalHistoryPanel(), "RentalHistory", 4);
                } else {
                    statusLabel.setText("Failed to return the bike. It might already be available.");
                    statusLabel.setForeground(Color.RED);
                }
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        content.add(selectBikeLabel, gbc);

        gbc.gridx = 1;
        content.add(rentedBikeComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        content.add(returnButton, gbc);

        gbc.gridy = 2;
        content.add(statusLabel, gbc);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);

        // Add a listener to refresh the combo box when this panel is shown
        panel.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                updateRentedBikeComboBox(rentedBikeComboModel);
                statusLabel.setText("");
            }
        });

        return panel;
    }

    // Helper method to update the rented bike combo box
    private void updateRentedBikeComboBox(DefaultComboBoxModel<String> model) {
        model.removeAllElements();
        for (Bike bike : rentalManager.getAllBikes()) { // Iterate all bikes
            if (!bike.isAvailable()) { // Add only rented bikes
                model.addElement(bike.getId() + " - " + bike.getName() + " (" + bike.getType() + ")");
            }
        }
    }

    // Create welcome panel
    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(SECONDARY_COLOR);

        JLabel welcomeLabel = new JLabel("Welcome to Bike Rental System");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomeLabel.setForeground(PRIMARY_COLOR);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(SECONDARY_COLOR);

        JLabel infoLabel = new JLabel(
                "<html><div >This application allows you to manage a bike rental business.<br>You can add new bikes, rent them to customers, and view rental history.</div></html>");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        infoLabel.setHorizontalAlignment(JLabel.CENTER);
        infoLabel.setMinimumSize(new Dimension(400, 100));

        centerPanel.add(infoLabel);

        panel.add(welcomeLabel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }

    // Create add bike panel
    private JPanel createAddBikePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(SECONDARY_COLOR);

        JLabel titleLabel = new JLabel("Add New Bike");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(SECONDARY_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Bike type selection
        JLabel typeLabel = new JLabel("Bike Type:");
        typeLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        String[] bikeTypes = { "Mountain", "Folding", "Electric" };
        JComboBox<String> typeComboBox = new JComboBox<>(bikeTypes);
        typeComboBox.setFont(new Font("Arial", Font.PLAIN, 16));

        // Bike name field
        JLabel nameLabel = new JLabel("Bike Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JTextField nameField = new JTextField(20);
        nameField.setFont(new Font("Arial", Font.PLAIN, 16));

        // Bike price field
        JLabel priceLabel = new JLabel("Price per Hour (Rp):");
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JTextField priceField = new JTextField(20);
        priceField.setFont(new Font("Arial", Font.PLAIN, 16));

        // Add button
        JButton addButton = new JButton("Add Bike");
        addButton.setFont(new Font("Arial", Font.BOLD, 16));
        addButton.setBackground(Color.BLUE);
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);

        // Status label
        JLabel statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        statusLabel.setHorizontalAlignment(JLabel.CENTER);

        // Add action listener for the add button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String bikeType = (String) typeComboBox.getSelectedItem();
                    String bikeName = nameField.getText();
                    double bikePrice = Double.parseDouble(priceField.getText());

                    if (bikeName.trim().isEmpty()) {
                        statusLabel.setText("Please enter a bike name.");
                        statusLabel.setForeground(Color.RED);
                        return;
                    }

                    Bike newBike = null;

                    switch (bikeType) {
                        case "Mountain":
                            newBike = new MountainBike(bikeName, bikePrice);
                            break;
                        case "Folding":
                            newBike = new FoldingBike(bikeName, bikePrice);
                            break;
                        case "Electric":
                            newBike = new ElectricBike(bikeName, bikePrice);
                            break;
                    }

                    if (newBike != null) {
                        rentalManager.addBike(newBike);
                        statusLabel.setText("Bike added successfully!");
                        statusLabel.setForeground(new Color(0, 128, 0)); // Dark green

                        // Clear fields
                        nameField.setText("");
                        priceField.setText("");

                        // Update the list of bikes panel
                        cardsPanel.remove(3); // Remove current ListBikes panel
                        cardsPanel.add(createListBikesPanel(), "ListBikes", 3);
                    }

                } catch (NumberFormatException ex) {
                    statusLabel.setText("Please enter a valid price.");
                    statusLabel.setForeground(Color.RED);
                }
            }
        });

        // Add components to the form panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(typeLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(typeComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(priceLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(priceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        formPanel.add(addButton, gbc);

        gbc.gridy = 4;
        formPanel.add(statusLabel, gbc);

        // Create a container panel to center the form
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(SECONDARY_COLOR);
        containerPanel.add(formPanel, BorderLayout.CENTER);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(containerPanel, BorderLayout.CENTER);

        return panel;
    }

    // Create rent bike panel
    private JPanel createRentBikePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(SECONDARY_COLOR);

        JLabel titleLabel = new JLabel("Rent a Bike");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(SECONDARY_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // User details section
        JLabel userSectionLabel = new JLabel("Customer Information");
        userSectionLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JTextField nameField = new JTextField(20);
        nameField.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JTextField phoneField = new JTextField(20);
        phoneField.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel durationLabel = new JLabel("Rental Duration (hours):");
        durationLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JTextField durationField = new JTextField(20);
        durationField.setFont(new Font("Arial", Font.PLAIN, 16));

        // Bike selection section
        JLabel bikeSectionLabel = new JLabel("Select a Bike");
        bikeSectionLabel.setFont(new Font("Arial", Font.BOLD, 18));

        // Create bike selection combo box
        DefaultComboBoxModel<String> bikeComboModel = new DefaultComboBoxModel<>();
        JComboBox<String> bikeComboBox = new JComboBox<>(bikeComboModel);
        bikeComboBox.setFont(new Font("Arial", Font.PLAIN, 16));

        // Get available bikes and add to combo box
        List<Bike> availableBikes = rentalManager.getAvailableBikes();
        for (Bike bike : availableBikes) {
            bikeComboModel.addElement(bike.getId() + " - " + bike.getName() + " (" + bike.getType() + ") - Rp "
                    + bike.getPricePerHour() + "/hr");
        }

        // Rent button
        JButton rentButton = new JButton("Rent Now");
        rentButton.setFont(new Font("Arial", Font.BOLD, 16));
        rentButton.setBackground(Color.BLUE);
        rentButton.setForeground(Color.WHITE);
        rentButton.setFocusPainted(false);

        // Status label
        JLabel statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        statusLabel.setHorizontalAlignment(JLabel.CENTER);

        // Add action listener for the rent button
        rentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String userName = nameField.getText();
                    String userPhone = phoneField.getText();
                    int duration = Integer.parseInt(durationField.getText());

                    if (userName.trim().isEmpty()) {
                        statusLabel.setText("Please enter customer name.");
                        statusLabel.setForeground(Color.RED);
                        return;
                    }

                    if (userPhone.trim().isEmpty()) {
                        statusLabel.setText("Please enter phone number.");
                        statusLabel.setForeground(Color.RED);
                        return;
                    }

                    if (duration <= 0) {
                        statusLabel.setText("Duration must be greater than 0.");
                        statusLabel.setForeground(Color.RED);
                        return;
                    }

                    if (bikeComboBox.getSelectedIndex() == -1) {
                        statusLabel.setText("Please select a bike.");
                        statusLabel.setForeground(Color.RED);
                        return;
                    }

                    String selectedItem = (String) bikeComboBox.getSelectedItem();
                    String bikeId = selectedItem.split(" - ")[0];

                    User user = new User(userName, userPhone);

                    boolean success = rentalManager.rentBike(user, bikeId, duration);

                    if (success) {
                        statusLabel.setText("Bike rented successfully!");
                        statusLabel.setForeground(new Color(0, 128, 0)); // Dark green

                        // Clear fields
                        nameField.setText("");
                        phoneField.setText("");
                        durationField.setText("");

                        // Update combo box
                        bikeComboModel.removeAllElements();
                        List<Bike> updatedBikes = rentalManager.getAvailableBikes();
                        for (Bike bike : updatedBikes) {
                            bikeComboModel.addElement(bike.getId() + " - " + bike.getName() + " (" + bike.getType()
                                    + ") - Rp " + bike.getPricePerHour() + "/hr");
                        }

                        // Update the list of bikes panel
                        cardsPanel.remove(3); // Remove current ListBikes panel
                        cardsPanel.add(createListBikesPanel(), "ListBikes", 3);

                        // Update rental history panel
                        cardsPanel.remove(4); // Remove current RentalHistory panel
                        cardsPanel.add(createRentalHistoryPanel(), "RentalHistory", 4);
                    } else {
                        statusLabel.setText("Failed to rent the bike.");
                        statusLabel.setForeground(Color.RED);
                    }

                } catch (NumberFormatException ex) {
                    statusLabel.setText("Please enter a valid duration.");
                    statusLabel.setForeground(Color.RED);
                }
            }
        });

        // Add components to the form panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(userSectionLabel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(phoneLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(durationLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(durationField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        formPanel.add(bikeSectionLabel, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(10, 10, 10, 10);
        formPanel.add(bikeComboBox, gbc);

        gbc.gridy = 6;
        gbc.insets = new Insets(20, 10, 10, 10);
        formPanel.add(rentButton, gbc);

        gbc.gridy = 7;
        formPanel.add(statusLabel, gbc);

        // Create a container panel with scroll pane
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(SECONDARY_COLOR);

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(SECONDARY_COLOR);

        containerPanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(containerPanel, BorderLayout.CENTER);

        return panel;
    }

    // Create list bikes panel
    private JPanel createListBikesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(SECONDARY_COLOR);

        JLabel titleLabel = new JLabel("Available Bikes");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Create table with bike data
        String[] columnNames = { "ID", "Type", "Name", "Price per Hour (Rp )", "Status" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        // Fill table with data
        List<Bike> bikes = rentalManager.getAllBikes();
        for (Bike bike : bikes) {
            Object[] row = {
                    bike.getId(),
                    bike.getType(),
                    bike.getName(),
                    bike.getPricePerHour(),
                    bike.isAvailable() ? "Available" : "Rented"
            };
            model.addRow(row);
        }

        // Create scroll pane for table
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // Create rental history panel
    private JPanel createRentalHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(SECONDARY_COLOR);

        JLabel titleLabel = new JLabel("Rental History");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Create table with rental history data
        String[] columnNames = { "Customer", "Phone", "Bike", "Duration (hrs)", "Total Cost (Rp )" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        // Fill table with data
        List<Rental> rentalHistory = rentalManager.getRentalHistory();
        for (Rental rental : rentalHistory) {
            Object[] row = {
                    rental.getUser().getName(),
                    rental.getUser().getPhoneNumber(),
                    rental.getBike().getName() + " (" + rental.getBike().getType() + ")",
                    rental.getDuration(),
                    rental.getTotalCost()
            };
            model.addRow(row);
        }

        // Create scroll pane for table
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // Main method
    public static void main(String[] args) {
        // try {
        // // Set system look and feel
        // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        // } catch (Exception e) {
        // e.printStackTrace();
        // }

        // Launch the application
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                App frame = new App();
                frame.setVisible(true);
            }
        });
    }
}