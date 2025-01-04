package ru.ershov.project.common.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitConfig {

    @Value("${spring.rabbitmq.host}")
    private String rabbitHost;

    @Value("${spring.rabbitmq.username}")
    private String rabbitUsername;

    @Value("${spring.rabbitmq.password}")
    private String rabbitPassword;

    /**
     * Создаёт бин {@link ConnectionFactory} для подключения к RabbitMQ.
     * Настраивает автоматическое восстановление соединения и интервал восстановления сети.
     *
     * @return {@link ConnectionFactory} для работы с RabbitMQ.
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(rabbitHost);
        cachingConnectionFactory.setUsername(rabbitUsername);
        cachingConnectionFactory.setPassword(rabbitPassword);

        cachingConnectionFactory.getRabbitConnectionFactory().setAutomaticRecoveryEnabled(true);
        cachingConnectionFactory.getRabbitConnectionFactory().setNetworkRecoveryInterval(5000);

        return cachingConnectionFactory;
    }

    /**
     * Создаёт бин {@link AmqpAdmin} для управления очередями, обменниками и привязками в RabbitMQ.
     *
     * @return {@link AmqpAdmin} для административных операций.
     */
    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    /**
     * Создаёт бин {@link RabbitTemplate} для отправки и получения сообщений из RabbitMQ.
     * Настраивает конвертацию сообщений в JSON с использованием {@link Jackson2JsonMessageConverter}.
     *
     * @return {@link RabbitTemplate} для работы с сообщениями.
     */
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    /**
     * Создаёт бин {@link SimpleRabbitListenerContainerFactory} для настройки контейнера слушателей RabbitMQ.
     * Настраивает конвертацию сообщений в JSON с использованием {@link Jackson2JsonMessageConverter}.
     *
     * @param connectionFactory фабрика соединений, используемая контейнером.
     * @return {@link SimpleRabbitListenerContainerFactory} для обработки входящих сообщений.
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }

    /**
     * Создаёт бин {@link ObjectMapper} для сериализации и десериализации JSON.
     * Настраивает стратегию именования полей в snake_case и добавляет поддержку Java 8 Date/Time API.
     *
     * @return {@link ObjectMapper} для работы с JSON.
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        objectMapper.registerModule(new JavaTimeModule()); // Добавляем поддержку Java 8 Date/Time
        return objectMapper;
    }

    /**
     * Создаёт бин {@link Jackson2JsonMessageConverter} для конвертации сообщений в JSON и обратно.
     * Использует настроенный {@link ObjectMapper} для поддержки snake_case и Java 8 Date/Time API.
     *
     * @return {@link Jackson2JsonMessageConverter} для конвертации сообщений.
     */
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter(objectMapper());
    }
}