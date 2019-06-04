public class Node {
    private Point p;
    private int g;
    private int h;
    private int f;
    private Node prev_node;

    public Node(Point p, int g, int h, Node prev_node) {
        this.p = p;
        this.g = g;
        this.h = h;
        f = g + h;
        this.prev_node = prev_node;
    }


    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public Node getPrev() {
        return prev_node;
    }

    public void setPrev(Node prev_node) {
        this.prev_node = prev_node;
    }

    public Point getPos() {
        return p;
    }

    public boolean equals(Node o) {
        return this.p.equals(o.p);
    }

    public String toString() {
        return ("Node x: " + p.x + ", y: " + p.y);
    }
}


