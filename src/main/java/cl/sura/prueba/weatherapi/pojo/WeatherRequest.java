package cl.sura.prueba.weatherapi.pojo;

import cl.sura.prueba.weatherapi.validation.IsOptionCorrect;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeatherRequest {

    @NotNull
    private String city;
    @NotNull
    @IsOptionCorrect(optionA = "F", optionB = "C",
            message = "MeasureUnit only can be C or F")
    private String measureUnit;
    @NotNull
    @IsOptionCorrect(optionA = "D", optionB = "W",
            message = "DayOrWeek only can be D or W")
    private String dayOrWeek;
}
