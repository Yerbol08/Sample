package com.example.android.camera2basic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.os.Bundle;
import android.util.Log;
import com.example.android.camera2basic.NetWorking.GetPhotoAlign;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Image21Activity extends FragmentActivity {
    static final String TAG = "myLogs";
    public static  int[] ints;
    public String[] res;
    //  final int PAGE_COUNT =5;
    ViewPager pager;
    PagerAdapter pagerAdapter;
    public static Bitmap[] bm =new Bitmap[15];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image21);

        String[] str=getIntent().getStringArrayExtra("Len1");
        int len = str.length;
        ints = new int[len];
        res = new String[len];
        for (int i =0 ; i<len; i++){
            res[i]= str[i];
        }
        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        MyTask myTask=new MyTask();
        myTask.execute();
        pager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected, position = " + position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            String[] str=getIntent().getStringArrayExtra("Len1");
            int len = str.length;
            if (bm[position]==null){Log.d("Len","Bitmap Nuull getItem #"+position);}
            return PageFragment.newInstance(position,bm[position], len);
        }

        @Override
        public int getCount() {
            String[] str=getIntent().getStringArrayExtra("Len1");
            return str.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return "Страница " +(position+1);
        }
    }

    public class MyTask extends AsyncTask<Integer, Void, Bitmap[]>
    {
        String[] str=getIntent().getStringArrayExtra("Len1");
        int len=str.length;

        Bitmap [] bitmaps =new Bitmap[len];
        @Override
        protected Bitmap[] doInBackground(Integer... integers) {
            for (int i =0;i<len;i++){
                String BASE_URL= Camera2BasicFragment.URL;
                Retrofit.Builder builder= new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());
                Retrofit retrofit=builder.build();
                GetPhotoAlign getapi= retrofit.create(GetPhotoAlign.class);
                Map<String,String> map= new HashMap<>();
                map.put("data", res[i]);
                Call<ResponseBody> call = getapi.getData(map);
                try {
                    Response response=call.execute();
                    ResponseBody body = (ResponseBody) response.body();
                    try {
                        byte[] bytes = body.bytes();
                        bitmaps[i]= BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        if(len== 1){
                            JSONObject jsonObject = new JSONObject(String.valueOf(bytes));
                            JSONArray array = jsonObject.getJSONArray("error");
                            String respS = array.getString(0);

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d("Len","Error execute");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return bitmaps;
        }
        @Override
        protected void onPostExecute(Bitmap[] bitmaps) {
            for (int i=0;i<len;i++){
                bm[i]=bitmaps[i];
            }
            pager.setAdapter(pagerAdapter);
        }
    }
}
