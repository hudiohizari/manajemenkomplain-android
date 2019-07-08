package id.rumahawan.manajemenkomplain;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.rumahawan.manajemenkomplain.Data.Session;
import id.rumahawan.manajemenkomplain.Data.VolleyController;

import static id.rumahawan.manajemenkomplain.MainActivity.apiUrl;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.tilEmail)
    TextInputLayout tilEmail;
    @BindView(R.id.tilPassword)
    TextInputLayout tilPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;

    private Session session;
    private ProgressDialog progressDialog;

    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        session = new Session(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        btnLogin.setOnClickListener(v -> {
            if (tilEmail.getEditText() != null){
                email = tilEmail.getEditText().getText().toString();
            }
            if (tilPassword.getEditText() != null) {
                password = tilPassword.getEditText().getText().toString();
            }
            if (isValidEmail(email)) {
                checkLogin();
            } else {
                Toast.makeText(LoginActivity.this, "Format email salah", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private String getMd5(String string) {
//        try {
//            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
//            digest.update(string.getBytes());
//            byte[] messageDigest = digest.digest();
//
//            // Create Hex String
//            StringBuilder hexString = new StringBuilder();
//            for (byte aMessageDigest : messageDigest) {
//                StringBuilder h = new StringBuilder(Integer.toHexString(0xFF & aMessageDigest));
//                while (h.length() < 2)
//                    h.insert(0, "0");
//                hexString.append(h);
//            }
//            return hexString.toString();
//
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }

    private boolean isValidEmail(String email) {
        String regex = "^[\\w-_.+]*[\\w-_.]@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    private void checkLogin() {
        progressDialog.setMessage("Sedang login...");
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, apiUrl + "?check-login&email=" + email + "&password=" + password, response -> {
            if (response.equalsIgnoreCase("empty")){
                Toast.makeText(this, "Email tidak ditemukan", Toast.LENGTH_SHORT).show();
            }
            else if (response.equalsIgnoreCase("ok")){
                session.setSessionString("email", email);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
            else if (response.equalsIgnoreCase("deny")){
                Toast.makeText(this, "Password salah", Toast.LENGTH_SHORT).show();
            }
        }, error -> Log.e("volley", "error : " + error));
        VolleyController.getInstance().addToRequestQueue(request);
    }
}
