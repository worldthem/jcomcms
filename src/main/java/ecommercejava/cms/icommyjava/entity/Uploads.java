package ecommercejava.cms.icommyjava.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity // This tells Hibernate to make a table out of this class
public class Uploads {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    @Column(length = 20)
    private String type;

    private Integer id_post;

    @Column(length = 255)
    private String file_title;

    @Column(length = 200)
    private String file;

    @Column(length = 255)
    private String originalFileName;
 
    private LocalDateTime date;


   public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
 
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
 
    public Integer getId_post() {
        return id_post;
    }

    public void setId_post(Integer id_post) {
        this.id_post = id_post;
    }
 
    public String getFile_title() {
        return file_title;
    }

    public void setFile_title(String file_title) {
        this.file_title = file_title;
    }
 
    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
 
    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }
 
    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

}
