package ojackkyo.nero.ojackkyo;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.*;
import java.net.HttpURLConnection;
import java.nio.file.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import ojackkyo.nero.ojackkyo.connection.*;

public class EditTextActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edit_title, edit_text;
    private Button write_btn;
//    private ImageView upload_btn;
    private final int GALLERY_CODE = 1112;

    UserInfo userInfo;
    String imagePath;
    String imageName;
    int articleId;
    Bitmap bm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);
        userInfo = (UserInfo) getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_text_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        edit_title = (EditText) findViewById(R.id.edit_title);
        edit_text = (EditText) findViewById(R.id.edit_text);
        write_btn = (Button) findViewById(R.id.write_btn);
//        upload_btn = (ImageView) findViewById(R.id.image_upload);
        edit_text.setHorizontallyScrolling(false);

        write_btn.setOnClickListener(this);
//        upload_btn.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditTextActivity.this, MainActivity.class);
        startActivity(intent);
        EditTextActivity.this.finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.write_btn:
                final String title = edit_title.getText().toString();
                final String text = edit_text.getText().toString();

                JsonObject resultObject = null;
                Connection connection = new Connection();
                Connection_image connection_image = new Connection_image();
                JsonObject jsonObject = new JsonObject();
                JsonObject jsonObject1 = new JsonObject();

                jsonObject.addProperty("title", title);
                jsonObject.addProperty("text", text);
                JsonArray jsonArray = new JsonArray();

                ArrayList<String> tags_list = new ArrayList();
                tags_list.add("통합");

                Pattern MY_PATTERN = Pattern.compile("#(\\S+)");
                Matcher mat = MY_PATTERN.matcher(text);
                while (mat.find()) {
                    tags_list.add(mat.group(1));
                }

                HashSet<String> datalist = new HashSet(tags_list);
                tags_list = new ArrayList(datalist);

                Log.e("태그 테스트", String.valueOf(tags_list));

                for (int i = 0; i < tags_list.size(); i++) {
                    // 게시글 태그 추가하기
                    JsonObject tag = new JsonObject();
                    tag.addProperty("name", tags_list.get(i));
                    jsonArray.add(tag);

                    Log.e("태그 테스트 - 최종", tags_list.get(i));
                }
                jsonObject.add("tags", jsonArray);
                String result = "";

                try {
                    result = (String) connection.execute(jsonObject, "article", "POST", userInfo.getToken()).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if (result.equals("400")) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(EditTextActivity.this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.setMessage("제목 혹은 내용이 비어있습니다.");
                    alert.show();
                } else {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://117.17.102.131:4000/article/image");

                    if (imagePath != null) {
                        Gson gson = new Gson();     // 글을 올리고 나서 아이디를 받아가지고 이미지를 따로 올려야되서 여기다가 이미지 경로 유무에 따라서 업로드 하는 커넥션 수행하게 했음.
                        JsonElement jsonElement = gson.fromJson(result, JsonElement.class);
                        jsonObject1 = jsonElement.getAsJsonObject();

                        JsonObject imageObject = new JsonObject();

                        //여기서 MultiPartFile 처리 해줘야 함
                        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                        File file = new File(imagePath);
                        Log.d("EDIT USER PROFILE", "UPLOAD: file length = " + file.length());
                        Log.d("EDIT USER PROFILE", "UPLOAD: file exist = " + file.exists());
                        builder.addPart("image", new FileBody(file, "application/octet"));
                        HttpEntity entity = builder.build();
                        httppost.setEntity(entity);
                        try {
                            HttpResponse response = httpclient.execute(httppost);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        articleId = Integer.parseInt(String.valueOf(jsonObject1.get("id")));  //아이디 받아오는거 그냥 갔다가 썼어 ㅡ.,ㅡ 정신이 없어
                        imageObject.addProperty("token", userInfo.getToken());
                        imageObject.addProperty("image", imagePath);
                        imageObject.addProperty("articleId", articleId);
                        Log.e("id테스트", String.valueOf(articleId));
                        Log.e("이미지테스트", imagePath);
                        Log.e("잘 들어갔니", String.valueOf(imageObject));

                        try {
                            String image_result = connection_image.POST_Data(imagePath, userInfo.getToken(), articleId);  // connection_image 클래스 새로 만들었고, 이미지 파일을 올리는게 경로올리는거 같아서 경로 받아서 넣었고 , 토큰이랑 아티클 아이디 같이 넣었음.
                            Log.e("제대로??", image_result);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.e("이미지", "포함하고 게시글 작성 ");
                    } else {
                        Log.e("이미지", "포함하지 않고 게시글 작성");
                    }
                    Intent intent = new Intent(EditTextActivity.this, MainActivity.class);
                    startActivity(intent);
                    EditTextActivity.this.finish();
                }
                break;
//            case R.id.image_upload:
//                selectGallery();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                Intent intent = new Intent(EditTextActivity.this, MainActivity.class);
                startActivity(intent);
                EditTextActivity.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GALLERY_CODE:

                    Uri selectedImage = data.getData();
                    imagePath = getImagePath(selectedImage); //갤러리에서 가져오기
                    String file_extn = imagePath.substring(imagePath.lastIndexOf(".") + 1);
                    imageName = file_extn;
                    Log.e("이미지 이름??", "이름 : " + imagePath + ", 확장자 : " + file_extn);
                    if (file_extn.equals("img") || file_extn.equals("jpg") || file_extn.equals("jpeg") || file_extn.equals("gif") || file_extn.equals("png")) {
                        //FINE
                    } else {
                        //NOT IN REQUIRED FORMAT
                    }
                    break;

                default:
                    break;
            }
        }
    }

    private String getImagePath(Uri imgUri) {
        String path = getRealPathFromURI(imgUri); // path 경로

        Log.d("이미지패스", "getImagePath: " + path + "이미지이름" + imageName);
        return path;
    }

    private String getRealPathFromURI(Uri contentUri) {
        int column_index = 0;
        String[] proj = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.TITLE,
                MediaStore.Images.Media.ORIENTATION
        };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }

        return cursor.getString(column_index);
    }


    private void selectGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_CODE);
    }
}
