// Main.java - FIX: Centralized ML recording and prediction update
package src;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Main extends JFrame {
    private VisualizerPanel panel;
    private JTextField valueField;
    private JTextField positionField;
    private JComboBox<String> listType;
    private JToggleButton darkModeToggle;

    // ML Component - Simple addition
    private SmartPredictor smartPredictor;
    private JPanel predictionPanel;
    private JTextArea predictionArea;

    public Main() {
        setTitle("🚀 Linked List Visualizer");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        initializeComponents();
        setupUI();
        setVisible(true);
    }

    private void initializeComponents() {
        panel = new VisualizerPanel();
        valueField = new JTextField(8);
        positionField = new JTextField(8);

        String[] modes = { "Singly", "Doubly", "Circular" };
        listType = new JComboBox<>(modes);
        darkModeToggle = new JToggleButton("🌙 Dark Mode", true);

        // ML Initialization
        smartPredictor = new SmartPredictor();
        predictionArea = new JTextArea(8, 20);
        predictionArea.setEditable(false);
    }

    private void setupUI() {
        // Header with solid background
        JPanel headerPanel = createHeader();
        add(headerPanel, BorderLayout.NORTH);

        // Visualization panel
        add(panel, BorderLayout.CENTER);

        // Control panel
        add(createControlPanel(), BorderLayout.SOUTH);

        // ML Predictions panel - Simple addition to right side
        add(createPredictionPanel(), BorderLayout.EAST);
    }

    private JPanel createPredictionPanel() {
        predictionPanel = new JPanel(new BorderLayout());
        predictionPanel.setBackground(new Color(40, 40, 60));
        predictionPanel.setPreferredSize(new Dimension(250, getHeight()));
        predictionPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 140), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JLabel titleLabel = new JLabel("🤖 Smart Predictions");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        predictionArea.setFont(new Font("Arial", Font.PLAIN, 12));
        predictionArea.setBackground(new Color(30, 30, 40));
        predictionArea.setForeground(Color.WHITE);
        predictionArea.setLineWrap(true);
        predictionArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(predictionArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 140)));

        JButton refreshBtn = new JButton("🔄 Update Predictions");
        refreshBtn.setBackground(new Color(52, 152, 219));
        refreshBtn.setForeground(Color.WHITE);
        // Action Listener is correct: it forces a refresh of the prediction display
        refreshBtn.addActionListener(e -> updatePredictions());

        predictionPanel.add(titleLabel, BorderLayout.NORTH);
        predictionPanel.add(scrollPane, BorderLayout.CENTER);
        predictionPanel.add(refreshBtn, BorderLayout.SOUTH);

        // Initial prediction update
        updatePredictions();

        return predictionPanel;
    }

    private void updatePredictions() {
        java.util.List<SmartPredictor.Prediction> predictions = smartPredictor.getPredictions();

        StringBuilder predictionText = new StringBuilder();
        predictionText.append("ML learns your patterns and suggests:\n\n");

        if (predictions.isEmpty()) {
            predictionText.append("Start using the visualizer!\n");
            predictionText.append("ML will learn your patterns\n");
            predictionText.append("and suggest next operations.");
        } else {
            for (SmartPredictor.Prediction prediction : predictions) {
                predictionText.append("• ").append(prediction.toString()).append("\n");
                // The reasoning field may use two spaces in the SmartPredictor provided,
                // keeping the formatting consistent with the input file.
                predictionText.append("  ").append(prediction.reasoning).append("\n\n");
            }
        }

        predictionArea.setText(predictionText.toString());
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(70, 0, 120));
        header.setPreferredSize(new Dimension(getWidth(), 100));
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JLabel titleLabel = new JLabel("🚀 Linked List Visualizer Pro");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitleLabel = new JLabel("Visualize • Animate • Learn • Predict");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(70, 0, 120));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);

        JPanel togglePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        togglePanel.setBackground(new Color(70, 0, 120));
        togglePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30));

        darkModeToggle.setFont(new Font("Arial", Font.BOLD, 14));
        darkModeToggle.setBackground(new Color(255, 215, 0));
        darkModeToggle.setForeground(Color.BLACK);
        darkModeToggle.setFocusPainted(false);
        darkModeToggle.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        darkModeToggle.addActionListener(e -> {
            panel.setDarkMode(darkModeToggle.isSelected());
            darkModeToggle.setText(darkModeToggle.isSelected() ? "🌙 Dark Mode" : "☀️ Light Mode");
        });

        togglePanel.add(darkModeToggle);

        header.add(titlePanel, BorderLayout.CENTER);
        header.add(togglePanel, BorderLayout.EAST);

        return header;
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBackground(new Color(50, 50, 80));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel inputPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        inputPanel.setBackground(new Color(50, 50, 80));

        inputPanel.add(createInputField("🎯 Node Value", valueField));
        inputPanel.add(createInputField("📍 Position", positionField));
        inputPanel.add(createListTypeSelector());
        inputPanel.add(new JLabel());

        JPanel buttonPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        buttonPanel.setBackground(new Color(50, 50, 80));

        buttonPanel.add(createColorButton("Insert Start", "🚀", new Color(46, 204, 113)));
        buttonPanel.add(createColorButton("Insert End", "⭐", new Color(52, 152, 219)));
        buttonPanel.add(createColorButton("Insert At", "🎯", new Color(155, 89, 182)));
        buttonPanel.add(createColorButton("Delete Value", "🗑️", new Color(231, 76, 60)));
        buttonPanel.add(createColorButton("Delete At", "❌", new Color(192, 57, 43)));
        buttonPanel.add(createColorButton("Reverse", "🔄", new Color(142, 68, 173)));
        buttonPanel.add(createColorButton("Search", "🔍", new Color(241, 196, 15)));
        buttonPanel.add(createColorButton("Clear", "✨", new Color(39, 174, 96)));

        controlPanel.add(inputPanel);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        controlPanel.add(buttonPanel);

        return controlPanel;
    }

    private JPanel createInputField(String label, JTextField field) {
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(new Color(50, 50, 80));

        JLabel jLabel = new JLabel(label);
        jLabel.setForeground(Color.WHITE);
        jLabel.setFont(new Font("Arial", Font.BOLD, 14));
        jLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setBackground(new Color(30, 30, 40));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 140), 2),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        containerPanel.add(jLabel, BorderLayout.NORTH);
        containerPanel.add(field, BorderLayout.CENTER);

        return containerPanel;
    }

    private JPanel createListTypeSelector() {
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(new Color(50, 50, 80));

        JLabel label = new JLabel("📋 List Type");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        listType.setFont(new Font("Arial", Font.BOLD, 14));
        listType.setBackground(new Color(30, 30, 40));
        listType.setForeground(Color.WHITE);
        listType.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 140), 2),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        listType.addActionListener(e -> {
            panel.mode = (String) listType.getSelectedItem();
            panel.clear();
            updatePredictions(); // Also update predictions when the list type changes
        });

        containerPanel.add(label, BorderLayout.NORTH);
        containerPanel.add(listType, BorderLayout.CENTER);

        return containerPanel;
    }

    private JButton createColorButton(String text, String emoji, Color color) {
        JButton button = new JButton(emoji + " " + text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 8, 12, 8));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });

        // Simplified action listener: pass control to the central handler
        button.addActionListener(e -> {
            handleButtonAction(text);
        });

        return button;
    }

    private void handleButtonAction(String action) {
        try {
            // --- 1. Execute Linked List Operation ---
            switch (action) {
                case "Insert Start":
                    panel.insertStart(Integer.parseInt(valueField.getText()));
                    break;
                case "Insert End":
                    panel.insertEnd(Integer.parseInt(valueField.getText()));
                    break;
                case "Insert At":
                    panel.insertAt(Integer.parseInt(valueField.getText()),
                            Integer.parseInt(positionField.getText()));
                    break;
                case "Delete Value":
                    panel.deleteValue(Integer.parseInt(valueField.getText()));
                    break;
                case "Delete At":
                    panel.deleteAt(Integer.parseInt(positionField.getText()));
                    break;
                case "Reverse":
                    panel.reverse();
                    break;
                case "Search":
                    panel.search(Integer.parseInt(valueField.getText()));
                    break;
                case "Clear":
                    panel.clear();
                    break;
            }

            // --- 2. Record Operation for Smart Predictor ---
            String operation = action.toUpperCase().replace(" ", "_");
            int value = 0;
            try {
                if (action.equals("Insert Start") || action.equals("Insert End") ||
                        action.equals("Delete Value") || action.equals("Search")) {
                    // Use valueField for these
                    if (!valueField.getText().isEmpty()) {
                        value = Integer.parseInt(valueField.getText());
                    }
                } else if (action.equals("Insert At") || action.equals("Delete At")) {
                    // Use positionField for these (which is treated as the 'value' context in the
                    // predictor)
                    if (!positionField.getText().isEmpty()) {
                        value = Integer.parseInt(positionField.getText());
                    }
                }
            } catch (NumberFormatException ignored) {
                // If parsing fails, value remains 0.
            }

            smartPredictor.recordOperation(operation, value);

            // --- 3. Clear fields and Update UI ---
            valueField.setText("");
            positionField.setText("");
            updatePredictions(); // Update predictions after EACH successful operation!

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers!",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main();
        });
    }
}