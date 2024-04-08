package cloud.deuterium.wb.books.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by Milan Stojkovic 26-Mar-2024
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookQuery {
    @NotBlank(message = "title cannot be blank")
    private String title;
    @NotBlank(message = "Description cannot be blank")
    private String description;
    private String cover;
    private BigDecimal price;
}
