package ma.enset.accountservice.config;

import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Yousef ELHAID
 */
@Configuration
public class AxonSerializerConfiguration {

    @Bean
    public Serializer messageSerializer() {
        return JacksonSerializer.builder().defaultTyping().build();
    }
}
