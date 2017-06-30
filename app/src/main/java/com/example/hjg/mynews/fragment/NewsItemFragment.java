package com.example.hjg.mynews.fragment;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.hjg.mynews.NewsDetailActivity;
import com.example.hjg.mynews.R;
import com.example.hjg.mynews.adapter.NewsAdapter;
import com.example.hjg.mynews.base.URLManager;
import com.example.hjg.mynews.bean.NewsEntity;
import com.google.gson.Gson;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.container.MeituanHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.w3c.dom.Text;

import java.net.URL;
import java.util.List;

/**
 * Created by HJG on 2017/6/28.
 */

public class NewsItemFragment extends BaseFragment{
    private TextView textView;
    private ListView listView;
    /** 新闻类别id */
    private String newsCategoryId;
    private NewsAdapter newsAdapter;
    private SpringView springView;
    private View headerView;
    private List<NewsEntity.ResultBean> listDatas;

    /** 设置新闻类别id */
    public void setNewsCategoryId(String newsCategoryId) {
        this.newsCategoryId = newsCategoryId;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_news_item;
    }

    @Override
    public void initView() {
        TextView textView = (TextView) mRoot.findViewById(R.id.tv_01);
        listView = (ListView) mRoot.findViewById(R.id.list_view);

        newsAdapter = new NewsAdapter(null, getContext());
        listView.setAdapter(newsAdapter);

        textView.setText("类别id:"+newsCategoryId);        // 显示新闻类别id，以作区分
        initSpringView();
    }
    //显示下拉刷新和加载更多的控件
    private void initSpringView() {
        springView = (SpringView) mRoot.findViewById(R.id.spring_view);
        //设置SpringView头部和尾部
        springView.setHeader(new MeituanHeader(getContext()));
        springView.setFooter(new DefaultFooter(getContext()));
        springView.setType(SpringView.Type.FOLLOW);

        //设置监听器
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                //下拉，刷新第一页数据
                /*showToast("下拉");*/
                getNewsDatas(true);
            }

            @Override
            public void onLoadmore() {
                //上拉，加载下一页数据
                /*showToast("上拉");*/
                getNewsDatas(false);
            }
        });
    }

    @Override
    public void initListener() {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   int index = position;

                    if (listView.getHeaderViewsCount()>0){
                        index = index-1;
                    }

                    //点击列表项，转跳到新闻详情界面
                    //方式1
                    NewsEntity.ResultBean news =listDatas.get(index);
                    //方式1
                    //NewsEntity.ResultBean news = (NewsEntity.ResultBean) parent.getItemAtPosition(position);
                    Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                    intent.putExtra("news", news);
                    startActivity(intent);
                }
            });
    }

    @Override
    public void initData() {
        //获取服务器新闻数据
        getNewsDatas(true);
    }

    /** 要加载第几页数据 */
    private int pageNo = 1;

    /**
     * 获取服务器新闻数据
     *
     * @param refresh true表示下拉刷新，false表示加载下一页数据
     *
     */
    private void getNewsDatas(final boolean refresh) {
        if (refresh)
            pageNo = 1;

        String newsUrl = URLManager.getUrl(newsCategoryId,pageNo);
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, newsUrl, new RequestCallBack<String>() {
            @Override        // 请求成功
            public void onSuccess(ResponseInfo<String> responseInfo) {
                // （1）服务器返回的json数据
                String json = responseInfo.result;
                System.out.println("---------json: " + json);

                // （2）解析json数据
                // 替换json字符串中的新闻类别id
                json = json.replace(newsCategoryId,"result");
                Gson gson = new Gson();

                // 使用到反射
                NewsEntity newsEntity=gson.fromJson(json, NewsEntity.class);
                int count = newsEntity.getResult().size();
                System.out.println("--------解析：" + count + " 条新闻");

                //显示列表的数据集合
                listDatas = newsEntity.getResult();

                // （3）显示列表(数据，列表项布局，适配器BaseAdapter)
                if (refresh){
                    showListView(listDatas);
                }else {
                    newsAdapter.appendData(listDatas);
                }
                pageNo++;
                springView.onFinishFreshAndLoad();
            }


            @Override       // 请求失败
            public void onFailure(HttpException error, String msg) {
                System.out.println("---------msg: " + msg);
            }
        });
    }

    private void showListView(List<NewsEntity.ResultBean> listDatas) {
        //(1)显示轮播图
        // 如果列表已经添加了头部布局，则先移除
        if (listView.getHeaderViewsCount() > 0) {
            listView.removeHeaderView(headerView);
        }

        //第一条新闻
        NewsEntity.ResultBean firstNews = listDatas.get(0);

        //有轮播图
        if (firstNews.getAds() != null && firstNews.getAds().size()>0){
            headerView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_view_header,listView,false);

            listDatas.remove(0);

            //查找轮播图控件
            SliderLayout sliderLayout = (SliderLayout) headerView
                    .findViewById(R.id.slider_layout);
            //准备轮播图要显示的数据
            List<NewsEntity.ResultBean.AdsBean> ads = firstNews.getAds();
            //添加轮播图子界面
            for (int i=0 ; i<ads.size(); i++){
                NewsEntity.ResultBean.AdsBean bean = ads.get(i);
                TextSliderView textSliderView = new TextSliderView(getContext());
                textSliderView.description(bean.getTitle())     //显示标题
                        .image(bean.getImgsrc());       //显示图片

                sliderLayout.addSlider(textSliderView);     //添加一个子界面

            }

            //添加轮播图到列表头部
            listView.addHeaderView(headerView);

        }else {     //没有轮播图


        }

        //(2)显示列表
        newsAdapter.setData(listDatas);
    }
}
