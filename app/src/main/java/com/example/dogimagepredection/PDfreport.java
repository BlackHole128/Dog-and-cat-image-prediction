package com.example.dogimagepredection;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.OutputStream;

public class PDfreport extends AppCompatActivity {
    EditText editText;
    Button button;
    PdfDocument document;
    String[] infoArr = new String[] {"Name", "Email", "Birth date", "Mobile", "Gender"};
    String[] userInfo = new String[5];
    FirebaseUser firebaseUser;
    String fullName, email, dob, gender, mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfreport);

        editText = findViewById(R.id.pdfE);
        button = findViewById(R.id.pdfB);

        ActivityCompat.requestPermissions(this, new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createReport();
            }
        });


    }


    public Task<String[]> getUserData() {
        // create a TaskCompletionSource object
        TaskCompletionSource<String[]> tcs = new TaskCompletionSource<>();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("User").document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        // do something with the user data
                        String  name1 = (String) documentSnapshot.get("name");
                        userInfo[0] = name1;

                        String email1 = (String) documentSnapshot.get("eamil");
                        userInfo[1] = email1;

                        String date1 = (String) documentSnapshot.get("birthDay");


                            userInfo[2] = date1;


                        String number1 = (String) documentSnapshot.get("number");
                        userInfo[3] = number1;

                        // set the result of the TaskCompletionSource
                        tcs.setResult(userInfo);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // set the exception of the TaskCompletionSource
                        tcs.setException(e);
                    }
                });

        // return the task created by the TaskCompletionSource
        return tcs.getTask();
    }










    private void createReport() {


        // call the method and get the task
        Task<String[]> task = getUserData();

        // wait for the task to complete
        task.addOnCompleteListener(new OnCompleteListener<String[]>() {
            @Override
            public void onComplete(@NonNull Task<String[]> task) {
                if (task.isSuccessful()) {

                    // get the data from the task result
                    String[] userInfo = task.getResult();

                    // use the data here
                    if (userInfo[2]==null){
                        userInfo[2]="";
                    }
                    //PDF generator start
                    PdfDocument pdfDocument = new PdfDocument();
                    Paint paint = new Paint();

                    PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(400, 600, 1).create();
                    PdfDocument.Page page1 = pdfDocument.startPage(pageInfo);
                    Canvas canvas = page1.getCanvas();
                    //  Toast.makeText(this, "A", Toast.LENGTH_SHORT).show();


                    //    Toast.makeText(this, "B", Toast.LENGTH_SHORT).show();
                    paint.setTextAlign(Paint.Align.CENTER);
                    paint.setTextSize(20f);
                    paint.setColor(Color.rgb(22, 30, 126));
                    canvas.drawText("Cat and Dog Detection", pageInfo.getPageWidth()/2 , 50, paint);

                    //     Toast.makeText(this, "J", Toast.LENGTH_SHORT).show();
                    paint.setTextAlign(Paint.Align.LEFT);
                    paint.setTextSize(15f);
                    paint.setColor(Color.rgb(22, 30, 126));
                    canvas.drawText("Personal Information", 20 , 80, paint);
                    //   Toast.makeText(this, "C", Toast.LENGTH_SHORT).show();
                    paint.setTextAlign(Paint.Align.LEFT);
                    paint.setTextSize(12f);
                    paint.setColor(Color.BLACK);

                    int startXPos = 20, startYPos = 100, endXPos = pageInfo.getPageWidth() - 20;
                    canvas.drawLine(startXPos, startYPos + 5, endXPos, startYPos + 5, paint);
                    startYPos += 15;
                    for(int i=0;i<4;i++){
                        startYPos += 10;
                        canvas.drawText(infoArr[i], startXPos + 5, startYPos, paint);
                        canvas.drawText(userInfo[i], 105, startYPos, paint);
                        startYPos += 10;
                        canvas.drawLine(startXPos, startYPos + 5,endXPos, startYPos + 5, paint);
                        startYPos += 15;
                    }

                    canvas.drawLine(20, 105, 20, 246, paint);
                    canvas.drawLine(100, 105, 100, 246, paint);
                    canvas.drawLine(pageInfo.getPageWidth() - 20, 105, pageInfo.getPageWidth() - 20, 246, paint);

                    pdfDocument.finishPage(page1);

                    ContentValues values = new ContentValues();
                    values.put(MediaStore.MediaColumns.DISPLAY_NAME, "PersonalInfo.pdf");
                    values.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
                    values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

                    Uri uri = getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);

                    try {
                        OutputStream outputStream = getContentResolver().openOutputStream(uri);
                        pdfDocument.writeTo(outputStream);
                        pdfDocument.close();
                        outputStream.close();
                        Toast.makeText(PDfreport.this, "PDF saved successfully", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(PDfreport.this, "Failed to save PDF", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // handle the error
                    Exception e = task.getException();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}