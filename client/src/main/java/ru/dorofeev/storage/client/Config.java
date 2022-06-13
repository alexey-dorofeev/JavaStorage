package ru.dorofeev.storage.client;

public final class Config {

    public final String clientId;
    public final String workDirectory;

    public final int delay = 5000;
    public final String host = "localhost";
    public final int port = 9999;

    public Config(String clientId, String workDirectory) {
        this.clientId = clientId;
        this.workDirectory = workDirectory;
    }

    @Override
    public String toString() {
        return "Config{" +
                "clientId='" + clientId + '\'' +
                ", workDirectory='" + workDirectory + '\'' +
                ", delay=" + delay +
                ", host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}
