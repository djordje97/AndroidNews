package djordje97.com.androidnews.service;

import djordje97.com.androidnews.model.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface UserService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("users/get/{username}")
    Call<User> getUserByUsername(@Path("username") String username);
}
