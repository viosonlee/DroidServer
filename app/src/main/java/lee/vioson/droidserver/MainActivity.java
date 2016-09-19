package lee.vioson.droidserver;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private SimpleHttpServer simpleHttpServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ImageView imageView = (ImageView) findViewById(R.id.image);
        WebConfig webConfig = new WebConfig();
        webConfig.setPort(8088);
        webConfig.setMaxParallels(50);
        simpleHttpServer = new SimpleHttpServer(webConfig);
        simpleHttpServer.registerResourceHandler(new ResourceAssetsHandler(this));
        simpleHttpServer.registerResourceHandler(new UpLoadImageHandler(this) {
            @Override
            protected void onImageUpLoaded(String path) {
                super.onImageUpLoaded(path);
                final Bitmap bitmap = BitmapFactory.decodeFile(path);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                    }
                });
            }
        });
        simpleHttpServer.startAsync();
    }

    @Override
    protected void onDestroy() {
        try {
            simpleHttpServer.stopAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
