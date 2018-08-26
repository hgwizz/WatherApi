package cl.sura.prueba.weatherapi.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Temperature",
        "Date"
})
public class Weathers {

    @JsonProperty("Temperature")
    private String temperature;
    @JsonProperty("Date")
    private String date;

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Weathers{" +
                "temperature='" + temperature + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
