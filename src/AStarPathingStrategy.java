import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy
{
    private Point goal;

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        HashMap closed = new HashMap();
        PriorityQueue<Point> open = new PriorityQueue(10, new Comparator<Point>() {
            public int compare(Point o1, Point o2) {
                return (int)( (manhattanDistance(start, o1.previous) + manhattanDistance(end, o1) )
                        - (manhattanDistance(start, o2.previous) + manhattanDistance(end, o2)) );
            }
        });
        List<Point> path = new LinkedList<Point>();
        if (!open.contains(start) ){ open.add(start);}
        Point current = start;
        current.previous = current;
        while(!withinReach.test(current, end)) {
            List<Point> validAdj = potentialNeighbors.apply(current)
                    .filter(canPassThrough)
                    .filter(pt -> !closed.containsKey(pt))
                    .limit(8)
                    .collect(Collectors.toList());
            for (Point pt : validAdj) {
                pt.previous = current;
                /*if (open.contains(pt)) {
                    open.remove(pt);
                }*/
                open.add(pt);
            }
            closed.put(current, manhattanDistance(start, current.previous) + manhattanDistance(current, end) +1);
            open.remove(current);
            current = open.peek();
            if (current == null){
                return new LinkedList<Point>();
            }
            goal = current;
        }
        Point pt = goal;
        while(pt != start){
            path.add(0, pt);
            pt = pt.previous;
        }
        return path;
    }

    private double manhattanDistance(Point start, Point end){
        return Math.abs(start.x - end.x) + Math.abs(start.y - end.y);
    }
}
