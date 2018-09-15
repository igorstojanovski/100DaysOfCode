package co.igorski.mongodbspringexample.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Document
public class Test {
    @Id
    private String id;
    @Indexed(unique=true)
    private String testName;
    @NotNull
    private String testPath;
}
