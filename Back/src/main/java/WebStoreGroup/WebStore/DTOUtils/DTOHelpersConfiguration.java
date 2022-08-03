package WebStoreGroup.WebStore.DTOUtils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

@Configuration
public class DTOHelpersConfiguration {
    @Bean
    ProjectionFactory ProjectionFactoryBean() {
        return new SpelAwareProxyProjectionFactory();
    }
}
