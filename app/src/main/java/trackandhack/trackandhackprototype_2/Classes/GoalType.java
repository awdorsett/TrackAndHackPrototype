package trackandhack.trackandhackprototype_2.Classes;

/**
 * Created by andrewdorsett on 8/15/15.
 */
public enum GoalType {
    GIFT_CARD("gift_card"),
    MIN_SPEND("min_spend");

    private final String name;

    GoalType(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
