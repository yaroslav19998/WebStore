package WebStoreGroup.WebStore.User;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;

@Configuration
public class RolesUserAdminDefaultConfig {
    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        return args -> {
            if (roleRepository.findByName("ROLE_USER") == null) {
                Role role = new Role("ROLE_USER");
                role = roleRepository.save(role);
            }
            if (roleRepository.findByName("ROLE_ADMIN") == null) {
                Role role2 = new Role("ROLE_ADMIN");
                role2 = roleRepository.save(role2);
            }
            if (userRepository.findByUsername("admin") == null
                    && roleRepository.findByName("ROLE_USER") != null
                    && roleRepository.findByName("ROLE_ADMIN") != null) {

                User admin = new User("admin", bCryptPasswordEncoder.encode("admin123"));
                admin.setRoles(new ArrayList<>(Arrays.asList(roleRepository.findByName("ROLE_USER"), roleRepository.findByName("ROLE_ADMIN"))));
                userRepository.save(admin);
            }


        };
    }
}
