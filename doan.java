package doancsdl1;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;



import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class doan extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private String loggedInUsername;
    public String getLoggedInUsername() {
    
        return loggedInUsername;
    }
    public doan() {
        super("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
//        JButton registerButton = new JButton("Register");
//        registerButton.addActionListener(e -> showRegisterDialog());

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // Check login information
            if (isValidLogin(username, password)) {
                // Display the test3Form when login is successful
                setVisible(false);
                new test3Form(this);
            } else {
                JOptionPane.showMessageDialog(doan.this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Panel for login components
        JPanel loginPanel = new JPanel(new GridLayout(2, 2));
        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);

        // Panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(loginButton);
//        buttonPanel.add(registerButton);

        // Main panel containing login and button panels
        JPanel mainPanel = new JPanel(new GridLayout(2, 1));
        mainPanel.add(loginPanel);
        mainPanel.add(buttonPanel);

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }


    private boolean isValidLogin(String username, String password) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fabric", "root", "");
            String query = "SELECT * FROM accounts WHERE username=? AND password_hash=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashPassword(password));

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                loggedInUsername = username; // Lưu trữ tên đăng nhập khi đăng nhập thành công
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder stringBuilder = new StringBuilder();

            for (byte b : hashedBytes) {
                stringBuilder.append(String.format("%02x", b));
            }

            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        try {
            // Set Nimbus Look and Feel
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(doan::new);
    }

    }


class test3Form extends JFrame {
	private JTable orderTable;
    private JTextField supplierTextField;
    private JTextField materialTextField;
    private JPanel searchPanel;
    private String loggedInUsername;

    public test3Form(doan loginFrame) {
        super("fabric AGENCY");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.loggedInUsername = loginFrame.getLoggedInUsername();
        orderTable = new JTable();
        supplierTextField = new JTextField(20);
        materialTextField = new JTextField(20);
        searchPanel = new JPanel();

        JScrollPane scrollPane = new JScrollPane(orderTable);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("Menu");
        menuBar.add(fileMenu);

        JMenuItem materialMenuItem = new JMenuItem("Material Search");
        fileMenu.add(materialMenuItem);

        JMenuItem suppliersMenuItem = new JMenuItem("Suppliers");
        fileMenu.add(suppliersMenuItem);

        JMenuItem listCategoryMenuItem = new JMenuItem("List Category");
        fileMenu.add(listCategoryMenuItem);

        JMenuItem orderReportMenuItem = new JMenuItem("Order Report");
        fileMenu.add(orderReportMenuItem);
        
        JMenuItem logoutMenuItem = new JMenuItem("Logout");
        fileMenu.add(logoutMenuItem);
        
        
        
        logoutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Hiển thị lại form đăng nhập và đóng form test3
                new doan();
                dispose();
            }
        });

        materialMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMaterialList();
            }
        });

        suppliersMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSuppliersMenu();
            }
        });

        listCategoryMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCategoryList();
            }
        });

        orderReportMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showOrderReport();
            }
        });

        setSize(1000, 600);
        setLocationRelativeTo(null);
        
        showMaterialList();
        setVisible(true);
    }

    void showMaterialList() {
        searchPanel.removeAll();
        searchPanel.setLayout(new FlowLayout());

        JLabel materialLabel = new JLabel("Material Name:");
        searchPanel.add(materialLabel);

        searchPanel.add(materialTextField);

        JButton searchButton = new JButton("Search");
        searchPanel.add(searchButton);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadMaterialData(materialTextField.getText().trim());
            }
        });

        searchPanel.setVisible(false);
        getContentPane().add(searchPanel, BorderLayout.NORTH);
        searchPanel.setVisible(true);

        loadMaterialData("");
    }

    private void showSuppliersMenu() {
        searchPanel.removeAll();
        searchPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        // Không yêu cầu nhập Supplier ID
        JLabel parEmployeeIdLabel = new JLabel("Par Employee ID:");
        JComboBox<String> parEmployeeIdComboBox = new JComboBox<>();
        loadPartnerStaffData(parEmployeeIdComboBox); // Load data into the combo box
        
        addComponent(parEmployeeIdLabel, gbc);
        addComponent(parEmployeeIdComboBox, gbc);

        JLabel taxcodeLabel = new JLabel("Taxcode:");
        JTextField taxcodeTextField = new JTextField(20);
        addComponent(taxcodeLabel, gbc);
        addComponent(taxcodeTextField, gbc);

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameTextField = new JTextField(20);
        addComponent(nameLabel, gbc);
        addComponent(nameTextField, gbc);

        JLabel addressLabel = new JLabel("Address:");
        JTextField addressTextField = new JTextField(20);
        addComponent(addressLabel, gbc);
        addComponent(addressTextField, gbc);

        JLabel bankAccountLabel = new JLabel("Bank Account:");
        JTextField bankAccountTextField = new JTextField(20);
        addComponent(bankAccountLabel, gbc);
        addComponent(bankAccountTextField, gbc);

        JLabel phoneNumberLabel = new JLabel("Phone Number:");
        JTextField phoneNumberTextField = new JTextField(20);
        addComponent(phoneNumberLabel, gbc);
        addComponent(phoneNumberTextField, gbc);

        JButton addButton = new JButton("Add Supplier");
        gbc.gridwidth = 2;
        gbc.gridy++;
        addComponent(addButton, gbc);

        JButton deleteButton = new JButton("Delete Supplier");
        gbc.gridy++;
        addComponent(deleteButton, gbc);
        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected row
                int selectedRow = orderTable.getSelectedRow();

                if (selectedRow != -1) {
                    // Get the supplier ID from the selected row
                    String supplierID = orderTable.getValueAt(selectedRow, 0).toString();

                    // Call a method to delete the supplier
                    deleteSupplier(supplierID);

                    // Update the table
                    loadSuppliersData();
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a supplier to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSupplier(
                        parEmployeeIdComboBox.getSelectedItem().toString().trim(),
                        taxcodeTextField.getText().trim(),
                        nameTextField.getText().trim(),
                        addressTextField.getText().trim(),
                        bankAccountTextField.getText().trim(),
                        phoneNumberTextField.getText().trim()
                );
                taxcodeTextField.setText("");
                nameTextField.setText("");
                addressTextField.setText("");
                bankAccountTextField.setText("");
                phoneNumberTextField.setText("");
            }
        });
        JButton addMaterialButton = new JButton("Add Material for Supplier");
        gbc.gridy++;
        addComponent(addMaterialButton, gbc);

        addMaterialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = orderTable.getSelectedRow();

                if (selectedRow != -1) {
                    // Get the supplier ID from the selected row
                    String supplierID = orderTable.getValueAt(selectedRow, 0).toString();

                    // Kiểm tra vai trò của người dùng
                    if (hasPermissionForMaterialAdd(loggedInUsername)) {
                        // Call the method to show the "Add Material" frame
                        showAddMaterialFrame(supplierID);
                    } else {
                        JOptionPane.showMessageDialog(null, "You do not have permission to add material for a supplier.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a supplier to add material for.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
        

        searchPanel.setVisible(false);
        getContentPane().add(searchPanel, BorderLayout.NORTH);
        searchPanel.setVisible(true);
        loadSuppliersData();
    }
 // Phương thức kiểm tra quyền cho việc thêm vật liệu
    private boolean hasPermissionForMaterialAdd(String loggedInUsername) {
    	
        // Thực hiện truy vấn để kiểm tra vai trò của người dùng
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fabric", "root", "");

            // Lấy vai trò của người dùng hiện tại
            String userRole = null;
            
            String getRoleQuery = "SELECT role FROM accounts WHERE username=?";
            try (PreparedStatement getRoleStatement = connection.prepareStatement(getRoleQuery)) {
                getRoleStatement.setString(1, loggedInUsername);

                try (ResultSet roleResultSet = getRoleStatement.executeQuery()) {
                    if (roleResultSet.next()) {
                        userRole = roleResultSet.getString("role");
                    }
                }
            }
            // Kiểm tra xem vai trò có phù hợp không
            if( "OperationStaff".equals(userRole) || "admin".equals(userRole)) {
            return true; }
            return false;
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private void showAddMaterialFrame(String supplierId) {
        JFrame addMaterialFrame = new JFrame("Add Material for Supplier");

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        // Supplier ID label and value
        panel.add(new JLabel("Supplier ID:"), gbc);
        gbc.gridx++;
        panel.add(new JLabel(supplierId), gbc);

        // Name label and text field
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx++;
        JTextField nameField = new JTextField(20);
        panel.add(nameField, gbc);

        // Color label and text field
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Color:"), gbc);
        gbc.gridx++;
        JTextField colorField = new JTextField(20);
        panel.add(colorField, gbc);

        // Add Material button
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2; // Span two columns
        JButton addButton = new JButton("Add Material");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the values from the fields
                String name = nameField.getText().trim();
                String color = colorField.getText().trim();

                // Call a method to add the material to the category table
                addMaterialToCategory(supplierId, name, color);

                // Close the addMaterialFrame
                addMaterialFrame.dispose();
            }
        });
        panel.add(addButton, gbc);

        addMaterialFrame.add(panel);
        addMaterialFrame.setSize(300, 150);
        addMaterialFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addMaterialFrame.setVisible(true);
    }
	
    private void addMaterialToCategory(String supplierId, String name, String color) {
        // Kiểm tra xem có trường nào trống không
        if (supplierId.isEmpty() || name.isEmpty() || color.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return; // Không thực hiện thêm vật liệu vào danh mục nếu có trường nào trống
        }

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fabric", "root", "");

            // Get the latest category_id
            int categoryId = getLatestCategoryId();

            // Prepare the SQL statement to insert a new material into the categories table
            String sql = "INSERT INTO categories (supplier_id, category_id, name, color) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, supplierId);
                statement.setInt(2, categoryId + 1); // Assuming category_id auto-increments
                statement.setString(3, name);
                statement.setString(4, color);

                // Execute the SQL statement
                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    // Material added successfully
                    JOptionPane.showMessageDialog(null, "Material added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Material not added
                    JOptionPane.showMessageDialog(null, "Failed to add material", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            // Close the connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private int getLatestCategoryId() {
        int latestCategoryId = 0;

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fabric", "root", "");

            // Prepare the SQL statement to get the maximum category_id from the category table
            String sql = "SELECT MAX(category_id) AS maxCategoryId FROM categories";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                ResultSet resultSet = statement.executeQuery();

                // Retrieve the maximum category_id
                if (resultSet.next()) {
                    latestCategoryId = resultSet.getInt("maxCategoryId");
                }

                // Close the result set
                resultSet.close();
            }

            // Close the connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return latestCategoryId;
    }


    private void deleteSupplier(String supplierID) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fabric", "root", "");

            // Lấy vai trò của người dùng hiện tại
            String userRole = null;

            // Thực hiện truy vấn để lấy vai trò
            String getRoleQuery = "SELECT role FROM accounts WHERE username=?";
            try (PreparedStatement getRoleStatement = connection.prepareStatement(getRoleQuery)) {
                getRoleStatement.setString(1, loggedInUsername);

                try (ResultSet roleResultSet = getRoleStatement.executeQuery()) {
                    if (roleResultSet.next()) {
                        userRole = roleResultSet.getString("role");
                    }
                }
            }

            // Kiểm tra xem người dùng có vai trò officestaff hoặc operationalstaff không
            if (!"OfficeStaff".equals(userRole) && !"OperationStaff".equals(userRole)) {
                // Check if the supplier is associated with any categories
                if (isSupplierAssociatedWithCategories(connection, supplierID)) {
                    JOptionPane.showMessageDialog(null, "Cannot be deleted because information about materials provided by this supplier still exists.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;  // Exit the method without attempting to delete
                }

                // Use transactions to ensure data consistency
                connection.setAutoCommit(false);

                try {
                    // Tiếp tục quá trình xóa...

                    // Xóa các mục trong bảng 'bolts' trước
                    String sqlDeleteBolts = "DELETE FROM bolts WHERE category_id IN (SELECT category_id FROM categories WHERE supplier_id = ?)";
                    try (PreparedStatement deleteBoltsStatement = connection.prepareStatement(sqlDeleteBolts)) {
                        deleteBoltsStatement.setString(1, supplierID);
                        deleteBoltsStatement.executeUpdate();
                    }

                    // Xóa các mục trong bảng 'categories'
                    String sqlDeleteCategories = "DELETE FROM categories WHERE supplier_id = ?";
                    try (PreparedStatement deleteCategoriesStatement = connection.prepareStatement(sqlDeleteCategories)) {
                        deleteCategoriesStatement.setString(1, supplierID);
                        deleteCategoriesStatement.executeUpdate();
                    }

                    // Xóa các mục trong bảng 'supplierphonenumber'
                    String sqlDeleteSupplierPhoneNumber = "DELETE FROM supplierphonenumber WHERE supplier_id = ?";
                    try (PreparedStatement deletePhoneNumberStatement = connection.prepareStatement(sqlDeleteSupplierPhoneNumber)) {
                        deletePhoneNumberStatement.setString(1, supplierID);
                        deletePhoneNumberStatement.executeUpdate();
                    }

                    // Xóa mục trong bảng 'suppliers'
                    String sqlDeleteSupplier = "DELETE FROM suppliers WHERE supplier_id = ?";
                    try (PreparedStatement deleteSupplierStatement = connection.prepareStatement(sqlDeleteSupplier)) {
                        deleteSupplierStatement.setString(1, supplierID);
                        deleteSupplierStatement.executeUpdate();
                    }

                    // Commit giao dịch
                    connection.commit();

                    // Hiển thị thông báo thành công
                    JOptionPane.showMessageDialog(null, "Supplier deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

                } catch (SQLException ex) {
                    // Rollback giao dịch trong trường hợp có lỗi
                    connection.rollback();
                    ex.printStackTrace();
                } finally {
                    // Thiết lập lại auto-commit thành true
                    connection.setAutoCommit(true);
                    connection.close();
                }

            } else {
                JOptionPane.showMessageDialog(null, "The account is not authorized to perform this function", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Helper method to check if a supplier is associated with any categories
    private boolean isSupplierAssociatedWithCategories(Connection connection, String supplierID) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM categories WHERE supplier_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, supplierID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0;
                }
            }
        }
        return false;
    }




    private void loadSuppliersData() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fabric", "root", "");

            String sql = "SELECT s.supplier_id, s.parEmployeeID, s.taxcode, s.name, s.address, s.bankAccount, GROUP_CONCAT(spn.phonenumber SEPARATOR ', ') AS phone_numbers " +
                    "FROM suppliers s " +
                    "LEFT JOIN supplierphonenumber spn ON s.supplier_id = spn.supplier_id " +
                    "GROUP BY s.supplier_id";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Supplier ID");
            model.addColumn("parEmployeeID");
            model.addColumn("Taxcode");
            model.addColumn("Name");
            model.addColumn("Address");
            model.addColumn("Bank Account");
            model.addColumn("Phone Numbers");

            while (resultSet.next()) {
                model.addRow(new Object[]{
                        resultSet.getString("supplier_id"),
                        resultSet.getString("parEmployeeID"),
                        resultSet.getString("taxcode"),
                        resultSet.getString("name"),
                        resultSet.getString("address"),
                        resultSet.getString("bankAccount"),
                        resultSet.getString("phone_numbers")
                });
            }

            orderTable.setModel(model);

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadSupplierData() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fabric", "root", "");

            String sql = "SELECT name FROM suppliers";
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            supplierComboBox.removeAllItems();

            while (resultSet.next()) {
                supplierComboBox.addItem(resultSet.getString("name"));
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    private void loadPartnerStaffData(JComboBox<String> comboBox) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fabric", "root", "");

            String sql = "SELECT employee_id FROM partnerstaff";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                comboBox.addItem(resultSet.getString("employee_id"));
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private void addComponent(Component component, GridBagConstraints gbc) {
        searchPanel.add(component, gbc);
        gbc.gridy++;
    }
    
    private JComboBox<String> supplierComboBox = new JComboBox<>();

    private void showCategoryList() {
        searchPanel.removeAll();
        searchPanel.setLayout(new FlowLayout());

        JLabel supplierLabel = new JLabel("Supplier Name:");
        searchPanel.add(supplierLabel);

        loadSupplierData(); // Hàm này cần phải được thêm
        searchPanel.add(supplierComboBox);

        JButton searchButton = new JButton("Search");
        searchPanel.add(searchButton);
        
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedSupplier = (String) supplierComboBox.getSelectedItem();
                loadCategoryData(selectedSupplier != null ? selectedSupplier.trim() : "");
            }
        });


        searchPanel.setVisible(false);
        getContentPane().add(searchPanel, BorderLayout.NORTH);
        searchPanel.setVisible(true);

        loadCategoryData("");
    }
    private JComboBox<String> orderComboBox = new JComboBox<>();
    private void showOrderReport() {
    	searchPanel.removeAll();
        searchPanel.setLayout(new FlowLayout());

        JLabel orderLabel = new JLabel("Customer:");
        searchPanel.add(orderLabel);

        loadOrderIDData();
        searchPanel.add(orderComboBox);

        JButton searchButton = new JButton("Search");
        searchPanel.add(searchButton);
        
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedOrderID = (String) orderComboBox.getSelectedItem();
                loadOrderData(selectedOrderID != null ? selectedOrderID.trim() : "");
            }
        });
        
        searchPanel.setVisible(false);
        getContentPane().add(searchPanel, BorderLayout.NORTH);
        searchPanel.setVisible(true);

        loadOrderData("");
    }
    
    
    private void loadOrderIDData() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fabric", "root", "");

            String sql = "SELECT CONCAT(customer_id,'-',first_name,' ',last_name) AS name FROM customers;";
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            orderComboBox.removeAllItems();

            while (resultSet.next()) {
            	orderComboBox.addItem(resultSet.getString("name"));
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void loadMaterialData(String materialName) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fabric", "root", "");

            String sql = "SELECT s.taxcode, s.name AS supplier_name, s.address, s.bankAccount, " +
                    "GROUP_CONCAT(DISTINCT CONCAT(c.name, ' - ', c.color) SEPARATOR ', ') AS categories_and_colors, " +
                    "GROUP_CONCAT(sp.phonenumber SEPARATOR ', ') AS phone_numbers " +
                    "FROM Suppliers s " +
                    "INNER JOIN Categories c ON s.supplier_id = c.supplier_id " +
                    "INNER JOIN SupplierPhoneNumber sp ON s.supplier_id = sp.supplier_id " +
                    "WHERE LOWER(c.name) LIKE LOWER(?) " +
                    "GROUP BY s.taxcode, supplier_name, s.address, s.bankAccount";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + materialName + "%");

            ResultSet resultSet = statement.executeQuery();

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("TaxCode");
            model.addColumn("Supplier Name");
            model.addColumn("Address");
            model.addColumn("PhoneNumber");
            model.addColumn("Bank Account");
            model.addColumn("Categories and Colors");

            while (resultSet.next()) {
                model.addRow(new Object[]{
                        resultSet.getString("taxcode"),
                        resultSet.getString("supplier_name"),
                        resultSet.getString("address"),
                        resultSet.getString("phone_numbers"),
                        resultSet.getString("bankAccount"),
                        resultSet.getString("categories_and_colors")
                });
            }

            orderTable.setModel(model);

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void loadCategoryData(String supplierName) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fabric", "root", "");

            String sql = "SELECT s.name, c.category_id, c.name, c.color " +
                    "FROM suppliers s JOIN categories c ON s.supplier_id = c.supplier_id " +
                    "WHERE LOWER(s.name) LIKE LOWER(?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + supplierName + "%");

            ResultSet resultSet = statement.executeQuery();

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Supplier Name");
            model.addColumn("Category ID");
            model.addColumn("Category Name");
            model.addColumn("Color");

            while (resultSet.next()) {
                model.addRow(new Object[]{
                        resultSet.getString("s.name"),
                        resultSet.getString("c.category_id"),
                        resultSet.getString("c.name"),
                        resultSet.getString("c.color")
                });
            }

            orderTable.setModel(model);

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private JLabel customerInfoLabel;

    private void loadOrderData(String id) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fabric", "root", "");
            String sql = "SELECT o.order_id, o.operational_staffID, CONCAT(o.orderDate, ' ; ', o.orderTime) AS orderDateTime, " +
                    "o.total_price, o.order_status, o.cancelReason, o.processEmployeeID, " +
                    "GROUP_CONCAT(DISTINCT CONCAT(cat.name,' ', b.length)) AS bolt, " +
                    "GROUP_CONCAT(DISTINCT CONCAT(p.date, ' pay ', p.amoutOfMoney) SEPARATOR ', ') AS pay " +
                    "FROM orders o " +
                    "JOIN customers c ON o.customer_id = c.customer_id " +
                    "JOIN bolts b ON o.order_id = b.order_id " +
                    "JOIN categories cat ON b.category_id = cat.category_id " +
                    "JOIN partialpayment p ON p.order_id = b.order_id " +
                    "WHERE o.customer_id = ? " +   
                    "GROUP BY o.order_id " +
                    "ORDER BY cat.name";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id);

            ResultSet resultSet = statement.executeQuery();
            
            if (customerInfoLabel != null) {
                searchPanel.remove(customerInfoLabel);
            }

            String sql1 = "SELECT CONCAT(first_name, ' ', last_name) AS customer_name, address FROM customers WHERE customer_id = ?";
            PreparedStatement statement1 = connection.prepareStatement(sql1);
            statement1.setString(1, id);

            ResultSet resultSet1 = statement1.executeQuery();

            if (resultSet1.next()) {
                String customerName = resultSet1.getString("customer_name");
                String address = resultSet1.getString("address");
                String s = "Customer Name: " + customerName + ", Address: " + address;

                // Thêm JLabel mới và cập nhật biến customerInfoLabel
                customerInfoLabel = new JLabel(s);
                searchPanel.add(customerInfoLabel);
                searchPanel.revalidate();
                searchPanel.repaint();
            }

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Order ID");
            model.addColumn("Operation Staff ID");
            model.addColumn("Order DateTime");
            model.addColumn("Total Price");
            model.addColumn("Status");
            model.addColumn("Cancel Reason");
            model.addColumn("Both(Category Length)");
            model.addColumn("Partial Payment");
            while (resultSet.next()) {
                model.addRow(new Object[]{
                        resultSet.getString("order_id"),
                        resultSet.getString("operational_staffID"),
                        resultSet.getString("orderDateTime"),
                        resultSet.getString("total_price"),
                        resultSet.getString("order_status"),
                        resultSet.getString("cancelReason"),
                        resultSet.getString("bolt"),
                        resultSet.getString("pay")
                });
            }

            orderTable.setModel(model);
            ((AbstractTableModel) orderTable.getModel()).fireTableDataChanged();

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void addSupplier(String parEmployeeId, String taxcode, String name, String address, String bankAccount, String phoneNumber) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fabric", "root", "");
            if (parEmployeeId.isEmpty() || taxcode.isEmpty() || name.isEmpty() || address.isEmpty() || bankAccount.isEmpty() || phoneNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields");
                return; // Không thực hiện thêm nhà cung cấp nếu có trường nào trống
            }
            // Lấy vai trò của người dùng hiện tại
            String userRole = null;

            // Thực hiện truy vấn để lấy vai trò
            String getRoleQuery = "SELECT role FROM accounts WHERE username=?";
            try (PreparedStatement getRoleStatement = connection.prepareStatement(getRoleQuery)) {
                getRoleStatement.setString(1, loggedInUsername);

                try (ResultSet roleResultSet = getRoleStatement.executeQuery()) {
                    if (roleResultSet.next()) {
                        userRole = roleResultSet.getString("role");
                    }
                }
            }

            // Kiểm tra xem người dùng có vai trò partner_staff không
            if (!"partner_staff".equals(userRole)&& !"OfficeStaff".equals(userRole)) {
                // Lấy Supplier ID lớn nhất từ cơ sở dữ liệu
                String getMaxSupplierIdSql = "SELECT MAX(supplier_id) AS max_id FROM suppliers";
                try (Statement getMaxSupplierIdStatement = connection.createStatement();
                     ResultSet maxIdResultSet = getMaxSupplierIdStatement.executeQuery(getMaxSupplierIdSql)) {

                    int newSupplierId;
                    if (maxIdResultSet.next()) {
                        // Lấy giá trị lớn nhất và cộng thêm 1
                        newSupplierId = maxIdResultSet.getInt("max_id") + 1;
                    } else {
                        // Nếu không có dữ liệu, bắt đầu từ 1
                        newSupplierId = 1;
                    }

                    // Thêm nhà cung cấp vào bảng suppliers
                    String sqlSupplier = "INSERT INTO suppliers (supplier_id, parEmployeeID, taxcode, name, address, bankAccount) VALUES (?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement statementSupplier = connection.prepareStatement(sqlSupplier)) {
                        statementSupplier.setInt(1, newSupplierId);
                        statementSupplier.setString(2, parEmployeeId);
                        statementSupplier.setString(3, taxcode);
                        statementSupplier.setString(4, name);
                        statementSupplier.setString(5, address);
                        statementSupplier.setString(6, bankAccount);

                        int rowsAffectedSupplier = statementSupplier.executeUpdate();

                        // Nếu truy vấn thêm nhà cung cấp thành công, thêm số điện thoại vào bảng supplierphonenumber
                        if (rowsAffectedSupplier > 0) {
                            String sqlPhoneNumber = "INSERT INTO supplierphonenumber (supplier_id, phonenumber) VALUES (?, ?)";
                            try (PreparedStatement statementPhoneNumber = connection.prepareStatement(sqlPhoneNumber)) {
                                statementPhoneNumber.setInt(1, newSupplierId);  // Sử dụng ID vừa lấy
                                statementPhoneNumber.setString(2, phoneNumber);

                                int rowsAffectedPhoneNumber = statementPhoneNumber.executeUpdate();

                                if (rowsAffectedPhoneNumber > 0) {
                                    JOptionPane.showMessageDialog(this, "Supplier added successfully!");

                                    loadSuppliersData();
                                } else {
                                    JOptionPane.showMessageDialog(this, "Failed to add phone number");
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "Failed to add supplier");
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "The account does not have sufficient authority to perform this function\r\n"
                		+ "");
            }

            connection.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
