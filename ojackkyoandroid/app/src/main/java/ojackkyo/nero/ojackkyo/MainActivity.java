package ojackkyo.nero.ojackkyo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import ojackkyo.nero.ojackkyo.fragment.ScrollViewFragment;

import com.github.florent37.hollyviewpager.HollyViewPager;
import com.github.florent37.hollyviewpager.HollyViewPagerConfigurator;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // 게시판 갯수
    int pageCount = 4;

    Toolbar toolbar;
    HollyViewPager hollyViewPager;
    FloatingActionButton fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        hollyViewPager = (HollyViewPager) findViewById(R.id.hollyViewPager);
        fb = (FloatingActionButton)findViewById(R.id.edit_text);
        fb.setOnClickListener(this);

        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
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
                return ScrollViewFragment.newInstance((String) getPageTitle(position));
            }

            @Override
            public int getCount() {
                return pageCount;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return "" + position + "게시판";
            }
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
                // User chose the "Settings" item, show the app settings UI...
                Toast.makeText(getApplicationContext(), "검색버튼 버튼 클릭됨", Toast.LENGTH_LONG).show();
                break;

            case R.id.user_info:
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer) ;
                if (!drawer.isDrawerOpen(Gravity.END)) {
                    drawer.openDrawer(Gravity.END);
                }else {
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
            super.onBackPressed();
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.edit_text:
                Intent intent = new Intent(this, EditTextActivity.class);
                startActivity(intent);
        }
    }
}