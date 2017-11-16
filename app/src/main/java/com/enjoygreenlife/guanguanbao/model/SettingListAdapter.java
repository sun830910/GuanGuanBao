package com.enjoygreenlife.guanguanbao.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.enjoygreenlife.guanguanbao.R;

import java.util.List;

/**
 * Created by luthertsai on 2017/11/15.
 */

public class SettingListAdapter extends BaseAdapter {
    private LayoutInflater myInflater;
    private List<SettingItem> settingItemList;

    public SettingListAdapter(Context context, List<SettingItem> movie) {
        myInflater = LayoutInflater.from(context);
        this.settingItemList = movie;
    }

    @Override
    public int getCount() {
        return settingItemList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return settingItemList.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return settingItemList.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        SettingItem settingItem = (SettingItem) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = myInflater.inflate(R.layout.setting_list_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.list_item_title);
            viewHolder.info = (ImageView) convertView.findViewById(R.id.list_item_icon);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.txtName.setText(settingItem.GetTitle());
        // Return the completed view to render on screen
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        ImageView info;
    }
}