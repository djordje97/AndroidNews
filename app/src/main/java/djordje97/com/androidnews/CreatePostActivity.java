package djordje97.com.androidnews;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import djordje97.com.androidnews.adapters.DrawerListAdapter;
import djordje97.com.androidnews.model.NavItem;
import djordje97.com.androidnews.model.Post;
import djordje97.com.androidnews.model.Tag;
import djordje97.com.androidnews.model.User;
import djordje97.com.androidnews.service.PostService;
import djordje97.com.androidnews.service.ServiceUtils;
import djordje97.com.androidnews.service.TagService;
import djordje97.com.androidnews.service.UserService;
import djordje97.com.androidnews.util.LocationDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePostActivity extends AppCompatActivity  implements LocationListener{


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
    private Bitmap bitmap;
    private byte[] byteArray;

    private double longitude;
    private double latitude;
    private Button location_btn;
    private EditText  location_text;

    private LocationManager locationManager;
    private AlertDialog dialog;
    private String provider;
    private Location location;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

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


        location_text = findViewById(R.id.location_edit);
        location_btn = findViewById(R.id.location_btn);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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

     //   getAllTags();
        location_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getProvider();

                if (location == null) {
                    Toast.makeText(getApplicationContext(), "Location not found", Toast.LENGTH_SHORT).show();
                }
                if (location != null) {
                    System.out.println("LONGITUDEEE: "+location.getLongitude() + "LATITUDEEEE:" + location.getLatitude());
                    getAddress(location.getLatitude(),location.getLongitude());
                    onLocationChanged(location);
                }
            }
        });

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
    public void createPost(){
        EditText title=(EditText) findViewById(R.id.title_edit);
        EditText description=(EditText) findViewById(R.id.description_edit);
        EditText tags=(EditText) findViewById(R.id.tags_edit);

        String titleText=title.getText().toString();
        String descriptionText=description.getText().toString();
        Post post=new Post();
        post.setTitle(titleText);
        post.setDescription(descriptionText);
        post.setLike(0);
        post.setPhoto(bitmap);
        post.setDislike(0);
        post.setLongitude(longitude);
        post.setLatitude(latitude);

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
                Intent postsAll=new Intent(CreatePostActivity.this,PostsActivity.class);
                startActivity(postsAll);
            }

            @Override
            public void onFailure(retrofit2.Call<Post> call, Throwable t) {

            }
        });

        TextView userName=(TextView)findViewById(R.id.userName);
        userName.setText(logged.getUsername());
    }

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
        inflater.inflate(R.menu.create_post,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id == R.id.action_settings)
        {
            Intent openSettings=new Intent(CreatePostActivity.this,SettingsActivity.class);
            startActivity(openSettings);
        }else if(id == R.id.submitCreate){
            createPost();
        }else if(id == R.id.cancel_create){
            Intent backToPost=new Intent(CreatePostActivity.this,PostsActivity.class);
            startActivity(backToPost);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);


            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        System.out.println("******************");
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private void showLocatonDialog() {
        if (dialog == null) {
            dialog = new LocationDialog(this).prepareDialog();
        } else {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        dialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(this);
    }


    public void getProvider(){
        Criteria criteria = new Criteria();

        provider = locationManager.getBestProvider(criteria, true);

        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean wifi = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(!gps &&  !wifi){
            showLocatonDialog();
        }else{
            if(checkLocationPermission()){
                if(ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                    locationManager.requestLocationUpdates(provider,0,0,this);
                    System.out.println("FINE LOC");

                }else if(ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                    locationManager.requestLocationUpdates(provider,0,0,this);
                    System.out.println("COARSE LOC");
                }
            }
        }

        location = null;

        if(checkLocationPermission()){
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                location = locationManager.getLastKnownLocation(provider);
            }else if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                location = locationManager.getLastKnownLocation(provider);
            }
        }
    }

    public boolean checkLocationPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                new AlertDialog.Builder(this)
                        .setTitle("Allow user location")
                        .setMessage("To continue working we need your locations... Allow now?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(CreatePostActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();

            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},MY_PERMISSIONS_REQUEST_LOCATION);

            }
            return false;
        }else{
            return true;
        }
    }

    public void getAddress(double latitude,double longitude){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());



        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String city = addresses.get(0).getLocality();
            String country = addresses.get(0).getCountryName();
            location_text.setText(city + "," + country);


            System.out.println(city);
            System.out.println(country);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
