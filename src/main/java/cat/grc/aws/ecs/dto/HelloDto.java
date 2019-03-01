package cat.grc.aws.ecs.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class HelloDto implements Serializable {

    private final long id;
    private final String content;

}
