package org.example.mqtt.simulazione;

import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.example.mqtt.model.Misura;
import org.example.mqtt.model.TopicCreator;
import org.example.mqtt.model.Topics;
import org.example.mqtt.publisher.Publisher;
import org.example.mqtt.subscriber.Subscriber;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;


/**
 * @author Alessandro Gattico
 */

public class Simulazione {
	
	private final Publisher publisher;
	private final Subscriber subscriber;
	
	private final String urlTopics = "http://localhost:8080/api/v1/utils/topics/get/all";
	
	
	public Simulazione() {
		this.publisher = new Publisher();
		this.subscriber = new Subscriber();
	}
	
	
	public Simulazione(Publisher publisher, Subscriber subscriber) {
		this.publisher = publisher;
		this.subscriber = subscriber;
	}
	
	
	public void start() throws MqttException {
		Gson gson = new Gson();
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject(urlTopics, String.class);
		
		Topics topics = gson.fromJson(result, Topics.class);
		
		for (TopicCreator topic : topics.getTopics()) {
			subscriber.subscribe(topic.getTopic());
			
			if (topic.getTypeSensore().equals("TEMPERATURA")) {
				String time =
						LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
				publisher.publish(topic.getTopic(),
						gson.toJson(new Misura(new Random().nextDouble(100), time, topic.getIdSensore())));
			} else if (topic.getTypeSensore().equals("UMIDITA")) {
				String time =
						LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
				publisher.publish(topic.getTopic(),
						gson.toJson(new Misura(new Random().nextDouble(50), time, topic.getIdSensore())));
			}
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		
		
	}
	
}
