package com.example.android.camera2basic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.android.camera2basic.NetWorking.ApiConfig;
import com.example.android.camera2basic.NetWorking.AppConfig;
import com.example.android.camera2basic.NetWorking.AppConfig2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Timer;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Image2Activity extends AppCompatActivity {


    private static final String TAG = "Image2";
    ImageView imageView;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image2);


        dialog= new ProgressDialog(Image2Activity.this,R.style.ProgressDialogStyl);
        dialog.setTitle("Загрузка");
        dialog.setMessage("Идет подготовка файла, пожалуйста подождите...");
        imageView= (ImageView)findViewById(R.id.imageView);
        String path = getIntent().getStringExtra("Path");
        File  file = new File(path);
        if (file.exists())
        {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            imageView.setImageBitmap(bitmap);
            uploadImage(file);
            dialog.show();
        }
        else{
            File  file1 = new File(path);
            Bitmap bitmap = BitmapFactory.decodeFile(file1.getAbsolutePath());
            imageView.setImageBitmap(bitmap);
            uploadImage(file);
        }

    }

    public void  uploadImage(final File file)
    {
        final boolean[] twice = {false};

      //  Camera2BasicFragment.URL="http://10.1.2.3:9000";
        dialog.setMessage("Идет отправка файла, пожалуйста подождите...");
        dialog.show();
        Retrofit retrofit = AppConfig.getRetrofitClient(this);
        ApiConfig apiConfig = retrofit.create(ApiConfig.class);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part part=MultipartBody.Part.createFormData("file",file.getName(),requestBody);
        Call call = apiConfig.uploadImage(part);
        Log.d(TAG, "Str");
        final int[] img = {0};
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!twice[0]){
                  //  Toast.makeText(getApplicationContext(), "Время ожидания истекло.", Toast.LENGTH_LONG).show();
           //         uploadImage2(file);
                    img[0] =1;
                }
            }
        },5000);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                twice[0] = true;
                if (response.isSuccessful()) {
                    dialog.dismiss();
                    ResponseBody body = (ResponseBody) response.body();
                    Log.d(TAG, String.valueOf(response.code())+" response:"+response);

                    try {

                        JSONObject object = new JSONObject(body.string());
                        JSONArray array = object.getJSONArray("res");
                        Log.d(TAG, object.toString());
                        String[] res= new String[array.length()];
                        for (int i =0; i<array.length();i++){
                            res[i]= array.getString(i);
                            Log.d("res "+i,res[i]);
                        }
                        if (object.toString()=="" || object.equals("") || object.toString().equals("none") || object.equals(null)){
                      //      uploadImage2(file);
                         //   Toast.makeText(getApplicationContext(), "На отправлен 2-ой сервер", Toast.LENGTH_LONG).show();
                        }
                        if (array.length() >= 1 && img[0]!=1) {
                            Intent intent = new Intent(Image2Activity.this, Image21Activity.class);
                            intent.putExtra("Len1", res);
                            startActivity(intent);
                        } else if (array.length() == 0 ) {
                            Toast.makeText(getApplicationContext(), "Лицо не обнаружено, пожалуйста сфотографируйте ещё раз.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Image2Activity.this, CameraActivity.class);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                     //   twice[0] = true;
                        dialog.dismiss();
                        if (img[0] !=1){
                          //  Toast.makeText(getApplicationContext(), "Проблема в отправке изображения. Сервер 1-ый. Код ошибки: "+response.code(),Toast.LENGTH_SHORT).show();
                            Log.e(TAG, response.toString());
                           // uploadImage2(file);
                        }
                }
            }
            @Override
            public void onFailure(Call call, Throwable t) {
                dialog.dismiss();
                twice[0] = true;
                if(img[0] !=1){
                    Toast.makeText(getApplicationContext(), "Проблемы с сетью. Проверьте соединение. Отправляем на второй сервер.", Toast.LENGTH_SHORT).show();
                    //uploadImage2(file);
                }
            }
        });

    }

    public void uploadImage2(File file)
    {
        Camera2BasicFragment.URL="http://10.1.2.2:9000";
        Log.d("URL",Camera2BasicFragment.URL);
        Toast.makeText(getApplicationContext(), Camera2BasicFragment.URL,Toast.LENGTH_LONG).show();
        dialog.setMessage("Идет отправка файла, пожалуйста подождите...");
        dialog.show();
        Retrofit retrofit = AppConfig2.getRetrofitClient(this);
        ApiConfig apiConfig = retrofit.create(ApiConfig.class);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part part=MultipartBody.Part.createFormData("file",file.getName(),requestBody);
        Call call = apiConfig.uploadImage(part);
        Log.d(TAG, "Str");
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                if (response.isSuccessful()) {
                    dialog.dismiss();
                    ResponseBody body = (ResponseBody) response.body();
                    Log.d(TAG, String.valueOf(response.code()));

                    try {

                        JSONObject object = new JSONObject(body.string());
                        JSONArray array = object.getJSONArray("res");
                        Log.d(TAG, object.toString());

                        String[] res= new String[array.length()];
                        for (int i =0; i<array.length();i++){
                            res[i]= array.getString(i);
                            Log.d("res "+i,res[i]);
                        }
                        if (array.length() >= 1) {
                            Intent intent = new Intent(Image2Activity.this, Image21Activity.class);
                            intent.putExtra("Len1", res);
                            startActivity(intent);
                        } else if (array.length() == 0) {
                            Toast.makeText(getApplicationContext(), "Лицо не обнаружено, пожалуйста сфотографируйте ещё раз.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Image2Activity.this, CameraActivity.class);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Проблема в отправке изображения. Сервер 2-ой. Код ошибки: "+response.code(),Toast.LENGTH_SHORT).show();
                    Log.e(TAG, response.toString());
                }
            }
            @Override
            public void onFailure(Call call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Проблемы с сетью. Оба сервера не работает.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
