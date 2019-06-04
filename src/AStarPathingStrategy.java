import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy {
    public List<Point> computePath(Point start, Point end, Predicate<Point> canPassThrough, BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {
        List<Point> path = new LinkedList<>();
        PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparing(Node::getF));
        HashMap<Point, Node> openListMap = new HashMap<>();
        HashMap<Point, Node> closedList = new HashMap<>();
        HashMap<Point, Node> camefrom = new HashMap<>();

        Node last = null;
        Node beginning = new Node(start, 0, heurestic(start, end), null); // starting node
        openList.add(beginning);// adds the start to openList

        while (!openList.isEmpty()) {

            Node current = openList.poll();//smallest F value is set to the current Node

            //checks to see if we are in reach of the end goal
            if (withinReach.test(current.position(), end)) {
                path.add(current.position());
                return buildPath(camefrom, current);
            }

            //retrives all neighbors for current node
            List<Point> neighbors = potentialNeighbors.apply(current.position()).filter(canPassThrough)
                    .filter(p -> !p.equals(start) && !p.equals(end)).collect(Collectors.toList());

            //Analyzes all adjacent nodes that are not in the closed List
            for (Point neighborPoint : neighbors) {

                Node nextNode = new Node(neighborPoint, current.getG() + 1, heurestic(neighborPoint, end), current);
                if (!closedList.containsKey(neighborPoint)) {// checks to see if we havent all ready evealuated the node
                    //checks to see if point is all ready in the open list
                    //checks to see if the new g is better than the one all ready in the openlist
                    if (openListMap.containsKey(neighborPoint) && (current.getG() + 1) <= openListMap.get(neighborPoint).getG()) {
                        Node newNode = new Node(openListMap.get(neighborPoint).position(), current.getG() + 1,
                                openListMap.get(neighborPoint).getH(), current);

                        openList.remove(openListMap.get(neighborPoint).position());
                        openList.add(newNode);
                        openListMap.replace(neighborPoint, newNode);
                    }
                    camefrom.put(neighborPoint, nextNode);
                    openList.add(nextNode);
                    openListMap.put(neighborPoint, nextNode);
                }
            }

            last = current;
            closedList.put(current.position(), current);// current node is added to closed list
            openListMap.remove(current.position());

        }
        return buildPath(camefrom, last);
    }

    public int heurestic(Point p, Point end) {
        return Math.abs(end.x - p.x) + Math.abs(end.y - p.y);
    }

    private List<Point> buildPath(Map<Point, Node> camefrom, Node current) {
        List<Point> totalPath = new ArrayList<>();
        totalPath.add(current.position());
        while (camefrom.containsKey(current.getPriorNode().position())) {
            current = camefrom.get(current.getPriorNode().position());
            if (current.getPriorNode() == null)
                return totalPath;
            totalPath.add(current.position());
        }
        Collections.reverse(totalPath);
        return totalPath;
    }
}
