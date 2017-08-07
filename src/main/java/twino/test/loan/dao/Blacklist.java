package twino.test.loan.dao;

import javax.persistence.*;

@Entity
@Table(indexes = {@Index(name = "person_idx", columnList = "personId")})
public class Blacklist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long personId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }
}
