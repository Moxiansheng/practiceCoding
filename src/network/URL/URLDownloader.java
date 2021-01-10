package network.URL;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class URLDownloader {
    String urlStr = "";
    URL url = null;
    HttpURLConnection urlConnection = null;
    InputStream is = null;
    FileOutputStream fos = null;
    int defaultSize = 1024;
    byte[] buffer = null;

    public URLDownloader(String url) throws Exception {
        urlStr = url;
        this.url = new URL(url);
    }

    public void download() throws Exception{
        download(common.common.analyzeURL(urlStr));
    }

    public void download(String outputPath) throws Exception{
        urlConnection = (HttpURLConnection) url.openConnection();

        is = urlConnection.getInputStream();

        fos = new FileOutputStream(outputPath);

        byte[] buffer = new byte[defaultSize];
        int len;
        while((len = is.read(buffer)) != -1){
            fos.write(buffer, 0, len);
        }

        fos.close();
        is.close();
        urlConnection.disconnect();
    }
}
