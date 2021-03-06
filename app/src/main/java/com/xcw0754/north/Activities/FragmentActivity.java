package com.xcw0754.north.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;
import com.xcw0754.north.Libraries.SharedPreferences.SPUtils;
import com.xcw0754.north.Libraries.aboutRecycleView.TabSortRecyclerView.MyLayoutManager;
import com.xcw0754.north.Libraries.aboutRecycleView.TabSortRecyclerView.RecyclerViewAdapter;

import com.xcw0754.north.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;


public class FragmentActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    private PagerAdapter mAdapter;
    private List<View> mViews = new ArrayList<View>();

    // 四个主要的TAB层
    private LinearLayout mTabHome;
    private LinearLayout mTabSort;
    private LinearLayout mTabSearch;
    private LinearLayout mTabSelf;

    // 四个主要的TAB按钮
    private ImageButton mHomeImg;
    private ImageButton mSortImg;
    private ImageButton mSearchImg;
    private ImageButton mSelfImg;

    // 个人中心
    private ImageButton btn_self_login;
    private ImageView iv_self_head;
    private ImageView iv_self_head_bg;
    private final static int REQUEST_CODE=1;

    // 产品分类
    private List<ImageView> ivList;
    private List<RecyclerView> rvList;
    private List<RecyclerViewAdapter> rvaList;
    private List<Integer> num;

    // 同步信号量
    final private Semaphore sema = new Semaphore(0);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        Log.i("dbg", "创建了FragmentActivity。");


        initViews();
        initEvents();
    }

    /**
     * 个人中心：所有都自己单独预处理
     */
    private void handleSelf() {
        if ( iv_self_head==null ) {
            iv_self_head = (ImageView) findViewById(R.id.id_self_iv_head);
            btn_self_login = (ImageButton) findViewById(R.id.id_self_btn_login);
            iv_self_head_bg = (ImageView) findViewById(R.id.id_iv_self_head_bg);
            Picasso.with(getApplicationContext())   //很卡的头像背景
                    .load(R.drawable.tab_image_self_head_bg)
                    .into(iv_self_head_bg);
        }


        // 如果已经登录过了，就可以直接登录，不用弹出登录头像。
        if ( SPUtils.contains(getApplicationContext(), "email") ) {
            //TODO 这里其实还可以验证一下密码，有必要再做

            //标记为已登录。
            SPUtils.put(getApplicationContext(), "isLogin", true); // 暂时还没有用到

            //显示头像
            btn_self_login.setVisibility(View.INVISIBLE);
            iv_self_head.setVisibility(View.VISIBLE);
        } else {
            //绑定登陆按钮切换到登录界面
            btn_self_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FragmentActivity.this, LoginActivity.class);
                    startActivityForResult( intent, REQUEST_CODE );
                }
            });
        }
    }

    private void handleSort() throws InterruptedException {
        //TODO 需要事先保存的数据，待展示出来的，应该从服务器抓取
        if ( num==null ) {

            final ArrayList<String> blocks = new ArrayList<>();
            blocks.add("tjpp");blocks.add("cfxd");blocks.add("cwdd");blocks.add("ghjk");
            blocks.add("shdq");blocks.add("djd");blocks.add("wjtz");
            num = new ArrayList<>();

            // 计算各个类别中有多少个item
            Runnable requestTask = new Runnable() {
                @Override
                public void run() {
                    String url = "http://10.0.3.2:5000/count/source/sort/product/";

                    for (int i = 0; i < 7; i++) {
                        String response = HttpRequest.get(url + blocks.get(i)).body();
                        JsonObject json = new JsonParser().parse(response).getAsJsonObject();
                        num.add(json.get("count").getAsInt());
                    }
                    sema.release();
                }
            };
            new Thread(requestTask).start();

            // 7个imageview
            ImageView iv_title1 = (ImageView)  findViewById(R.id.id_sort_iv_title1);
            ImageView iv_title2 = (ImageView)  findViewById(R.id.id_sort_iv_title2);
            ImageView iv_title3 = (ImageView)  findViewById(R.id.id_sort_iv_title3);
            ImageView iv_title4 = (ImageView)  findViewById(R.id.id_sort_iv_title4);
            ImageView iv_title5 = (ImageView)  findViewById(R.id.id_sort_iv_title5);
            ImageView iv_title6 = (ImageView)  findViewById(R.id.id_sort_iv_title6);
            ImageView iv_title7 = (ImageView)  findViewById(R.id.id_sort_iv_title7);
            // 对象装进数组中
            ivList = new ArrayList<>();
            ivList.add(iv_title1);
            ivList.add(iv_title2);
            ivList.add(iv_title3);
            ivList.add(iv_title4);
            ivList.add(iv_title5);
            ivList.add(iv_title6);
            ivList.add(iv_title7);
            // 加载7个title个
            for (int i = 0; i<7 ; i++ ) {
                Picasso.with(getApplicationContext())
                        .load("http://10.0.3.2:5000/source/sort/title/title"+ i +".jpg")
                        .into( ivList.get(i) );
            }

            // ***********************************************************************

            RecyclerView mRV1 = (RecyclerView) findViewById(R.id.id_sort_rv1);
            RecyclerView mRV2 = (RecyclerView) findViewById(R.id.id_sort_rv2);
            RecyclerView mRV3 = (RecyclerView) findViewById(R.id.id_sort_rv3);
            RecyclerView mRV4 = (RecyclerView) findViewById(R.id.id_sort_rv4);
            RecyclerView mRV5 = (RecyclerView) findViewById(R.id.id_sort_rv5);
            RecyclerView mRV6 = (RecyclerView) findViewById(R.id.id_sort_rv6);
            RecyclerView mRV7 = (RecyclerView) findViewById(R.id.id_sort_rv7);

            rvList = new ArrayList<>();
            rvList.add(mRV1);rvList.add(mRV2);rvList.add(mRV3);rvList.add(mRV4);
            rvList.add(mRV5);rvList.add(mRV6);rvList.add(mRV7);


            sema.acquire();
            Log.d("sort", "num数量:"+num.size());
            Log.d("sort", "block数量:"+blocks.size());
            rvaList = new ArrayList<>();

            for (int i = 0; i<7 ; i++ ) {
                RecyclerViewAdapter rva = new RecyclerViewAdapter(this, num.get(i), blocks.get(i));
                rva.setOnItemClickListener(new RecyclerViewAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, String data) {
                        Log.d("sort","分类item被点击了。");
                        //TODO 点击item后将调出其他的activity进行显示，得传参数进去
                        Intent intent = new Intent(getApplicationContext(), ProductListActivity.class);
                        intent.putExtra("msg", data);
                        startActivity(intent);
                    }
                });
                // 绑定点击事件后才装进数组
                rvaList.add(rva);
            }

            // 设置Adapter
            for (int i = 0; i<7; i++) {
                rvList.get(i).setAdapter( rvaList.get(i) );
                rvList.get(i).setLayoutManager( new MyLayoutManager(getApplicationContext(), 3) );
            }


//                    mmAdapter1 =  new SimpleAdapter(getApplicationContext(), mDatas1 );
//                    mmAdapter2 =  new SimpleAdapter2(getApplicationContext(), mDatas2 );
//                    mRVkind.setAdapter( mmAdapter1 );
//                    mRVbrand.setAdapter( mmAdapter2 );

//                    设置recycleview布局管理
//                    MyLayoutManager gridLayoutManager1 = new MyLayoutManager(getApplicationContext(), 3); //三列
//                    mRVkind.setLayoutManager(gridLayoutManager1);

            Log.d("sort", "又产生一次资源访问的请求");

        }


//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }


    /**
     * 调用其他的activity后从其返回的数据，一般指的是login是否成功。
     * @param requestCode   请求码，在本class
     * @param resultCode    返回码，在被调用的activity
     * @param data          装进intent的数据，在被调用的activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 判断是否登录成功
        if (requestCode==REQUEST_CODE)
        {
            if (resultCode==LoginActivity.RESULT_CODE)
            {
                Bundle bundle = data.getExtras();
                Boolean islogin = bundle.getBoolean("islogin");
                //如果成功了，异步请求获取头像
                if ( islogin ){
                    btn_self_login.setVisibility(View.INVISIBLE);
                    iv_self_head.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    /**
     * 初始化四个view
     */
    private void initViews() {

        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

        mTabHome = (LinearLayout) findViewById(R.id.id_tab_home);
        mTabSort = (LinearLayout) findViewById(R.id.id_tab_sort);
        mTabSearch = (LinearLayout) findViewById(R.id.id_tab_search);
        mTabSelf = (LinearLayout) findViewById(R.id.id_tab_self);

        mHomeImg = (ImageButton) findViewById(R.id.id_tab_home_img);
        mSortImg = (ImageButton) findViewById(R.id.id_tab_sort_img);
        mSearchImg = (ImageButton) findViewById(R.id.id_tab_search_img);
        mSelfImg = (ImageButton) findViewById(R.id.id_tab_self_img);


        LayoutInflater mInflater = LayoutInflater.from(this);

        // 切换的对象
        View tab01 = mInflater.inflate(R.layout.tab_home, null);
        View tab02 = mInflater.inflate(R.layout.tab_sort, null);
        View tab03 = mInflater.inflate(R.layout.tab_search, null);
        View tab04 = mInflater.inflate(R.layout.tab_self, null);

        //将四个页面装进去
        mViews.add(tab01);
        mViews.add(tab02);
        mViews.add(tab03);
        mViews.add(tab04);


        // 设置切换过程
        mAdapter = new PagerAdapter() {

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mViews.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = mViews.get(position);
                container.addView(view);
                return view;
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return mViews.size();
            }
        };
        mViewPager.setAdapter(mAdapter);

    }


    /**
     * 监听tab切换事件
     */
    private void initEvents() {

        mTabHome.setOnClickListener(new viewListener());
        mTabSort.setOnClickListener(new viewListener());
        mTabSearch.setOnClickListener(new viewListener());
        mTabSelf.setOnClickListener(new viewListener());

        // 监听当前页面是否被切换
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // 更新当前被选中的tab的图标
            @Override
            public void onPageSelected(int arg0) {
                int currentItem = mViewPager.getCurrentItem();
                resetImg();
                switch (currentItem) {
                    case 0:
                        mHomeImg.setImageResource(R.drawable.tab_icon_home_pressed);
                        break;
                    case 1:
                        mSortImg.setImageResource(R.drawable.tab_icon_sort_pressed);
                        try {
                            handleSort();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2:
                        mSearchImg.setImageResource(R.drawable.tab_icon_search_pressed);
                        break;
                    case 3:
                        mSelfImg.setImageResource(R.drawable.tab_icon_self_pressed);
                        handleSelf();
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

    }


    /**
     * 将底标的图片切换为暗色（即未选中状态）
     */
    private void resetImg() {
        mHomeImg.setImageResource(R.drawable.tab_icon_home_normal);
        mSortImg.setImageResource(R.drawable.tab_icon_sort_normal);
        mSearchImg.setImageResource(R.drawable.tab_icon_search_normal);
        mSelfImg.setImageResource(R.drawable.tab_icon_self_normal);
    }

    @Override
    public void onBackPressed() {
        //在此页面禁止往后退
    }


    /**
     * 内部类：底下4个图标的单击事件
     */
    class viewListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.i("dbg", "四个底图标所产生点击事件。");
            resetImg();
            switch (v.getId()) {
                case R.id.id_tab_home:
                    mViewPager.setCurrentItem(0);
                    mHomeImg.setImageResource(R.drawable.tab_icon_home_pressed);
                    break;
                case R.id.id_tab_sort:
                    mViewPager.setCurrentItem(1);
                    mSortImg.setImageResource(R.drawable.tab_icon_sort_pressed);
                    break;
                case R.id.id_tab_search:
                    mViewPager.setCurrentItem(2);
                    mSearchImg.setImageResource(R.drawable.tab_icon_search_pressed);
                    break;
                case R.id.id_tab_self:
                    mViewPager.setCurrentItem(3);
                    mSelfImg.setImageResource(R.drawable.tab_icon_self_pressed);
                    break;
                default:
                    break;
            }
        }
    }


}
