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
    PriorityQueue<Point> open = new PriorityQueue(10, new Comparator<Point>() {
        public int compare(Point o1, Point o2) {
            return (int)( (manhattanDistance(begin, o1) + manhattanDistance(last, o1))
                    - (manhattanDistance(begin, o2) + manhattanDistance(last, o2)) );
        }
    });
    Point begin = null;
    Point last = null;

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        if (begin == null){begin = start;}
        if (last == null){last = end;}
        List<Point> path = new LinkedList<Point>();

        if (!open.contains(begin) ){ open.add(begin);}
        Point current = start;

            List<Point> validAdj = potentialNeighbors.apply(current)
                    .filter(canPassThrough)
                    .filter(pt -> !closed.containsKey(pt))
                    .limit(8)
                    .collect(Collectors.toList());

            double f = manhattanDistance(open.peek(), begin) + manhattanDistance(open.peek(), end);
            for (Point pt : validAdj) {
                if (!open.contains(pt)) {
                    open.add(pt);
                }
                double g = manhattanDistance(begin, current) + 1;
                double h = manhattanDistance(pt, end);
                if (g + h < f) {
                    f = g + h;
                }

            }
            closed.put(current, manhattanDistance(begin, current) + manhattanDistance(current, end) + 1);
            open.remove(current);
            current = open.peek();
            if (current != null){
            path.add(current);}
        return path;
    }

    private double manhattanDistance(Point start, Point end){
        return Math.abs(start.x - end.x) + Math.abs(start.y - end.y);
    }
}
