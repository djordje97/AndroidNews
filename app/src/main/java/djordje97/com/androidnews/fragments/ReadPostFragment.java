package djordje97.com.androidnews.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.List;

import djordje97.com.androidnews.R;
import djordje97.com.androidnews.model.Post;
import djordje97.com.androidnews.model.Tag;
import djordje97.com.androidnews.service.ServiceUtils;
import djordje97.com.androidnews.service.TagService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadPostFragment extends Fragment {

    View view;

    private TagService tagService;
    private TextView tags_view;
    private List<Tag> tags;
    private LinearLayout linearLayout;

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
        Post post = new Gson().fromJson(jsonMyObject, Post.class);

        TextView title_view = view.findViewById(R.id.title_view);
        title_view.setText(post.getTitle());

        TextView author_view = view.findViewById(R.id.author_view);
        author_view.setText(post.getAuthor().getName());

        TextView date_view = view.findViewById(R.id.date_view);
        String newDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(post.getDate());
        date_view.setText(newDate);

        TextView description_view = view.findViewById(R.id.description_view);
        description_view.setText(post.getDescription());

        TextView like_text = view.findViewById(R.id.like_text);
        like_text.setText(String.valueOf(post.getLike()));

        TextView dislike_text = view.findViewById(R.id.dislike_text);
        dislike_text.setText(String.valueOf(post.getDislike()));

        linearLayout = view.findViewById(R.id.linear_layout);

        //tags_view = view.findViewById(R.id.tags_view);
        //tags_view.setText(getActivity().getIntent().getStringExtra("tags"));

         tagService = ServiceUtils.tagService;
        System.out.print(post.getId());
        Call<List<Tag>> call = tagService.getTagsByPost(post.getId());

        call.enqueue(new Callback<List<Tag>>() {
            @Override
            public void onResponse(Call<List<Tag>> call, Response<List<Tag>> response) {
                tags = response.body();


                for(Tag t:tags){
                    TextView tag_view = new TextView(getContext());
                    tag_view.setId(t.getId());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                            (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                    params.setMargins(10,10,10,10);
                    tag_view.setTextColor(getResources().getColor(R.color.white));
                    tag_view.setLayoutParams(params);
                    tag_view.setText(t.getName());
                    linearLayout.addView(tag_view);


                }
            }

            @Override
            public void onFailure(Call<List<Tag>> call, Throwable t) {

            }
        });


        ImageView image_view = view.findViewById(R.id.image_view);
    }



}
