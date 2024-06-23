package top.frankxxj.homework.api.videobackend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import top.frankxxj.homework.api.videobackend.security.role.Role;
import top.frankxxj.homework.api.videobackend.security.role.RoleRepository;
import top.frankxxj.homework.api.videobackend.security.user.BasicUserService;
import top.frankxxj.homework.api.videobackend.security.user.UserDto;

import java.util.List;

@SpringBootApplication
@Slf4j
public class VideoBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideoBackendApplication.class, args);
    }

    /**
     * Initialize basic authorities
     *
     * @param roleRepository {@link RoleRepository},a jpa repository for role management
     * @return command line runner
     */
    @Bean
    public CommandLineRunner initAuthorities(RoleRepository roleRepository, BasicUserService basicUserService) {
        return args -> {
            if (roleRepository.findAll().size() < 2) {
                log.info("Initializing basic authorities...");
                roleRepository.saveAllAndFlush(List.of(
                        new Role(null, "USER", null),
                        new Role(null, "ADMIN", null)
                ));
                log.info("Basic authorities initialized.");
            }
            if (!basicUserService.existAnyUser()) {
                log.info("Initializing basic user...");
                var user = basicUserService.create(new UserDto("user", "123456"));
                log.info("Basic user {} initialized.", user);
            }
        };
    }

}
