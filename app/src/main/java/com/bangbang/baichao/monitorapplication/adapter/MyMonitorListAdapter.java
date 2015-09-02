package com.bangbang.baichao.monitorapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bangbang.baichao.monitorapplication.R;
import com.bangbang.baichao.monitorapplication.entity.ManagerVO;
import com.bangbang.baichao.monitorapplication.entity.MonitorVO;

import java.util.ArrayList;

/**
 * Created by baichao on 2015/8/17.
 */
public class MyMonitorListAdapter extends BaseAdapter {

    private ArrayList<MonitorVO> mArrayList;
    private Context mContext;

    public MyMonitorListAdapter(Context mContext) {
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
    public MonitorVO getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.monitor_item, null);
            wHolder = new ViewHolder();
            wHolder.serveView = (TextView) convertView.findViewById(R.id.serve);
            wHolder.pathView = (TextView) convertView.findViewById(R.id.path);
            wHolder.intervalView = (TextView) convertView.findViewById(R.id.interval);
            wHolder.threshold = (TextView) convertView.findViewById(R.id.threshold);
            wHolder.managerView = (TextView) convertView.findViewById(R.id.manager);
            convertView.setTag(wHolder);
        } else {
            wHolder = (ViewHolder) convertView.getTag();
        }
        MonitorVO vo = mArrayList.get(position);
        wHolder.serveView.setText(vo.getServe());
        wHolder.pathView.setText(vo.getPath());
        wHolder.intervalView.setText(vo.getInterval());
        wHolder.threshold.setText(vo.getThreshold());
        wHolder.managerView.setText(vo.getName());
        return convertView;
    }

    private static final class ViewHolder {
        public TextView serveView;
        public TextView pathView;
        public TextView intervalView;
        public TextView threshold;
        public TextView managerView;
    }

    public void setListData(ArrayList<MonitorVO> al) {
        mArrayList = al;
        notifyDataSetChanged();
    }
}
