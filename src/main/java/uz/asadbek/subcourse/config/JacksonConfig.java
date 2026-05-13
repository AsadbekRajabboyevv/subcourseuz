package uz.asadbek.subcourse.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.MapperFeature;


@Configuration
public class JacksonConfig {

    @Bean
    public JsonMapperBuilderCustomizer jacksonCustomizer() {
        return jsonMapperBuilder -> jsonMapperBuilder
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(DeserializationFeature.ACCEPT_FLOAT_AS_INT)
                .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public RestTemplate restTemplate() {
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(60000);
        factory.setReadTimeout(60000);
        return new RestTemplate(factory);
    }
}
