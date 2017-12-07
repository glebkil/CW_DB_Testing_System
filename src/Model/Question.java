package Model;
// Generated Dec 4, 2017 10:13:39 PM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Question generated by hbm2java
 */
@Entity
@Table(name="question"
    ,catalog="testsystem"
)
public class Question  implements java.io.Serializable {


     private String id;
     private Test test;
     private String text;
     private Byte deleted;
     private Set<Answer> answers = new HashSet<Answer>(0);

    public Question() {
    }

	
    public Question(String id, Test test, String text) {
        this.id = id;
        this.test = test;
        this.text = text;
    }
    public Question(String id, Test test, String text, Byte deleted, Set<Answer> answers) {
       this.id = id;
       this.test = test;
       this.text = text;
       this.deleted = deleted;
       this.answers = answers;
    }
   
     @Id 

    
    @Column(name="id", unique=true, nullable=false, length=36)
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="test_id", nullable=false)
    public Test getTest() {
        return this.test;
    }
    
    public void setTest(Test test) {
        this.test = test;
    }

    
    @Column(name="text", nullable=false, length=400)
    public String getText() {
        return this.text;
    }
    
    public void setText(String text) {
        this.text = text;
    }

    
    @Column(name="deleted")
    public Byte getDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(Byte deleted) {
        this.deleted = deleted;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="question")
    public Set<Answer> getAnswers() {
        return this.answers;
    }
    
    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }




}

