package androidproject.applicationlejosev3.utils;

public class MQTTConfig {
    private String broker;
    private String port;
    private String topic;
    private String clientID;
    private String username;
    private String password;

    public MQTTConfig(String broker, String topic, String clientID, String username, String password, String port) {
        this.broker = broker;
        this.port = port;
        this.topic = topic;
        this.clientID = clientID;
        this.username = username;
        this.password = password;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
