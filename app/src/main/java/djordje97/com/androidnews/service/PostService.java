package djordje97.com.androidnews.service;

import android.content.Intent;

import java.util.List;

import djordje97.com.androidnews.model.Post;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PostService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("posts")
    Call<List<Post>> getPosts();

    @POST("posts")
    Call<Post> createPost(@Body Post post);

    @PUT("posts/{id}")
    Call<Post> updatePost(@Body Post post, @Path("id")Integer id);

    @DELETE("posts/{id}")
    Call<Void>deletePost(@Path("id") Integer id);
}
