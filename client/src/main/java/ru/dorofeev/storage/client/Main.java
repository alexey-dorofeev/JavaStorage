package ru.dorofeev.storage.client;

public class Main {
    public static void main(String[] args) {
        new Client("localhost",9999).send();
    }
}
