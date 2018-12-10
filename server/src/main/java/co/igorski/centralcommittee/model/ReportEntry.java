package co.igorski.centralcommittee.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ReportEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date timestamp;
    private String key;
    private String value;

    public ReportEntry(Date timestamp, String key, String value) {
        this.key = key;
        this.value = value;
        this.timestamp = timestamp;
    }
}
