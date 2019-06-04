public class Node {
    private Point position;
    private int g;
    private int h;
    private Node priorNode;

    public Node(Point position, int g, int h, Node lastNode) {
        this.position = position;
        this.g = g;
        this.h = h;
        this.priorNode = lastNode;
    }

    public int getF() {
        return g + h;
    }

    public Point position() {
        return position;
    }

    public int getG() {
        return g;
    }

    public int getH() {
        return h;
    }

    public Node getPriorNode() {
        return priorNode;
    }

}
