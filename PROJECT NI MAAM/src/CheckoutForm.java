import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


public class CheckoutForm extends JFrame {
    private JLabel totalLabel, discountedTotalLabel;
    private JComboBox<String> discountCombo;
    private JButton checkoutButton, newBillButton;

    public CheckoutForm() {
        setTitle("Checkout");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        JPanel discountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        discountPanel.setBorder(BorderFactory.createTitledBorder("Discount Options"));

        discountPanel.add(new JLabel("Apply Discount: "));

        discountCombo = new JComboBox<>();
        discountCombo.addItem("None (0%)");
        for (Map.Entry<String, Double> entry : SupermarketBillingSystem.getDiscounts().entrySet()) {
            discountCombo.addItem(entry.getKey() + " (" + (entry.getValue() * 100) + "%)");
        }
        discountCombo.addActionListener(e -> updateTotals());
        discountPanel.add(discountCombo);

        mainPanel.add(discountPanel, BorderLayout.NORTH);


        JPanel summaryPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Order Summary"));

        summaryPanel.add(new JLabel("Cart Items:"));
        JLabel itemCountLabel = new JLabel("0");


        javax.swing.Timer timer = new javax.swing.Timer(500, e -> {
            int count = SupermarketBillingSystem.getCart().size();
            itemCountLabel.setText(Integer.toString(count));
        });
        timer.start();
        summaryPanel.add(itemCountLabel);

        summaryPanel.add(new JLabel("Total Amount:"));
        totalLabel = new JLabel("₱0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        summaryPanel.add(totalLabel);

        summaryPanel.add(new JLabel("Discounted Total:"));
        discountedTotalLabel = new JLabel("₱0.00");
        discountedTotalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        discountedTotalLabel.setForeground(new Color(0, 128, 0));
        summaryPanel.add(discountedTotalLabel);

        mainPanel.add(summaryPanel, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 5, 5));

        checkoutButton = new JButton("Checkout & Print Receipt");
        checkoutButton.addActionListener(e -> {
            String selectedDiscount = (String) discountCombo.getSelectedItem();
            boolean success = SupermarketBillingSystem.checkout(selectedDiscount);
            if (!success) {
                JOptionPane.showMessageDialog(this, "Cart is empty! Add items before checkout.");
            }
        });
        buttonPanel.add(checkoutButton);

        newBillButton = new JButton("Start New Bill");
        newBillButton.addActionListener(e -> {
            SupermarketBillingSystem.clearCart();
        });
        buttonPanel.add(newBillButton);

        JButton exitButton = new JButton("Exit Application");
        exitButton.addActionListener(e -> System.exit(0));
        buttonPanel.add(exitButton);


        JPanel paddedButtonPanel = new JPanel(new BorderLayout());
        paddedButtonPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        paddedButtonPanel.add(buttonPanel, BorderLayout.CENTER);

        mainPanel.add(paddedButtonPanel, BorderLayout.SOUTH);


        add(mainPanel);


        updateTotals();


        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void updateTotals() {
        double total = SupermarketBillingSystem.calculateTotal();
        double discountRate = getSelectedDiscountRate();
        double discountedTotal = total * (1 - discountRate);

        totalLabel.setText(String.format("₱%.2f", total));
        discountedTotalLabel.setText(String.format("₱%.2f", discountedTotal));
    }

    public void resetDiscount() {
        discountCombo.setSelectedIndex(0);
    }

    private double getSelectedDiscountRate() {
        String selected = (String) discountCombo.getSelectedItem();
        if (selected.startsWith("None")) {
            return 0;
        }

        for (Map.Entry<String, Double> entry : SupermarketBillingSystem.getDiscounts().entrySet()) {
            if (selected.startsWith(entry.getKey())) {
                return entry.getValue();
            }
        }
        return 0;
    }
}