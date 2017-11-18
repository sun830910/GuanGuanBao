package com.enjoygreenlife.guanguanbao.model.ViewModel.SettingList;

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
    private LayoutInflater _myInflater;
    private List<SettingItem> _settingItemList;
    private Context _context;
    public SettingListAdapter(Context context, List<SettingItem> movie) {
        _context = context;
        _myInflater = LayoutInflater.from(context);
        this._settingItemList = movie;
    }

    @Override
    public int getCount() {
        return _settingItemList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return _settingItemList.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return _settingItemList.indexOf(getItem(position));
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
            convertView = _myInflater.inflate(R.layout.setting_list_item, parent, false);
            viewHolder.itemName = (TextView) convertView.findViewById(R.id.list_item_title);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.list_item_icon);
            viewHolder.arrow = (ImageView) convertView.findViewById(R.id.list_item_arrow);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.itemName.setText(settingItem.GetTitle());
        viewHolder.icon.setImageResource(_context.getResources().getIdentifier(settingItem.getImgResName(),"drawable","com.enjoygreenlife.guanguanbao"));
        viewHolder.arrow.setImageResource(R.drawable.ic_next);
        // Return the completed view to render on screen
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView itemName;
        ImageView icon;
        ImageView arrow;
    }
}