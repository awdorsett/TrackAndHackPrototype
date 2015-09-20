package trackandhack.trackandhackprototype_2.Classes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * Created by andrewdorsett on 9/19/15.
 */
public class HistoryItem implements Serializable {
    Long uid = -1L;
    Long goalId;
    Date date = null;
    Double amount = 0.00;
    String notes = "";

    public HistoryItem() {}

    public HistoryItem(Long uid, Long goalId, Date date, Double amount, String notes) {
        this.uid = uid;
        this.goalId = goalId;

        this.date = date;
        this.amount = amount;
        this.notes = notes;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        BigDecimal bigDecimal = new BigDecimal(amount);
        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_DOWN);
        this.amount = bigDecimal.doubleValue();
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getUid() {
        return uid;
    }
    public void setUid(Long uid) {
         this.uid = uid;
    }


    public Long getGoalId() {
        return goalId;
    }

    public void setGoalId(Long goalId) {
        this.goalId = goalId;
    }
}
