package com.xcw0754.north.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xcw0754.north.Libraries.SharedPreferences.SPUtils;
import com.xcw0754.north.Libraries.testRecycleView.MyLayoutManager;
import com.xcw0754.north.Libraries.testRecycleView.SimpleAdapter;
import com.xcw0754.north.R;

import java.util.ArrayList;
import java.util.List;



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
    private Button btn_self_login;
    private ImageView iv_self_head;
    private final static int REQUEST_CODE=1;

    // 产品分类
    private RecyclerView mRVkind;
    private RecyclerView mRVbrand;
    private List<String> mDatas1;
    private List<String> mDatas2;
    private SimpleAdapter mmAdapter1;
    private SimpleAdapter mmAdapter2;

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
            btn_self_login = (Button) findViewById(R.id.id_self_btn_login);
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

    private void handleSort() {
        //TODO 需要事先保存的数据，待展示出来的，应该从服务器抓取
        if ( mDatas1==null ) {
            mRVkind = (RecyclerView) findViewById(R.id.id_sort_rv_kind);
            mRVbrand = (RecyclerView) findViewById(R.id.id_sort_rv_brand);

            mDatas1 = new ArrayList<String>();
            mDatas2 = new ArrayList<String>();

            for (int i = 0; i < 10; i++) {
                mDatas1.add("热水器");
            }
            for (int i = 0; i < 10; i++) {
                mDatas2.add("西门子");
            }
            mmAdapter1 =  new SimpleAdapter(this, mDatas1 );
            mmAdapter2 =  new SimpleAdapter(this, mDatas2 );
            mRVkind.setAdapter( mmAdapter1 );
            mRVbrand.setAdapter( mmAdapter2 );

            // 设置recycleview布局管理
            MyLayoutManager gridLayoutManager1 = new MyLayoutManager(this, 3); //三列
            gridLayoutManager1.setSmoothScrollbarEnabled(true);
            mRVkind.setLayoutManager(gridLayoutManager1);

            MyLayoutManager gridLayoutManager2 = new MyLayoutManager(this, 2); //两列
            gridLayoutManager1.setSmoothScrollbarEnabled(true);
            mRVbrand.setLayoutManager(gridLayoutManager2);

            Log.d("sort", "又产生一次");
        }

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }


    /**
     *
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
                        mHomeImg.setImageResource(R.drawable.tab_icon_01_pressed);
                        break;
                    case 1:
                        mSortImg.setImageResource(R.drawable.tab_icon_01_pressed);
                        handleSort();
                        break;
                    case 2:
                        mSearchImg.setImageResource(R.drawable.tab_icon_01_pressed);
                        break;
                    case 3:
                        mSelfImg.setImageResource(R.drawable.tab_icon_01_pressed);
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
     * 将所有的图片切换为暗色（即未选中状态）
     */
    private void resetImg() {
        mHomeImg.setImageResource(R.drawable.tab_icon_home_normal);
        mSortImg.setImageResource(R.drawable.tab_icon_sort_normal);
        mSearchImg.setImageResource(R.drawable.tab_icon_search_normal);
        mSelfImg.setImageResource(R.drawable.tab_icon_self_normal);
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
