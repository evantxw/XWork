package com.evan.xtool.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by evan on 2015/7/25.
 */
public class ViewHolder {

    private SparseArray<View> mViews; //Item中view控件的集合
    private View mConvertView; //adapter中的convertView
    private int mPosition; //adapter中的位置

    /**
     * 实例化对象
     * @param context
     * @param parent
     * @param layoutId
     * @param position
     */
    private ViewHolder(Context context, ViewGroup parent,int layoutId, int position){
        mPosition = position;
        mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId,parent,false);
        mConvertView.setTag(this);

    }

    /**
     * 初始化ViewHolder
     * @param context
     * @param convertView
     * @param parent
     * @param layoutId
     * @param position
     * @return
     */
    public static ViewHolder get(Context context, View convertView, ViewGroup parent,int layoutId, int position){
        if(convertView == null){
            return new ViewHolder(context,parent,layoutId,position);
        }else{
            return (ViewHolder)convertView.getTag();
        }
    }

    /**
     * 返回Adapter中的convertView
     * @return
     */
    public View getConvertView(){
        return mConvertView;
    }

    /**
     * 通过viewId 获取控件
     * @param resId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int resId){
        View view = mViews.get(resId);
        if(view == null){
            view = mConvertView.findViewById(resId);
            mViews.put(resId,view);
        }
        return (T)view;
    }
}
