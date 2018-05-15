package djordje97.com.androidnews.fragments;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.List;

import djordje97.com.androidnews.R;
import djordje97.com.androidnews.adapters.CommentListAdapter;
import djordje97.com.androidnews.model.Comment;
import djordje97.com.androidnews.model.Post;
import djordje97.com.androidnews.service.CommentService;
import djordje97.com.androidnews.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsFragment extends Fragment {

    View view;

    private CommentService commentService;

    private List<Comment> comments;
    private ListView listView;
    private CommentListAdapter commentListAdapter;

    private SharedPreferences sharedPreferences;

    private boolean sortCommentsByDate;
    private boolean sortCommentsByPopularity;

    public CommentsFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        view = inflater.inflate(R.layout.comment_fragment,container,false);
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

        listView = view.findViewById(R.id.comment_list);

        commentService = ServiceUtils.commentService;

        Call<List<Comment>> call = commentService.getCommentsByPost(post.getId());

        call.enqueue(new Callback<List<Comment>>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                comments = response.body();
                commentListAdapter = new CommentListAdapter(getContext(),comments);
                listView.setAdapter(commentListAdapter);
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {

            }
        });


        setUp();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void setUp() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
    }
}
