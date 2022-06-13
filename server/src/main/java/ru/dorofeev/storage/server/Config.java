package ru.dorofeev.storage.server;

public final class Config {

    public final int port = 9999;
    public final String workDirectory;

    public Config(String workDirectory) {
        this.workDirectory = workDirectory;
    }

    @Override
    public String toString() {
        return "Config{" +
                "port=" + port +
                ", workDirectory='" + workDirectory + '\'' +
                '}';
    }
}
