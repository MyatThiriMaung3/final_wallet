package tdtu.edu.course.mobiledev.mobileappdevfinalwallet.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import tdtu.edu.course.mobiledev.mobileappdevfinalwallet.services.DailyReportService;

public class DailyReportReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, DailyReportService.class);
        context.startService(serviceIntent);
    }
}
