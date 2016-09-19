package lee.vioson.droidserver;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author:李烽
 * Date:2016-07-29
 * FIXME
 * Todo
 */
public class SimpleHttpServer {

    private static final String TAG = "SimpleHttpServer";
    private boolean isRun;
    private WebConfig webConfig;
    private ServerSocket socket;

    private ExecutorService threadPool;

    private Set<IResourceUriHandler> resourceUriHandlerSet;

    public SimpleHttpServer(WebConfig webConfig) {
        this.webConfig = webConfig;
        threadPool = Executors.newCachedThreadPool();
        resourceUriHandlerSet = new HashSet<>();
    }

    public void registerResourceHandler(IResourceUriHandler handler) {
        resourceUriHandlerSet.add(handler);
    }

    public void startAsync() {
        isRun = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                doProcSync();
            }
        }).start();
    }

    private void doProcSync() {
        try {
            InetSocketAddress socketAddress = new InetSocketAddress(webConfig.getPort());
            socket = new ServerSocket();
            socket.bind(socketAddress);
            while (isRun) {
                final Socket remotePeer = socket.accept();
                threadPool.submit(new Runnable() {
                    @Override
                    public void run() {
                        onAcceptRemotePeer(remotePeer);
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onAcceptRemotePeer(Socket remotePeer) {
        try {
            HttpContext httpContext = new HttpContext();
            httpContext.setUnderlySocket(remotePeer);
//            remotePeer.getOutputStream().write("connect success".getBytes());
            InputStream inputStream = remotePeer.getInputStream();
            String readLine = null;
            String resourceUri = StreamToolKit.readLine(inputStream).split(" ")[1];
            Log.d(TAG, resourceUri);
            while ((readLine = StreamToolKit.readLine(inputStream)) != null) {
                if (readLine.equals("\r\n")) {
                    break;
                }
                String[] split = readLine.split(": ");
                if (split.length > 1) {
                    httpContext.addResquestHeader(split[0], split[1]);
                }
                if (!readLine.equals("  "))
                    Log.d(TAG, readLine);
            }
                //遍历回调操作
            for (IResourceUriHandler handler : resourceUriHandlerSet) {
                if (!handler.accept(resourceUri))
                    continue;
                handler.handle(resourceUri, httpContext);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopAsync() throws IOException {
        if (!isRun)
            return;
        isRun = false;
        socket.close();
        socket = null;
    }


}
