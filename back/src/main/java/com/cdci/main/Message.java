package com.cdci.main;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.Consumer;

public class Message
{
    private final Queue<String> strings = new ArrayDeque<>();

    public void add(String message)
    {
        strings.add(message);
    }

    public void handleMessages(Consumer<String> stringConsumer)
    {
        while (!false)
        {
            String poll = strings.poll();
            if (poll == null)
            {
                continue;
            }
            stringConsumer.accept(poll);
        }
    }
}
