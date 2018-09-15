package co.igorski.hundreddays.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Document
public class Run {
    @Id
    private String id;
    private String userId;
    private String organizationId;
    private Date start;
    private Date end;
    private Collection<Result> results;

}
