package ws.serverprinter.server;

import java.io.*;
import java.nio.ByteBuffer;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/receive/fileserver")
public class FileServerEndpoint {

    static ByteArrayOutputStream baos = null;

    @OnOpen
    public void open(Session session, EndpointConfig conf) {
        System.out.println("FileServerEndpoint is open");
    }

    @OnMessage
    public void processUpload(ByteBuffer msg, boolean last, Session session) {
        System.out.println("ByteBuffer message");
        while(msg.hasRemaining()) {
            baos.write(msg.get());
        }
    }

    @OnMessage
    public void message(Session session, String msg) {
        System.out.println("Message: " + msg);
        if(!msg.equals("end")) {
            baos = new ByteArrayOutputStream();
        }else {
            try {
                baos.flush();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClose
    public void close(Session session, CloseReason reason) {
        System.out.println("WebSocket closed: "+ reason.getReasonPhrase());
    }

    @OnError
    public void error(Session session, Throwable t) {
        t.printStackTrace();

    }
}

