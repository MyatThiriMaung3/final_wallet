package tdtu.edu.course.mobiledev.mobileappdevfinalwallet.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import tdtu.edu.course.mobiledev.mobileappdevfinalwallet.services.LowBatteryService;

public class LowBatteryReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BATTERY_LOW.equals(intent.getAction())) {
            Intent serviceIntent = new Intent(context, LowBatteryService.class);
            context.startForegroundService(serviceIntent);
        }
    }
}
