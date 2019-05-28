package id.rumahawan.manajemenkomplain.Service;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import id.rumahawan.manajemenkomplain.Data.Session;
import id.rumahawan.manajemenkomplain.Data.VolleyController;
import id.rumahawan.manajemenkomplain.MainActivity;
import id.rumahawan.manajemenkomplain.R;

import static id.rumahawan.manajemenkomplain.MainActivity.apiUrl;

public class Notification extends Service {

    private static final String CHANNEL_ID = "komplain";
    private static final String CHANNEL_NAME = "sdm";
    private static final String CHANNEL_DESC = "Notifikasi Manajemen Komplain";

    private Session session;

    private int komplain;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Notification", "Service Started");
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        session = new Session(this);
        komplain = 0;

        if (!session.isExist("lastKomplain")){
            session.setSessionInt("lastKomplain", 0);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationChannel.setDescription(CHANNEL_DESC);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleWithFixedDelay(() -> {
            if (session.isExist("email")){
                @SuppressLint("SetTextI18n") JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.POST, apiUrl + "?komplain&email=" + session.getSessionString("email"), null, response -> {
                    komplain = response.length();
                    if (session.getSessionInt("lastKomplain") != komplain){
                        Log.d("Notification Complaint", komplain + " || " + session.getSessionInt("lastKomplain"));
                        crateNotification();
                    }
                }, error -> Log.e("Volley Notifikasi", "error : " + error.getMessage()));
                VolleyController.getInstance().addToRequestQueue(arrayRequest);
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    private void crateNotification(){
        int total = komplain - session.getSessionInt("lastKomplain");
        if (total < 0) total *= -1;
        session.setSessionInt("lastKomplain", komplain);
        Intent intent = new Intent(this, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                .setColor(getResources().getColor(android.R.color.white))
                .setContentTitle("Komplain Baru")
                .setContentText("Anda memiliki " + total + " komplain baru.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1, builder.build());
    }
}
