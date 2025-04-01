import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;


public class ShoppingCartForm extends JFrame {
    private JTable cartTable;
    private DefaultTableModel cartTableModel;
    private JButton removeButton;

    public ShoppingCartForm() {
        setTitle("Shopping Cart");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        cartTableModel = new DefaultTableModel(
                new Object[]{"ID", "Name", "Price", "Quantity", "Total"}, 0);
        cartTable = new JTable(cartTableModel);
        JScrollPane cartScrollPane = new JScrollPane(cartTable);
        cartScrollPane.setBorder(BorderFactory.createTitledBorder("Cart Items"));
        mainPanel.add(cartScrollPane, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        removeButton = new JButton("Remove Selected");
        removeButton.addActionListener(e -> removeProductFromCart());
        buttonPanel.add(removeButton);

        JButton clearButton = new JButton("Clear Cart");
        clearButton.addActionListener(e -> {
            SupermarketBillingSystem.clearCart();
        });
        buttonPanel.add(clearButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);


        add(mainPanel);


        refreshTable();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void refreshTable() {
        cartTableModel.setRowCount(0);
        for (Product product : SupermarketBillingSystem.getCart()) {
            double total = product.getPrice() * product.getQuantity();
            cartTableModel.addRow(new Object[]{
                    product.getId(),
                    product.getName(),
                    String.format("%.2f", product.getPrice()),
                    product.getQuantity(),
                    String.format("%.2f", total)
            });
        }
    }

    private void removeProductFromCart() {
        int selectedRow = cartTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to remove!");
            return;
        }

        String id = cartTable.getValueAt(selectedRow, 0).toString();
        int quantity = Integer.parseInt(cartTable.getValueAt(selectedRow, 3).toString());

        SupermarketBillingSystem.removeFromCart(id, quantity);
    }
}