package com.example.swaraj.bloodbook;

/**
 * Created by SWARAJ on 29-10-2017.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Dell on 16-10-17.
 */

public class AdapterDonor extends BaseExpandableListAdapter {
    private Context ctx;
    private HashMap<String,List<String>> plasma;
    private List<String> DocList;

    public AdapterDonor(Context ctx,HashMap<String,List<String>> plasma,List<String> DocList)
    {
        this.ctx=ctx;
        this.plasma=plasma;
        this.DocList=DocList;
    }

    @Override
    public int getGroupCount() {
        return this.DocList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.plasma.get(this.DocList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.DocList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.plasma.get(this.DocList.get(groupPosition)).get(childPosition);
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String parent_title=(String) getGroup(groupPosition);
        if(convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.parent_layout,parent,false);
        }
        TextView parent_textview =(TextView) convertView.findViewById(R.id.parent_txt);
        parent_textview.setTypeface(null, Typeface.BOLD);
        parent_textview.setText(parent_title);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String child_title=(String) getChild(groupPosition,childPosition);

        if(convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.child_layout,parent,false);
        }
        TextView child_textview =(TextView) convertView.findViewById(R.id.child_txt);
        child_textview.setText(child_title);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
