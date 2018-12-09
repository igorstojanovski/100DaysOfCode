package co.igorski.centralcommittee.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Outcome outcome = Outcome.UNKNOWN;
    @Enumerated(EnumType.STRING)
    private Status status = Status.QUEUED;
    private Date startTime;
    private Date endTime;
    @OneToOne(cascade = CascadeType.ALL)
    private CcTest test;
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="run_id")
    private Run run;
    @Column(length=10485760)
    private String error;

    public Result(CcTest test, Run run) {
        this.test = test;
        this.run = run;
    }
}