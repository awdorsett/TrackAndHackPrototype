package trackandhack.trackandhackprototype_2.Classes;

/**
 * Created by andrewdorsett on 8/15/15.
 */
public enum GoalType {
    GIFT_CARD("gift_card", GiftCardStatus.class);

    private final String name;
    private final Class goalClass;

    GoalType(final String name, final Class goalClass) {
        this.name = name;
        this.goalClass = goalClass;
    }

    public String getName() {
        return name;
    }

    public Class getGoalClass() {
        return this.goalClass;
    }
}
