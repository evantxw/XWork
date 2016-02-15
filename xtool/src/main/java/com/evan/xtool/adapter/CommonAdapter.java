package com.evan.xtool.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;


/**
 * 通过Adapter
 * Created by evan on 2015/7/25.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    private Context mContext;
    private int layoutId;
    private List<T> mDatas;

    public CommonAdapter(Context context, List<T> datas, int layoutId){
        this.mContext = context;
        this.layoutId = layoutId;
        mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.get(mContext,convertView,parent,layoutId,position);

        T t = mDatas.get(position);

        convert(holder,t);
        return holder.getConvertView();
    }

    //实现此方法来为每个Item设置布局
    protected abstract void convert(ViewHolder holder, T t);
}
