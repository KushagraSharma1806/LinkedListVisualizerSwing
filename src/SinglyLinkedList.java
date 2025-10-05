// SinglyLinkedList.java - UPDATED
package src;

import java.util.*;

public class SinglyLinkedList {
    public Node head;
    private List<NodeChangeListener> listeners = new ArrayList<>();

    public interface NodeChangeListener {
        void onNodeChanged(String operation, String nodeId);
    }

    public void addChangeListener(NodeChangeListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners(String operation, String nodeId) {
        for (NodeChangeListener listener : listeners) {
            listener.onNodeChanged(operation, nodeId);
        }
    }

    public List<Node> getNodes() {
        List<Node> list = new ArrayList<>();
        Node temp = head;
        while (temp != null) {
            list.add(temp);
            temp = temp.next;
        }
        return list;
    }

    public void insertStart(int data) {
        Node newNode = new Node(data);
        newNode.next = head;
        head = newNode;
        notifyListeners("ins-start", newNode.getId());
    }

    public void insertEnd(int data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = newNode;
            notifyListeners("ins-end", newNode.getId());
            return;
        }
        Node temp = head;
        while (temp.next != null)
            temp = temp.next;
        temp.next = newNode;
        notifyListeners("ins-end", newNode.getId());
    }

    public void insertAt(int data, int position) {
        if (position == 0) {
            insertStart(data);
            return;
        }

        Node newNode = new Node(data);
        Node temp = head;
        for (int i = 0; i < position - 1 && temp != null; i++) {
            temp = temp.next;
        }

        if (temp == null) {
            insertEnd(data);
        } else {
            newNode.next = temp.next;
            temp.next = newNode;
            notifyListeners("ins-pos", newNode.getId());
        }
    }

    public void deleteValue(int data) {
        if (head == null)
            return;

        if (head.data == data) {
            String deletedId = head.getId();
            head = head.next;
            notifyListeners("del-value", deletedId);
            return;
        }

        Node temp = head;
        while (temp.next != null && temp.next.data != data) {
            temp = temp.next;
        }

        if (temp.next != null) {
            String deletedId = temp.next.getId();
            temp.next = temp.next.next;
            notifyListeners("del-value", deletedId);
        }
    }

    public void deleteAt(int position) {
        if (head == null)
            return;

        if (position == 0) {
            String deletedId = head.getId();
            head = head.next;
            notifyListeners("del-position", deletedId);
            return;
        }

        Node temp = head;
        for (int i = 0; i < position - 1 && temp != null; i++) {
            temp = temp.next;
        }

        if (temp != null && temp.next != null) {
            String deletedId = temp.next.getId();
            temp.next = temp.next.next;
            notifyListeners("del-position", deletedId);
        }
    }

    public void reverse() {
        Node prev = null;
        Node current = head;
        Node next = null;

        while (current != null) {
            next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }

        head = prev;
        notifyListeners("reverse", null);
    }

    public Node search(int data) {
        Node temp = head;
        while (temp != null) {
            if (temp.data == data)
                return temp;
            temp = temp.next;
        }
        return null;
    }

    public void clear() {
        head = null;
        notifyListeners("clear", null);
    }
}