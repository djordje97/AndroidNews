package djordje97.com.androidnews;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import djordje97.com.androidnews.adapters.DrawerListAdapter;
import djordje97.com.androidnews.adapters.ListViewAdapter;
import djordje97.com.androidnews.model.NavItem;
import djordje97.com.androidnews.model.Post;
import djordje97.com.androidnews.model.User;

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
    ArrayList<Post> posts=new ArrayList<>();
    private ListViewAdapter listViewAdapter;
    private Post post1;
    private Post post2;
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

        Bitmap b = BitmapFactory.decodeResource(getResources(),R.mipmap.slika);
        User user = new User(1, "Petar", b, "pera", "123", null, null);
        Date date = new Date(2018-1900,3-1,23,8,45);
        Date d2=new Date(2018-1900,2-1,25,9,45);
        post1=new Post(1, "Avengers", "Avengers Infinity war,best movie", b, user, date, null, null, null, 12, 3);
        post2=new Post(2,"Super News","Extraaa",b,user,d2,null,null,null,50,7);

        posts.add(post1);
        posts.add(post2);
        ListView listView=findViewById(R.id.list_view);

        listViewAdapter=new ListViewAdapter(this,posts);
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent startReadPost=new Intent(PostsActivity.this,ReadPostActivity.class);
                startActivity(startReadPost);
            }
        });

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        consultPreference();
    }

    private void consultPreference(){
        sortPostByDate=sharedPreferences.getBoolean(getString(R.string.prefer_sort_post_date_key),false);
        sortPostByPopularity=sharedPreferences.getBoolean(getString(R.string.sort_post_popularity_key),false);

        if(sortPostByDate == true){
            sortDate();
        }
        if(sortPostByPopularity == true){
            sortByPopularity();
        }
    }

    public void sortDate(){
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

    private void prepareMenu(ArrayList<NavItem> mNavItems ){
        mNavItems.add(new NavItem(getString(R.string.settings), "", R.drawable.ic_settings));
        mNavItems.add(new NavItem(getString(R.string.create_post), "", R.drawable.ic_create_post));

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
        }else if(position == 1){Intent preference = new Intent(PostsActivity.this,CreatePostActivity.class);
            startActivity(preference);
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
        consultPreference();
    }
}
