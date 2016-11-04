package xyz.damonwong.todolist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Damon on 4/11/2016.
 */

public class ItemsAdapter extends BaseAdapter {
    private LayoutInflater myInflater;
    private List<Items> items;

    public ItemsAdapter(Context context, List<Items> itema){
        myInflater = LayoutInflater.from(context);
        this.items=itema;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            convertView = myInflater.inflate(R.layout.item, null);
            holder = new ViewHolder(
                    (TextView) convertView.findViewById(R.id.item)
            );
            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        Items item= (Items)getItem(position);
        holder.txtItemName.setText(item.getCont());

        return convertView;
    }

    private class ViewHolder {
        TextView txtItemName;
        public ViewHolder(TextView txtItemName){
            this.txtItemName = txtItemName;
        }
    }

}
