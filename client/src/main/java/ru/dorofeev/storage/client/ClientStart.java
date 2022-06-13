package ru.dorofeev.storage.client;

import common.file.FileSynchronizer;

import java.io.IOException;

public class ClientStart {

    public static void main(String[] args) throws InterruptedException, IOException {

        Config config = new Config("7e705660-97da-45a9-a347-7d89187b242c", "C:\\client-storage");
        System.out.println(config);

        var client = new Client(config, new FileSynchronizer(config.workDirectory));

        while (true) {
            System.out.println("Start sync...");
            client.sync();
            System.out.println("Sync ended");
            Thread.sleep(config.delay);
        }
    }
}
