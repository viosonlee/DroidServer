package lee.vioson.droidserver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Author:李烽
 * Date:2016-07-29
 * FIXME
 * Todo 读出字节流
 */
public class StreamToolKit {
    public static final String readLine(InputStream nis) throws IOException {
        StringBuilder sb = new StringBuilder();
        int c1 = 0, c2 = 0;
        while (c2 != -1 && !(c1 == '\r' && c2 == '\n')) {
            c1 = c2;
            c2 = nis.read();
            sb.append((char) c2);
        }
        if (sb.length() == 0)
            return null;
        return sb.toString();
    }

    public static byte[] readRowFromStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        int readed;
        while ((readed = inputStream.read(buffer)) > 0) {
            byteArrayOutputStream.write(buffer, 0, readed);
        }

        return byteArrayOutputStream.toByteArray();
    }
}
