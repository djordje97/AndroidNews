package djordje97.com.androidnews;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PostsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
    }

    public void StartReadPostActivity(View view) {
        Intent startReadPost=new Intent(PostsActivity.this,ReadPostActivity.class);
        startActivity(startReadPost);
    }

    public void OpenSettings(View view) {
        Intent openSettings=new Intent(PostsActivity.this,SettingsActivity.class);
        startActivity(openSettings);
    }

    public void StartCreatePostActivity(View view) {
        Intent startCreatePost=new Intent(PostsActivity.this,CreatePostActivity.class);
        startActivity(startCreatePost);
    }
}
