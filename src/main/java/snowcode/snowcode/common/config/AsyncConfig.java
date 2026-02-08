package snowcode.snowcode.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean(name = "taskExecutor")
    public TaskExecutor taskExecutor() {
        // 테스크 수행시 스레드풀 사용
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 유지할 풀 사이즈
        taskExecutor.setCorePoolSize(5);
        // 최대 풀 사이즈
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setQueueCapacity(10);
        // 스레드 종료까지 시간
        taskExecutor.setAwaitTerminationSeconds(30); // 30초 기다려주고 안전하게 종료
        // 스레드 네임 프리픽스 설정
        taskExecutor.setThreadNamePrefix("compile"); // 로그용
        taskExecutor.initialize();
        return taskExecutor;
    }
}