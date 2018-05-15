package djordje97.com.androidnews.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import djordje97.com.androidnews.R;
import djordje97.com.androidnews.model.Post;

public class PostListAdapter extends ArrayAdapter<Post> {


    public PostListAdapter(Context context, List<Post> posts){
        super(context,0,posts);
    }




    @Override
    public View getView(int position, View view, ViewGroup viewGroup){
        final Post post = getItem(position);

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_view_item,viewGroup,false);
        }

        final TextView date_view = view.findViewById(R.id.date_view);
        final TextView title_view = view.findViewById(R.id.title_view);
        final ImageView image_view = view.findViewById(R.id.image_view);



        String newDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(post.getDate());


        date_view.setText(newDate);
        title_view.setText(post.getTitle());

        image_view.setImageBitmap(post.getPhoto());


        return view;
    }
}
