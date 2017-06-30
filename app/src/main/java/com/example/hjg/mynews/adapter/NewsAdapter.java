package com.example.hjg.mynews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hjg.mynews.R;
import com.example.hjg.mynews.bean.NewsEntity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by HJG on 2017/6/29.
 */

public class NewsAdapter extends BaseAdapter {
    private List<NewsEntity.ResultBean> listDatas;

    public NewsAdapter(List<NewsEntity.ResultBean> listDatas, Context context) {
        this.listDatas = listDatas;
        this.context = context;
    }

    private Context context;


    /** 列表显示的新闻数据 */


    // 返回列表项视图，只要显示列表项时，就会调用此方法
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);
        if (type==ITEM_TYPE_WITH_1_IMAGE){
//创建列表项item视图
            if (convertView == null){
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.item_news_1,parent,false);
            }

            //获取列表项中的子控件
            ImageView iv01 = (ImageView) convertView.findViewById(R.id.iv_01);
            TextView tvNewsTitle = (TextView) convertView.findViewById(R.id.tv_news_title);
            TextView tvNewsFrom = (TextView) convertView.findViewById(R.id.tv_news_from);
            TextView tvCommentCount = (TextView) convertView.findViewById(R.id.tv_comment_count);
            //查找列表项对应的数据
            NewsEntity.ResultBean news = (NewsEntity.ResultBean) getItem(position);
            //显示列表项中的子控件
            tvNewsTitle.setText(news.getTitle());
            tvNewsFrom.setText(news.getSource());
            tvCommentCount.setText(news.getReplyCount() + "跟帖");

            //显示图片
            Picasso.with(context).load(news.getImgsrc()).into(iv01);
        }else {
//创建列表项item视图
            if (convertView == null){
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.item_news_2,parent,false);
            }

            //获取列表项中的子控件
            ImageView iv01 = (ImageView) convertView.findViewById(R.id.iv_01);
            ImageView iv02 = (ImageView) convertView.findViewById(R.id.iv_02);
            ImageView iv03 = (ImageView) convertView.findViewById(R.id.iv_03);
            TextView tvNewsTitle = (TextView) convertView.findViewById(R.id.tv_news_title);
            TextView tvCommentCount = (TextView) convertView.findViewById(R.id.tv_comment_count);
            //查找列表项对应的数据
            NewsEntity.ResultBean news = (NewsEntity.ResultBean) getItem(position);
            //显示列表项中的子控件
            tvNewsTitle.setText(news.getTitle());
            tvCommentCount.setText(news.getReplyCount() + "跟帖");

            //显示3图片
            Picasso.with(context).load(news.getImgsrc()).into(iv01);
            Picasso.with(context).load(news.getImgextra().get(0).getImgsrc()).into(iv02);
            Picasso.with(context).load(news.getImgextra().get(1).getImgsrc()).into(iv03);
        }


        return convertView;
    }
    // 有多少个列表项
    @Override
    public int getCount() {
        return listDatas== null ? 0: listDatas.size();
    }
    // 返回指定位置的列表项对应的实体数据（javabean）
    @Override
    public Object getItem(int position) {
        return listDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //================多种item的列表显示(begin)=======================
    private static final int ITEM_TYPE_WITH_1_IMAGE = 0;
    private static final int ITEM_TYPE_WITH_3_IMAGE = 1;

    @Override
    public int getItemViewType(int position) {
        NewsEntity.ResultBean news = (NewsEntity.ResultBean) getItem(position);
        if (news.getImgextra() == null || news.getImgextra().size() == 0) {
            // 只有一张图片的item
            return ITEM_TYPE_WITH_1_IMAGE;
        } else {
            // item中有三张图片
            return ITEM_TYPE_WITH_3_IMAGE;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }
    //================多种item的列表显示(end)=========================
    public void setData(List<NewsEntity.ResultBean> listDatas) {
        this.listDatas = listDatas;
        notifyDataSetChanged();
    }

    public void appendData(List<NewsEntity.ResultBean> listDatas) {
        this.listDatas.addAll(listDatas);
        notifyDataSetChanged();
    }

}
