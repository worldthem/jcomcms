package ecommercejava.cms.icommyjava.entity;

import javax.persistence.*;

@Entity // This tells Hibernate to make a table out of this class
public class Author {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private Integer post_id;
 
    @Column(columnDefinition = "TEXT")
    private String title;
 
    @Column(columnDefinition = "TEXT")
    private String text;


   public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
 
    public Integer getPost_id() {
        return post_id;
    }

    public void setPost_id(Integer post_id) {
        this.post_id = post_id;
    }
 
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
 
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
