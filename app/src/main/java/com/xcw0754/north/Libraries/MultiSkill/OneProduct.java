package com.xcw0754.north.Libraries.MultiSkill;

/**
 * Created by xiao on 16-4-24.
 * 为了缓存数据
 */


public class OneProduct {
    public int pos;     //在后台的名称
    public String price;
    public String title1;
    public String title2;
    public String url;

    public OneProduct(int pos, String price, String title1, String title2, String url) {
        this.pos = pos;
        this.price = price;
        this.title1 = title1;
        this.title2 = title2;
        this.url = url;
    }
}