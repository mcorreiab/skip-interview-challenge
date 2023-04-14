package com.justeattakeaway.courierstatement.rabbitmq;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class RabbitConfiguration implements RabbitListenerConfigurer {

  private final RabbitProperties rabbitProperties;

  private final LocalValidatorFactoryBean validator;

  public RabbitConfiguration(RabbitProperties rabbitProperties, LocalValidatorFactoryBean validator) {
    this.rabbitProperties = rabbitProperties;
    this.validator = validator;
  }

  @Bean
  public Queue queue() {
    return new Queue(rabbitProperties.queues().deliveryCreated());
  }

  @Bean
  public TopicExchange exchange() {
    return new TopicExchange(rabbitProperties.queues().deliveryCreated());
  }

  @Bean
  public Binding bindingDeliveryCreated(Queue queue, TopicExchange exchange) {
    return BindingBuilder.bind(queue()).to(exchange())
        .with(rabbitProperties.queues().deliveryCreated());
  }

  @Bean
  public Jackson2JsonMessageConverter converter() {
    return new Jackson2JsonMessageConverter(
        JsonMapper.builder().addModule(new JavaTimeModule()).build());
  }

  @Override
  public void configureRabbitListeners(
      RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {
    rabbitListenerEndpointRegistrar.setValidator(this.validator);
  }
}
