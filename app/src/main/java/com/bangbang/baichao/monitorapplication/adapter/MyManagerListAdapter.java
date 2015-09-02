package com.bangbang.baichao.monitorapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bangbang.baichao.monitorapplication.R;
import com.bangbang.baichao.monitorapplication.entity.ManagerVO;

import java.util.ArrayList;

/**
 * Created by baichao on 2015/8/17.
 */
public class MyManagerListAdapter extends BaseAdapter {

    private ArrayList<ManagerVO> mArrayList;
    private Context mContext;

    public MyManagerListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        int size = 0;
        if (mArrayList != null)
            size = mArrayList.size();
        return size;
    }

    @Override
    public ManagerVO getItem(int position) {
        return mArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder wHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.manager_list_table, null);
            wHolder = new ViewHolder();
            wHolder.nameView = (TextView) convertView.findViewById(R.id.name);
            wHolder.telView = (TextView) convertView.findViewById(R.id.phone);
            wHolder.mailView = (TextView) convertView.findViewById(R.id.mail);
            convertView.setTag(wHolder);
        } else {
            wHolder = (ViewHolder) convertView.getTag();
        }
        ManagerVO vo = mArrayList.get(position);
        wHolder.nameView.setText(vo.getmMangerName());
        wHolder.telView.setText(vo.getmManagerTel());
        wHolder.mailView.setText(vo.getmManagerMail());
        return convertView;
    }

    private static final class ViewHolder {
        public TextView nameView;
        public TextView telView;
        public TextView mailView;
    }

    public void setListData(ArrayList<ManagerVO> al) {
        mArrayList = al;
        notifyDataSetChanged();
    }
}
