package djordje97.com.androidnews;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class PostsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id == R.id.action_settings)
        {
            Intent openSettings=new Intent(PostsActivity.this,SettingsActivity.class);
            startActivity(openSettings);
        }

        return super.onOptionsItemSelected(item);
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
