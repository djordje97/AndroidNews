package djordje97.com.androidnews.service;

import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import djordje97.com.androidnews.model.Post;
import djordje97.com.androidnews.util.DateSerialization;
import djordje97.com.androidnews.util.ImageSerialization;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceUtils {

     static final String BASE_URL="http://192.168.72.244:8080/api/";


    public static OkHttpClient test(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(200, TimeUnit.SECONDS)
                .readTimeout(200, TimeUnit.SECONDS)
                .writeTimeout(200, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        return client;
    }
    static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Bitmap.class, ImageSerialization.getBitmapTypeAdapter())
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            .create();

    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(test())
            .build();

//    static Gson gson = new GsonBuilder()
//            .registerTypeAdapter(Date.class, DateSerialization.getUnixEpochDateTypeAdapter())
//            .create();



    public static UserService userService=retrofit.create(UserService.class);
    public static  TagService tagService=retrofit.create(TagService.class);
    public static  CommentService commentService=retrofit.create(CommentService.class);
    public static  PostService postService=retrofit.create(PostService.class);
}
