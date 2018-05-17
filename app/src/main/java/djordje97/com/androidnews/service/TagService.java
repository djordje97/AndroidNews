package djordje97.com.androidnews.service;

import java.util.List;

import djordje97.com.androidnews.model.Tag;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface TagService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("tags")
    Call<Tag> getTags();

    @GET("posts/tag/{id}")
    Call<List<Tag>> getTagsByPost(@Path("id") Integer id);
}
