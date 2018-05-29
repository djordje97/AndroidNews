package djordje97.com.androidnews.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import djordje97.com.androidnews.LoginActivity;
import djordje97.com.androidnews.R;
import djordje97.com.androidnews.model.Post;
import djordje97.com.androidnews.model.Tag;
import djordje97.com.androidnews.model.User;
import djordje97.com.androidnews.service.PostService;
import djordje97.com.androidnews.service.ServiceUtils;
import djordje97.com.androidnews.service.TagService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadPostFragment extends Fragment {

    View view;

    private TagService tagService;
    private TextView tag_view;
    private List<Tag> tags;
    private LinearLayout linearLayout;
    private TextView place;
    private Post post;
    private LinearLayout newLinearLayout;
    private ImageButton like_view;
    private ImageButton dislike_view;
    private int counterLikes;
    private int counterDislikes;
    private PostService postService=ServiceUtils.postService;
    private TextView dislike_text;
    private TextView like_text;

    private boolean clickedLike;
    private boolean clickedDislike;
    SharedPreferences sharedPreferences;

    public ReadPostFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){



        view = inflater.inflate(R.layout.read_post_fragment,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String jsonMyObject = null;
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            jsonMyObject = extras.getString("Post");
        }
         post = new Gson().fromJson(jsonMyObject, Post.class);

        TextView title_view = view.findViewById(R.id.title_view);
        title_view.setText(post.getTitle());

        TextView author_view = view.findViewById(R.id.author_view);
        author_view.setText(post.getAuthor().getName());

        TextView date_view = view.findViewById(R.id.date_view);
        String newDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(post.getDate());
        date_view.setText(newDate);

         place = view.findViewById(R.id.place);



        TextView description_view = view.findViewById(R.id.description_view);
        description_view.setText(post.getDescription());

        like_text = view.findViewById(R.id.like_text);
        like_text.setText(String.valueOf(post.getLike()));

         dislike_text = view.findViewById(R.id.dislike_text);
        dislike_text.setText(String.valueOf(post.getDislike()));

        linearLayout = view.findViewById(R.id.linear_layout);

       tag_view = view.findViewById(R.id.tags_view);
        //tags_view.setText(getActivity().getIntent().getStringExtra("tags"));

        tagService = ServiceUtils.tagService;
        System.out.print(post.getId());
        Call<List<Tag>> call = tagService.getTagsByPost(post.getId());

        call.enqueue(new Callback<List<Tag>>() {
            @Override
            public void onResponse(Call<List<Tag>> call, Response<List<Tag>> response) {
                tags = response.body();
                newLinearLayout = new LinearLayout(getContext());
                newLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

                for(Tag t:tags){
                    tag_view.setText("#"+t.getName());
                }
            }

            @Override
            public void onFailure(Call<List<Tag>> call, Throwable t) {

            }
        });


        like_view=view.findViewById(R.id.like_view);
        dislike_view=view.findViewById(R.id.dislike_view);
        sharedPreferences = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String userJson= sharedPreferences.getString("loggedUser","");
        final User logged=new Gson().fromJson(userJson,User.class);

        ImageView image_view = view.findViewById(R.id.image_view);

        clickedLike = false;
        like_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(logged.getUsername().equals(post.getAuthor().getUsername())){
                    Toast.makeText(getContext(),"You can't like your post",Toast.LENGTH_SHORT).show();
                }else{
                    if(clickedLike == false){
                        addLike();
                        like_text.setText(String.valueOf(post.getLike()));
                        clickedLike = true;
                        dislike_view.setEnabled(false);
                    }else{
                        removeLike();
                        like_text.setText(String.valueOf(post.getLike()));
                        clickedLike = false;
                        dislike_view.setEnabled(true);
                    }
                }
            }
        });


        clickedDislike = false;
        dislike_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(logged.getUsername().equals(post.getAuthor().getUsername())){
                    Toast.makeText(getContext(),"You can't dislike your post", Toast.LENGTH_SHORT).show();
                }else{
                    if(clickedDislike == false){
                        addDislike();
                        dislike_text.setText(String.valueOf(post.getDislike()));
                        clickedDislike = true;
                        like_view.setEnabled(false);
                    }else{

                        removeDislike();
                        dislike_text.setText(String.valueOf(post.getDislike()));
                        clickedDislike = false;
                        like_view.setEnabled(false);
                    }

                }
            }
        });



        getAddress();

    }

    public void addLike(){

        counterLikes = post.getLike();

        post.setLike(counterLikes+1);

        System.out.println(post.getId());
        System.out.println(post.getLike());

        Call<Post> call = postService.updatePost(post,post.getId());
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                like_text.setText(String.valueOf(counterLikes+1));
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }

    public void addDislike(){

        counterDislikes = post.getDislike();
        post.setDislike(counterDislikes+1);

        Call<Post> call = postService.updatePost(post,post.getId());
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                dislike_text.setText(String.valueOf(counterDislikes+1));
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });

    }

    public void removeLike(){
        counterLikes = post.getLike();
        post.setLike(counterLikes-1);
        Call<Post> call = postService.updatePost(post,post.getId());
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                like_text.setText(String.valueOf(counterLikes-1));
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }

    public void removeDislike(){
        counterDislikes = post.getDislike();
        post.setDislike(counterDislikes - 1);
        Call<Post> call = postService.updatePost(post,post.getId());
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                dislike_text.setText(String.valueOf(counterDislikes-1));
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }

    public void getAddress(){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getContext(), Locale.getDefault());

        double lon = post.getLongitude();
        double lat = post.getLatitude();
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
            String city = addresses.get(0).getLocality();
            String country = addresses.get(0).getCountryName();
            place.setText(city + "," + country);
            Log.i(country,city);

            System.out.println(city);
            System.out.println(country);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
