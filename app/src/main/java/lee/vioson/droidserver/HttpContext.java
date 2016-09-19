package lee.vioson.droidserver;

import java.net.Socket;
import java.util.HashMap;

/**
 * Author:李烽
 * Date:2016-07-29
 * FIXME
 * Todo
 */
public class HttpContext {

    private final HashMap<String, String> requestHeaders;
    private Socket socket;

    public HttpContext() {
        requestHeaders = new HashMap<String, String>();
    }

    public void setUnderlySocket(Socket remotePeer) {
        this.socket = remotePeer;
    }


    public Socket getUnderlySocket() {
        return this.socket;
    }

    public void addResquestHeader(String key, String value) {
        requestHeaders.put(key, value);
    }

    public String getResquestHeader(String key) {
        return requestHeaders.get(key);
    }
}
