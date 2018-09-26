package co.igorski.hundreddays.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Document
public class CcTest {
    @Id
    private String id;
    @Indexed(unique=true)
    private String testName;
    @NotNull
    private String testPath;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CcTest test = (CcTest) o;
        return Objects.equals(testName, test.testName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(testName);
    }
}
