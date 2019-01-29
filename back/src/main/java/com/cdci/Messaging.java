package com.cdci;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Arrays;
import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class Messaging {
    private void getProperties(Supplier<ProducerRecord<String, String>> kafkaProducerConsumer) {
        var props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        producer.send(kafkaProducerConsumer.get());
        producer.close();
    }

    public void sendMessage(String topic, String key, String message) {
        getProperties(() -> new ProducerRecord<>(topic, 0, key, message));
    }

    public void consumeTopicMessage(String topic, String key, BiConsumer<String, String> biConsumer) throws InterruptedException {
        this.consumeTopicMessage(topic, key, biConsumer, false);
    }

    public void consumeTopicMessage(String topic, String key, BiConsumer<String, String> biConsumer, boolean join) throws InterruptedException {
        var props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-consumer-group");
        Runnable run = () -> {
            try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
                consumer.subscribe(Arrays.asList(topic, ""));

                while (!Thread.interrupted()) {
                    ConsumerRecords<String, String> recs = consumer.poll(1000);
                    if (recs.count() == 0) {
                        continue;
                    }

                    for (ConsumerRecord<String, String> rec : recs) {
                        System.out.println("New " + rec.key() + " recived.");
                        biConsumer.accept(rec.key(), rec.value());
                    }
                }
            }
        };
        Thread thread = new Thread(run);
        thread.start();
        if (join) {
            thread.join();
        }
    }

}
