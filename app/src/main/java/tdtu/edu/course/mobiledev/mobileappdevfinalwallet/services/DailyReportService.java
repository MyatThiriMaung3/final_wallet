package tdtu.edu.course.mobiledev.mobileappdevfinalwallet.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import tdtu.edu.course.mobiledev.mobileappdevfinalwallet.R;

public class DailyReportService extends Service {

    private static final String CHANNEL_ID = "daily_report_channel";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        double income = getTodayIncome();
        double expense = getTodayExpense();

        showNotification(income, expense);
        stopSelf();

        return START_NOT_STICKY;
    }

    private void showNotification(double income, double expense) {
        createNotificationChannel();
        NotificationCompat.Builder builder = buildNotification(income, expense);
        showNotification(builder);
    }

    private void showNotification(NotificationCompat.Builder builder) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(1, builder.build());
        }
    }

    private NotificationCompat.Builder buildNotification(double income, double expense) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Daily Report")
                .setContentText("Income: $" + income + " | Expenses: $" + expense)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        return builder;
    }

    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID, "Daily Report", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.createNotificationChannel(channel);
        }
    }

    private double getTodayIncome() {
        return 150.0; // Example value
    }

    private double getTodayExpense() {
        return 75.0; // Example value
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
