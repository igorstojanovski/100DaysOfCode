package co.igorski.centralcommittee.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Entry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entry_id")
    private Long id;
    @ManyToOne(cascade = {CascadeType.REFRESH})
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Result result;
    @ManyToOne
    private CcTest test;
}
