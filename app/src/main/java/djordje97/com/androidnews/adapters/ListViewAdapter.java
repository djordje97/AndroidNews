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

import java.util.ArrayList;

import djordje97.com.androidnews.R;
import djordje97.com.androidnews.model.Post;

public class ListViewAdapter extends ArrayAdapter <Post>{

  public ListViewAdapter(Context context, ArrayList<Post> posts){
      super(context,0,posts);
  }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Post post=getItem(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.list_view_item,null);
        ImageView image=(ImageView)convertView.findViewById(R.id.image_post);
        TextView title=(TextView)convertView.findViewById(R.id.post_title);
        TextView description=(TextView)convertView.findViewById(R.id.post_description);


        image.setImageResource(R.mipmap.slika);
        title.setText(post.getTitle());
        description.setText(post.getDescription());

        return convertView;
    }
}
