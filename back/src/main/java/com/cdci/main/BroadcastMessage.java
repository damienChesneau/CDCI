package com.cdci.main;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Function;

public class BroadcastMessage
{
    public void out() throws IOException, ClassNotFoundException
    {
        ServerSocket z = new ServerSocket(5555);
        Socket accept = z.accept();
        InputStream inputStream = accept.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(inputStream);
        Object o = ois.readObject();
        if (o instanceof Function)
        {
            Function<String, String> os = (Function<String, String>) o;
            String apply = os.apply(null);

        }
    }
}
