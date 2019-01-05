package co.igorski.centralcommittee.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cache.annotation.Cacheable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Cacheable
@Entity
public class CcTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String testName;
    @NotNull
    @Column(unique=true)
    private String testPath;
    @ManyToOne
    private Project project;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CcTest ccTest = (CcTest) o;
        return id.equals(ccTest.id) &&
                testName.equals(ccTest.testName) &&
                testPath.equals(ccTest.testPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, testName, testPath);
    }
}
