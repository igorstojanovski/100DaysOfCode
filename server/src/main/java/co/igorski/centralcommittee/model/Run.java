package co.igorski.centralcommittee.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Run {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;
    @ManyToOne
    private Organization organization;
    private Date startTime;
    private Date endTime;
    //    @ElementCollection(targetClass = Entry.class, fetch = FetchType.EAGER)
    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL)
    private List<Entry> entries;

}
