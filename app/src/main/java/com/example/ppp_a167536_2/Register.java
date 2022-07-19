package com.example.ppp_a167536_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.example.ppp_a167536_2.Models.User;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements View.OnClickListener {

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

    EditText etUsername2, etPassword2, usname, paword;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViewById(R.id.btn_register2).setOnClickListener(this);

        etUsername2 = findViewById(R.id.et_username2);
        etPassword2 = findViewById(R.id.et_password2);
        usname = findViewById(R.id.u_name);
        paword = findViewById(R.id.p_word);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    private boolean validateForm() {
        boolean valid = true;

        String username = etUsername2.getText().toString();
        if (TextUtils.isEmpty(username)) {
            etUsername2.setError("Required.");
            valid = false;
        } else {
            etUsername2.setError(null);
        }

        String uname = usname.getText().toString();
        if (TextUtils.isEmpty(uname)) {
            usname.setError("Required.");
            valid = false;
        } else {
            usname.setError(null);
        }

        String pword = paword.getText().toString();
        if (pword.length()<6) {
            paword.setError("Must be at least 6 character");
            valid = false;
        } else {
            paword.setError(null);
        }

        String password = etPassword2.getText().toString();
        if (password.length()<6) {
            etPassword2.setError("Must be at least 6 character");
            valid = false;
        } else {
            etPassword2.setError(null);
            if (pword.equals(password)) {
                etPassword2.setError(null);
            } else {
                etPassword2.setError("Password not the same");
                valid = false;
            }
        }

        return valid;
    }

    private void createAccount(String username, String password) {
        Log.d(TAG, "createAccount:" + username);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    sendEmailVerification();
                    writeNewUser(user.getUid(), usname.getText().toString(), user.getEmail());
                    Toast.makeText(Register.this, "Verification email sent to " + user.getEmail() + ". You have to verify first before continue to log in.", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(Register.this, Login.class);
                    startActivity(intent);

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(Register.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }

                hideProgressDialog();

            }
        });

    }

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("users").child(userId).setValue(user);
    }

    private void sendEmailVerification() {

        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


                if (task.isSuccessful()) {
                    //    Toast.makeText(Register.this, "Verification email sent to " + user.getEmail() + ".", Toast.LENGTH_LONG).show();
                } else {
//                    Toast.makeText(Register.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                }
                // [END_EXCLUDE]
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {

            String name = user.getDisplayName();
            String email = user.getEmail();

        } else {

        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_register2) {
            createAccount(etUsername2.getText().toString(), etPassword2.getText().toString());
        }
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }
}
