package org.example.mqtt.publisher;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TemperaturePublisher {
	
	private final IMqttClient mqttClient;
	private final static String TOPIC = "serra/temperature";
	private final static boolean RETAINED = true;
	private final static int QOS = 1;
	
	
	@Autowired
	public TemperaturePublisher(IMqttClient mqttClient) {
		this.mqttClient = mqttClient;
	}
	
	
	public void publish(String misura) throws MqttException {
		mqttClient.publish(TOPIC, misura.getBytes(), QOS, RETAINED);
	}
	
}