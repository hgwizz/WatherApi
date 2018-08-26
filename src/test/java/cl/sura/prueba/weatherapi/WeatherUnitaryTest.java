package cl.sura.prueba.weatherapi;

import cl.sura.prueba.weatherapi.pojo.WeatherRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WeatherApiApplication.class)
@SpringBootTest
public class WeatherUnitaryTest {

    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setup () {
        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
        this.mockMvc = builder.build();
    }

    @Test
    public void POST_GetFullDataOfDayOK() throws Exception {

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post("/v1/weather")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(
                                WeatherRequest.builder().city("Santiago").dayOrWeek("D").measureUnit("F").build()
                        ));
        this.mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.CityName").exists())
                .andExpect(jsonPath("$.CityCode").exists())
                .andExpect(jsonPath("$.Weather").exists())
                .andExpect(jsonPath("$.Weather", hasSize(1)))
                .andDo(MockMvcResultHandlers.print());


    }
    @Test
    public void POST_GetFullDataOfWeekOK() throws Exception {

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post("/v1/weather")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(
                                WeatherRequest.builder().city("Santiago").dayOrWeek("W").measureUnit("F").build()
                        ));
        this.mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.CityName").exists())
                .andExpect(jsonPath("$.CityCode").exists())
                .andExpect(jsonPath("$.Weather").exists())
                .andExpect(jsonPath("$.Weather.[*]", hasSize(6)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void POST_OnMessagesWithEmptyDataReturnsBadRequest400() throws Exception {
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post("/v1/weather")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(
                                WeatherRequest.builder().build()
                        ));
        this.mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message", is("Bad Request")))
                .andDo(MockMvcResultHandlers.print());

    }

    private String toJson(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

}