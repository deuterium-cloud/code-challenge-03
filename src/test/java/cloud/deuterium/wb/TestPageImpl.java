package cloud.deuterium.wb;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Milan Stojkovic 02-Feb-2024
 */
public class TestPageImpl<T> extends PageImpl<T> {
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public TestPageImpl(@JsonProperty("content") List<T> content, @JsonProperty("number") int number,
                        @JsonProperty("size") int size, @JsonProperty("totalElements") Long totalElements,
                        @JsonProperty("pageable") JsonNode pageable, @JsonProperty("last") boolean last,
                        @JsonProperty("totalPages") int totalPages, @JsonProperty("sort") JsonNode sort,
                        @JsonProperty("numberOfElements") int numberOfElements) {
        super(content, PageRequest.of(number, 1), 10);
    }

    public TestPageImpl(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public TestPageImpl(List<T> content) {
        super(content);
    }

    public TestPageImpl() {
        super(new ArrayList<>());
    }
}
