package cloud.deuterium.wb.books.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
public class CreateBookRequest {

    @NotBlank(message = "title cannot be blank")
    private String title;
    @NotBlank(message = "Description cannot be blank")
    @Size(min = 10, max = 50, message = "Description must be between 10 and 50 characters")
    private String description;
    @Size(max = 250, message = "Cover must be between up to 250 characters")
    private String cover;
    @Positive
    private BigDecimal price;
}
