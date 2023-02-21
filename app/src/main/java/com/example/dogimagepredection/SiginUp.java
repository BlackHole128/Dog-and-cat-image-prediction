package com.example.dogimagepredection;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dogimagepredection.databinding.ActivitySiginUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.regex.Pattern;

public class SiginUp extends AppCompatActivity {
    ActivitySiginUpBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    int p=0,e=0,n=0;
    private DatePickerDialog picker;

    ProgressDialog progressDialog;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySiginUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        progressDialog= new ProgressDialog(this);


        binding.birthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                //Date picaker
                picker = new DatePickerDialog(SiginUp.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        binding.birthDay.setText(dayOfMonth+ "/"+ (month+1)+"/"+year);
                    }
                }, year,month,day);
                picker.show();
            }
        });

        binding.signup2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.name.getText().toString();
                String number = binding.mobilenumber.getText().toString();
                String email = binding.email.getText().toString().trim();
                String password = binding.password2.getText().toString();
                String BirthDay = binding.birthDay.getText().toString();
                if(e==0 && p==0 && n==0) {
                    progressDialog.show();
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Intent intent = new Intent(getApplicationContext(), sign_in.class);
                                    startActivity(intent);
                                    progressDialog.cancel();


                                    firebaseFirestore.collection("User")
                                            .document(FirebaseAuth.getInstance().getUid())
                                            .set(new UserModel(name, number, BirthDay, email));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SiginUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    progressDialog.cancel();
                                }
                            });
                }
            }
        });


    //email validation
    binding.email.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String emailInput = binding.email.getText().toString().trim();
            if (emailInput.isEmpty()){
                binding.email.setError("can not be empty");
                e=1;
            }
           else if(!emailInput.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
                  e=0;
            }

            else {
                binding.email.setError("not valid email");
                e=1;
            }
            if (Patterns.EMAIL_ADDRESS.matcher(binding.email.getText().toString().trim()).matches() ){
                firebaseAuth.fetchSignInMethodsForEmail(binding.email.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {


                                boolean check = !task.getResult().getSignInMethods().isEmpty();
                                if (!check){
                                    binding.usedEmail.setText(null);
                                    e=0;
                                }
                                else {
                                    binding.email.setError("Email alrady present");
                                    binding.usedEmail.setText("Enter a diffrent email");
                                    e=1;
                                }

                            }
                        });
            }

        }
    });




        final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])" +  //must cantain a number
             //   "(?=.*[a-z])" +                                            //must contain one lowar case
             //   "(?=.*[A-Z])" +                                            //must contain one upper case
                "(?=.*[@#$%^&+=])" +                                       //must have one special cahr
                "(?=\\S+$)" +                                              // no white space
                ".{6,}" +                                                  //at least 6 length
                "$");
    //password validation
    binding.password2.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
                   String pass = binding.password2.getText().toString().trim();
                   if(!PASSWORD_PATTERN.matcher(pass).matches() && (pass!=null)){
                       binding.password2.setError("must have one special cahr, a number, at least of length 6");
                       p=1;
                   }
                   else {
                       p=0;
                   }

        }
    });


        binding.name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String sn = binding.name.getText().toString();
                firebaseFirestore.collection("User")
                        .whereEqualTo("name", sn)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                int a = queryDocumentSnapshots.size();
                                if(a >= 1){
                                    binding.name.setError("username already taken");
                                    n=1;
                                }
                                else {
                                    binding.name.setError(null);
                                    n=0;
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });


    }
}