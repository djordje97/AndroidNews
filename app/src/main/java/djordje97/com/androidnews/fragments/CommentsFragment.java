package djordje97.com.androidnews.fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import djordje97.com.androidnews.R;
import djordje97.com.androidnews.adapters.CommentListAdapter;
import djordje97.com.androidnews.model.Comment;
import djordje97.com.androidnews.model.Post;
import djordje97.com.androidnews.model.User;
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
    private EditText title_comment_ET;
    private EditText description_comment_ET;
   private  Button addBtn;
   private Post post;

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
        sharedPreferences = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String jsonMyObject = null;
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            jsonMyObject = extras.getString("Post");
        }
         post = new Gson().fromJson(jsonMyObject, Post.class);

        listView = view.findViewById(R.id.comment_list);

        commentService = ServiceUtils.commentService;

        Call<List<Comment>> call = commentService.getCommentsByPost(post.getId());

        call.enqueue(new Callback<List<Comment>>() {
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
        title_comment_ET=view.findViewById(R.id.title_comment_edit);
        description_comment_ET=view.findViewById(R.id.comment_edit);
        addBtn=view.findViewById(R.id.btn_comment);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
                title_comment_ET.setText("");
                description_comment_ET.setText("");

            }
        });


    }



    public void add(){
        Comment comment=new Comment();
        comment.setTitle(title_comment_ET.getText().toString());
        comment.setDescription(description_comment_ET.getText().toString());
        comment.setLikes(0);comment.setDislikes(0);
        Date date = Calendar.getInstance().getTime();
        comment.setDate(date);
        String loggedUser=sharedPreferences.getString("loggedUser","");
        User logged=new Gson().fromJson(loggedUser,User.class);
        comment.setAuthor(logged);
        comment.setPost(post);

        CommentService commentService=ServiceUtils.commentService;
        Call<Comment>call=commentService.addComment(comment);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                Toast.makeText(getContext(),"Added comment",Toast.LENGTH_SHORT).show();
                FragmentTransaction t = getActivity().getSupportFragmentManager().beginTransaction();
                t.setAllowOptimization(false);
                t.detach(CommentsFragment.this).attach(CommentsFragment.this).commitAllowingStateLoss();
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {

            }
        });
    }
}
