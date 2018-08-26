package cl.sura.prueba.weatherapi.controller;

import cl.sura.prueba.weatherapi.pojo.CityWeather;
import cl.sura.prueba.weatherapi.pojo.ErrorDetails;
import cl.sura.prueba.weatherapi.pojo.WeatherRequest;
import cl.sura.prueba.weatherapi.service.WeatherApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/v1")
public class WeatherApiController {

    private static Logger logger = LoggerFactory.getLogger(WeatherApiController.class);

    @Autowired
    private WeatherApiService weatherApiService;


    @PostMapping("/weather")
    public ResponseEntity<?> getWeather(@Valid @RequestBody WeatherRequest weatherRequest){
        logger.info("Transaccion " + weatherRequest.toString());
        CityWeather responses = this.weatherApiService.getWeather(weatherRequest);
        return ResponseEntity.ok(responses);

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDetails handleException(MethodArgumentNotValidException exception) {

        String errorMsg = exception.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse(exception.getMessage());

        return ErrorDetails.builder().timestamp(new Date()).details(errorMsg).message("Bad Request").build();
    }
}
