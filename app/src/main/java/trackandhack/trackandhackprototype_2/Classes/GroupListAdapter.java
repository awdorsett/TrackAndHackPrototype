package trackandhack.trackandhackprototype_2.Classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import trackandhack.trackandhackprototype_2.Activity.NewGiftCardActivity;
import trackandhack.trackandhackprototype_2.MainActivity;
import trackandhack.trackandhackprototype_2.R;

/**
 * Created by andrewdorsett on 8/15/15.
 */
public class GroupListAdapter extends BaseExpandableListAdapter {
    private Context ctx;
    private List<Group> groupList;

    public GroupListAdapter(Context ctx, List<Group> groupList) {
        this.ctx = ctx;
        this.groupList = groupList;
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
        String groupTitle = ((Group) getGroup(groupPosition)).getTitle();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_layout, parentView, false);
        }

        TextView groupView = (TextView) convertView.findViewById(R.id.groupListTitle);
        groupView.setText(groupTitle);
        groupView.setTextColor(Color.WHITE);

        Button newGiftCardButton = (Button) convertView.findViewById(R.id.createNewEntryButton);
        newGiftCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newGiftCardIntent = new Intent(v.getContext(), NewGiftCardActivity.class);
                ((Activity) ctx).startActivityForResult(newGiftCardIntent, 1);
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parentView) {
        String goalTitle = ((Goal) getChild(groupPosition, childPosition)).getTitle();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.goal_layout, parentView, false);
        }

        TextView childView = (TextView) convertView.findViewById(R.id.goalListElement);
        childView.setText(goalTitle);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
