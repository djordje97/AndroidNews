package djordje97.com.androidnews.service;

import java.util.List;

import djordje97.com.androidnews.model.Comment;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface CommentService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })

    @GET("comments/post/{id}")
    Call<List<Comment>> getCommentsByPost(@Path("id") Integer id);

}
