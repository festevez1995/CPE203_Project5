import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy {
    public static int heurestic(Point current, Point goal) {
        return current.distanceSquared(goal);
    }

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {
        List<Point> path = new LinkedList<>();
        ArrayList<Node> closedList = new ArrayList<>();
        ArrayList<Node> openList = new ArrayList<>();
        /*top node*/
        Node top_node = new Node(start, 0, heurestic(start, end), null);
        Node curr_node = top_node;
        openList.add(top_node);


        while (!curr_node.getPos().adjacent(end) && closedList.size() < 250) {
            List<Point> neighbors = potentialNeighbors.apply(curr_node.getPos())
                    .filter(canPassThrough)
                    .filter(p -> !p.equals(start) && !p.equals(end)).collect(Collectors.toList());

            ArrayList<Node> neighborNodes = new ArrayList<>();
            int temp_g = curr_node.getG() + 1;
            for (Point p : neighbors) {
                neighborNodes.add(new Node(p, temp_g, heurestic(p, end), curr_node));
            }

            for (Node neighborN : neighborNodes) {
                if (!checkList(closedList, neighborN)) {
                    if (!checkList(openList, neighborN)) {
                        openList.add(neighborN);
                    }
                    int temp_n = heurestic(neighborN.getPos(), end);
                    for (Node betterNode : openList) {
                        if (betterNode.getPos().equals(neighborN.getPos())) {
                            if (neighborN.getG() < betterNode.getG()) {
                                betterNode.setG(neighborN.getG());
                                betterNode.setH(temp_n);
                                betterNode.setF(neighborN.getG() + temp_n);
                                neighborN.setPrev(curr_node);
                            }
                        }
                    }

                }

            }
            /*update lists*/
            openList.remove(curr_node);
            closedList.add(curr_node);
            Node fastF = new Node(curr_node.getPos(), 0, 0, curr_node.getPrev());
            fastF.setF(9999999);//make it big as all squares to start finding small path
            for (Node curOL : openList) {
                if (curOL.getF() < fastF.getF()) {
                    fastF = curOL;
                }
            }
            curr_node = fastF;
        }
        if (!curr_node.getPos().adjacent(end)) {
            return Collections.emptyList();
        }
        while (!curr_node.getPos().equals(start)) {
            return computedPath(path, curr_node);
        }
        return path;

    }

    public List<Point> computedPath(List<Point> compPath, Node goal) {
        compPath.add(goal.getPos());
        if (goal.getPrev() == null) {
            Collections.reverse(compPath);
            return compPath;
        }
        return computedPath(compPath, goal.getPrev());
    }

    public boolean checkList(List<Node> nodeList, Node node) {
        for (Node cur : nodeList) {
            if (cur.equals(node)) {
                return true;
            }
        }
        return false;
    }
}
