package trackandhack.trackandhackprototype_2.Classes;

import java.io.Serializable;

/**
 * Created by andrewdorsett on 9/19/15.
 */
public class HistoryItem implements Serializable {
    Long id;
    String date = "";
    Double amount = 0.00;
    String notes = "";

    public HistoryItem() {}
    public HistoryItem(Long id, String date, Double amount, String notes) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.notes = notes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }
}
