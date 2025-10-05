// Node.java - UPDATED
package src;

public class Node {
    int data;
    Node next;
    Node prev;
    String id; // Unique identifier for animation

    public Node(int data) {
        this.data = data;
        this.next = null;
        this.prev = null;
        this.id = System.currentTimeMillis() + "-" + Math.random();
    }

    public String getId() {
        return id;
    }
}