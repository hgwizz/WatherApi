package cl.sura.prueba.weatherapi.service;

import cl.sura.prueba.weatherapi.exceptions.NoDataFoundException;
import cl.sura.prueba.weatherapi.exceptions.NotAuthorizedException;
import cl.sura.prueba.weatherapi.pojo.CityWeather;
import cl.sura.prueba.weatherapi.pojo.WeatherApi;
import cl.sura.prueba.weatherapi.pojo.WeatherRequest;
import cl.sura.prueba.weatherapi.pojo.Weathers;
import cl.sura.prueba.weatherapi.pojo.multiple.ForeCastWeather;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class WeatherApiService {


    private static Logger logger = LoggerFactory.getLogger(WeatherApiService.class);

    @Value("${api.url}")
    private String weatherAPIUrl;
    @Value("${api.url.forecast}")
    private String weatherAPIDailyUrl;
    @Value("${api.key}")
    private String weatherApiKey;
    @Value("${api.unit.celsius}")
    private String unitCelsius;
    @Value("${api.unit.fahrenheit}")
    private String unitFahrenheit;

    @Autowired
    private RestTemplate restTemplate;

    public CityWeather getWeather(WeatherRequest weatherRequest) {

        CityWeather response = new CityWeather();
        ResponseEntity responseEntity = null;
        String url = this.buildUrl(weatherRequest.getCity(), weatherRequest.getMeasureUnit(), weatherRequest.getDayOrWeek());

        logger.debug("Consultado ... " + url);

            if ("D".equalsIgnoreCase(weatherRequest.getDayOrWeek()))
                responseEntity = this.getWeatherFromService(url, WeatherApi.class);
            else
                responseEntity = this.getWeatherFromService(url, ForeCastWeather.class);

            if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
                if (responseEntity.getBody() instanceof WeatherApi) {

                    WeatherApi weatherApi = (WeatherApi) responseEntity.getBody();

                    response.setCityName(weatherApi.getName());
                    response.setCityCode(weatherApi.getSys().getCountry());

                    Weathers weathers = new Weathers();

                    weathers.setDate(this.getCurrentDate());
                    weathers.setTemperature(weatherApi.getMain().getTemp().toString());
                    response.addWeather(weathers);

                } else if (responseEntity.getBody() instanceof ForeCastWeather) {

                    ForeCastWeather foreCastWeather = (ForeCastWeather) responseEntity.getBody();

                    response.setCityName(foreCastWeather.getCity().getName());
                    response.setCityCode(foreCastWeather.getCity().getCountry());

                    if (foreCastWeather.getList() != null && !foreCastWeather.getList().isEmpty()) {

                        List<cl.sura.prueba.weatherapi.pojo.multiple.List> temperatureDates = foreCastWeather.getList().stream().distinct().collect(Collectors.toList());

                        for (cl.sura.prueba.weatherapi.pojo.multiple.List temperature : temperatureDates) {

                            Weathers weathers = new Weathers();

                            weathers.setDate(temperature.getDtTxt());
                            weathers.setTemperature(temperature.getMain().getTemp().toString());
                            response.addWeather(weathers);
                        }
                    }else
                        throw new NoDataFoundException("No data found");
                }
            } else
                throw new NotAuthorizedException("Error in get data from api");

        return response;

    }

    private ResponseEntity getWeatherFromService(String url, Class clazz){
        return restTemplate.exchange(url,HttpMethod.GET,null,clazz);
    }

    private String getCurrentDate(){
        Date currentDate = new Date();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return df.format(currentDate);
    }


    private String buildUrl(String city, String unit, String dayOrWeek){
        StringBuffer urlCompose = new StringBuffer();

        if ("W".equalsIgnoreCase(dayOrWeek))
            urlCompose.append(weatherAPIDailyUrl);
        else
            urlCompose.append(weatherAPIUrl);

        urlCompose.append(city);

        if ("F".equalsIgnoreCase(unit))
            urlCompose.append(unitFahrenheit);
        else
            urlCompose.append(unitCelsius);

        urlCompose.append(weatherApiKey);

        return urlCompose.toString();

    }
}
