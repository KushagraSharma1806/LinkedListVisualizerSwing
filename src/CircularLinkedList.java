// CircularLinkedList.java - UPDATED
package src;

import java.util.*;

public class CircularLinkedList {
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
        if (head == null)
            return list;
        Node temp = head;
        do {
            list.add(temp);
            temp = temp.next;
        } while (temp != head);
        return list;
    }

    public void insertStart(int data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = newNode;
            newNode.next = head;
        } else {
            Node temp = head;
            while (temp.next != head) {
                temp = temp.next;
            }
            temp.next = newNode;
            newNode.next = head;
            head = newNode;
        }
        notifyListeners("ins-start", newNode.getId());
    }

    public void insertEnd(int data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = newNode;
            newNode.next = head;
        } else {
            Node temp = head;
            while (temp.next != head) {
                temp = temp.next;
            }
            temp.next = newNode;
            newNode.next = head;
        }
        notifyListeners("ins-end", newNode.getId());
    }

    public void insertAt(int data, int position) {
        if (position == 0) {
            insertStart(data);
            return;
        }

        Node newNode = new Node(data);
        Node temp = head;
        for (int i = 0; i < position - 1 && temp.next != head; i++) {
            temp = temp.next;
        }

        newNode.next = temp.next;
        temp.next = newNode;
        notifyListeners("ins-pos", newNode.getId());
    }

    public void deleteValue(int data) {
        if (head == null)
            return;

        if (head.data == data) {
            String deletedId = head.getId();
            if (head.next == head) {
                head = null;
            } else {
                Node temp = head;
                while (temp.next != head) {
                    temp = temp.next;
                }
                temp.next = head.next;
                head = head.next;
            }
            notifyListeners("del-value", deletedId);
            return;
        }

        Node temp = head;
        while (temp.next != head && temp.next.data != data) {
            temp = temp.next;
        }

        if (temp.next.data == data) {
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
            if (head.next == head) {
                head = null;
            } else {
                Node temp = head;
                while (temp.next != head) {
                    temp = temp.next;
                }
                temp.next = head.next;
                head = head.next;
            }
            notifyListeners("del-position", deletedId);
            return;
        }

        Node temp = head;
        for (int i = 0; i < position - 1 && temp.next != head; i++) {
            temp = temp.next;
        }

        if (temp.next != head) {
            String deletedId = temp.next.getId();
            temp.next = temp.next.next;
            notifyListeners("del-position", deletedId);
        }
    }

    public void reverse() {
        if (head == null || head.next == head)
            return;

        Node prev = null;
        Node current = head;
        Node next = null;

        do {
            next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        } while (current != head);

        head.next = prev;
        head = prev;
        notifyListeners("reverse", null);
    }

    public Node search(int data) {
        if (head == null)
            return null;
        Node temp = head;
        do {
            if (temp.data == data)
                return temp;
            temp = temp.next;
        } while (temp != head);
        return null;
    }

    public void clear() {
        head = null;
        notifyListeners("clear", null);
    }
}