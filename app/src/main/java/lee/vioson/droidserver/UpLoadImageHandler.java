package lee.vioson.droidserver;

import android.content.Context;
import android.util.Log;

import java.io.FileOutputStream;
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
public class UpLoadImageHandler implements IResourceUriHandler {
    private String acceptPrefix = "/upLoadImage/";

    private Context context;

    public UpLoadImageHandler(Context context) {
        this.context = context;
    }

    @Override
    public boolean accept(String uri) {
        return uri.startsWith(acceptPrefix);
    }

    @Override
    public void handle(String uri, HttpContext httpContext) throws IOException {
        String imagePath = context.getExternalFilesDir(null).getAbsolutePath() + "temp.jpg";
        FileOutputStream fos = new FileOutputStream(imagePath);

        InputStream inputStream = httpContext.getUnderlySocket().getInputStream();
        byte[] buffer = new byte[10240];
        int nReaded;
        String resquestHeader = httpContext.getResquestHeader("Content-Length").trim();
        long totalLength = Long.parseLong(resquestHeader);
        long leftLength = totalLength;

        while (leftLength > 0 && (nReaded = inputStream.read(buffer)) > 0) {
            fos.write(buffer, 0, nReaded);
            leftLength -= nReaded;
            Log.i("UpLoadImageHandler", "nReaded:" + nReaded + "\nleftLength:" + leftLength
                    + "\ntotal:" + totalLength);
        }

        fos.close();

        OutputStream outputStream = httpContext.getUnderlySocket().getOutputStream();
        PrintStream printer = new PrintStream(outputStream);
        printer.println("HTTP/1.1 200 OK");
        printer.println();
        printer.close();//必须调用close 不然客户端接收不到完成信号
        onImageUpLoaded(imagePath);
    }


    protected void onImageUpLoaded(String path) {

    }
}
