package cn.lemon.jcourse.model;


import cn.lemon.jcourse.model.bean.Account;
import cn.lemon.jcourse.model.bean.BBS;
import cn.lemon.jcourse.model.bean.Banner;
import cn.lemon.jcourse.model.bean.Info;
import cn.lemon.jcourse.model.bean.JVideo;
import cn.lemon.jcourse.model.bean.JavaCourse;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by linlongxin on 2016/8/7.
 */

public interface ServiceAPI {

    //banner
    @GET("accounts/banner.php")
    Observable<Banner> getBanner();

    //Accounts相关
    @FormUrlEncoded
    @POST("accounts/register.php")
    Observable<Info> register(@Field("name") String name, @Field("password") String password);

    @FormUrlEncoded
    @POST("accounts/login.php")
    Observable<Account> login(@Field("name") String name, @Field("password") String password);

    @POST("accounts/prolongToken.php")
    Observable<Account> prolongToken();

    @FormUrlEncoded
    @POST("accounts/updateAccountInfo.php")
    Observable<Account> updateAcconuntInfo(@Field("name") String name, @Field("sign") String sign, @Field("avatar") String avatar);

    @Multipart
    @POST("accounts/updateAvatar.php")
    Observable<Info> updateAvatar(@Part MultipartBody.Part file);

    /**
     * courses相关
     */
    @FormUrlEncoded
    @POST("courses/javaCourseList.php")
    Observable<JavaCourse[]> getTextJavaCourseList(@Field("page") int page);

    @FormUrlEncoded
    @POST("courses/starJCourse.php")
    Observable<Info> starJCourse(@Field("id") int id);

    @FormUrlEncoded
    @POST("courses/isStarJCourse.php")
    Observable<Info> getIsStar(@Field("id") int id);

    @FormUrlEncoded
    @POST("courses/unstarJCourse.php")
    Observable<Info> unstarJCourse(@Field("id") int id);

    @FormUrlEncoded
    @POST("courses/starJCourseList.php")
    Observable<JavaCourse[]> getStarJCourseList(@Field("page") int page);

    @FormUrlEncoded
    @POST("courses/javaCourseFromDir.php")
    Observable<JavaCourse[]> getJavaCourseFromDir(@Field("unit") int unit,@Field("page") int page);

    @FormUrlEncoded
    @POST("courses/javaVideoList.php")
    Observable<JVideo[]> getVideoList(@Field("page") int page);

    //社区相关
    @FormUrlEncoded
    @POST("bbs/bbsList.php")
    Observable<BBS[]> getBBSList(@Field("page") int page);

    @FormUrlEncoded
    @POST("bbs/bbsDetail.php")
    Observable<BBS> getBBSDetail(@Field("id") int id);
}
