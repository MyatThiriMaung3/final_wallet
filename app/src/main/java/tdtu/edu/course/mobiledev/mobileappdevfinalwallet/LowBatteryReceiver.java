package tdtu.edu.course.mobiledev.mobileappdevfinalwallet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class LowBatteryReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BATTERY_LOW.equals(intent.getAction())) {
            Log.d("Low battery message triggered", "Please charge your phone.");
            // Start the Foreground Service
            Intent serviceIntent = new Intent(context, LowBatteryService.class);
            context.startForegroundService(serviceIntent);
        }
    }
}
