import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class App extends JFrame {
    private RentalManager rentalManager;
    private JPanel contentPane;
    private CardLayout cardLayout;
    private JPanel cardsPanel;

    private static final Color PRIMARY_COLOR = new Color(70, 130, 180);
    private static final Color SECONDARY_COLOR = new Color(240, 248, 255);
    private static final Color PRI = new Color(255, 69, 0);

    public App() {
        setTitle("Bike Rental System"); // Mengatur judul jendela aplikasi
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Mengatur operasi default saat menutup jendela
        setSize(800, 600); // Mengatur ukuran jendela
        setLocationRelativeTo(null); // Menempatkan jendela di tengah layar

        rentalManager = new RentalManager(); // Inisialisasi manajer penyewaan

        // Menambahkan beberapa contoh sepeda
        rentalManager.addBike(new MountainBike("Mountain Explorer", 10000));
        rentalManager.addBike(new FoldingBike("City Folder", 8000));
        rentalManager.addBike(new ElectricBike("Electric Cruiser", 15000));

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);

        JPanel sidebarPanel = createSidebarPanel(); // Membuat panel sidebar
        contentPane.add(sidebarPanel, BorderLayout.WEST);

        cardLayout = new CardLayout(); // Menggunakan CardLayout untuk beralih antar panel
        cardsPanel = new JPanel(cardLayout);
        contentPane.add(cardsPanel, BorderLayout.CENTER);

        // Menambahkan panel untuk setiap bagian
        cardsPanel.add(createWelcomePanel(), "Welcome");
        cardsPanel.add(createAddBikePanel(), "AddBike");
        cardsPanel.add(createRentBikePanel(), "RentBike");
        cardsPanel.add(createListBikesPanel(), "ListBikes");
        cardsPanel.add(createRentalHistoryPanel(), "RentalHistory");
        cardsPanel.add(createReturnBikePanel(), "ReturnBike");

        cardLayout.show(cardsPanel, "Welcome"); // Menampilkan panel selamat datang secara default
    }

    private JPanel createSidebarPanel() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS)); // Menggunakan BoxLayout untuk tata letak vertikal
        sidebar.setBackground(PRIMARY_COLOR);
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        sidebar.setPreferredSize(new Dimension(200, getHeight()));

        JLabel titleLabel = new JLabel("Bike Rental"); // Label judul aplikasi
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(titleLabel);

        sidebar.add(Box.createRigidArea(new Dimension(0, 30))); // Spasi kaku

        // Label dan nama kartu untuk tombol navigasi
        String[] buttonLabels = { "Home", "Add Bike", "Rent Bike", "Available Bikes", "Rental History", "Return Bike" };
        String[] cardNames = { "Welcome", "AddBike", "RentBike", "ListBikes", "RentalHistory", "ReturnBike" };

        for (int i = 0; i < buttonLabels.length; i++) {
            JButton button = createMenuButton(buttonLabels[i], cardNames[i]); // Membuat tombol menu
            sidebar.add(button);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        return sidebar;
    }

    private JButton createMenuButton(String text, String cardName) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(160, 40));
        button.setFocusPainted(false);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);

        // Menambahkan efek hover pada tombol
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(PRI);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }
        });

        // Menambahkan action listener untuk beralih kartu
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

        JPanel content = new JPanel(new GridBagLayout()); // Menggunakan GridBagLayout untuk konten
        content.setBackground(SECONDARY_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel selectBikeLabel = new JLabel("Select Rented Bike:");
        selectBikeLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        DefaultComboBoxModel<String> rentedBikeComboModel = new DefaultComboBoxModel<>();
        JComboBox<String> rentedBikeComboBox = new JComboBox<>(rentedBikeComboModel);
        rentedBikeComboBox.setFont(new Font("Arial", Font.PLAIN, 16));

        updateRentedBikeComboBox(rentedBikeComboModel); // Memperbarui combo box dengan sepeda yang sedang disewa

        JButton returnButton = new JButton("Return Bike"); // Tombol untuk mengembalikan sepeda
        returnButton.setFont(new Font("Arial", Font.BOLD, 16));
        returnButton.setBackground(PRIMARY_COLOR);
        returnButton.setForeground(Color.WHITE);
        returnButton.setFocusPainted(false);

        JLabel statusLabel = new JLabel(""); // Label status untuk pesan
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        statusLabel.setHorizontalAlignment(JLabel.CENTER);

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (rentedBikeComboBox.getSelectedIndex() == -1) { // Validasi jika tidak ada sepeda yang dipilih
                    statusLabel.setText("Please select a bike to return.");
                    statusLabel.setForeground(Color.RED);
                    return;
                }

                String selectedItem = (String) rentedBikeComboBox.getSelectedItem();
                String bikeId = selectedItem.split(" - ")[0]; // Mengambil ID sepeda dari item yang dipilih

                boolean success = rentalManager.returnBike(bikeId); // Mengembalikan sepeda

                if (success) {
                    statusLabel.setText("Bike returned successfully!");
                    statusLabel.setForeground(new Color(0, 128, 0));
                    updateRentedBikeComboBox(rentedBikeComboModel); // Memperbarui daftar setelah pengembalian

                    // Memperbarui panel daftar sepeda
                    cardsPanel.remove(3);
                    cardsPanel.add(createListBikesPanel(), "ListBikes", 3);

                    // Memperbarui panel riwayat penyewaan
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

        // Menambahkan listener untuk memperbarui combo box saat panel ditampilkan
        panel.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                updateRentedBikeComboBox(rentedBikeComboModel);
                statusLabel.setText("");
            }
        });

        return panel;
    }

    private void updateRentedBikeComboBox(DefaultComboBoxModel<String> model) {
        model.removeAllElements(); // Menghapus semua elemen yang ada
        for (Bike bike : rentalManager.getAllBikes()) {
            if (!bike.isAvailable()) { // Hanya menambahkan sepeda yang sedang disewa
                model.addElement(bike.getId() + " - " + bike.getName() + " (" + bike.getType() + ")");
            }
        }
    }

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

        JLabel typeLabel = new JLabel("Bike Type:");
        typeLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        String[] bikeTypes = { "Mountain", "Folding", "Electric" };
        JComboBox<String> typeComboBox = new JComboBox<>(bikeTypes); // Combo box untuk memilih jenis sepeda
        typeComboBox.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel nameLabel = new JLabel("Bike Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JTextField nameField = new JTextField(20); // Field untuk nama sepeda
        nameField.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel priceLabel = new JLabel("Price per Hour (Rp):");
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JTextField priceField = new JTextField(20); // Field untuk harga per jam
        priceField.setFont(new Font("Arial", Font.PLAIN, 16));

        JButton addButton = new JButton("Add Bike"); // Tombol untuk menambahkan sepeda
        addButton.setFont(new Font("Arial", Font.BOLD, 16));
        addButton.setBackground(Color.BLUE);
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);

        JLabel statusLabel = new JLabel(""); // Label status
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        statusLabel.setHorizontalAlignment(JLabel.CENTER);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String bikeType = (String) typeComboBox.getSelectedItem();
                    String bikeName = nameField.getText();
                    double bikePrice = Double.parseDouble(priceField.getText());

                    if (bikeName.trim().isEmpty()) { // Validasi nama sepeda
                        statusLabel.setText("Please enter a bike name.");
                        statusLabel.setForeground(Color.RED);
                        return;
                    }

                    Bike newBike = null;

                    switch (bikeType) { // Membuat objek sepeda berdasarkan jenis yang dipilih
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
                        rentalManager.addBike(newBike); // Menambahkan sepeda baru
                        statusLabel.setText("Bike added successfully!");
                        statusLabel.setForeground(new Color(0, 128, 0));

                        nameField.setText(""); // Mengosongkan field
                        priceField.setText("");

                        // Memperbarui panel daftar sepeda
                        cardsPanel.remove(3);
                        cardsPanel.add(createListBikesPanel(), "ListBikes", 3);
                    }

                } catch (NumberFormatException ex) { // Menangani kesalahan format angka
                    statusLabel.setText("Please enter a valid price.");
                    statusLabel.setForeground(Color.RED);
                }
            }
        });

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

        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(SECONDARY_COLOR);
        containerPanel.add(formPanel, BorderLayout.CENTER);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(containerPanel, BorderLayout.CENTER);

        return panel;
    }

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

        JLabel userSectionLabel = new JLabel("Customer Information");
        userSectionLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JTextField nameField = new JTextField(20); // Field nama pelanggan
        nameField.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JTextField phoneField = new JTextField(20); // Field nomor telepon pelanggan
        phoneField.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel durationLabel = new JLabel("Rental Duration (hours):");
        durationLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JTextField durationField = new JTextField(20); // Field durasi penyewaan
        durationField.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel bikeSectionLabel = new JLabel("Select a Bike");
        bikeSectionLabel.setFont(new Font("Arial", Font.BOLD, 18));

        DefaultComboBoxModel<String> bikeComboModel = new DefaultComboBoxModel<>();
        JComboBox<String> bikeComboBox = new JComboBox<>(bikeComboModel); // Combo box untuk memilih sepeda yang
                                                                          // tersedia
        bikeComboBox.setFont(new Font("Arial", Font.PLAIN, 16));

        // Mengisi combo box dengan sepeda yang tersedia
        List<Bike> availableBikes = rentalManager.getAvailableBikes();
        for (Bike bike : availableBikes) {
            bikeComboModel.addElement(bike.getId() + " - " + bike.getName() + " (" + bike.getType() + ") - Rp "
                    + bike.getPricePerHour() + "/hr");
        }

        JButton rentButton = new JButton("Rent Now"); // Tombol untuk menyewa sepeda
        rentButton.setFont(new Font("Arial", Font.BOLD, 16));
        rentButton.setBackground(Color.BLUE);
        rentButton.setForeground(Color.WHITE);
        rentButton.setFocusPainted(false);

        JLabel statusLabel = new JLabel(""); // Label status
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        statusLabel.setHorizontalAlignment(JLabel.CENTER);

        rentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String userName = nameField.getText();
                    String userPhone = phoneField.getText();
                    int duration = Integer.parseInt(durationField.getText());

                    if (userName.trim().isEmpty()) { // Validasi nama pelanggan
                        statusLabel.setText("Please enter customer name.");
                        statusLabel.setForeground(Color.RED);
                        return;
                    }

                    if (userPhone.trim().isEmpty()) { // Validasi nomor telepon
                        statusLabel.setText("Please enter phone number.");
                        statusLabel.setForeground(Color.RED);
                        return;
                    }

                    if (duration <= 0) { // Validasi durasi
                        statusLabel.setText("Duration must be greater than 0.");
                        statusLabel.setForeground(Color.RED);
                        return;
                    }

                    if (bikeComboBox.getSelectedIndex() == -1) { // Validasi pemilihan sepeda
                        statusLabel.setText("Please select a bike.");
                        statusLabel.setForeground(Color.RED);
                        return;
                    }

                    String selectedItem = (String) bikeComboBox.getSelectedItem();
                    String bikeId = selectedItem.split(" - ")[0];

                    User user = new User(userName, userPhone); // Membuat objek pengguna

                    boolean success = rentalManager.rentBike(user, bikeId, duration); // Melakukan penyewaan

                    if (success) {
                        statusLabel.setText("Bike rented successfully!");
                        statusLabel.setForeground(new Color(0, 128, 0));

                        nameField.setText(""); // Mengosongkan field
                        phoneField.setText("");
                        durationField.setText("");

                        // Memperbarui combo box sepeda
                        bikeComboModel.removeAllElements();
                        List<Bike> updatedBikes = rentalManager.getAvailableBikes();
                        for (Bike bike : updatedBikes) {
                            bikeComboModel.addElement(bike.getId() + " - " + bike.getName() + " (" + bike.getType()
                                    + ") - Rp " + bike.getPricePerHour() + "/hr");
                        }

                        // Memperbarui panel daftar sepeda
                        cardsPanel.remove(3);
                        cardsPanel.add(createListBikesPanel(), "ListBikes", 3);

                        // Memperbarui panel riwayat penyewaan
                        cardsPanel.remove(4);
                        cardsPanel.add(createRentalHistoryPanel(), "RentalHistory", 4);
                    } else {
                        statusLabel.setText("Failed to rent the bike.");
                        statusLabel.setForeground(Color.RED);
                    }

                } catch (NumberFormatException ex) { // Menangani kesalahan format angka
                    statusLabel.setText("Please enter a valid duration.");
                    statusLabel.setForeground(Color.RED);
                }
            }
        });

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

        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(SECONDARY_COLOR);

        JScrollPane scrollPane = new JScrollPane(formPanel); // Menambahkan scroll pane jika form terlalu panjang
        scrollPane.setBorder(null);
        scrollPane.setBackground(SECONDARY_COLOR);

        containerPanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(containerPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createListBikesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(SECONDARY_COLOR);

        JLabel titleLabel = new JLabel("Available Bikes");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        String[] columnNames = { "ID", "Type", "Name", "Price per Hour (Rp )", "Status" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Membuat tabel tidak dapat diedit
            }
        };

        JTable table = new JTable(model); // Tabel untuk menampilkan data sepeda
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        List<Bike> bikes = rentalManager.getAllBikes(); // Mengambil semua sepeda
        for (Bike bike : bikes) {
            Object[] row = {
                    bike.getId(),
                    bike.getType(),
                    bike.getName(),
                    bike.getPricePerHour(),
                    bike.isAvailable() ? "Available" : "Rented" // Menampilkan status ketersediaan
            };
            model.addRow(row);
        }

        JScrollPane scrollPane = new JScrollPane(table); // Scroll pane untuk tabel
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createRentalHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(SECONDARY_COLOR);

        JLabel titleLabel = new JLabel("Rental History");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        String[] columnNames = { "Customer", "Phone", "Bike", "Duration (hrs)", "Total Cost (Rp )" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Membuat tabel tidak dapat diedit
            }
        };

        JTable table = new JTable(model); // Tabel untuk menampilkan riwayat penyewaan
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        List<Rental> rentalHistory = rentalManager.getRentalHistory(); // Mengambil riwayat penyewaan
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

        JScrollPane scrollPane = new JScrollPane(table); // Scroll pane untuk tabel
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public static void main(String[] args) {
        // Meluncurkan aplikasi di event dispatch thread
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                App frame = new App();
                frame.setVisible(true);
            }
        });
    }
}