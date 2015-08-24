package trackandhack.trackandhackprototype_2.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import trackandhack.trackandhackprototype_2.Classes.GiftCard;
import trackandhack.trackandhackprototype_2.Classes.GoalType;
import trackandhack.trackandhackprototype_2.Classes.Group;
import trackandhack.trackandhackprototype_2.Classes.MinSpend;

/**
 * Created by andrewdorsett on 8/23/15.
 */
public class GroupModule {
    static Group giftCards = new Group("Gift Cards", new ArrayList<GiftCard>(), GroupType.GIFT_CARD, GoalType.GIFT_CARD);
    static Group minSpends = new Group("Minimum Spends", new ArrayList<MinSpend>(), GroupType.MIN_SPEND, GoalType.MIN_SPEND);

    public static HashMap<GroupType, GoalType> goalMap = new HashMap<>();
    public static List<Group> groups = new ArrayList<>(Arrays.asList(giftCards, minSpends));

    public static enum GroupType {
        GIFT_CARD("Gift Card"),
        MIN_SPEND("Min Spend");
        private final String name;

        GroupType(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
