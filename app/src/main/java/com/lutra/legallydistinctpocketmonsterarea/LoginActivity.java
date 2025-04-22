package com.lutra.legallydistinctpocketmonsterarea;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.lutra.legallydistinctpocketmonsterarea.database.AppRepository;
import com.lutra.legallydistinctpocketmonsterarea.database.entities.User;
import com.lutra.legallydistinctpocketmonsterarea.databinding.ActivityLoginBinding;


public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private AppRepository repository;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        repository = AppRepository.getRepository(getApplication());

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyUser();
                startActivity(SignUpActivity.intentFactory(LoginActivity.this));


            }
        });

    }

    private void verifyUser(){
        String username = binding.userNameLoginEditText.getText().toString();


        if(username.isEmpty()){
            ToastMaker("Username should not be blank");
            return;
        }
        LiveData<User> userObserver = repository.getUserByUserName(username);
        userObserver.observe(this,user -> {
            if(user != null){
                String password = binding.passwordLoginEditText.getText().toString();
                if(password.equals(user.getPassword())){

                    startActivity(MainActivity.mainActivityIntentFactory(getApplicationContext(),user.getId()));
                }else{
                    ToastMaker("Invalid password");
                    binding.passwordLoginEditText.setSelection(0);
                }
            }else{
                ToastMaker(String.format(" %s is not a valid username", username));
                binding.userNameLoginEditText.setSelection(0);
            }
        });

    }

    private void ToastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    static Intent loginIntentFactory(Context context){
        return new Intent(context, LoginActivity.class);
    }

}