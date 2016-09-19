package lee.vioson.droidserver;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Author:李烽
 * Date:2016-07-29
 * FIXME
 * Todo
 */
public class ResourceAssetsHandler implements IResourceUriHandler {
    private String acceptPrefix = "/static/";

    private Context context;

    public ResourceAssetsHandler(Context context) {
        this.context = context;
    }

    @Override
    public boolean accept(String uri) {
        return uri.startsWith(acceptPrefix);
    }

    @Override
    public void handle(String uri, HttpContext httpContext) throws IOException {
        int startIndex = acceptPrefix.length();

        String assetPath = uri.substring(startIndex);

        InputStream inputStream = context.getAssets().open(assetPath);

        byte[] raw = StreamToolKit.readRowFromStream(inputStream);

        OutputStream outputStream = httpContext.getUnderlySocket().getOutputStream();
        PrintStream printer = new PrintStream(outputStream);
        printer.println("HTTP/1.1 200 OK");
        printer.println("Content-Length:" + raw.length);
        if (assetPath.endsWith(".html")) {
            printer.println("Content-Type:text/html");
        } else if (assetPath.endsWith(".js")) {
            printer.println("Content-Type:text/js");
        } else if (assetPath.endsWith(".css")) {
            printer.println("Content-Type:text/css");
        } else if (assetPath.endsWith(".jpg")) {
            printer.println("Content-Type:text/jpg");
        } else if (assetPath.endsWith(".png")) {
            printer.println("Content-Type:text/png");
        }
        printer.println();
        printer.write(raw);
        printer.close();
    }
}
