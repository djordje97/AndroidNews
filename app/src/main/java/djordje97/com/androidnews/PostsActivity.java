package djordje97.com.androidnews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import djordje97.com.androidnews.adapters.DrawerListAdapter;
import djordje97.com.androidnews.adapters.ListViewAdapter;
import djordje97.com.androidnews.adapters.PostListAdapter;
import djordje97.com.androidnews.model.NavItem;
import djordje97.com.androidnews.model.Post;
import djordje97.com.androidnews.model.User;
import djordje97.com.androidnews.service.PostService;
import djordje97.com.androidnews.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout mDrawerPane;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    private SharedPreferences sharedPreferences;
    private boolean sortPostByDate;
    private boolean sortPostByPopularity;
    List<Post> posts;
    private Post post;
    private ListViewAdapter listViewAdapter;
    private Post post1;
    private Post post2;
    private PostService postService;
    private PostListAdapter postListAdapter;
    private ListView listView;
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
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        prepareMenu(mNavItems);

        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerList = (ListView) findViewById(R.id.navList);

        // Populate the Navigtion Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);


        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerList.setAdapter(adapter);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {
              getActionBar().setTitle(mTitle);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
               getActionBar().setTitle(mDrawerTitle);
                getSupportActionBar().setTitle("Android News");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

      /*  Bitmap b = BitmapFactory.decodeResource(getResources(),R.mipmap.slika);
        User user = new User(1, "Petar", b, "pera", "123", null, null);
        Date date = new Date(2018-1900,3-1,23,8,45);
        Date d2=new Date(2018-1900,2-1,25,9,45);
        post1=new Post(1, "Avengers", "Avengers Infinity war,best movie", b, user, date, null, null, null, 12, 3);
        post2=new Post(2,"Super News","Extraaa",b,user,d2,null,null,null,50,7);

        posts.add(post1);
        posts.add(post2);*/
         listView=findViewById(R.id.list_view);

        postService = ServiceUtils.postService;

        Call call = postService.getPosts();



        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                posts = response.body();
                postListAdapter = new PostListAdapter(getApplicationContext(),posts);
                listView.setAdapter(postListAdapter);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                t.printStackTrace();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                post=posts.get(position);
                Intent startReadPost=new Intent(PostsActivity.this,ReadPostActivity.class);
                startReadPost.putExtra("Post",new Gson().toJson(post));
                startActivity(startReadPost);
            }
        });

        sharedPreferences=getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        //consultPreference();
    }

  /*  private void consultPreference(){
        sortPostByDate=sharedPreferences.getBoolean(getString(R.string.prefer_sort_post_date_key),false);
        sortPostByPopularity=sharedPreferences.getBoolean(getString(R.string.sort_post_popularity_key),false);

        if(sortPostByDate == true){
            sortDate();
        }
        if(sortPostByPopularity == true){
            sortByPopularity();
        }
    }*/

  /*  public void sortDate(){
        Collections.sort(posts, new Comparator<Post>() {
            @Override
            public int compare(Post post, Post t1) {
                return post1.getDate().compareTo(post.getDate());
            }
        });


        listViewAdapter.notifyDataSetChanged();
    }

    public void sortByPopularity(){

        Collections.sort(posts, new Comparator<Post>() {
            @Override
            public int compare(Post post, Post t1) {
                int first;
                int second ;
                first = post.getLike() - post.getDislike();
                second = post1.getLike() - post1.getDislike();
                return Integer.valueOf(second).compareTo(first);
            }
        });


        listViewAdapter.notifyDataSetChanged();
    }
*/
    private void prepareMenu(ArrayList<NavItem> mNavItems ){
        mNavItems.add(new NavItem(getString(R.string.settings), "", R.drawable.ic_settings));
        mNavItems.add(new NavItem(getString(R.string.create_post), "", R.drawable.ic_create_post));
        mNavItems.add(new NavItem(getString(R.string.log_out),"",R.drawable.ic_logout));

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItemFromDrawer(position);
        }
    }

    private void selectItemFromDrawer(int position) {
        if(position == 0){
            Intent preference = new Intent(PostsActivity.this,SettingsActivity.class);
            startActivity(preference);
        }else if(position == 1){Intent create = new Intent(PostsActivity.this,CreatePostActivity.class);
            startActivity(create);
        }else if(position == 2){
            Intent login = new Intent(PostsActivity.this,LoginActivity.class);
            sharedPreferences.edit().clear().commit();
            startActivity(login);
            finish();
        }else{
            Log.e("DRAWER", "Nesto van opsega!");
        }

        mDrawerList.setItemChecked(position, true);
        if(position != 1)
        {
            setTitle(mNavItems.get(position).getmTitle());
        }
        mDrawerLayout.closeDrawer(mDrawerPane);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
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

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onResume() {
        super.onResume();
      //  consultPreference();
    }
}
