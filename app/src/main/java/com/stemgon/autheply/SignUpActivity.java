package com.stemgon.autheply;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stemgon.autheply.webApi.models.User;
import com.stemgon.autheply.webApi.services.UserAPIService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity {
    Button signUpNowButon;
    EditText usernameEd, emailEd, firstNameEd, lastNameEd, passwordEd;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpNowButon = findViewById(R.id.signUpNowButon);
        emailEd = findViewById(R.id.email);
        usernameEd = findViewById(R.id.username);
        firstNameEd = findViewById(R.id.firstName);
        lastNameEd = findViewById(R.id.lastName);
        passwordEd = findViewById(R.id.password);
        signUpNowButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEd.getText().toString();
                String firstName = firstNameEd.getText().toString();
                String lastName = lastNameEd.getText().toString();
                String password = passwordEd.getText().toString();
                String email = emailEd.getText().toString();
//                createUser(email=email, username=username,  firstName=firstName, lastName=lastName,
//                         password=password);
                createUser(username, email, firstName, lastName, password);
            }
        });



    }

    private void createUser(String username, String email,
                            String firstName, String lastName,
                            String password) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://jsonplaceholder.typicode.com/")
                .baseUrl("http://192.168.8.129:8000/")
//                .baseUrl("http://10.0.2.2:8000/")
//                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        UserAPIService userAPIService = retrofit.create(UserAPIService.class);
        User user = new User(firstName=firstName, lastName=lastName,
                username=username, email=email, password=password);
        Call<User> userCreatedAPI = userAPIService.createUser(user);
        userCreatedAPI.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(SignUpActivity.this, "Failed to create user"+ response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                User userCreated = response.body();
                Toast.makeText(SignUpActivity.this, "Fetched user: " + userCreated.getEmail().toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}