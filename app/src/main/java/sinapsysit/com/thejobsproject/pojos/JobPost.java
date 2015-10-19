package sinapsysit.com.thejobsproject.pojos;

import java.io.Serializable;

/**
 * Created by jujomago on 11/10/2015.
 */
public class JobPost implements Serializable {
    private int id;
    private String title;
    private String postDate;
    private String description;
    private String[] contacts;

    public JobPost(int id, String title, String postDate, String description) {
        this.id = id;
        this.title = title;
        this.postDate = postDate;
        this.description = description;
    }

    public JobPost() {

    }

    public String[] getContacts() {
        return contacts;
    }

    public void setContacts(String[] contacts) {
        this.contacts = contacts;
    }

    @Override
    public String toString() {
       return "JobPost{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", postDate='" + postDate + '\'' +
                ", description='" + description + '\'' +
                '}';

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

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
