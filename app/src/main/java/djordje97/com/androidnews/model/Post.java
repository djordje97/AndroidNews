package djordje97.com.androidnews.model;

import android.graphics.Bitmap;
import android.location.Location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Djole on 17/04/2018.
 */

public class Post implements Serializable {
    @SerializedName(value = "id")
    @Expose
    private Integer id;
    @SerializedName(value = "title")
    @Expose
    private String title;
    @SerializedName(value = "description")
    @Expose
    private String description;
    @SerializedName(value = "photo")
    @Expose
    private Bitmap photo;
    @SerializedName(value = "user")
    @Expose
    private User author;
    @SerializedName(value = "date")
    @Expose
    private Date date;
    private Location location;
    private List<Tag> tags;
    private List<Comment> comments;
    @SerializedName(value = "likes")
    @Expose
    private int like;
    @SerializedName(value = "dislike")
    @Expose
    private int dislike;


    public Post() {
        }

    public Post(Post post){
        this(post.getId(),post.getTitle(),post.getDescription(),post.getPhoto(),post.getAuthor(),post.getDate(),null,null,null,post.getLike(),post.getDislike());
    }

    public Post(int id, String title, String description, Bitmap photo, User author, Date date, Location location, List<Tag> tags, List<Comment> comments, int like, int dislike) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.photo = photo;
        this.author = author;
        this.date = date;
        this.location = location;
        this.tags = tags;
        this.comments = comments;
        this.like = like;
        this.dislike = dislike;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getDislike() {
        return dislike;
    }

    public void setDislike(int dislike) {
        this.dislike = dislike;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", photo=" + photo +
                ", author=" + author +
                ", date=" + date +
                ", location=" + location +
                ", tags=" + tags +
                ", comments=" + comments +
                ", like=" + like +
                ", dislike=" + dislike +
                '}';
    }
}
