package Model;
// Generated Dec 4, 2017 10:13:39 PM by Hibernate Tools 4.3.1

import java.util.ArrayList;
//import java.util.HashSet;
import java.util.List;
import javax.persistence.CascadeType;
//import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import jdk.nashorn.internal.ir.annotations.Ignore;

@Entity
@Table(name = "question",
        catalog = "testsystem"
)
public class Question implements java.io.Serializable {

    private String id;
    private Test test;
    private String text;
    private boolean deleted;
    private List<Answer> answers = new ArrayList<>(0);

    public Question() {
    }

    public Question(String id, Test test, String text) {
        this.id = id;
        this.test = test;
        this.text = text;
        this.deleted = false;
    }

    @Transient
    public boolean getHasOnlyOneCorrectAnswerOrNoAnswersYet() {        
        int correct_ans_count = 0;
        for(Answer ans : answers){
            if(ans.getIsCorrect()){
                correct_ans_count++;
            }
        }
        return correct_ans_count <= 1;
    }
    
    @Transient
    public boolean getHasNoCorrectAnswers() {        
        int correct_ans_count = 0;
        for(Answer ans : answers){
            if(ans.getIsCorrect()){
                correct_ans_count++;
            }
        }
        return correct_ans_count == 0;
    }


    @Id

    @Column(name = "id", unique = true, nullable = false, length = 36)
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "test_id", nullable = false)
    public Test getTest() {
        return this.test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    @Column(name = "text", nullable = false, length = 400)
    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Column(name = "deleted")
    public boolean getDeleted() {
        return this.deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question", cascade = { CascadeType.ALL })
    public List<Answer> getAnswers() {
        return this.answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
