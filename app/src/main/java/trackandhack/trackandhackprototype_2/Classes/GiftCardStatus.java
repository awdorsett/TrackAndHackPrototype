package trackandhack.trackandhackprototype_2.Classes;

/**
 * Created by andrewdorsett on 8/15/15.
 */
public enum GiftCardStatus implements Status {
    OPEN("open"),
    CLOSED("closed");

    String name;

    GiftCardStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
