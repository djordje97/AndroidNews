package djordje97.com.androidnews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import djordje97.com.androidnews.adapters.DrawerListAdapter;
import djordje97.com.androidnews.model.NavItem;
import djordje97.com.androidnews.model.Post;
import djordje97.com.androidnews.model.Tag;
import djordje97.com.androidnews.model.User;
import djordje97.com.androidnews.service.PostService;
import djordje97.com.androidnews.service.ServiceUtils;
import djordje97.com.androidnews.service.TagService;
import djordje97.com.androidnews.service.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePostActivity extends AppCompatActivity {


    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout mDrawerPane;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    private SharedPreferences sharedPreferences;
    TextView titleEdit;
    TextView tagsEdit;
    TextView description;
    Button  uploadPhoto;
    Button createPost;
    private UserService userService;
    private  User inBody;
    private PostService postService;
    private Post responsePost;
    private List<Tag> allTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
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
        sharedPreferences=getSharedPreferences("MyPref", Context.MODE_PRIVATE);

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

        createPost=(Button)findViewById(R.id.create_btn);
        titleEdit=(TextView)findViewById(R.id.title_edit);
        tagsEdit=(TextView)findViewById(R.id.tags_edit);
        description=(TextView)findViewById(R.id.description_edit);
        uploadPhoto=(Button)findViewById(R.id.upload_btn);
        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), 1);
            }
        });
    }

    private void prepareMenu(ArrayList<NavItem> mNavItems ){
        mNavItems.add(new NavItem(getString(R.string.settings), "", R.drawable.ic_settings));
        mNavItems.add(new NavItem(getString(R.string.post), "", R.drawable.ic_posts));
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
            Intent preference = new Intent(CreatePostActivity.this,SettingsActivity.class);
            startActivity(preference);
        }else if(position == 1){Intent post = new Intent(CreatePostActivity.this,PostsActivity.class);
            startActivity(post);
        }else if(position == 2){
            Intent login = new Intent(CreatePostActivity.this,LoginActivity.class);
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
    protected void onResume() {
        super.onResume();
        createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText title=(EditText) findViewById(R.id.title_edit);
                EditText description=(EditText) findViewById(R.id.description_edit);
                EditText tags=(EditText) findViewById(R.id.tags_edit);

                String titleText=title.getText().toString();
                String descriptionText=description.getText().toString();
                Post post=new Post();
                post.setTitle(titleText);
                post.setDescription(descriptionText);
                post.setLike(0);
                post.setDislike(0);
                Date date = Calendar.getInstance().getTime();
                post.setDate(date);
                String loggedUser=sharedPreferences.getString("loggedUser","");
                User logged= new Gson().fromJson(loggedUser,User.class);
                post.setAuthor(logged);

                postService=ServiceUtils.postService;
                Call<Post> call=postService.createPost(post);
                call.enqueue(new Callback<Post>() {
                    @Override
                    public void onResponse(retrofit2.Call<Post> call, Response<Post> response) {
                        responsePost=response.body();
                        Toast.makeText(CreatePostActivity.this,"Post Created!",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(retrofit2.Call<Post> call, Throwable t) {

                    }
                });



            }
        });

        getAllTags();

    }
//    public  void addTags(){
//        String tagsString = tagsEdit.getText().toString().trim();
//        String[] separated = tagsString.split("#");
//        List<String> tagFilter = Arrays.asList(separated);
//        List<Tag> tagsForAdd=new ArrayList<>();
//
//        for(Tag t:allTags){
//
//        }
//
//
//
//    }

    public void getAllTags(){

        TagService tagService=ServiceUtils.tagService;
        Call<List<Tag>>call=tagService.getTags();
        call.enqueue(new Callback<List<Tag>>() {
            @Override
            public void onResponse(Call<List<Tag>> call, Response<List<Tag>> response) {
                allTags=response.body();
            }

            @Override
            public void onFailure(Call<List<Tag>> call, Throwable t) {

            }
        });
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
            Intent openSettings=new Intent(CreatePostActivity.this,SettingsActivity.class);
            startActivity(openSettings);
        }

        return super.onOptionsItemSelected(item);
    }
}
