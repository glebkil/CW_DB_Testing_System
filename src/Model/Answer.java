package Model;
// Generated Dec 4, 2017 10:13:39 PM by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "answer",
        catalog = "testsystem"
)
public class Answer implements java.io.Serializable {

    private String id;
    private Question question;
    private String text;
    private boolean isCorrect;
    private boolean deleted;

    public Answer() {
    }

    public Answer(String id, Question question, String text, boolean isCorrect) {
        this.id = id;
        this.question = question;
        this.text = text;
        this.isCorrect = isCorrect;
        this.deleted = false;
    }

    @Id

    @Column(name = "id", unique = true, nullable = false, length = 36)
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    public Question getQuestion() {
        return this.question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Column(name = "text", nullable = false, length = 300)
    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Column(name = "is_correct", nullable = false)
    public boolean getIsCorrect() {
        return this.isCorrect;
    }

    public void setIsCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    @Column(name = "deleted")
    public boolean getDeleted() {
        return this.deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
