package com.takacsbence.simpletcp;

public class TerminalMessageParser implements MessageParser {

    @Override
    public Message parseMessage(String message) {
        if (message == null) {
            throw new IllegalArgumentException("message should not be empty");
        }
        String[] split = message.split(",");
        String author = split[0];
        String msg = split[1];
        return new Message(author, msg);
    }

    @Override
    public String parseMessage(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("message should not be null");
        }
        return message.getAuthor() + "," + message.getMessage();
    }

    public String prettyPrint(Message msg) {
        return "    [" + msg.getAuthor() + "]        " + msg.getMessage();
    }
}
