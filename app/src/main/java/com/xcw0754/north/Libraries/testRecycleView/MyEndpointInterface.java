package com.xcw0754.north.Libraries.testRecycleView;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by xiao on 16-3-13.
 * 接口文档：http://www.open-open.com/lib/view/open1453552147323.html
 *          http://square.github.io/retrofit/#api-declaration
 *          https://segmentfault.com/a/1190000004536439
 */

public interface MyEndpointInterface {


    @GET("count/source/{tab}/product/{mod}")
    Response getProductCount(@Path("tab") String tab, @Path("mod") String mod);


}
