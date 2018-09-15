package co.igorski.hundreddays.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

@Getter
@Setter
@NoArgsConstructor
@Document
public class User {
    @Id
    private String id;
    @Indexed(unique=true)
    private String username;
    @NonNull
    private String name;
    @NonNull
    private String organizationId;
}
