package djordje97.com.androidnews.model;

import android.os.AsyncTask;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;


/**
 * Created by Djole on 17/04/2018.
 */

public class Comment implements Serializable {
    @SerializedName(value = "id")
    @Expose
    private Integer id;
    @SerializedName(value = "title")
    @Expose
    private String title;
    @SerializedName(value = "description")
    @Expose
    private String description;
    @SerializedName(value = "user")
    @Expose
    private User author;
    @SerializedName(value = "photo")
    @Expose
    private Post post;
    @SerializedName(value = "date")
    @Expose
    private Date date;
    @SerializedName(value = "likes")
    @Expose
    private int likes;
    @SerializedName(value = "dislikes")
    @Expose
    private int dislikes;

    public Comment(int id, String title, String description, User author, Post post, Date date, int likes, int dislikes) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = author;
        this.post = post;
        this.date = date;
        this.likes = likes;
        this.dislikes = dislikes;

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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", author=" + author +
                ", post=" + post +
                ", date=" + date +
                ", likes=" + likes +
                ", dislikes=" + dislikes +
                '}';
    }
}
