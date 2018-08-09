package ojackkyo.nero.ojackkyo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        userInfo = (UserInfo) getApplicationContext();

        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(), 1500); // 3초 후에 hd handler 실행  3000ms = 3초
    }

    private class splashhandler implements Runnable {
        public void run() {
            if (userInfo.getToken() == null) {
                startActivity(new Intent(getApplication(), LoginActivity.class)); //로딩이 끝난 후, MainActivity 이동
                SplashActivity.this.finish(); // 로딩페이지 Activity stack에서 제거
            } else {
                startActivity(new Intent(getApplication(), MainActivity.class));
                SplashActivity.this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        //초반 플래시 화면에서 넘어갈때 뒤로가기 버튼 못누르게 함
    }
}