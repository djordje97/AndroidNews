package djordje97.com.androidnews.service;

import java.util.List;

import djordje97.com.androidnews.model.Post;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface PostService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("posts")
    Call<List<Post>> getPosts();
}
