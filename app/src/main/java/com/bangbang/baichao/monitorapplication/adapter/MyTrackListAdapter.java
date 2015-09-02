package com.bangbang.baichao.monitorapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bangbang.baichao.monitorapplication.R;
import com.bangbang.baichao.monitorapplication.entity.ManagerVO;
import com.bangbang.baichao.monitorapplication.entity.TrackVO;

import java.util.ArrayList;

/**
 * Created by baichao on 2015/8/17.
 */
public class MyTrackListAdapter extends BaseAdapter {

    private ArrayList<TrackVO> mArrayList;
    private Context mContext;

    public MyTrackListAdapter(Context mContext) {
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
    public TrackVO getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.track_list_table, null);
            wHolder = new ViewHolder();
            wHolder.serve = (TextView) convertView.findViewById(R.id.serve);
            wHolder.time = (TextView) convertView.findViewById(R.id.time);
            wHolder.monitorCount = (TextView) convertView.findViewById(R.id.monitorcount);
            wHolder.failCount = (TextView) convertView.findViewById(R.id.failcount);
            convertView.setTag(wHolder);
        } else {
            wHolder = (ViewHolder) convertView.getTag();
        }
        TrackVO vo = mArrayList.get(position);
        wHolder.serve.setText(vo.getServe());
        wHolder.time.setText(vo.getTime());
        wHolder.monitorCount.setText(vo.getMonitorCount());
        wHolder.failCount.setText(vo.getFailCount());
        return convertView;
    }

    private static final class ViewHolder {
        public TextView serve;
        public TextView time;
        public TextView monitorCount;
        public TextView failCount;
    }

    public void setListData(ArrayList<TrackVO> al) {
        mArrayList = al;
        notifyDataSetChanged();
    }
}
