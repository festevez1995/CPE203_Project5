import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.*;

class AStarPathingStrategy implements PathingStrategy{

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {
        List<Point> path = new ArrayList<>(); //changed this to arraylist from LinkedList bc LL is O(N)
        /*ArrayList<Node> closedList = new ArrayList<>();
        ArrayList<Node> openList = new ArrayList<>();*/
        Map<Point,Node> closedMap = new HashMap<>(); // changed to HashMap because lookup is O(1) search vs O(N) search time
        Map<Point,Node> openMap = new HashMap<>();
        Queue<Node> openList = new PriorityQueue<>(Comparator.comparingInt(Node::getF));

        Node top_node = new Node(0,heurestic(start,end), 0 + heurestic(start,end), start, null);
        Node curr_node = top_node;
        openList.add(top_node);
        while (!curr_node.getPos().adjacent(end) && closedMap.size() < 250) {
            curr_node = openList.remove();
            /*checking end point*/
            if (withinReach.test(curr_node.getPos(), end)) {
                return computedPath(path, curr_node);
            }

            List<Point> neighbors = potentialNeighbors.apply(curr_node.getPos())
                    .filter(canPassThrough)
                    .filter(p -> !p.equals(start) && !p.equals(end)).collect(Collectors.toList());
            //ArrayList<Node> neighborNodes = new ArrayList<>(); //No need for extra data structures if using a map

            int temp_g = curr_node.getG() + 1;
           /* for (Point p : neighbors) {
                neighborNodes.add(new Node(temp_g, heurestic(p, end), 0 + heurestic(start, end), p, curr_node));
            }*/
            /*for (Node neighborN : neighborNodes) {*/ //was able to take it off because it was O(N) time
            for (Point neighbor : neighbors) {
               /*check if not in closed list*/
                if (!closedMap.containsKey(neighbor)) {
                    int temp_n = heurestic(neighbor, end);
                    if (openMap.containsKey(neighbor)) {
                        if (temp_g < openMap.get(neighbor).getG()) {
                            Node betterNode = new Node(temp_g, heurestic(neighbor, end), heurestic(neighbor, end) + temp_n, neighbor, curr_node);
                                /*neighborN.setG(neighborN.getG());
                                neighborN.setH(temp_n);
                                neighborN.setF(neighborN.getG() + temp_n);
                                neighborN.setPrev(curr_node);*/
                            openList.add(betterNode);
                            openList.remove(openMap.get(neighbor));
                            openMap.replace(neighbor, betterNode);
                        }
                    } else {
                        Node neighborNode = new Node(curr_node.getG() + 1, heurestic(neighbor, end), curr_node.getG() + 1 + heurestic(neighbor, end), neighbor, curr_node);
                        openList.add(neighborNode);
                        openMap.put(neighbor, neighborNode);

                    }
                }
                closedMap.put(curr_node.getPos(), curr_node);
            }
        }
        //}
        return path;
    }


    public List<Point> computedPath(List<Point> compPath, Node goal)
    {
        compPath.add(goal.getPos());
        if(goal.getPrev() == null)
        {
            Collections.reverse(compPath);
            return compPath;
        }
        return computedPath(compPath, goal.getPrev());
    }

    public int heurestic(Point current, Point goal)
    {
        return Point.distanceSquared(current,goal);
    }


}