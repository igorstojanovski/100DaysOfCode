package co.igorski.centralcommittee.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Run {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "run_id")
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;
    @ManyToOne
    private Organization organization;
    private Date startTime;
    private Date endTime;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "run", fetch = FetchType.EAGER)
    private Map<@NotNull String, Result> results;
    @OneToOne(cascade = CascadeType.ALL)
    private Project project;
}
