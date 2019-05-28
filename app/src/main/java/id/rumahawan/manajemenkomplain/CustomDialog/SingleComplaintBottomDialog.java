package id.rumahawan.manajemenkomplain.CustomDialog;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.zxy.tiny.Tiny;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.rumahawan.manajemenkomplain.Data.Session;
import id.rumahawan.manajemenkomplain.Data.VolleyController;
import id.rumahawan.manajemenkomplain.Data.VolleyMultipartRequest;
import id.rumahawan.manajemenkomplain.R;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static id.rumahawan.manajemenkomplain.MainActivity.apiUrl;
import static id.rumahawan.manajemenkomplain.MainActivity.baseUrl;

public class SingleComplaintBottomDialog extends BottomSheetDialogFragment {
    @BindView(R.id.clBackground)
    NestedScrollView clBackground;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.ivClose)
    ImageView ivClose;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.ivStatus)
    ImageView ivStatus;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvDescription)
    TextView tvDescription;
    @BindView(R.id.ivViewImageDescription)
    ImageView ivViewImageDescription;
    @BindView(R.id.etResponse)
    EditText etResponse;
    @BindView(R.id.tvResponseLabel)
    TextView tvResponseLabel;
    @BindView(R.id.tvResponseAnswered)
    TextView tvResponseAnswered;
    @BindView(R.id.tvResponseLabelAnswered)
    TextView tvResponseLabelAnswered;
    @BindView(R.id.ivViewImage)
    ImageView ivViewImage;
    @BindView(R.id.tvGambarLabel)
    TextView tvGambarLabel;
    @BindView(R.id.vAddImage)
    View vAddImage;
    @BindView(R.id.ivAddImage)
    ImageView ivAddImage;
    @BindView(R.id.ivAddedImage)
    ImageView ivAddedImage;
    @BindView(R.id.tvGambarOptional)
    TextView tvGambarOptional;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    private Session session;
    private Uri uriDeskripsi;
    private Uri uriBalasan;
    private RequestQueue rQueue;
    private ProgressDialog progressDialog;

    private int id;
    private boolean isAnswered = false;
    private String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @SuppressLint({"RestrictedApi", "SetTextI18n"})
    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View view = View.inflate(getContext(), R.layout.single_complaint_bottom_dialog, null);
        ButterKnife.bind(this, view);
//        Tiny.getInstance().init(Objects.requireNonNull(getActivity()).getApplication());
        Tiny.getInstance();
        dialog.setContentView(view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getInt("id");
        }
        if (getContext() != null) {
            session = new Session(getContext());
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setCancelable(false);
        }
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.POST, apiUrl + "?komplain&id=" + id, null, response -> {
            for (int i=0; i < response.length(); i++){
                try {
                    JSONObject data = response.getJSONObject(i);

                    ViewGroup.LayoutParams layoutParams = clBackground.getLayoutParams();
                    layoutParams.height = FrameLayout.LayoutParams.WRAP_CONTENT;
                    clBackground.setLayoutParams(layoutParams);

                    tvName.setText(data.getString("email"));
                    if (data.getString("status").equalsIgnoreCase("s")){
                        isAnswered = true;
                        tvResponseAnswered.setText(data.getString("balasan"));
                    }
                    int finalI = i;
                    JsonArrayRequest arrayRequestUser = new JsonArrayRequest(Request.Method.POST, apiUrl + "?user&email=" + data.getString("email"), null, responseUser -> {
                        for (int j=0; j < responseUser.length(); j++){
                            try {
                                JSONObject dataUser = responseUser.getJSONObject(j);
                                tvName.setText(dataUser.getString("nama"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (finalI == response.length() - 1 && j == responseUser.length() - 1){
                                updateUi();
                            }
                        }
                    }, error -> {
                        Toast.makeText(getContext(), "Tidak terhubung keserver", Toast.LENGTH_SHORT).show();
                        Log.e("volley", "error : " + error.getMessage());
                    });
                    VolleyController.getInstance().addToRequestQueue(arrayRequestUser);

                    if (getContext() != null) {
                        if (data.getString("status").equalsIgnoreCase("p")) {
                            ivStatus.setImageDrawable(getContext().getResources().getDrawable(R.drawable.yellow_circle));
                        } else if (data.getString("status").equalsIgnoreCase("s")) {
                            ivStatus.setImageDrawable(getContext().getResources().getDrawable(R.drawable.green_circle));
                        }
                    }
                    tvTitle.setText(data.getString("judul"));
                    tvDescription.setText(data.getString("deskripsi"));
                    if (!data.getString("deskripsi_gambar").equals("null")){
                        ivViewImageDescription.setVisibility(View.VISIBLE);
                        uriDeskripsi = Uri.parse(baseUrl + "komplain/" + data.getString("deskripsi_gambar"));
                        Picasso.get()
                                .load(uriDeskripsi)
                                .centerCrop()
                                .fit()
                                .into(ivViewImageDescription);
                        Log.d("Load gambar from", uriDeskripsi.toString());
                    }
                    if (!data.getString("balasan_gambar").equals("null")){
                        ivViewImage.setVisibility(View.VISIBLE);
                        uriBalasan =   Uri.parse(baseUrl + "komplain/" + data.getString("balasan_gambar"));
                        Picasso.get()
                                .load(uriBalasan)
                                .centerCrop()
                                .fit()
                                .into(ivViewImage);
                        Log.d("Load gambar from", uriBalasan.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), "Tidak terhubung keserver", Toast.LENGTH_SHORT).show();
            Log.e("volley", "error : " + error.getMessage());
        });
        VolleyController.getInstance().addToRequestQueue(arrayRequest);

        ivClose.setOnClickListener(v -> dismiss());
        vAddImage.setOnClickListener(v -> selectPhoto());
        ivAddImage.setOnClickListener(v -> selectPhoto());
        ivAddedImage.setOnClickListener(v -> selectPhoto());
        btnSubmit.setOnClickListener(v -> submit());
        ivViewImageDescription.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW , uriDeskripsi)));
        ivViewImage.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW , uriBalasan)));
    }

    private void updateUi(){
        ivStatus.setVisibility(View.VISIBLE);
        tvDescription.setVisibility(View.VISIBLE);
        if (isAnswered){
            tvResponseAnswered.setVisibility(View.VISIBLE);
            tvResponseLabelAnswered.setVisibility(View.VISIBLE);
        }
        else{
            etResponse.setVisibility(View.VISIBLE);
            tvResponseLabel.setVisibility(View.VISIBLE);
            tvGambarLabel.setVisibility(View.VISIBLE);
            vAddImage.setVisibility(View.VISIBLE);
            ivAddImage.setVisibility(View.VISIBLE);
            tvGambarOptional.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length == 3){
                Toast.makeText(getActivity(), "Terima semua permission dahulu", Toast.LENGTH_SHORT).show();
            }
            selectPhoto();
        }
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void selectPhoto(){
        if(!hasPermissions(getActivity(), PERMISSIONS)){
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, 1);
        }
        else {
            final String[] options = {"Ambil gambar", "Pilih gambar", "Batal"};
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setItems(options, (dialog, item) -> {
                switch (options[item]) {
                    case "Ambil gambar": {
                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        @SuppressLint("SdCardPath") Uri uri = Uri.parse("file:///sdcard/taken_image.jpg");
                        takePicture.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(takePicture, 0);
                        dialog.dismiss();
                        break;
                    }
                    case "Pilih gambar": {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, 1);
                        dialog.dismiss();
                        break;
                    }
                    case "Batal":
                        dialog.dismiss();
                        break;
                }
            });
            builder.show();
        }
    }

    private static void copyStream(InputStream input, FileOutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    private File createImageFile() throws IOException {
        File image = null;
        if (getActivity() != null) {
            File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            image = File.createTempFile(
                    "balasan-" + System.currentTimeMillis(),
                    ".jpg",
                    storageDir
            );
        }
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    File file = new File(Environment.getExternalStorageDirectory().getPath(), "taken_image.jpg");
                    uriBalasan = Uri.fromFile(file);
                    updateUiFromResult();
                }
                break;
            case 1:
                if(resultCode == RESULT_OK){
                    if (getActivity() != null && data.getData() != null) {
                        try {
                            File file = null;
                            try {
                                file = createImageFile();
                            } catch (IOException ex) {
                                Log.d(TAG, "Terjadi kesalahan mengambil file");
                            }

                            InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());
                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            if (inputStream != null) {
                                copyStream(inputStream, fileOutputStream);
                                fileOutputStream.close();
                                inputStream.close();
                            }

                            uriBalasan = Uri.fromFile(file);

                        } catch (Exception e) {
                            Log.d(TAG, "onActivityResult: " + e.toString());
                        }
                        updateUiFromResult();
                    }
                }
                break;
        }
    }

    private void updateUiFromResult(){
        tvGambarLabel.setVisibility(View.GONE);
        tvGambarOptional.setVisibility(View.GONE);
        ivAddImage.setVisibility(View.GONE);
        vAddImage.setVisibility(View.INVISIBLE);
        ivAddedImage.setVisibility(View.VISIBLE);
        Picasso.get()
                .load(uriBalasan)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .centerCrop()
                .fit()
                .into(ivAddedImage);
        Log.d("Image Loaded", "" + uriBalasan.getPath());
    }

    private void submit(){
        if (getActivity() != null) {
            if(uriBalasan == null){
                updateKomplain(null);
            }
            else{
                Tiny.BitmapCompressOptions options = new Tiny.BitmapCompressOptions();
                Tiny.getInstance().source(uriBalasan.getPath()).asBitmap().withOptions(options).compress((isSuccess, bitmap, t) -> updateKomplain(bitmap));
            }
        }
    }

    private String getMimeType() {
        String extension;

        if (Objects.equals(uriBalasan.getScheme(), ContentResolver.SCHEME_CONTENT)) {
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(Objects.requireNonNull(getContext()).getContentResolver().getType(uriBalasan));
        } else {
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uriBalasan.getPath())).toString());
        }

        return extension;
    }

    private void updateKomplain(final Bitmap bitmap){
        String answer = etResponse.getText().toString();
        if (answer.equalsIgnoreCase("")) {
            Toast.makeText(getContext(), "Isi jawaban terlebih dahulu", Toast.LENGTH_SHORT).show();
        }
        else{
            progressDialog.setMessage("Mengunggah balasan...");
            progressDialog.show();
            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, apiUrl + "?update-komplain&id=" + id + "&response=" + answer, response -> {
                    if (new String(response.data).equalsIgnoreCase("ok")) {
                        Toast.makeText(getContext(), "Berhasil memberikan jawaban", Toast.LENGTH_SHORT).show();
                        session.setSessionInt("lastResultLength", -1);
                        dismiss();
                    }
                    else {
                        Toast.makeText(getContext(), new String(response.data), Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                    rQueue.getCache().clear();
                },
                error -> {
                    Toast.makeText(getContext(), "Terjadi kesalahan: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Volley Upload", error.getMessage());
                    progressDialog.dismiss();
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        return new HashMap<>();
                    }

                    @Override
                    protected Map<String, DataPart> getByteData() {
                        Map<String, DataPart> params = new HashMap<>();
                        if (bitmap != null){
                            long imagename = System.currentTimeMillis();
                            params.put("filename", new DataPart(imagename + "." + getMimeType(), getFileDataFromDrawable(bitmap)));
                        }
                        return params;
                    }
                };

            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            if (getContext() != null) {
                rQueue = Volley.newRequestQueue(getContext());
            }
            rQueue.add(volleyMultipartRequest);
        }
    }

    private byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
