package com.enjoygreenlife.guanguanbao.model.ViewModel.DrawCashList;

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
 * Created by luthertsai on 2017/12/23.
 */

public class DrawCashListAdapter extends BaseAdapter {

    private LayoutInflater _myInflater;
    private List<DrawCashItem> _drawCashItemList;
    private Context _context;

    public DrawCashListAdapter(Context context, List<DrawCashItem> drawCashItems) {
        _context = context;
        _myInflater = LayoutInflater.from(context);
        this._drawCashItemList = drawCashItems;
    }

    @Override
    public int getCount() {
        return _drawCashItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return _drawCashItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return _drawCashItemList.indexOf(getItem(i));
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DrawCashItem drawCashItem = (DrawCashItem) getItem(i);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = _myInflater.inflate(R.layout.draw_cash_selection_list_item, parent, false);
            viewHolder.textViewCost = (TextView) convertView.findViewById(R.id.title_cost_points);
            viewHolder.textViewRealMoney = (TextView) convertView.findViewById(R.id.title_real_value);
            viewHolder.imageViewCoin = (ImageView) convertView.findViewById(R.id.img_coin_single);
            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.textViewCost.setText(drawCashItem.getCost() + _context.getResources().getString(R.string.unit_draw_cash_points));
        viewHolder.textViewRealMoney.setText(drawCashItem.getRealMoney()+ _context.getResources().getString(R.string.unit_draw_cash_money));
        viewHolder.imageViewCoin.setImageResource(R.drawable.img_coin_single);
        // Return the completed view to render on screen
        return result;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView textViewCost;
        TextView textViewRealMoney;
        ImageView imageViewCoin;
    }
}
