package com.example.android.camera2basic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Network;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.camera2basic.NetWorking.ApiMetaData;
import com.example.android.camera2basic.NetWorking.AppMetaData;

import java.io.IOException;
import java.util.Random;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PageFragment extends Fragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    public int pageNumber;
    int backColor, Lenght, leng;
    Bitmap bm1;
    Button send_img;
    ProgressDialog dialog;

   // public  int[] ints = new int[15];
    static PageFragment newInstance(int page, Bitmap bitmap, int len) {
        PageFragment pageFragment = new PageFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        arguments.putParcelable("Bitmap",bitmap);
        arguments.putInt("Len", len);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
        Random rnd = new Random();
        backColor = Color.argb(40, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        bm1=getArguments().getParcelable("Bitmap");
        leng= getArguments().getInt("Len");
        dialog= new ProgressDialog(getContext(),R.style.ProgressDialogStyl);
        dialog.setTitle("Загрузка");
        dialog.setMessage("Идет отправка запроса, пожалуйста подождите...");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, null);
        TextView tvPage = (TextView) view.findViewById(R.id.tvPage);
        ImageView imV = (ImageView) view.findViewById(R.id.imageView);
        Lenght = 0;
        tvPage.setText("Лицо №" + (pageNumber+1));
     //   tvPage.setBackgroundColor(backColor);
        imV.setImageBitmap(bm1);
        send_img=(Button)view.findViewById(R.id.send_btn);
        send_img.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                try {
                    Upload(pageNumber);

                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }) ;
        return view;
    }

    public  void Upload(final int i) throws IOException {

        dialog.show();
       // Toast.makeText(getContext(),"URl:" +Camera2BasicFragment.URL,Toast.LENGTH_SHORT).show();
        Retrofit retrofit= AppMetaData.getRetrofitClient(this);
        ApiMetaData uploadApi =retrofit.create(ApiMetaData.class);
        final String nom=String.valueOf(i);
        MultipartBody.Part part = MultipartBody.Part.createFormData("data", nom);
        Call call = uploadApi.uploadImage(part);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        dialog.dismiss();
                        ResponseBody body = (ResponseBody) response.body();
                        try {
                            String repString = body.string();
                            //Log.d("String",repString);
                            if (repString.equals("{}" )|| repString.equals("") || repString.equals(" ") || repString.equals(null))
                            {
                                Toast.makeText(getContext(), "Не удалось распознать лицо, используйте другое лицо.", Toast.LENGTH_SHORT).show();
                                int size =Image21Activity.ints.length, count=0;
                                Log.d("String", "size :"+String.valueOf (size));
//                                Log.d("String", "int 1 :" +Image21Activity.ints[0]);
//                                Log.d("String", "int 2 :" +Image21Activity.ints[1]);
                                if (size ==1)
                                {
                                    Intent intent = new Intent(getContext(), CameraActivity.class);
                                    startActivity(intent);

                                }
                                else {

                                    Image21Activity.ints[i]=1;
                                    for (int j = 0; j<size; j++ )
                                    {
                                        count += Image21Activity.ints[j];
                                    }
                                    Log.d("String", String.valueOf(count));
                                    if (count == size)
                                    {
                                        Toast.makeText(getContext(), "Не удалось распознать все лица, используйте другое изображение.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getContext(), CameraActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            }
                            else {
                                //Toast.makeText(getContext(), repString,Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getView().getContext(),Image3Activity.class);
                                intent.putExtra("Len1",repString);
                                startActivity(intent);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();

                        }
                    }
                }
                else {
                    dialog.dismiss();
                    Toast.makeText(getContext(), "Не удалось найти распознать лицо, используйте другое лицо", Toast.LENGTH_SHORT).show();
                    Log.d("Len","Error: "+response.toString());
                }
            }
            @Override
            public void onFailure(Call call, Throwable t) {
                dialog.dismiss();
                Log.v("Response gotten is", t.getMessage());
                Toast.makeText(getContext(), "Проблемы с сетью. Проверьте соединение.",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
