package cl.sura.prueba.weatherapi.pojo;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "CityName",
        "CityCode"
})
public class CityWeather {

    @JsonProperty("CityName")
    private String cityName;
    @JsonProperty("CityCode")
    private String cityCode;
    @JsonProperty("Weather")
    private List<Weathers> weathers = new ArrayList<>();

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public List<Weathers> getWeathers() {
        return weathers;
    }

    public void setWeathers(List<Weathers> weathers) {
        this.weathers = weathers;
    }

    public void addWeather(Weathers weathers){
        this.weathers.add(weathers);
    }

    @Override
    public String toString() {
        return "CityWeather{" +
                "cityName='" + cityName + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", weather=" + weathers +
                '}';
    }
}
