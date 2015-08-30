package trackandhack.trackandhackprototype_2.Classes;

import java.util.Date;

/**
 * Created by andrewdorsett on 8/15/15.
 */
public class GiftCard extends Goal {
    String digits;
    Double fee;

    static public GiftCard clone(GiftCard original) {
        return new GiftCard(
                original.getCurrentAmount(),
                null,
                original.getEndDate(),
                original.getFee(),
                original.getInitialAmount(),
                original.getStartDate(),
                GiftCardStatus.OPEN,
                original.getTitle(),
                original.getNotes()
        );

    }

    public GiftCard() {
        this(0.0, "0", null, 0.0, 0.0, null, GiftCardStatus.OPEN, "", null);
    }

    public GiftCard(Double currentAmount, String digits, Date endDate, Double fee, Double initialAmount, Date startDate, GiftCardStatus status, String title, String notes) {
        super(currentAmount, endDate, initialAmount, startDate, status, title, notes);
        this.digits = digits;
        this.fee = fee;
    }

    public GiftCard(Long id, Double currentAmount, String digits, Date endDate, Double fee, Double initialAmount, Date startDate, GiftCardStatus status, String title, String notes) {
        super(id, currentAmount, endDate, initialAmount, startDate, status, title, notes);
        this.digits = digits;
        this.fee = fee;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public String getDigits() {
        return digits;
    }

    @Override
    public String getDisplayTitle() {
        return title + " - x" + digits;
    }

    public void setDigits(String digits) {
        this.digits = digits;
    }
}
