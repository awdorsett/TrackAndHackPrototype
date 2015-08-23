package trackandhack.trackandhackprototype_2.Classes;

import java.util.Date;

/**
 * Created by andrewdorsett on 8/23/15.
 */
public class MinSpend extends Goal {

    protected MinSpend(Double currentAmount, Date endDate, Double initialAmount, Date startDate, Status status, String title) {
        super(currentAmount, endDate, initialAmount, startDate, status, title);
    }
}
