package djordje97.com.androidnews;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.time.Duration;

import djordje97.com.androidnews.model.User;
import djordje97.com.androidnews.service.ServiceUtils;
import djordje97.com.androidnews.service.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {
    ServiceUtils serviceUtils;
    UserService userService;
    User u;
    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



    }

    public void StartPostActivity(View view) {
        EditText usernameText=(EditText)findViewById(R.id.username_edit_text);
        EditText passwordtext=(EditText)findViewById(R.id.password_edit_text);

         username =usernameText.getText().toString();
         password=passwordtext.getText().toString();
        userService=ServiceUtils.userService;
        Call<User> call=userService.getUserByUsername(username);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                u=response.body();
                if(u.getUsername().equals(username) && u.getPassword().equals(password)){
                    Intent intent=new Intent(LoginActivity.this,PostsActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });




    }
}
