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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.camera2basic.NetWorking.GetPhotoImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Image3Activity extends FragmentActivity {

    static final String TAG = "myLogs";
    static final int PAGE_COUNT = 5;
    TextView textView;
    ViewPager pager1;
    PagerAdapter pagerAdapter0;
    public static Bitmap[] bm0 =new Bitmap[5];
    private String[] name= new String[5];
    private String[] iin=new String[5];
    private String[] probability =new String[5];
    private String[] ud_numb =new String[5];
    private String[] id =new String[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image3);

        String str=getIntent().getStringExtra("Len1");

        Log.d("Len",str);
        try {
            JSONObject obj = new JSONObject(str);
            Log.d("Len3",obj.toString());
            Iterator<String> keys = obj.keys();
            int j =0;
            while (keys.hasNext())
            {
                String key =keys.next();
                Log.d("Len3",key);
                JSONArray array = obj.getJSONArray(key);
                iin[j]=key;
                name[j]=array.getString(3);
                ud_numb[j]=array.getString(2);
                probability[j]=array.getString(0);
                id[j]=array.getString(1);
                j++;

            }
            for(int i =0;i<5;i++)
            {
                Log.d("Len3", (iin[i]+" "+name[i]+" "+ probability[i]));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        pager1 = (ViewPager) findViewById(R.id.pager0);
        pagerAdapter0 = new MyFragmentPagerAdapter1(getSupportFragmentManager());

        // MyTask myTask=new MyTask();
        // myTask.execute();
        MyAsync myAsync =new MyAsync();
        myAsync.execute();
        pager1.addOnPageChangeListener(new OnPageChangeListener() {

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
    private class MyFragmentPagerAdapter1 extends FragmentPagerAdapter {


        public MyFragmentPagerAdapter1(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return PageFragment2.newInstance1(position,name[position],iin[position],probability[position],bm0[position],ud_numb[position],id[position]);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return "Страница " + (position+1);
        }
    }

    public class MyAsync extends AsyncTask<Integer, Void, Bitmap[]>
    {
        String str=getIntent().getStringExtra("Len1");
        String[] keyy=new String[5];
        int index=0;
        JSONObject object;
        {
            try {
                object = new JSONObject(str);
                Iterator<String> keys =object.keys();
                while (keys.hasNext())
                {
                    String key=keys.next();
                    keyy[index]=key;
                    index++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        int len=5;
        Bitmap [] bitmaps =new Bitmap[len];
        protected Bitmap[] doInBackground(Integer... integers) {
            for (int i =0;i<len;i++){
                if (Camera2BasicFragment.URL.equals("http://10.1.2.2:9000")){
                    Camera2BasicFragment.URL="http://10.1.2.3:9001";
//                    Toast.makeText(getApplicationContext(), Camera2BasicFragment.URL,Toast.LENGTH_LONG).show();
                }
                String BASE_URL= Camera2BasicFragment.URL;

                Retrofit.Builder builder= new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());
                Retrofit retrofit=builder.build();
                GetPhotoImage getapi= retrofit.create(GetPhotoImage.class);
                Map<String,String> map= new HashMap<>();
                map.put("data", keyy[i]);
                Call<ResponseBody> call = getapi.getData(map);

                try {
                    Response response=call.execute();
                    ResponseBody body = (ResponseBody) response.body();
                    try {
                        byte[] bytes = body.bytes();
                        bitmaps[i]= BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        if (bitmaps[i]==null){Log.d("Len","Bitmap Nuull");}
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d("Len","Error execute");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return bitmaps;
        }
        protected void onPostExecute(Bitmap[] bitmaps) {
            for (int i=0;i<len;i++){
                bm0[i]=bitmaps[i];
            }
            pager1.setAdapter(pagerAdapter0);
            if (Camera2BasicFragment.URL.equals("http://10.1.2.3:9001")){
                Camera2BasicFragment.URL="http://10.1.2.3:9000";
            }
        }

    }
}
