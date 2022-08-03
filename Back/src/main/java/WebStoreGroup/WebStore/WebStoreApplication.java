package WebStoreGroup.WebStore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.RestController;
import th.co.geniustree.springdata.jpa.repository.support.JpaSpecificationExecutorWithProjectionImpl;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = JpaSpecificationExecutorWithProjectionImpl.class)
@RestController
public class WebStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebStoreApplication.class, args);

	}

}
