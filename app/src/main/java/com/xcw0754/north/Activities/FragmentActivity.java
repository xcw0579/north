package com.xcw0754.north.Activities;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.xcw0754.north.R;

import java.util.ArrayList;
import java.util.List;




public class FragmentActivity extends AppCompatActivity implements View.OnClickListener {

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

    // 个人中心的每个按钮
//    private


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);



        initViews();
        initEvents();
        handleSelf();   //个人中心：预处理
    }

    private void handleSelf() {


    }


    /**
     * 初始化四个view
     */
    private void initViews() {

        mViewPager = (ViewPager) findViewById(R.id.id_viewpager) ;

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

        mTabHome.setOnClickListener(this);
        mTabSort.setOnClickListener(this);
        mTabSearch.setOnClickListener(this);
        mTabSelf.setOnClickListener(this);

        // 监听当前页面是否被切换
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {

            // 更新当前被选中的tab的图标
            @Override
            public void onPageSelected(int arg0)
            {
                int currentItem = mViewPager.getCurrentItem();
                resetImg();
                switch (currentItem)
                {
                    case 0:
                        mHomeImg.setImageResource(R.drawable.tab_icon_01_pressed);
                        break;
                    case 1:
                        mSortImg.setImageResource(R.drawable.tab_icon_01_pressed);
                        break;
                    case 2:
                        mSearchImg.setImageResource(R.drawable.tab_icon_01_pressed);
                        break;
                    case 3:
                        mSelfImg.setImageResource(R.drawable.tab_icon_01_pressed);
                        break;
                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {

            }

            @Override
            public void onPageScrollStateChanged(int arg0)
            {

            }
        });

    }



    /**
     * 将所有的图片切换为暗色（即未选中状态）
     */
    private void resetImg(){
        mHomeImg.setImageResource(R.drawable.tab_icon_01_normal);
        mSortImg.setImageResource(R.drawable.tab_icon_01_normal);
        mSearchImg.setImageResource(R.drawable.tab_icon_01_normal);
        mSelfImg.setImageResource(R.drawable.tab_icon_01_normal);
    }

    /**
     * 底下4个图标的单击事件
     */
    @Override
    public void onClick(View v){
        resetImg();
        switch (v.getId())
        {
            case R.id.id_tab_home:
                mViewPager.setCurrentItem(0);
                mHomeImg.setImageResource(R.drawable.tab_icon_01_pressed);
                break;
            case R.id.id_tab_sort:
                mViewPager.setCurrentItem(1);
                mSortImg.setImageResource(R.drawable.tab_icon_01_pressed);
                break;
            case R.id.id_tab_search:
                mViewPager.setCurrentItem(2);
                mSearchImg.setImageResource(R.drawable.tab_icon_01_pressed);
                break;
            case R.id.id_tab_self:
                mViewPager.setCurrentItem(3);
                mSelfImg.setImageResource(R.drawable.tab_icon_01_pressed);
                break;
            default:
                break;
        }
    }





}
