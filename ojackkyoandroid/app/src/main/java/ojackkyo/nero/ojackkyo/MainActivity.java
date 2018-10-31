package ojackkyo.nero.ojackkyo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ojackkyo.nero.ojackkyo.fragment.ScrollViewFragment;

import com.github.florent37.hollyviewpager.HollyViewPager;
import com.github.florent37.hollyviewpager.HollyViewPagerConfigurator;

import java.util.List;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    // 게시판 갯수 --> 총 태그 갯수
    int pageCount;

    Toolbar toolbar;
    HollyViewPager hollyViewPager;
    FloatingActionButton fb;
    DrawerLayout drawer;
    TextView user_name;

    UserInfo userInfo;
    List<String> tag;
    long pressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        userInfo = (UserInfo) getApplicationContext();

        drawer =  findViewById(R.id.drawer);

        View nav_view = navigationView.getHeaderView(0);
        user_name = nav_view.findViewById(R.id.user_name);
        user_name.setText(userInfo.getNickname().replaceAll("\"",""));

        tag = userInfo.setTag(); //userinfo에서 tag 배열 호출
        pageCount = tag.size();

        Log.e("pagecount : ", "   " + pageCount);

        toolbar = findViewById(R.id.toolbar);
        hollyViewPager = findViewById(R.id.hollyViewPager);

        fb = findViewById(R.id.edit_text);
        fb.setOnClickListener(this);

        toolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        hollyViewPager.getViewPager().setPageMargin(getResources().getDimensionPixelOffset(R.dimen.viewpager_margin));
        hollyViewPager.setConfigurator(new HollyViewPagerConfigurator() {
            @Override
            public float getHeightPercentForPage(int page) {
                return 0;
            }
        });

        hollyViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
//                if(position%2==0)
//                    return new RecyclerViewFragment();
//                else
                Log.e("스크롤뷰는???", "getItem: " + position);
                return ScrollViewFragment.newInstance((String) getPageTitle(position));
            }

            @Override
            public int getCount() {
                return pageCount;
            }

            @Override
            public CharSequence getPageTitle(int position) {

                return "" + tag.get(position) + "게시판";
            } //태그 이름
        });
    }

    //추가된 소스, ToolBar에 menu.xml을 인플레이트함
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            //return super.onCreateOptionsMenu(menu);
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menu, menu);
            return true;
    }

    //추가된 소스, ToolBar에 추가된 항목의 select 이벤트를 처리하는 함수
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                //현재 로그아웃 기능!
                break;

            case R.id.user_info:
                if (!drawer.isDrawerOpen(Gravity.END)) {
                    drawer.openDrawer(Gravity.END);
                } else {
                    onBackPressed();
                }
                Toast.makeText(getApplicationContext(), "사용자정보 버튼 클릭됨", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        if (drawer.isDrawerOpen(Gravity.END)) {
            drawer.closeDrawer(Gravity.END);
        } else {
            if (pressedTime == 0) {
                Toast.makeText(MainActivity.this, " 한 번 더 누르면 종료됩니다.", Toast.LENGTH_LONG).show();
                pressedTime = System.currentTimeMillis();
            } else {
                int seconds = (int) (System.currentTimeMillis() - pressedTime);

                if (seconds > 2000) {
                    Toast.makeText(MainActivity.this, " 한 번 더 누르면 종료됩니다.", Toast.LENGTH_LONG).show();
                    pressedTime = 0;
                } else {
                    super.onBackPressed();
                    finish(); // app 종료 시키기
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.edit_text:
                Intent intent = new Intent(this, EditTextActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
                break;
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.message) {
            Toast.makeText(this,"테스트1",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_gallery) {
            Toast.makeText(this,"테스트2",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_slideshow) {
            Toast.makeText(this,"테스트3",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_manage) {
            Toast.makeText(getApplicationContext(), "로그아웃 버튼 클릭됨", Toast.LENGTH_LONG).show();
            userInfo.reset();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            MainActivity.this.finish();
        }
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }
}