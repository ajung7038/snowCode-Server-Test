package snowcode.snowcode.common.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatCustomConfig {

    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> containerCustomizer() {
        return factory -> {
            if (factory instanceof TomcatServletWebServerFactory tomcat) {
                tomcat.addConnectorCustomizers(connector -> {
                    // selectorTimeout을 5000ms(5초)로 변경 -> 0.5초로 변경
                    connector.setProperty("selectorTimeout", "2000");
                });
            }
        };
    }
}