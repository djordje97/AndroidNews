package djordje97.com.androidnews;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import djordje97.com.androidnews.adapters.DrawerListAdapter;
import djordje97.com.androidnews.adapters.ViewPagerAdapter;
import djordje97.com.androidnews.fragments.CommentsFragment;
import djordje97.com.androidnews.fragments.ReadPostFragment;
import djordje97.com.androidnews.model.Comment;
import djordje97.com.androidnews.model.NavItem;
import djordje97.com.androidnews.model.Post;
import djordje97.com.androidnews.model.Tag;
import djordje97.com.androidnews.model.User;

public class ReadPostActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout mDrawerPane;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_post);

        tabLayout = findViewById(R.id.tablayout);
        appBarLayout = findViewById(R.id.appbar);
        viewPager = findViewById(R.id.viewpager);

        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.AddFragment(new ReadPostFragment(),"Read post");
        pagerAdapter.AddFragment(new CommentsFragment(),"Comments");

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

      /*  Bitmap b = BitmapFactory.decodeResource(getResources(),R.mipmap.slika);
        User user = new User(1, "Petar", b, "pera", "123", null, null);
        Date date = new Date();
        Post post=new Post(1, "Avengers", "Avengers Infinity war,best movie", b, user, date, null, null, null, 12, 3);
        List<Post> posts = new ArrayList<Post>();
        posts.add(post);

        Tag tag = new Tag(1, "#good", posts);
        List<Tag> tags = new ArrayList<Tag>();
        tags.add(tag);
        post.setTags(tags);
*/


     /*   TextView tv = (TextView)findViewById(R.id.title_text_view);
        tv.setText(post.getTitle());
        TextView td = (TextView)findViewById(R.id.description_text_view);
        td.setText(post.getDescription());
        ImageView im = (ImageView) findViewById(R.id.picture);
        im.setImageBitmap(b);
        TextView tt = (TextView)findViewById(R.id.tags_text_view);
        tt.setText(post.getTags().get(0).getName());
        TextView tu = (TextView)findViewById(R.id.author_text_view);
        tu.setText(post.getAuthor().getUsername());
        TextView tdate = (TextView)findViewById(R.id.date_text_view);
        tdate.setText(post.getDate().toString());
        TextView tlike = (TextView)findViewById(R.id.like_number);
        tlike.setText(String.valueOf(post.getLike()));
        TextView tdislike = (TextView)findViewById(R.id.dislike_number);
        tdislike.setText(String.valueOf(post.getDislike()));
*/


        prepareMenu(mNavItems);

        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerList = (ListView) findViewById(R.id.navList);

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
              //  getActionBar().setTitle(mTitle);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
            //    getActionBar().setTitle(mDrawerTitle);
                getSupportActionBar().setTitle("Android News");
                invalidateOptionsMenu();
            }
        };
    }

    private void prepareMenu(ArrayList<NavItem> mNavItems ){
        mNavItems.add(new NavItem(getString(R.string.settings), "", R.drawable.ic_settings));
        mNavItems.add(new NavItem(getString(R.string.post), "", R.drawable.ic_posts));
        mNavItems.add(new NavItem("Log out","",R.drawable.ic_logout));

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItemFromDrawer(position);
        }
    }

    private void selectItemFromDrawer(int position) {
        if(position == 0){
            Intent preference = new Intent(ReadPostActivity.this,SettingsActivity.class);
            startActivity(preference);
        }else if(position == 1){Intent post = new Intent(ReadPostActivity.this,PostsActivity.class);
            startActivity(post);
        }else if(position == 2){
            Intent login = new Intent(ReadPostActivity.this,LoginActivity.class);
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
            Intent openSettings=new Intent(ReadPostActivity.this,SettingsActivity.class);
            startActivity(openSettings);
        }else if(id == R.id.action_add_post ){
            Toast toast=Toast.makeText(getApplicationContext(),"Add post",Toast.LENGTH_SHORT);
            toast.show();
        }else if(id == R.id.action_delete_post ){
            Toast toast=Toast.makeText(getApplicationContext(),"Delete post",Toast.LENGTH_SHORT);
            toast.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
