package tdtu.edu.course.mobiledev.mobileappdevfinalwallet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DailyReportReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Log the trigger for debugging
        Log.d("DailyReportReceiver", "Alarm Triggered");

        // Start the service or process the report
        Intent serviceIntent = new Intent(context, DailyReportService.class);
        context.startService(serviceIntent);
    }
}
