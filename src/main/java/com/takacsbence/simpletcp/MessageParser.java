package com.takacsbence.simpletcp;

public interface MessageParser {
    Message parseMessage(String message);
    String parseMessage(Message message);
    String prettyPrint(Message msg);
}
