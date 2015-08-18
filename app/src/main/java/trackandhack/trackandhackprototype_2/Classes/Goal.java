package trackandhack.trackandhackprototype_2.Classes;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Created by andrewdorsett on 8/15/15.
 */
public abstract class Goal implements Serializable {
    Double currentAmount;
    Date endDate;
    Status goalStatus;
    Double initialAmount;
    Date startDate;
    Status status;
    String title;
    UUID uid;

    protected Goal(Double currentAmount, Date endDate, Double initialAmount, Date startDate, Status status, String title) {
        this.currentAmount = currentAmount;
        this.endDate = endDate;
        this.goalStatus = goalStatus;
        this.initialAmount = initialAmount;
        this.startDate = startDate;
        this.status = status;
        this.title = title;
        uid = UUID.randomUUID();
    }

    public Double getCurrentAmount() {
        return currentAmount;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Status getGoalStatus() {
        return goalStatus;
    }

    public void setGoalStatus(Status goalStatus) {
        this.goalStatus = goalStatus;
    }

    public Double getInitialAmount() {
        return initialAmount;
    }

    public void setInitialAmount(Double initialAmount) {
        this.initialAmount = initialAmount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCurrentAmount(Double currentAmount) {
        this.currentAmount = currentAmount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public UUID getUid() {
        return uid;
    }

    @Override
    public int hashCode() {
        return uid.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Goal) {
            return obj.hashCode() == hashCode();
        }

        return false;
    }
}
