package digital.and.bootcamp.slackintegration.config;

import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class SlackConfig {
    @Bean
    public AppConfig loadAppConfig() {
        AppConfig config = new AppConfig();
        config.setSigningSecret(System.getenv("SIGNING_SECRET"));
        config.setSingleTeamBotToken(System.getenv("BOT_TOKEN"));
        return config;
    }

    @Bean
    public App initSlackApp(AppConfig config) {
        return new App(config);
    }

}
