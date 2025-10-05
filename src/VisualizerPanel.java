// VisualizerPanel.java - SIMPLIFIED BUT ATTRACTIVE
package src;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;

public class VisualizerPanel extends JPanel implements SinglyLinkedList.NodeChangeListener,
        DoublyLinkedList.NodeChangeListener,
        CircularLinkedList.NodeChangeListener {
    public String mode = "Singly";
    public SinglyLinkedList sList = new SinglyLinkedList();
    public DoublyLinkedList dList = new DoublyLinkedList();
    public CircularLinkedList cList = new CircularLinkedList();

    private String activeNodeId = null;
    private String currentOperation = null;
    private Timer highlightTimer;

    private boolean darkMode = true;

    public VisualizerPanel() {
        setPreferredSize(new Dimension(1400, 500));
        setBackground(darkMode ? new Color(30, 30, 40) : new Color(240, 240, 250));

        // Register listeners
        sList.addChangeListener(this);
        dList.addChangeListener(this);
        cList.addChangeListener(this);
    }

    public void setDarkMode(boolean dark) {
        this.darkMode = dark;
        setBackground(dark ? new Color(30, 30, 40) : new Color(240, 240, 250));
        repaint();
    }

    public void insertStart(int val) {
        switch (mode) {
            case "Singly":
                sList.insertStart(val);
                break;
            case "Doubly":
                dList.insertStart(val);
                break;
            case "Circular":
                cList.insertStart(val);
                break;
        }
    }

    public void insertEnd(int val) {
        switch (mode) {
            case "Singly":
                sList.insertEnd(val);
                break;
            case "Doubly":
                dList.insertEnd(val);
                break;
            case "Circular":
                cList.insertEnd(val);
                break;
        }
    }

    public void insertAt(int val, int pos) {
        switch (mode) {
            case "Singly":
                sList.insertAt(val, pos);
                break;
            case "Doubly":
                dList.insertAt(val, pos);
                break;
            case "Circular":
                cList.insertAt(val, pos);
                break;
        }
    }

    public void deleteValue(int val) {
        switch (mode) {
            case "Singly":
                sList.deleteValue(val);
                break;
            case "Doubly":
                dList.deleteValue(val);
                break;
            case "Circular":
                cList.deleteValue(val);
                break;
        }
    }

    public void deleteAt(int pos) {
        switch (mode) {
            case "Singly":
                sList.deleteAt(pos);
                break;
            case "Doubly":
                dList.deleteAt(pos);
                break;
            case "Circular":
                cList.deleteAt(pos);
                break;
        }
    }

    public void reverse() {
        switch (mode) {
            case "Singly":
                sList.reverse();
                break;
            case "Doubly":
                dList.reverse();
                break;
            case "Circular":
                cList.reverse();
                break;
        }
    }

    public void search(int val) {
        Node found = null;
        switch (mode) {
            case "Singly":
                found = sList.search(val);
                break;
            case "Doubly":
                found = dList.search(val);
                break;
            case "Circular":
                found = cList.search(val);
                break;
        }

        if (found != null) {
            activeNodeId = found.getId();
            currentOperation = "search";
            startHighlightTimer();
        }
        repaint();
    }

    public void clear() {
        sList.clear();
        dList.clear();
        cList.clear();
        activeNodeId = null;
        currentOperation = null;
        repaint();
    }

    @Override
    public void onNodeChanged(String operation, String nodeId) {
        this.currentOperation = operation;
        this.activeNodeId = nodeId;
        startHighlightTimer();
        repaint();
    }

    private void startHighlightTimer() {
        if (highlightTimer != null) {
            highlightTimer.cancel();
        }
        highlightTimer = new Timer();
        highlightTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                activeNodeId = null;
                currentOperation = null;
                repaint();
            }
        }, 1000);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        java.util.List<Node> nodes;
        switch (mode) {
            case "Doubly":
                nodes = dList.getNodes();
                break;
            case "Circular":
                nodes = cList.getNodes();
                break;
            default:
                nodes = sList.getNodes();
                break;
        }

        if (nodes.isEmpty()) {
            drawEmptyMessage(g2d);
            return;
        }

        drawNodes(g2d, nodes);
        drawConnections(g2d, nodes);

        if (mode.equals("Circular") && nodes.size() > 1) {
            drawCircularConnection(g2d, nodes);
        }
    }

    private void drawEmptyMessage(Graphics2D g2d) {
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.setColor(darkMode ? new Color(200, 200, 220) : new Color(80, 80, 100));

        String message = "🚀 Linked List is Empty";
        FontMetrics fm = g2d.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(message)) / 2;
        int y = getHeight() / 2 - 20;
        g2d.drawString(message, x, y);

        g2d.setFont(new Font("Arial", Font.ITALIC, 16));
        String subtitle = "Add nodes to begin visualization!";
        fm = g2d.getFontMetrics();
        x = (getWidth() - fm.stringWidth(subtitle)) / 2;
        y = getHeight() / 2 + 20;
        g2d.drawString(subtitle, x, y);
    }

    private void drawNodes(Graphics2D g2d, java.util.List<Node> nodes) {
        int startX = 100;
        int y = 200;
        int nodeWidth = 140;
        int nodeHeight = 80;
        int spacing = 160;

        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            int x = startX + i * spacing;

            // Draw node
            Color nodeColor = node.getId().equals(activeNodeId) ? new Color(255, 105, 180) : // Highlight color
                    new Color(65, 105, 225); // Normal blue color

            g2d.setColor(nodeColor);
            g2d.fillRoundRect(x, y, nodeWidth, nodeHeight, 20, 20);

            // Draw border
            g2d.setColor(darkMode ? Color.WHITE : Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(x, y, nodeWidth, nodeHeight, 20, 20);

            // Draw node content
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 12));

            int textX = x + 15;
            int textY = y + 25;
            g2d.drawString("Data: " + node.data, textX, textY);
            g2d.drawString("Next: " + (node.next != null ? node.next.data : "null"), textX, textY + 20);

            if (mode.equals("Doubly")) {
                g2d.drawString("Prev: " + (node.prev != null ? node.prev.data : "null"), textX, textY + 40);
            }

            // Draw node index
            g2d.setColor(new Color(255, 200, 100));
            g2d.setFont(new Font("Arial", Font.BOLD, 11));
            g2d.drawString("Node " + i, x + 10, y - 5);
        }
    }

    private void drawConnections(Graphics2D g2d, java.util.List<Node> nodes) {
        int startX = 100;
        int y = 200;
        int nodeWidth = 140;
        int nodeHeight = 80;
        int spacing = 160;

        g2d.setColor(darkMode ? Color.WHITE : Color.BLACK);
        g2d.setStroke(new BasicStroke(2));

        for (int i = 0; i < nodes.size() - 1; i++) {
            int x1 = startX + i * spacing + nodeWidth;
            int y1 = y + nodeHeight / 2;
            int x2 = startX + (i + 1) * spacing;
            int y2 = y + nodeHeight / 2;

            // Draw arrow
            g2d.drawLine(x1, y1, x2, y2);
            drawArrowHead(g2d, x2, y2, Math.PI);
        }
    }

    private void drawCircularConnection(Graphics2D g2d, java.util.List<Node> nodes) {
        int startX = 100;
        int y = 200;
        int nodeWidth = 140;
        int nodeHeight = 80;
        int spacing = 160;

        g2d.setColor(Color.GREEN);
        g2d.setStroke(new BasicStroke(2));

        int firstX = startX;
        int firstY = y + nodeHeight / 2;
        int lastX = startX + (nodes.size() - 1) * spacing + nodeWidth;
        int lastY = y + nodeHeight / 2;

        // Draw curved connection
        int controlX = (firstX + lastX) / 2;
        int controlY = y - 50;

        g2d.drawLine(lastX, lastY, controlX, controlY);
        g2d.drawLine(controlX, controlY, firstX, firstY);

        drawArrowHead(g2d, firstX, firstY, 0);

        // Label
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.drawString("↻ Circular", lastX + 10, y + nodeHeight / 2);
    }

    private void drawArrowHead(Graphics2D g2d, int x, int y, double angle) {
        int arrowSize = 10;
        int x1 = x - (int) (arrowSize * Math.cos(angle - Math.PI / 6));
        int y1 = y - (int) (arrowSize * Math.sin(angle - Math.PI / 6));
        int x2 = x - (int) (arrowSize * Math.cos(angle + Math.PI / 6));
        int y2 = y - (int) (arrowSize * Math.sin(angle + Math.PI / 6));

        g2d.fillPolygon(new int[] { x, x1, x2 }, new int[] { y, y1, y2 }, 3);
    }
}