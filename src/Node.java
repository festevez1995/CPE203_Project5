/* A-Star */
class Node {
    private int f; //total distance, Remember: f=g+h
    private int g; //distance from start
    private int h; //heursitc distance
    private Node prev_node;
    private Point p;

    public Node (int g, int h, int f, Point p, Node prev_node){
        this.g = g;
        this.h = h;
        this.f = f;
        this.p = p;
        this.prev_node = prev_node; //root
    }

    public int getH()
    {
        return h;
    }
    public int getF()
    {
        return f;
    }
    public void setG(int g)
    {
        this.g = g;
    }
    public void setH(int h)
    {
        this.h = h;
    }
    public int getG()
    {
        return g;
    }
    public void setF(int f) {this.f = f;}
    public void setPos(Point p)
    {
        p = p;
    }
    public Point getPos() {return p;}
    public void setPrev(Node prev_node) {this.prev_node = prev_node;}

    public Node getPrev()
    {
        return prev_node;
    }
    public String toString() {return ("Node x: " + p.x + ", y: " + p.y);}
}