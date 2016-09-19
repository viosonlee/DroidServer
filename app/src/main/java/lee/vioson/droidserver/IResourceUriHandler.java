package lee.vioson.droidserver;

import java.io.IOException;

/**
 * Author:李烽
 * Date:2016-07-29
 * FIXME
 * Todo
 */
public interface IResourceUriHandler {
    boolean accept(String uri);

    void handle(String uri, HttpContext httpContext) throws IOException;
}
