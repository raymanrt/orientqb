package it.raymanrt.orient.query;

/**
 * Created by Riccardo Tasso on 29/12/14.
 */
public class Ordering {
    private final Projection projection;
    private final Order order;

    public enum Order {
        ASC,
        DESC
    }

    public Ordering(Projection projection, Order order) {
        this.projection = projection;
        this.order = order;
    }

    public String toString() {
        return projection.toString() + " " + order.toString();
    }
}
