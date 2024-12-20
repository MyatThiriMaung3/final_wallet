package tdtu.edu.course.mobiledev.mobileappdevfinalwallet;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

public class DailyReportService extends Service {

    private static final String CHANNEL_ID = "daily_report_channel";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Fetch today's data from the database
        double income = getTodayIncome();
        double expense = getTodayExpense();

        // Build and show the notification
        showNotification(income, expense);

        // Stop the service once the task is complete
        stopSelf();

        return START_NOT_STICKY;
    }

    private void showNotification(double income, double expense) {
        // Create the notification channel
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID, "Daily Report", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.createNotificationChannel(channel);
        }

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Daily Report")
                .setContentText("Income: $" + income + " | Expenses: $" + expense)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Show the notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(1, builder.build());
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
