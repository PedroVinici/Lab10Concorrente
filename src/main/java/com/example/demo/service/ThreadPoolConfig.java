import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadPoolConfig {

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);  // Tamanho mínimo do pool de threads
        executor.setMaxPoolSize(50);   // Tamanho máximo do pool de threads
        executor.setQueueCapacity(100);  // Capacidade máxima da fila de espera para tarefas
        executor.setThreadNamePrefix("Worker-");  // Prefixo para os nomes das threads
        executor.initialize();  // Inicializa o executor
        return executor;
    }
}
