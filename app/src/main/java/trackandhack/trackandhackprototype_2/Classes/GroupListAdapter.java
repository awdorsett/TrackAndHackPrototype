package trackandhack.trackandhackprototype_2.Classes;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.HashMap;
import java.util.List;

import trackandhack.trackandhackprototype_2.R;

/**
 * Created by andrewdorsett on 8/15/15.
 */
public class GroupListAdapter extends BaseExpandableListAdapter {
    private Context ctx;
    private List<Group> groupList;
    private HashMap<Group, Boolean> bulkDelete = new HashMap<>();
    private boolean showClosed = false;

    public GroupListAdapter(Context ctx, List<Group> groupList) {
        this.ctx = ctx;
        this.groupList = groupList;
        for (Group group : groupList) {
            bulkDelete.put(group, false);
        }
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return ((Group) getGroup(groupPosition)).getGoalList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<Goal> goalList = (List<Goal>) groupList.get(groupPosition).getGoalList();

        return goalList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parentView) {
        final Group group = ((Group) getGroup(groupPosition));

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_layout, parentView, false);
        }
        Button toggleBulkDelete = (Button) convertView.findViewById(R.id.bulkCloseButton);
        TextView groupView = (TextView) convertView.findViewById(R.id.groupListTitle);
        groupView.setText(group.getTitle());
        groupView.setTextColor(Color.WHITE);
        toggleBulkDelete.setEnabled(false);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parentView) {
        Goal goal = ((Goal) getChild(groupPosition, childPosition));

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.goal_layout, parentView, false);
        }

        TextView childView = (TextView) convertView.findViewById(R.id.goalListElement);
        ImageView icon = (ImageView) convertView.findViewById(R.id.goalListIcon);
        TextView currentAmount = (TextView) convertView.findViewById(R.id.currentText);
        TextView initialAmount = (TextView) convertView.findViewById(R.id.initialText);
        ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.goalProgressBar);
        ToggleButton closeToggle = (ToggleButton) convertView.findViewById(R.id.closeToggle);
        closeToggle.setTextOff("Close");
        closeToggle.setTextOn("Close");
        closeToggle.setText("Close");
        closeToggle.setVisibility(View.GONE);
        progressBar.setMax(goal.getInitialAmount().intValue());
        progressBar.setProgress(goal.getCurrentAmount().intValue());
        currentAmount.setText(goal.getCurrentAmount().toString());
        initialAmount.setText(goal.getInitialAmount().toString());
        icon.setImageResource(R.drawable.ic_card_giftcard_white_24dp);
        childView.setText(goal.getDisplayTitle());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void showClosed(boolean showClosed) {
        this.showClosed = showClosed;
    }
}
