import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class ProductManagementForm extends JFrame {
    private JTable productTable;
    private DefaultTableModel productTableModel;
    private JTextField productIdField, productNameField, priceField, quantityField;
    private JButton addButton;

    public ProductManagementForm() {
        setTitle("Product Management");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add Product to Cart"));

        inputPanel.add(new JLabel("Product ID:"));
        productIdField = new JTextField();
        inputPanel.add(productIdField);

        inputPanel.add(new JLabel("Product Name:"));
        productNameField = new JTextField();
        productNameField.setEditable(false);
        inputPanel.add(productNameField);

        inputPanel.add(new JLabel("Price:"));
        priceField = new JTextField();
        priceField.setEditable(false);
        inputPanel.add(priceField);

        inputPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField("1");
        inputPanel.add(quantityField);

        addButton = new JButton("Add to Cart");
        addButton.addActionListener(e -> addProductToCart());
        inputPanel.add(addButton);

        mainPanel.add(inputPanel, BorderLayout.NORTH);


        productTableModel = new DefaultTableModel(
                new Object[]{"ID", "Name", "Price", "Available"}, 0);
        productTable = new JTable(productTableModel);
        JScrollPane productScrollPane = new JScrollPane(productTable);
        productScrollPane.setBorder(BorderFactory.createTitledBorder("Available Products"));
        mainPanel.add(productScrollPane, BorderLayout.CENTER);


        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && productTable.getSelectedRow() != -1) {
                int row = productTable.getSelectedRow();
                productIdField.setText(productTable.getValueAt(row, 0).toString());
                productNameField.setText(productTable.getValueAt(row, 1).toString());
                priceField.setText(productTable.getValueAt(row, 2).toString());
                quantityField.setText("1");
            }
        });


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));
        buttonPanel.add(exitButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);


        add(mainPanel);


        refreshTable();


        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void refreshTable() {
        productTableModel.setRowCount(0);
        for (Product product : SupermarketBillingSystem.getProducts()) {
            productTableModel.addRow(new Object[]{
                    product.getId(),
                    product.getName(),
                    String.format("%.2f", product.getPrice()),
                    product.getQuantity()
            });
        }
    }

    private void addProductToCart() {
        String id = productIdField.getText();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a product!");
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityField.getText());
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than 0!");
                return;
            }

            boolean success = SupermarketBillingSystem.addToCart(id, quantity);

            if (!success) {
                JOptionPane.showMessageDialog(this, "Failed to add product to cart!");
                return;
            }


            productIdField.setText("");
            productNameField.setText("");
            priceField.setText("");
            quantityField.setText("1");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid quantity!");
        }
    }
}