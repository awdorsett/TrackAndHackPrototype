package trackandhack.trackandhackprototype_2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import trackandhack.trackandhackprototype_2.Classes.GiftCard;
import trackandhack.trackandhackprototype_2.Classes.Goal;
import trackandhack.trackandhackprototype_2.Classes.Group;

/**
 * Created by andrewdorsett on 8/15/15.
 */
public class DataProvider {
    public static Group getTempGroup() {

        return new Group("Gift Cards", new ArrayList<GiftCard>());
    }
}
