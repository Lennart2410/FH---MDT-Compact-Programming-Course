package HomeworkAssignment3;

import HomeworkAssignment3.general.Item;
import HomeworkAssignment3.general.Order;
import HomeworkAssignment3.general.exceptions.WarehouseException;
import HomeworkAssignment3.logging.LogListener;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class WarehouseSystemUI extends JFrame implements LogListener {
    private JTextArea[] stationLogs = new JTextArea[5];
    private JComboBox<String> itemSelector;
    private JComboBox<String> addressSelector;
    private DefaultListModel<String> itemListModel;
    private JList<String> orderList;
    private JButton addButton, removeButton, createOrderButton;

    // New: Order overview
    private DefaultListModel<String> createdOrdersModel;
    private JList<String> createdOrdersList;

    // Track orders by ID → index in the list
    private final Map<String, Integer> orderIndexMap = new ConcurrentHashMap<>();
    private final Map<String, Order> activeOrders = new ConcurrentHashMap<>();

    private Warehouse warehouse;

    public WarehouseSystemUI() {
        setTitle("Warehouse Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // === LEFT: Split vertically (order creation + order overview) ===
        JPanel leftPanel = new JPanel(new GridLayout(2, 1, 5, 5));

        // === Order creation panel ===
        JPanel orderPanel = new JPanel(new BorderLayout(5,5));
        orderPanel.setBorder(BorderFactory.createTitledBorder("Create Order"));

        // Address picklist
        JPanel addressPanel = new JPanel(new BorderLayout(5,5));
        JLabel addressLabel = new JLabel("Address:");
        String[] predefinedAddresses = {"Dortmund", "Berlin", "Munich"};
        addressSelector = new JComboBox<>(predefinedAddresses);
        addressSelector.setSelectedIndex(-1);
        addressPanel.add(addressLabel, BorderLayout.WEST);
        addressPanel.add(addressSelector, BorderLayout.CENTER);

        // Item selector
        String[] predefinedItems = {"Phone", "Book", "Pen", "T-Shirt", "Jeans"};
        itemSelector = new JComboBox<>(predefinedItems);

        addButton = new JButton("Add Item");
        removeButton = new JButton("Remove Selected");
        createOrderButton = new JButton("Create Order");
        createOrderButton.setEnabled(false);

        itemListModel = new DefaultListModel<>();
        orderList = new JList<>(itemListModel);
        JScrollPane orderScroll = new JScrollPane(orderList);
        orderScroll.setBorder(BorderFactory.createTitledBorder("Items in Order"));

        JPanel buttonPanel = new JPanel(new GridLayout(3,1,5,5));
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(createOrderButton);

        JPanel topPanel = new JPanel(new BorderLayout(5,5));
        topPanel.add(addressPanel, BorderLayout.NORTH);
        topPanel.add(itemSelector, BorderLayout.CENTER);

        orderPanel.add(topPanel, BorderLayout.NORTH);
        orderPanel.add(orderScroll, BorderLayout.CENTER);
        orderPanel.add(buttonPanel, BorderLayout.SOUTH);

        // === Order overview panel ===
        JPanel createdOrdersPanel = new JPanel(new BorderLayout(5,5));
        createdOrdersPanel.setBorder(BorderFactory.createTitledBorder("All Created Orders"));

        createdOrdersModel = new DefaultListModel<>();
        createdOrdersList = new JList<>(createdOrdersModel);
        JScrollPane createdOrdersScroll = new JScrollPane(createdOrdersList);
        createdOrdersPanel.add(createdOrdersScroll, BorderLayout.CENTER);

        leftPanel.add(orderPanel);
        leftPanel.add(createdOrdersPanel);

        // === Logs on the right ===
        JPanel logPanel = new JPanel(new GridLayout(5,1,5,5));
        String[] logTitles = {"Picking Station", "AGV Runner", "Packing Station", "Loading Station", "Warehouse"};

        for (int i = 0; i < 5; i++) {
            stationLogs[i] = new JTextArea();
            stationLogs[i].setEditable(false);
            JScrollPane logScroll = new JScrollPane(stationLogs[i]);
            logScroll.setBorder(BorderFactory.createTitledBorder(logTitles[i]));
            logPanel.add(logScroll);
        }

        add(leftPanel, BorderLayout.WEST);
        add(logPanel, BorderLayout.CENTER);

        // === Event handling ===
        addButton.addActionListener(e -> {
            String selected = (String) itemSelector.getSelectedItem();
            if (selected != null) {
                itemListModel.addElement(selected);
                updateCreateButtonState();
            }
        });

        removeButton.addActionListener(e -> {
            int selectedIndex = orderList.getSelectedIndex();
            if (selectedIndex != -1) {
                String removed = itemListModel.get(selectedIndex);
                itemListModel.remove(selectedIndex);
                appendToLog(4, "Removed " + removed + " from order.");
                updateCreateButtonState();
            }
        });

        createOrderButton.addActionListener(e -> {
            String address = (String) addressSelector.getSelectedItem();
            if (address == null || address.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select an address!", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (itemListModel.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No items in the order!", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            List<Item> items = Collections.list(itemListModel.elements()).stream().map(Item::new).toList();
            Order order = new Order(address, items);

            try {
                warehouse.processOrder(order);
            } catch (WarehouseException ex) {
                throw new RuntimeException(ex);
            }

            appendToLog(4, "New order created for address: " + address + " with " + items.size() + " items.");

            // Track and display order
            activeOrders.put(order.getOrderNumber(), order);
            int index = createdOrdersModel.size();
            orderIndexMap.put(order.getOrderNumber(), index);
            createdOrdersModel.addElement(formatOrderText(order));

            // Reset UI
            itemListModel.clear();
            addressSelector.setSelectedIndex(-1);
            updateCreateButtonState();
        });

        // Enable/disable Create button dynamically
        itemListModel.addListDataListener(new ListDataListener() {
            @Override public void intervalAdded(ListDataEvent e) { updateCreateButtonState(); }
            @Override public void intervalRemoved(ListDataEvent e) { updateCreateButtonState(); }
            @Override public void contentsChanged(ListDataEvent e) { updateCreateButtonState(); }
        });

        addressSelector.addActionListener(e -> updateCreateButtonState());
    }

    // --- Format how orders appear in the list ---
    private String formatOrderText(Order order) {
        return "Order #" + order.getOrderNumber() + " - Status: " + order.getOrderStatusEnum().toString();
    }

    private void updateOrderDisplay(UUID orderId) {
        Order order = activeOrders.get(orderId);
        if (order == null) return;

        Integer index = orderIndexMap.get(orderId);
        if (index != null && index < createdOrdersModel.size()) {
            createdOrdersModel.set(index, formatOrderText(order));
        }
    }

    private void updateCreateButtonState() {
        boolean hasItems = !itemListModel.isEmpty();
        boolean hasAddress = addressSelector.getSelectedItem() != null;
        createOrderButton.setEnabled(hasItems && hasAddress);
    }

    private void appendToLog(int logIndex, String message) {
        stationLogs[logIndex].append(message + "\n");
        stationLogs[logIndex].setCaretPosition(stationLogs[logIndex].getDocument().getLength());
    }

    public Warehouse startup() throws WarehouseException {
        SwingUtilities.invokeLater(() -> this.setVisible(true));
        warehouse = new Warehouse();
        return warehouse;
    }

    // --- LogListener implementation ---
    @Override
    public void onLog(String stationName, String message) {
        SwingUtilities.invokeLater(() -> {
            // Detect if this log mentions an order status change
            if (message.contains("Order") && message.contains("Status:")) {
                // Example log message: "Order 12345 Status: COMPLETED"
                try {
                    String idPart = message.substring(message.indexOf("Order") + 6, message.indexOf("Status:")).trim();
                    UUID orderId = UUID.fromString(idPart);
                    Order order = activeOrders.get(orderId);
                    if (order != null) updateOrderDisplay(orderId);
                } catch (Exception ignored) {}
            }

            switch (stationName) {
                case "Warehouse" -> appendToLog(4, message);
                case "PickingStation" -> appendToLog(0, message);
                case "AGVRunner" -> appendToLog(1, message);
                case "PackingStation" -> appendToLog(2, message);
                case "LoadingStation" -> appendToLog(3, message);
                default -> appendToLog(4, "[Unknown] " + message);
            }
        });
    }
}
