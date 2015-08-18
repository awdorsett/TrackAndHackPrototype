package trackandhack.trackandhackprototype_2.Classes;

import java.util.Date;

/**
 * Created by andrewdorsett on 8/15/15.
 */
public class GiftCard extends Goal {
    Integer digits;
    Double fee;

    public GiftCard(Double currentAmount, Integer digits, Date endDate, Double fee, Double initialAmount, Date startDate, GiftCardStatus status, String title) {
        super(currentAmount, endDate, initialAmount, startDate, status, title);
        this.digits = digits;
        this.fee = fee;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public Integer getDigits() {
        return digits;
    }

    public void setDigits(Integer digits) {
        this.digits = digits;
    }

    public Double adjustCurrentAmount(Double adjustment) {
        currentAmount = Math.max(0.0, currentAmount + adjustment);
        currentAmount = Math.min(initialAmount, currentAmount);

        return currentAmount;
    }
}
