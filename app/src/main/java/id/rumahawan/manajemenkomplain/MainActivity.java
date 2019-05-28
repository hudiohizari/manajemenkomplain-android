package id.rumahawan.manajemenkomplain;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.rumahawan.manajemenkomplain.CustomDialog.SingleComplaintBottomDialog;
import id.rumahawan.manajemenkomplain.Data.Session;
import id.rumahawan.manajemenkomplain.Data.VolleyController;
import id.rumahawan.manajemenkomplain.RecyclerView.ComplaintList;
import id.rumahawan.manajemenkomplain.RecyclerView.ComplaintListAdapter;
import id.rumahawan.manajemenkomplain.Service.Notification;

public class MainActivity extends AppCompatActivity {
//    @BindView(R.id.ivNotification)
//    ImageView ivNotification;
    @BindView(R.id.ivExit)
    ImageView ivExit;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvPosition)
    TextView tvPosition;
    @BindView(R.id.rrComplaint)
    RecyclerView rrComplaint;
    @BindView(R.id.tvEmpty)
    TextView tvEmpty;
    @BindView(R.id.tvDisconnected)
    TextView tvDisconnected;

    private Session session;
    private ComplaintListAdapter adapter;
    private ProgressDialog progressDialog;
    private ScheduledExecutorService scheduledExecutorService;

    private ArrayList<ComplaintList> arrayList = new ArrayList<>();
    public static final String baseUrl = "https://manajemenkomplain.000webhostapp.com/";
    public static final String apiUrl = "https://manajemenkomplain.000webhostapp.com/end-point.php";

    //Start RecyclerView click listener
    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{
        private MainActivity.ClickListener clicklistener;
        private GestureDetector gestureDetector;

        RecyclerTouchListener(Context context, final MainActivity.ClickListener clicklistener){
            this.clicklistener = clicklistener;
            gestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if(child != null && clicklistener != null && gestureDetector.onTouchEvent(e)){
                clicklistener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {}
        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
    }
    //End RecyclerView click listener


    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null){
            loadKomplain();
            startScheduledLoadJSON();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (scheduledExecutorService != null){
            session.setSessionInt("lastResultLength", -1);
            scheduledExecutorService.shutdown();
            Log.d("ScheduledExecService", "Shutdown - Main Activity");
        }
    }

    private boolean isNotificationRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (Notification.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (!isNotificationRunning()){
            startService(new Intent(this, Notification.class));
        }

        session = new Session(this);
        session.setSessionInt("lastResultLength", -1);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new ComplaintListAdapter(this, arrayList);

        ivExit.setOnClickListener(v -> logout());
//        ivNotification.setOnClickListener(v -> Toast.makeText(this, "Notification not ready yet", Toast.LENGTH_SHORT).show());

        if (!session.isExist("email")){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
        else{
            rrComplaint.setLayoutManager(layoutManager);
            rrComplaint.setAdapter(adapter);
            rrComplaint.addOnItemTouchListener(new RecyclerTouchListener(this,
                    (view, position) -> {
                        Bundle bundle = new Bundle();
                        bundle.putInt("id", arrayList.get(position).getKomplainId());
                        SingleComplaintBottomDialog dialog = new SingleComplaintBottomDialog();
                        dialog.setArguments(bundle);
                        dialog.show(getSupportFragmentManager(), dialog.getTag());
                    }
            ));

            progressDialog.setMessage("Mengambil Data....");
            progressDialog.show();
            if (scheduledExecutorService != null && !scheduledExecutorService.isShutdown()) {
                startScheduledLoadJSON();
            }
            loadUser();
        }
    }

    private void startScheduledLoadJSON() {
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(
                this::loadKomplain
                , 0, 1, TimeUnit.SECONDS);
        Log.d("ScheduledExecService", "Started - Main Activity");
    }

    private void loadKomplain(){
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.POST, apiUrl + "?komplain&email=" + session.getSessionString("email"), null, response -> {
//            if (session.getSessionInt("lastResultLength") == response.length()){
//                Log.d("Load JSON", "Data same");
//            }
//            else{
            if (session.getSessionInt("lastResultLength") != response.length()){
                Log.d("Load JSON", "Data different");
                session.setSessionInt("lastResultLength", response.length());
                arrayList.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject data = response.getJSONObject(i);
                        if (data.getString("status").equalsIgnoreCase("b")) {
                            continue;
                        }

                        ComplaintList complaintList = new ComplaintList();
                        complaintList.setKomplainId(data.getInt("id"));
                        complaintList.setNama(data.getString("nama"));
                        if (data.getString("status").equalsIgnoreCase("p")) {
                            complaintList.setDrawableStatus(R.drawable.yellow_circle);
                        } else if (data.getString("status").equalsIgnoreCase("s")) {
                            complaintList.setDrawableStatus(R.drawable.green_circle);
                        }

                        arrayList.add(complaintList);
                        if (arrayList.size() == response.length()){
                            session.setSessionInt("lastResultLength", response.length());
                            Log.d("Load JSON", "Last Result Length Changed");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressDialog.cancel();
                    adapter.notifyDataSetChanged();
                }

//                for (int i = 0; i < arrayList.size(); i++) {
//                    int finalI = i;
//                    JsonArrayRequest arrayRequestUser = new JsonArrayRequest(Request.Method.POST, apiUrl + "?user&email=" + arrayList.get(i).getNama(), null, responseUser -> {
//                        for (int j = 0; j < responseUser.length(); j++) {
//                            try {
//                                JSONObject dataUser = responseUser.getJSONObject(j);
//                                arrayList.get(finalI).setNama(dataUser.getString("nama"));
//                                progressDialog.cancel();
//                                adapter.notifyDataSetChanged();
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }, error -> {
//                        tvDisconnected.setVisibility(View.VISIBLE);
//                        Log.e("volley", "error : " + error.getMessage());
//                        tvDisconnected.setVisibility(View.GONE);
//                    });
//                    VolleyController.getInstance().addToRequestQueue(arrayRequestUser);
//                }

                if (arrayList.size() == 0) {
                    progressDialog.cancel();
                    tvEmpty.setVisibility(View.VISIBLE);
                }
                tvDisconnected.setVisibility(View.GONE);
            }
        }, error -> {
            progressDialog.cancel();
            tvDisconnected.setVisibility(View.VISIBLE);
            Log.e("volley", "error : " + error.getMessage());
        });
        VolleyController.getInstance().addToRequestQueue(arrayRequest);
    }

    public void loadUser(){
        @SuppressLint("SetTextI18n") JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.POST, apiUrl + "?user&email=" + session.getSessionString("email"), null, response -> {
            if (response.length() == 0) {
                Toast.makeText(this, "Email tidak ditemukan", Toast.LENGTH_SHORT).show();
                logout();
            } else {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject dataUser = response.getJSONObject(i);
                        tvName.setText(dataUser.getString("nama"));
                        if (dataUser.getString("posisi").equals("kepala")){
                            tvPosition.setText("Kepala Unit");
                        }

                        @SuppressLint("SetTextI18n") JsonArrayRequest arrayRequestKategori = new JsonArrayRequest(Request.Method.POST, apiUrl + "?kategori&id=" + dataUser.getString("kategori"), null, responseKategori -> {
                            if (responseKategori.length() == 0) {
                                Toast.makeText(this, "kategori tidak ditemukan", Toast.LENGTH_SHORT).show();
                            } else{
                                for (int j = 0; j < responseKategori.length(); j++) {
                                    try {
                                        JSONObject dataKategori = responseKategori.getJSONObject(j);
                                        tvPosition.setText("Kepala Unit " + dataKategori.getString("nama"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }, error -> Log.e("volley", "error : " + error.getMessage()));
                        VolleyController.getInstance().addToRequestQueue(arrayRequestKategori);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, error -> Log.e("volley", "error : " + error.getMessage()));
        VolleyController.getInstance().addToRequestQueue(arrayRequest);
    }

    private void logout(){
        session.removeSession("email");
        session.removeSession("lastResultLength");
        session.removeSession("lastKomplain");
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public interface ClickListener{
        void onClick(View view, int position);
    }
}
