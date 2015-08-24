package trackandhack.trackandhackprototype_2.Classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import trackandhack.trackandhackprototype_2.Module.GroupModule;

/**
 * Created by andrewdorsett on 8/15/15.
 */
public class Group implements Serializable{
    List<?> goalList;
    String title;
    GroupModule.GroupType type;
    GoalType goalType;


   public <T> Group(String title, List<T> goalList, GroupModule.GroupType type, GoalType goalType) {
       this.goalList = goalList;
       this.title = title;
       this.type = type;
       this.goalType = goalType;
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

    public GroupModule.GroupType getType() {
        return type;
    }

    public void setType(GroupModule.GroupType type) {
        this.type = type;
    }

    public GoalType getGoalType() {
        return goalType;
    }

    public void setGoalType(GoalType goalType) {
        this.goalType = goalType;
    }
}
