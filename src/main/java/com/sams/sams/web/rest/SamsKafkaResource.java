package com.sams.sams.web.rest;

import com.sams.sams.service.SamsKafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/sams-kafka")
public class SamsKafkaResource {

    private final Logger log = LoggerFactory.getLogger(SamsKafkaResource.class);

    private SamsKafkaProducer kafkaProducer;

    public SamsKafkaResource(SamsKafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping(value = "/publish")
    public void sendMessageToKafkaTopic(@RequestParam("message") String message) {
        log.debug("REST request to send to Kafka topic the message : {}", message);
        this.kafkaProducer.sendMessage(message);
    }
}
