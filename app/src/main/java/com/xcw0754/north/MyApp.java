package com.xcw0754.north;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xcw0754.north.Libraries.MultiSkill.OneProduct;
import com.xcw0754.north.Libraries.SharedPreferences.SPUtils;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import butterknife.OnClick;

/**
 * Created by Administrator on 2016/3/3.
 */
public class MyApp extends Application {

    public static String myemail;

    private static Handler dataCacheHandler;               //缓存数据用的
    private static ArrayList<OneProduct> products;
    private static ArrayList<Integer> plist;                //这是有序的，产品编号

    private static ArrayList<String> xiaoxi;                //消息


    final private static Semaphore sema1 = new Semaphore(0); //同步用






    @Override
    public void onCreate() {
        super.onCreate();
        myemail = "";
        products = new ArrayList<>();
        plist = new ArrayList<>();
        xiaoxi = new ArrayList<>();

        dataCacheHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                //缓存待发货的编号
                if(msg.what==-1) {
                    String response = (String) msg.obj;
                    Log.d("msg", "待发货：请求产品编号的时候就是这样："+response);
                    JsonObject json = new JsonParser().parse(response).getAsJsonObject();
                    String errcode = json.get("errcode").getAsString();

                    if ( errcode.equals("1002") ) {
                        JsonArray jarray = json.get("products").getAsJsonArray();   //这里现在是一个array了

                        for(int i=0; i<jarray.size(); i++) {
                            Log.d("msg", "产品编号"+ jarray.get(i).getAsInt());
                            plist.add( jarray.get(i).getAsInt());    //这里还只是一堆产品的编号，有序的
                            products.add(null);
                        }
                        sema1.release();    //告诉那边可以行动了
                        Log.d("msg", "信号量释放了。");
                    } else {
                        // 出错
                        String errmsg = json.get("errmsg").getAsString();
                        Log.d("errmsg", "MyApp预加载时出现"+errmsg);
                    }
                }

                if(msg.what>=0) {
                    // 获取待发货的所有产品信息
                    String response = (String) msg.obj;
                    JsonObject json = new JsonParser().parse(response).getAsJsonObject();
                    String errcode = json.get("errcode").getAsString();
                    if ( errcode.equals("1002") ) {
                        products.set(msg.what,
                                new OneProduct(msg.arg1,
                                        json.get("price").getAsString(),
                                        json.get("title1").getAsString(),
                                        json.get("title2").getAsString(),
                                        "http://10.0.3.2:5000/source/sort/itemlist/cfxd/0/"+ msg.arg1+".jpg"
                                ));
                    } else {
                        String errmsg = json.get("errmsg").getAsString();
                        Log.d("errmsg", errmsg);
                    }
                }


                if(msg.what==-2) {
                    // 获取消息
                    String response = (String) msg.obj;
                    JsonObject json = new JsonParser().parse(response).getAsJsonObject();
                    String errcode = json.get("errcode").getAsString();
                    if ( errcode.equals("1002") ) {
                        xiaoxi.add( json.get("content").getAsString() );
                    } else {
                        String errmsg = json.get("errmsg").getAsString();
                        Log.d("errmsg", errmsg);
                    }

                }
            }
        };
    }



    // 如果有登录则记录email
    public static void setEmail(String email) {
        myemail = email;
    }

    public static void updateEmail(Context contex) {
        if ( SPUtils.contains(contex, "email") ) {
            myemail = (String) SPUtils.get(contex, "email", "");
            Log.d("msg", "email已经更新为" + myemail);
        } else {
            Log.d("msg", "未能更新到email.");
        }
    }

    public static String getEmail() {
        return myemail;
    }

    public static Boolean ContainEmail() {
        if( myemail.equals("") ) return false;
        return true;
    }









    // 缓存待发货
    public static void updataDelivery(Context context) {
        updateEmail(context);
        plist.clear();
        products.clear();

        Log.d("msg", "初始化待发货被调用。");

        dataCacheHandler.post(new Runnable() {
            @Override
            public void run() {
                Runnable requestTask = new Runnable() {
                    @Override
                    public void run() {
                        if( ContainEmail() ) {
                            Log.d("msg", "缓存待发货开始。");
                            Message msg = new Message();
                            String finalUrl = "http://10.0.3.2:5000/ask/delivery/size/" + MyApp.getEmail();
                            String response = HttpRequest.get(finalUrl).body();
                            msg.what=-1;
                            msg.obj = response;
                            dataCacheHandler.sendMessage(msg);

                            try {
                                sema1.acquire();
                                Log.d("msg", "捕获信号量。");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                Log.d("msg", "M信号量出错。");
                            }

                            //请求所有产品的数据
                            for(int i=0; i<plist.size(); i++) {
                                msg = new Message();
                                finalUrl = "http://10.0.3.2:5000/ask/product/detail/0/" + plist.get(i);
                                response = HttpRequest.get(finalUrl).body();
                                msg.what = i;
                                msg.arg1 = i;
                                msg.obj = response;
                                dataCacheHandler.sendMessage(msg);
                            }
                        }
                    }
                };
                new Thread(requestTask).start();
            }
        });

    }

    public static ArrayList<OneProduct> getProducts() {
        return products;
    }




    // 缓存“消息”
    public static void UpdateXiaoxi() {
        dataCacheHandler.post(new Runnable() {
            @Override
            public void run() {
                Runnable requestTask = new Runnable() {
                    @Override
                    public void run() {
                        if( ContainEmail() ) {
                            Message msg = new Message();
                            String finalUrl = "http://10.0.3.2:5000/get/xiaoxi";
                            String response = HttpRequest.get(finalUrl, true, "email", myemail).body();
                            msg.what = -2;
                            msg.obj = response;
                            dataCacheHandler.sendMessage(msg);
                        }
                    }
                };
                new Thread(requestTask).start();
            }
        });
    }

    public static ArrayList<String> GetXiaoxi() {
        return xiaoxi;
    }





}
