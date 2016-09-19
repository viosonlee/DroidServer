package lee.vioson.droidserver;

/**
 * Author:李烽
 * Date:2016-07-29
 * FIXME
 * Todo
 */
public class WebConfig {
    private int port;

    private int maxParallels;

    public int getMaxParallels() {
        return maxParallels;
    }

    public void setMaxParallels(int maxParallels) {
        this.maxParallels = maxParallels;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
