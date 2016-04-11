package com.xcw0754.north.Activities;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;
import com.xcw0754.north.Libraries.BigkooConvenientBanner.NetworkImageHolderView;
import com.xcw0754.north.Libraries.SharedPreferences.SPUtils;
import com.xcw0754.north.Libraries.aboutRecycleView.DividerItemDecoration;
import com.xcw0754.north.Libraries.aboutRecycleView.MiaoShaRecyclerView.MyLayoutManager4;
import com.xcw0754.north.Libraries.aboutRecycleView.MiaoShaRecyclerView.RecyclerViewAdapter4;
import com.xcw0754.north.Libraries.aboutRecycleView.ProductRecyclerView.MyLayoutManager1;
import com.xcw0754.north.Libraries.aboutRecycleView.ProductRecyclerView.RecyclerViewAdapter1;
import com.xcw0754.north.Libraries.aboutRecycleView.SearchRecyclerView.MyLayoutManager2;
import com.xcw0754.north.Libraries.aboutRecycleView.SearchRecyclerView.RecyclerViewAdapter2;
import com.xcw0754.north.Libraries.aboutRecycleView.TabSortRecyclerView.MyLayoutManager;
import com.xcw0754.north.Libraries.aboutRecycleView.TabSortRecyclerView.RecyclerViewAdapter;

import com.xcw0754.north.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    //首页
    private ConvenientBanner convenientBanner1;
    private ArrayList<String> networkImages1 = new ArrayList<>();
//    private RecyclerViewAdapter4 rvaHomeMiaosha;  先暂时搁着
//    private RecyclerView rcvHomeMiaosha;
    private ImageView iv_title1;
    private RecyclerView rcvBlock1;

    private ImageView iv_title2;
    private RecyclerView rcvBlock2;


    private RecyclerView rcvBlock3;
    private ConvenientBanner convenientBanner2;



    // 个人中心
    private ImageButton btn_self_login;
    private ImageView iv_self_head;
    private ImageView iv_self_head_bg;
    private final static int REQUEST_CODE=1;    //用于登录后传递消息

    // 产品分类
    private List<ImageView> ivList;
    private List<RecyclerView> rvList;
    private List<RecyclerViewAdapter> rvaList;
    private List<Integer> num;

    //收藏
    private RecyclerViewAdapter2 rvaSearch;
    private RecyclerView rcvSearch;
    private LinearLayout LlayoutNO;
    private RelativeLayout LlayoutYES;
    private Button deleteButton;
    private Button carButton;
    private int showItemCount = 0;

    // 同步信号量
    final private Semaphore sema = new Semaphore(0);













    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        Log.i("dbg", "创建了FragmentActivity。");


        initViews();
        initEvents();

//        非要这样才能正常显示么？
        new android.os.Handler().postDelayed(new Runnable() {
                                            public void run() {
                                                handleHome();
                                            }
                                        }, 500);

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
        //TODO 很多按钮需要绑定click，简单实现重要的几个即可。直接用临时变量了。
        //个人信息
        LinearLayout psn_msg = (LinearLayout) findViewById(R.id.id_layout_personel_message);
        psn_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PersonelMessageActivity.class);
                startActivity(intent);
            }
        });
        //购物车
        LinearLayout addincar = (LinearLayout) findViewById(R.id.id_self_shop_car);
        addincar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ShopCarActivity.class);
                startActivity(intent);
            }
        });






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
    /**
     * 分类
     */
    private void handleSort(){
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

            try {
                sema.acquire();     //因为这个才需要exception
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            rvaList = new ArrayList<>();

            for (int i = 0; i<7 ; i++ ) {
                RecyclerViewAdapter rva = new RecyclerViewAdapter(this, num.get(i), blocks.get(i));
                rva.setOnItemClickListener(new RecyclerViewAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, String data) {
                        Log.d("sort","分类item被点击了。");
                        //TODO 点击item后将调出其他的activity进行显示，得传参数进去
                        Intent intent = new Intent(getApplicationContext(), ProductListActivity.class);
                        intent.putExtra("msg", data);   //data是什么数据？
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
            Log.d("sort", "又产生一次资源访问的请求");
        }
    }

    /**
     * 收藏
     */
    private void handleSearch() {

        //TODO 将所有的收藏读出来，显示出来。（只作资源请求，而不提交本地数据到后台）
        //TODO 隐藏提示信息
        /*
        先把数组转为String类型。使用String s=Arrays.toString(String[] arr);
        使用SharedPreferences.getEditor().put("arr",s).commit(); 保存
        通过SharedPreferences.get("arr") 得到String串
        然后把String 转为 Array 即可
        */
        //有收藏则显示出来
        if( LlayoutNO==null ) {
            LlayoutNO = (LinearLayout) findViewById(R.id.id_layout_no_favorite);
            LlayoutYES = (RelativeLayout) findViewById(R.id.id_layout_favorite_page);
            rcvSearch = (RecyclerView) findViewById(R.id.id_search_rv);

            deleteButton = (Button) findViewById(R.id.id_btn_detail_delete);
            carButton = (Button) findViewById(R.id.id_btn_detail_addincar);
        }


        if( SPUtils.contains(getApplicationContext(), "favorite")  ) {

            String product = (String) SPUtils.get(getApplicationContext(), "favorite", "");
            JsonArray json = new JsonParser().parse(product).getAsJsonArray();
            Log.d("msg", product);  //收藏的东西
            if( json.size()==0 ) {  //并没有收藏

                LlayoutNO.setVisibility(View.VISIBLE);  //有了收藏的产品就要隐藏起来
                LlayoutYES.setVisibility(View.GONE);
            } else {
                if( showItemCount==json.size() )   return ;    //已经设置过一遍了，防止原来类中的array被析构了。
                showItemCount = json.size();
                rvaSearch = new RecyclerViewAdapter2(this, json.size());
                rvaSearch.setOnItemClickListener(new RecyclerViewAdapter2.OnRecyclerViewItemClickListener() {
                    //整个item的点击
                    @Override
                    public void onItemClick(View view, String data) {
//                        Intent intent = new Intent(getApplicationContext(), ProductDetailActivity.class);
//                        intent.putExtra("msg", data);       //传必要的数据,一般是产品编号
//                        startActivity(intent);
                    }

                    //item中的选中按钮的点击，data是产品编号
                    @Override
                    public void onCheckClick(View view, Object data) {
                        //TODO 在array中，如果存在，则删除，如果不存在，则添加进去
                        if( view.isSelected() ) {
                            rvaSearch.delCheckList(data);
                            view.setSelected(false);
                            Log.d("msg", "checklist删除。。");
                        } else {
                            rvaSearch.putCheckList(data);
                            view.setSelected(true);
                            Log.d("msg", "checklist变黑了。");
                        }
                    }
                });
                //TODO 一旦有新的物品加入的话，勾选的框都会被取消了。
                rcvSearch.setAdapter(rvaSearch);
                rcvSearch.setLayoutManager(new MyLayoutManager2(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                rcvSearch.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
                rcvSearch.setItemAnimator(new DefaultItemAnimator());   //默认的增删动画

                //删除按钮
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("msg", "删除按钮被点击了。");
                        rvaSearch.deleteAllChecked();
                    }
                });
                //加入购物车按钮
                carButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if( rvaSearch.addInCar() )
                            Toast.makeText(getBaseContext(), "成功添加到购物车", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getBaseContext(), "添加失败，请重试", Toast.LENGTH_LONG).show();
                    }
                });

                //显示出相应页面layout
                LlayoutNO.setVisibility(View.GONE);
                LlayoutYES.setVisibility(View.VISIBLE);
            }
        } else {

            LlayoutNO.setVisibility(View.VISIBLE);  //有了收藏的产品就要隐藏起来
            LlayoutYES.setVisibility(View.GONE);
        }

    }

    /**
     * APP的首页
     */
    private void handleHome() {
        Log.d("msg", "调用了");

        convenientBanner1 = (ConvenientBanner) findViewById(R.id.id_home_adv_convenientBanner1);
        if( convenientBanner1!=null ) {
            for(int i=0; i<4; i++)
                networkImages1.add("http://10.0.3.2:5000/source/home/ban1/" + i + ".jpg");

            convenientBanner1.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
                //顺便构造
                @Override
                public NetworkImageHolderView createHolder() {
                    return new NetworkImageHolderView();
                }
            }, networkImages1)
                    .setPageIndicator(new int[]{R.drawable.ic_page_indicator,
                            R.drawable.ic_page_indicator_focused});
        }


//        一张图片的导购假象
        ImageView iv_home_navigation = (ImageView) findViewById(R.id.id_home_navigation);
        Picasso.with(getApplicationContext()).load("http://10.0.3.2:5000/source/home/navigation/0.jpg").into(iv_home_navigation);






//        块1
        iv_title1 = (ImageView)findViewById(R.id.id_home_iv_title1);
        Picasso.with(getApplicationContext()).load("http://10.0.3.2:5000/source/home/block1/title/0.jpg").into(iv_title1);
        rcvBlock1 = (RecyclerView)findViewById(R.id.id_home_recyclerview2);

        ArrayList<String>   block1 = new ArrayList<>();
        for(int i=0; i<6; i++)  block1.add("http://10.0.3.2:5000/source/home/block1/view/"+i+".jpg");
        RecyclerViewAdapter4 mmAdapter1 =  new RecyclerViewAdapter4(this, block1);

        rcvBlock1.setAdapter(mmAdapter1);
        rcvBlock1.setLayoutManager(new MyLayoutManager4(getApplicationContext(), 2));



//        块2
        iv_title2 = (ImageView)findViewById(R.id.id_home_iv_title2);
        Picasso.with(getApplicationContext()).load("http://10.0.3.2:5000/source/home/block2/title/0.jpg").into(iv_title2);
        rcvBlock2 = (RecyclerView)findViewById(R.id.id_home_recyclerview3);

        ArrayList<String>   block2 = new ArrayList<>();
        for(int i=0; i<6; i++)  block2.add("http://10.0.3.2:5000/source/home/block2/view/"+i+".jpg");
        RecyclerViewAdapter4 mmAdapter2 =  new RecyclerViewAdapter4(this, block2);

        rcvBlock2.setAdapter(mmAdapter2);
        rcvBlock2.setLayoutManager(new MyLayoutManager4(getApplicationContext(), 3));







//        块3
        convenientBanner2 = (ConvenientBanner) findViewById(R.id.id_home_adv_convenientBanner4);
        ArrayList<String> networkImages2 = new ArrayList<>();
        for(int i=0; i<3; i++)  networkImages2.add("http://10.0.3.2:5000/source/home/ban3/" + i + ".jpg");
        convenientBanner2.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, networkImages2)
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator,
                        R.drawable.ic_page_indicator_focused});

        rcvBlock3 = (RecyclerView)findViewById(R.id.id_home_recyclerview4);
        ArrayList<String>   block3 = new ArrayList<>();
        for(int i=0; i<9; i++)  block3.add("http://10.0.3.2:5000/source/home/block3/view/"+i+".jpg");
        RecyclerViewAdapter4 mmAdapter3 =  new RecyclerViewAdapter4(this, block3);
        rcvBlock3.setAdapter(mmAdapter3);
        rcvBlock3.setLayoutManager(new MyLayoutManager4(getApplicationContext(), 3));





//        treat统一处理

        ImageView treat0 = (ImageView) findViewById(R.id.id_home_treat0);
        Picasso.with(getApplicationContext()).load("http://10.0.3.2:5000/source/home/treat/0.jpg").into(treat0);


        ImageView treat1 = (ImageView) findViewById(R.id.id_home_treat1);
        Picasso.with(getApplicationContext()).load("http://10.0.3.2:5000/source/home/treat/1.jpg").into(treat1);


        ImageView treat3 = (ImageView) findViewById(R.id.id_home_treat3);
        Picasso.with(getApplicationContext()).load("http://10.0.3.2:5000/source/home/treat/3.jpg").into(treat3);





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
        mViewPager.setCurrentItem(0);
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
                    default:
                    case 0:
                        mHomeImg.setImageResource(R.drawable.tab_icon_home_pressed);
                        handleHome();
                        break;
                    case 1:
                        mSortImg.setImageResource(R.drawable.tab_icon_sort_pressed);
                        handleSort();
                        break;
                    case 2:
                        mSearchImg.setImageResource(R.drawable.tab_icon_search_pressed);
                        handleSearch();
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
                default:
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
            }
        }
    }


}
