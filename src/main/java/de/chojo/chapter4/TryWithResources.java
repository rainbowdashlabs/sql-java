package de.chojo.chapter4;

import java.io.Closeable;
import java.io.IOException;

public class TryWithResources {

    public static void main(String[] args) {
        System.out.println(io());
        System.out.println(ioException());
    }

    public static String io() {
        try (MyCloseable closeable = new MyCloseable()) {
            return closeable.read();
        } catch (IOException e) {
            System.out.println(e);
        }
        return "failed";
    }

    public static String ioException() {
        MyCloseable closeable = new MyCloseable();
        MyCloseable anotherCloseable = new MyCloseable();
        try (closeable; anotherCloseable) {
            closeable.exception();
            return closeable.read();
        } catch (IOException e) {
            System.out.println(e);
        }
        return "failed";
    }

    static class MyCloseable implements Closeable {

        String read() {
            return "done";
        }

        void exception() throws IOException {
            throw new IOException();
        }

        @Override
        public void close() throws IOException {
            System.out.println("Closed");
        }
    }
}
