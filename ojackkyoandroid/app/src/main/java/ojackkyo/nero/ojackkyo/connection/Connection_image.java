package ojackkyo.nero.ojackkyo.connection;

import android.graphics.Bitmap;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class Connection_image {

    String serverURL = "http://117.17.102.131:4000/article/image";

    // 이미지

    // 기타 필요한 내용
    String attachmentName = "bitmap";
    String attachmentFileName = "bitmap.bmp";
    String crlf = "\r\n";
    String twoHyphens = "--";
    String boundary =  "*****";

    public String POST_Data(String filepath, String token, int articleId) throws Exception {
        Log.e("들어는", "왔니");
        URL url = new URL(serverURL);
        String boundary =  "*****"+Long.toString(System.currentTimeMillis())+"*****";

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setReadTimeout(5000);
        httpURLConnection.setConnectTimeout(5000);
        httpURLConnection.setRequestProperty("token", token);
        httpURLConnection.setRequestProperty("articleId", String.valueOf(articleId));
        httpURLConnection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
        httpURLConnection.setRequestProperty("content-Type", "multipart/form-data; boundary="+boundary);
        httpURLConnection.setRequestMethod("POST");

        InputStream inputStream = null;
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        String[] q = filepath.split("/");
        int idx = q.length - 1;
        File file = new File(filepath);
        FileInputStream fileInputStream = new FileInputStream(file);
        DataOutputStream outputStream = (DataOutputStream) httpURLConnection.getOutputStream();
        outputStream.writeBytes("--" + boundary + "\r\n");
        outputStream.writeBytes("Content-Disposition: form-data; name=\"" + "img_upload" + "\"; filename=\"" + q[idx] +"\"" + "\r\n");
        outputStream.writeBytes("Content-Type: image/*" + "\r\n");
        outputStream.writeBytes("Content-Transfer-Encoding: binary" + "\r\n");
        outputStream.writeBytes("\r\n");
        bytesAvailable = fileInputStream.available();
        bufferSize = Math.min(bytesAvailable, 1048576);
        buffer = new byte[bufferSize];
        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        while(bytesRead > 0) {
            outputStream.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, 1048576);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }
        outputStream.writeBytes("\r\n");
        outputStream.writeBytes("--" + boundary + "--" + "\r\n");
        inputStream = httpURLConnection.getInputStream();
        int status = httpURLConnection.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            inputStream.close();
            httpURLConnection.disconnect();
            fileInputStream.close();
            outputStream.flush();
            outputStream.close();
            Log.e("접속", "함");
            return response.toString();
        } else {
            Log.e("접속else", "못함");
            throw new Exception("Non ok response returned");
        }
    }
}
