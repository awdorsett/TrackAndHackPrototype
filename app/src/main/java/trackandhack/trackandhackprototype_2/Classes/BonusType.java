package trackandhack.trackandhackprototype_2.Classes;

/**
 * Created by andrewdorsett on 8/15/15.
 */
public enum BonusType {
    CASH("cash"),
    POINTS("points");

    String name;

    BonusType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
