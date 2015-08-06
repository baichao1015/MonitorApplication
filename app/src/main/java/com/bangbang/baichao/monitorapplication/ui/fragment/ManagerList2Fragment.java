package com.bangbang.baichao.monitorapplication.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bangbang.baichao.monitorapplication.R;
import com.bangbang.baichao.monitorapplication.entity.ManagerVO;
import com.bangbang.baichao.monitorapplication.proxy.ManagerListProxy;
import com.bangbang.baichao.monitorapplication.utils.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManagerList2Fragment extends BaseFragment {

    private PullToRefreshListView mList;
    private ManagerListProxy mProxy;
    private ManagerListAdapter mAdapter;
    private ArrayList<ManagerVO> mPuyArray = new ArrayList<ManagerVO>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }

    class ManagerListAdapter extends ArrayAdapter<ManagerVO> {
        private int mResourceId;

        public ManagerListAdapter(Context context, int resource, List<ManagerVO> objects) {
            super(context, resource, objects);
            mResourceId = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ManagerVO managerVO = getItem(position);
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(mResourceId, null);
            TextView nameText = (TextView) view.findViewById(R.id.name);
            TextView phoneText = (TextView) view.findViewById(R.id.phone);
            TextView mailText = (TextView) view.findViewById(R.id.mail);
            nameText.setText(managerVO.getmMangerName());
            phoneText.setText(managerVO.getmManagerTel());
            mailText.setText(managerVO.getmManagerMail());
            return view;
        }
    }


}
