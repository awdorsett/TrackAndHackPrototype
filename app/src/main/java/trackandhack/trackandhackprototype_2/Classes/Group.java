package trackandhack.trackandhackprototype_2.Classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrewdorsett on 8/15/15.
 */
public class Group implements Serializable{
    List<?> goalList;
    String title;


   public <T> Group(String title, List<T> goalList) {
        this.goalList = goalList;
        this.title = title;
    }

    <T> Group(String title, Class<T> goalClass) {
        this(title, new ArrayList<T>());
    }

    public List<?> getGoalList() {
        return goalList;
    }

    public void setGoalList(List<?> goalList) {
        this.goalList = goalList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
