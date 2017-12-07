package Model;
// Generated Dec 4, 2017 10:13:39 PM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Subject generated by hbm2java
 */
@Entity
@Table(name="subject"
    ,catalog="testsystem"
)
public class Subject  implements java.io.Serializable {


     private String id;
     private String title;
     private Byte deleted;
     private Set<Test> tests = new HashSet<Test>(0);

    public Subject() {
    }

	
    public Subject(String id, String title) {
        this.id = id;
        this.title = title;
        this.deleted = 0;
    }
    public Subject(String id, String title, Byte deleted, Set<Test> tests) {
       this.id = id;
       this.title = title;
       this.deleted = deleted;
       this.tests = tests;
    }
   
     @Id 

    
    @Column(name="id", unique=true, nullable=false, length=36)
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    
    @Column(name="title", nullable=false, length=300)
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    
    @Column(name="deleted")
    public Byte getDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(Byte deleted) {
        this.deleted = deleted;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="subject")
    public Set<Test> getTests() {
        return this.tests;
    }
    
    public void setTests(Set<Test> tests) {
        this.tests = tests;
    }




}


