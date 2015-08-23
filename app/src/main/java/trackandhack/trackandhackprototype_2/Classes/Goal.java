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
    Double initialAmount;
    Date startDate;
    Status status;
    String title;
    Long uid;

    protected Goal(Double currentAmount, Date endDate, Double initialAmount, Date startDate, Status status, String title) {
        this.currentAmount = currentAmount;
        this.endDate = endDate;
        this.initialAmount = initialAmount;
        this.startDate = startDate;
        this.status = status;
        this.title = title;
    }

    protected Goal(Long uid, Double currentAmount, Date endDate, Double initialAmount, Date startDate, Status status, String title) {
        this.currentAmount = currentAmount;
        this.endDate = endDate;
        this.initialAmount = initialAmount;
        this.startDate = startDate;
        this.status = status;
        this.title = title;
        this.uid = uid;
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

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    @Override
    public int hashCode() {
        if (uid != null) {
            return uid.hashCode();
        }
        return -1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Goal && ((Goal) obj).getUid() != -1) {
            return obj.hashCode() == hashCode();
        }

        return false;
    }
}
