import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// Message class representing a message in the system
class Message {
    private String content;
    private String topic;

    public Message(String content, String topic) {
        this.content = content;
        this.topic = topic;
    }

    public String getContent() {
        return content;
    }

    public String getTopic() {
        return topic;
    }
}

// Subscriber interface for subscribers to implement
interface Subscriber {
    void receiveMessage(Message message);
}

// Topic class representing a topic in the system
class Topic {
    private String name;
    private List<Subscriber> subscribers;

    public Topic(String name) {
        this.name = name;
        this.subscribers = new ArrayList<>();
    }

    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void unsubscribe(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    public void publishMessage(Message message) {
        for (Subscriber subscriber : subscribers) {
            subscriber.receiveMessage(message);
        }
    }
}

// Kafka-like system class managing topics and subscribers
class KafkaSystem {
    private Map<String, Topic> topics;

    public KafkaSystem() {
        this.topics = new ConcurrentHashMap<>();
    }

    public void createTopic(String name) {
        topics.putIfAbsent(name, new Topic(name));
    }

    public Topic getTopic(String name) {
        return topics.get(name);
    }

    public void deleteTopic(String name) {
        topics.remove(name);
    }

    public void publishMessage(String topicName, Message message) {
        Topic topic = topics.get(topicName);
        if (topic != null) {
            topic.publishMessage(message);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        KafkaSystem kafkaSystem = new KafkaSystem();

        // Create topics
        kafkaSystem.createTopic("topic1");
        kafkaSystem.createTopic("topic2");

        // Create subscribers
        Subscriber subscriber1 = new Subscriber() {
            @Override
            public void receiveMessage(Message message) {
                System.out.println("Subscriber 1 received message: " + message.getContent());
            }
        };

        Subscriber subscriber2 = new Subscriber() {
            @Override
            public void receiveMessage(Message message) {
                System.out.println("Subscriber 2 received message: " + message.getContent());
            }
        };

        // Subscribe subscribers to topics
        kafkaSystem.getTopic("topic1").subscribe(subscriber1);
        kafkaSystem.getTopic("topic1").subscribe(subscriber2);
        kafkaSystem.getTopic("topic2").subscribe(subscriber2);

        // Publish messages
        kafkaSystem.publishMessage("topic1", new Message("Message 1 for topic1", "topic1"));
        kafkaSystem.publishMessage("topic2", new Message("Message 1 for topic2", "topic2"));
    }
}
