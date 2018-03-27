package djordje97.com.androidnews;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void StartPostActivity(View view) {
        Intent openPosts=new Intent(LoginActivity.this,PostsActivity.class);
        startActivity(openPosts);
        finish();

    }
}
