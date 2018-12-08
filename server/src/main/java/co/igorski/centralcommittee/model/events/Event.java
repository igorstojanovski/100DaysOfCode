package co.igorski.centralcommittee.model.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;

@JsonTypeInfo(use= JsonTypeInfo.Id.NAME, property="type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TestFinished.class, name = "TestFinished"),
        @JsonSubTypes.Type(value = TestStarted.class, name = "TestStarted"),
        @JsonSubTypes.Type(value = TestDisabled.class, name = "TestDisabled"),
        @JsonSubTypes.Type(value = RunStarted.class, name = "RunStarted"),
        @JsonSubTypes.Type(value = RunFinished.class, name = "RunFinished"),
})
@Getter
@Setter
@NoArgsConstructor
public class Event {
    @NotNull
    private Date timestamp;
}
