import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy
{
    HashMap closed = new HashMap();

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        List<Point> path = new LinkedList<Point>();
        PriorityQueue open = new PriorityQueue(10, new Comparator<Point>() {
            public int compare(Point o1, Point o2) {
                return (int)(manhattanDistance(start, o1) + manhattanDistance(start, o2));
            }
        });


        if (!open.contains(start)){
        open.add(start);}
        Point current = start;

            List<Point> validAdj = potentialNeighbors.apply(current)
                    .filter(canPassThrough)
                    .filter(pt -> !closed.containsKey(pt))
                    .limit(4)
                    .collect(Collectors.toList());

            double f = manhattanDistance(current, end) + 2;
            Point next = null;
            for (Point pt : validAdj) {
                if (!open.contains(pt)) {
                    open.add(pt);
                }
                double g = manhattanDistance(start, current);
                double h = manhattanDistance(pt, end);
                if (g + h < f) {
                    f = g + h;
                    next = pt;
                }
                closed.put(pt, g + h);
                open.remove(pt);
            }
            current = next;
            if (current != null){
            path.add(current);}
        return path;
    }

    public double manhattanDistance(Point start, Point end){
        return Math.abs(start.x - end.x) + Math.abs(start.y - end.y);
    }
}
