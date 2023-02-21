package com.example.dogimagepredection;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.dogimagepredection.ml.ModelUnquant;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MainActivity extends AppCompatActivity {
    Button select,capture,map;
    TextView result;
    ImageView image;
    Bitmap bitmap;

    int imageSize= 224;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getPermission();

        select = findViewById(R.id.select);
        capture = findViewById(R.id.capture);
        result = findViewById(R.id.result);
        image = findViewById(R.id.image);
        map = findViewById(R.id.map1);


       map.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(getApplicationContext(),MapsActivity2.class);
               startActivity(intent);
           }
       });


        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,10);

            }
        });

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,12);

            }
        });

    }


    void getPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA},11);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==11){
            if (grantResults.length>0){
                if (grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                    this.getPermission();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==10){
            if (data!=null){
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                    image.setImageBitmap(bitmap);

                    bitmap = Bitmap.createScaledBitmap(bitmap,imageSize,imageSize,false);
                    calssifyImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if (requestCode==12){
            bitmap = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(bitmap);
            bitmap = Bitmap.createScaledBitmap(bitmap,imageSize,imageSize,false);
            calssifyImage(bitmap);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void calssifyImage(Bitmap image){
        try {
            ModelUnquant model = ModelUnquant.newInstance(MainActivity.this);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int [] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues,0,image.getWidth(),0,0,image.getWidth(),image.getHeight());
            int pixle = 0;
            for(int i = 0;i< imageSize;i++){
                for (int j=0; j< imageSize;j++){
                    int val = intValues[pixle++];//RGB
                    byteBuffer.putFloat(((val >> 16)& 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8)& 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            ModelUnquant.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float [] probability = outputFeature0.getFloatArray();
            int max =0;
            float maxprob = 0;
            for (int i =0; i<probability.length;i++){
                if(probability[i] > maxprob){
                    maxprob = probability[i];
                    max =i;
                }
            }

            String[] classes = {"Dog","cat"};
            if(classes[max]=="Dog") {
                result.setText(classes[max] +'\n'+classes[max] +" with probability" + maxprob +'\n'+"cat with probaility"+(1-maxprob));
            }
            else {
                result.setText(classes[max] +'\n'+classes[max] + " with probability" + maxprob +'\n'+"dog with probability"+(1-maxprob));
            }


            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }

    }


}