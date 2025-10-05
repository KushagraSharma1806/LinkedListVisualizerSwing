package src;
import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;

public class LinkedListVisualizer extends JPanel {
    private Node head;
    private java.util.List<Integer> listData = new ArrayList<>();

    public LinkedListVisualizer() {
        setPreferredSize(new Dimension(800, 300));
        setBackground(Color.WHITE);
    }

    public void insert(int data) {
        Node newNode = new Node(data);
        if (head == null) head = newNode;
        else {
            Node temp = head;
            while (temp.next != null) temp = temp.next;
            temp.next = newNode;
        }
        listData.add(data);
        repaint();
    }

    public void delete(int data) {
        if (head == null) return;
        if (head.data == data) {
            head = head.next;
            listData.remove(Integer.valueOf(data));
            repaint();
            return;
        }
        Node temp = head;
        while (temp.next != null && temp.next.data != data) temp = temp.next;
        if (temp.next != null) {
            temp.next = temp.next.next;
            listData.remove(Integer.valueOf(data));
            repaint();
        }
    }

    public boolean search(int data) {
        Node temp = head;
        while (temp != null) {
            if (temp.data == data) return true;
            temp = temp.next;
        }
        return false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = 50, y = 150;
        for (int i = 0; i < listData.size(); i++) {
            int val = listData.get(i);
            g.setColor(Color.CYAN);
            g.fillRect(x, y, 60, 40);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, 60, 40);
            g.drawString(String.valueOf(val), x + 25, y + 25);

            if (i < listData.size() - 1) {
                g.drawLine(x + 60, y + 20, x + 90, y + 20);
                g.drawLine(x + 85, y + 15, x + 90, y + 20);
                g.drawLine(x + 85, y + 25, x + 90, y + 20);
            }
            x += 100;
        }
    }
}
