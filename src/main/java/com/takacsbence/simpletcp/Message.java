package com.takacsbence.simpletcp;

import java.util.Objects;

public class Message {

    private final String author;
    private final String message;

    public Message(String author, String  message) {
        this.author = author;
        this.message = message;
    }

    public String getAuthor() {
        return author;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return author.equals(message1.author) && message.equals(message1.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, message);
    }

    @Override
    public String toString() {
        return "Message{" +
                "author='" + author + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
