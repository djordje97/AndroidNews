package djordje97.com.androidnews.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import djordje97.com.androidnews.R;
import djordje97.com.androidnews.model.Post;

public class ListViewAdapter extends ArrayAdapter <Post>{

  public ListViewAdapter(Context context, ArrayList<Post> posts){
      super(context,0,posts);
  }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup viewGroup
    ) {
        Post post=getItem(position);
        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_view_item,viewGroup,false);
        }
        ImageView image=(ImageView)view.findViewById(R.id.image_view);
        TextView date=(TextView)view.findViewById(R.id.date_view);
        TextView title=(TextView)view.findViewById(R.id.title_view);

        String newDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(post.getDate());
        image.setImageResource(R.mipmap.slika);
        title.setText(post.getTitle());
        date.setText(newDate);

        return view;
    }
}
