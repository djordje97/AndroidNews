package djordje97.com.androidnews.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.List;

import djordje97.com.androidnews.R;
import djordje97.com.androidnews.model.Comment;
import djordje97.com.androidnews.model.User;
import djordje97.com.androidnews.service.CommentService;
import djordje97.com.androidnews.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentListAdapter extends ArrayAdapter<Comment>{

    private SharedPreferences sharedPreferences;
    private User logged;
    private Comment comment;
    public CommentListAdapter(Context context, List<Comment> comments){super(context,0,comments);}

    @Override
    public View getView(int position, View view, ViewGroup viewGroup){
        comment = getItem(position);

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.comment_list_items,viewGroup,false);
        }

        TextView title_view = view.findViewById(R.id.title_comment_view);
        TextView author_view = view.findViewById(R.id.author_comment_view);
        TextView date_view = view.findViewById(R.id.date_comment_view);
        String formatedDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(comment.getDate());
        TextView comment_view = view.findViewById(R.id.comment_view);
        TextView like_view = view.findViewById(R.id.like_comment_text);
        TextView dislike_view = view.findViewById(R.id.dislike_comment_text);
        ImageButton deleteBtn = view.findViewById(R.id.delete_btn);
        sharedPreferences = getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String loggedUser=sharedPreferences.getString("loggedUser","");
        logged=new Gson().fromJson(loggedUser,User.class);

        if(!comment.getAuthor().getUsername().equals(logged.getUsername()))
            deleteBtn.setVisibility(View.INVISIBLE);


        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();

            }
        });
        title_view.setText(comment.getTitle());
        author_view.setText(comment.getAuthor().getName());
        date_view.setText(formatedDate);
        comment_view.setText(comment.getDescription());
        like_view.setText(String.valueOf(comment.getLikes()));
        dislike_view.setText(String.valueOf(comment.getDislikes()));

        return view;
    }
    public void delete(){
        CommentService commentService= ServiceUtils.commentService;
        Call<Void>call=commentService.deleteComment(comment.getId());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getContext(),"Comment is deleted",Toast.LENGTH_SHORT).show();
                CommentListAdapter.this.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });

    }
}
