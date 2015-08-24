package trackandhack.trackandhackprototype_2.Classes;

import java.util.Date;

/**
 * Created by andrewdorsett on 8/23/15.
 */
public class MinSpend extends Goal {
    Double bonus;
    BonusType bonusType;

    public MinSpend() {
        this(0.0, null, 0.0, null, MinSpendStatus.OPEN, "", null);
    }


    public MinSpend(Double currentAmount, Date endDate, Double initialAmount, Date startDate, Status status, String title, String notes) {
        this(currentAmount, endDate, initialAmount, startDate, status, title, notes, null);
    }

    public MinSpend(Double currentAmount, Date endDate, Double initialAmount, Date startDate, Status status, String title, String notes, Double bonus) {
        super(currentAmount, endDate, initialAmount, startDate, status, title, notes);
        this.bonus = bonus;
    }

    public Double getBonus() {
        return bonus;
    }

    public void setBonus(Double bonus) {
        this.bonus = bonus;
    }
}
