package ecommercejava.cms.icommyjava.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity // This tells Hibernate to make a table out of this class
public class Comments {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer commentsid;


   // private Integer  userid;

    @Column(length = 300, name = "comment_author")
    private String commentauthor;

    @Column(length = 255, name = "comment_author_email")
    private String commentauthoremail;

    @Column(length = 100, name = "comment_author_IP")
    private String commentathorip;
 
    @Column(columnDefinition = "TEXT")
    private String comment;
 
    @Column(length = 5)
    private Integer status;
 
    @Column(length = 5)
    private Integer stars;

    @UpdateTimestamp
    private LocalDateTime updated_at;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime created_at;

   /*
    @Column(name = "productid", nullable=false)
    private Integer productid;
    */

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="productid" )
    private Product product;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userid" )
    private Users user;

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getCommentAuthor() {
        return commentauthor;
    }

    public void setCommentAuthor(String commentAuthor) {
        this.commentauthor = commentAuthor;
    }

    public String getCommentAuthorEmail() {
        return commentauthoremail;
    }

    public void setCommentAuthorEmail(String commentAuthorEmail) {
        this.commentauthoremail = commentAuthorEmail;
    }

    public String getCommentAthorIp() {
        return commentathorip;
    }

    public void setCommentAthorIp(String commentAthorIp) {
        this.commentathorip = commentAthorIp;
    }


 
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
 
    public Integer getStatus() {
        return status == null ? 1 : status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
 
    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }
 
    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }
 
    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
    public void setCreated() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        this.created_at = LocalDateTime.now();
    }

    public Integer getCommentsid() {
        return commentsid;
    }

    public void setCommentsid(Integer commentsid) {
        this.commentsid = commentsid;
    }
/*
    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }


    public Integer getProductid() {
        return productid;
    }

    public void setProductid(Integer productid) {
        this.productid = productid;
    }

     */

}
