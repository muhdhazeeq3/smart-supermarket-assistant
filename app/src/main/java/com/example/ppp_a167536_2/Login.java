package com.example.ppp_a167536_2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ppp_a167536_2.Models.User;
import com.google.android.gms.common.util.VisibleForTesting;
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


public class Login extends AppCompatActivity implements View.OnClickListener{

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void hideKeyboard(View view) {
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    private static final String TAG = "EmailPassword";
    EditText etUsername, etPassword;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            boolean emailVerified = currentUser.isEmailVerified();

            if (!emailVerified) {
                signOut();
            } else {
                String uid = currentUser.getUid();
                DatabaseReference ref = mDatabase.child("users").child(uid);

                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        String un = user.username;

//                        Toast.makeText(Login.this, "Welcome " + un + " to CTRL +",
//                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });

                Intent intent = new Intent(Login.this, MainActivity.class);

                startActivity(intent);
            }

        } else {

        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String username = etUsername.getText().toString();
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Required.");
            valid = false;
        } else {
            etUsername.setError(null);
        }

        String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Required.");
            valid = false;
        } else {
            etPassword.setError(null);
        }

        return valid;
    }

    private void signIn(String username, String password) {
        Log.d(TAG, "signIn:" + username);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();


        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //String EmailPassword;
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();

                    updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(Login.this, "Not yet register.",
                            Toast.LENGTH_SHORT).show();

                    updateUI(null);
                }

                if (!task.isSuccessful()) {

                }
                hideProgressDialog();

            }
        });
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();

            boolean emailVerified = user.isEmailVerified();

            if(!emailVerified) {
                Toast.makeText(Login.this, email + ".You have not verify your email yet.",
                        Toast.LENGTH_LONG).show();
            } else {
                FirebaseUser user1 = mAuth.getCurrentUser();

                String uid = user1.getUid();
                DatabaseReference ref = mDatabase.child("users").child(uid);

                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        String un = user.username;

//                        Toast.makeText(Login.this, "Welcome " + un + " to CTRL +",
//                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });

                Intent intent = new Intent(Login.this, MainActivity.class);

                startActivity(intent);
            }

        } else {

        }
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_register) {
            Intent intent = new Intent(Login.this, Register.class);

            startActivity(intent);
        } else if (i == R.id.btn_login) {
            signIn(etUsername.getText().toString(), etPassword.getText().toString());
        }
    }
}

