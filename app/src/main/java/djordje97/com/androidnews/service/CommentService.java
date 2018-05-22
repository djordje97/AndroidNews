package djordje97.com.androidnews.service;

import android.content.Intent;

import java.util.List;

import djordje97.com.androidnews.model.Comment;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CommentService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })

    @GET("comments/post/{id}")
    Call<List<Comment>> getCommentsByPost(@Path("id") Integer id);

    @POST("comments")
    Call<Comment> addComment(@Body Comment comment);

    @DELETE("comments/{id}")
    Call<Void>deleteComment(@Path("id")Integer id);
}
