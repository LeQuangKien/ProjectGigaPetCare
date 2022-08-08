package tuan.aprotrain.projectpetcare.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.regex.Pattern;

import tuan.aprotrain.projectpetcare.R;
import tuan.aprotrain.projectpetcare.entity.Recycle;
import tuan.aprotrain.projectpetcare.entity.User;

public class LoginActivity extends AppCompatActivity {
    EditText uEmail, uPassword;
    Button btnLogin;
    ProgressBar progressBar;
    private TextView register;
    private DatabaseReference reference;
    private User user;
    private Recycle recycle;
    private TextView forgot_pass;
    private Boolean isUpdating = false;
    //SharedPreferences pref;
    private FirebaseAuth mAuth;

    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        uEmail = findViewById(R.id.user_email);
        uPassword = findViewById(R.id.pass);
        btnLogin = findViewById(R.id.loginBtn);
        register = findViewById(R.id.textRegister);
        forgot_pass = findViewById(R.id.forgot_pass);

        mAuth = FirebaseAuth.getInstance();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = uEmail.getText().toString().trim();
                String password = uPassword.getText().toString().trim();
                if(Verify(email,password)){
                    login(email, password);
                }


            }
        });

        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                //startActivity(new Intent(LoginActivity.this, SendOTPActivity.class));
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    public boolean Verify(String email, String password) {
        int i = 0;
        if (TextUtils.isEmpty(email)) {
            uEmail.setError("Required");
            uEmail.requestFocus();
            i++;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            uEmail.setError("Invalid email form !");
            i++;
        }
        if (TextUtils.isEmpty(password)) {
            uPassword.setError("Required");
            uPassword.requestFocus();
            i++;
        }
        if (i > 0) {
            isUpdating = false;
            return false;
        } else {
            return true;
        }
    }

    //public boolean i = false;
    public void login(String email, String password) {
        reference = FirebaseDatabase.getInstance().getReference().child(User.TABLE_NAME);

        //recycle = new Recycle();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Sign in success, update UI with the signed-in user's information
                            reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                   // if (snapshot.child("email").getValue(String.class).equals(email)){
                                        startActivity(new Intent(LoginActivity.this, BookingActivity.class));
                                    //}
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            FirebaseMessaging.getInstance().getToken()
                                    .addOnCompleteListener(new OnCompleteListener<String>() {
                                        @Override
                                        public void onComplete(@NonNull Task<String> task) {
                                            String token = task.getResult();
                                            //String token = task.getResult();
                                            System.out.println("Token: " + token);

                                            String android_id = Settings.Secure.getString(getContentResolver(),
                                                    Settings.Secure.ANDROID_ID);



                                            if (!reference.child(user.getUid()).child("token").getKey().equals(android_id)) {

//                                            userGet.setUserToken(token);
                                            reference.child(user.getUid()).child("token")
                                                    .child(android_id).setValue(token);
                                            }



//                                            reference.child(user.getUid())
//                                                    .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<DataSnapshot> task) {
//                                                    reference.child(user.getUid());
//                                                }
//                                            });


//                                            String oldToken = reference.child(user.getUid())
//                                                    .getKey();
////                            reference.child("Users")
////                                    .child(user.getUid())
////                                    .child("token")
////                                    .setValue()
//                                            Toast.makeText(LoginActivity.this, "LoginActivity Successful", Toast.LENGTH_SHORT).show();
//                                            startActivity(new Intent(LoginActivity.this, BookingActivity.class));
                                        }
                                    });
                        } else { // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Invalid Username or Password", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}