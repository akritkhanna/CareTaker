package iot.project.caretaker;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import iot.project.caretaker.interfacing_bridge.Processes;
import iot.project.caretaker.interfacing_bridge.Server;
import iot.project.caretaker.interfacing_bridge.model.ServerData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Akrit on 25-Feb-18.
 */

public class MedicineProcessReciever extends BroadcastReceiver {
    Processes prc_data;
    @Override
    public void onReceive(final Context context, Intent intent) {

        //Getting Services from the Server...!
        Server server = new Server();
        prc_data = server.getProcess();

        prc_data.getMedi().enqueue(new Callback<ServerData>() {
            @Override
            public void onResponse(Call<ServerData> call, Response<ServerData> response) {

                NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                Intent to_reminders_activity = new Intent(context, Reminder.class);
                to_reminders_activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                PendingIntent pendingIntent = PendingIntent.getActivity(context,12,to_reminders_activity,PendingIntent.FLAG_UPDATE_CURRENT);
                String CHANNEL_ID = "medicine";
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.care_icon)
                        .setContentTitle("Medicine")
                        .setContentText("Time for your Medicine")
                        .setAutoCancel(true);

                nm.notify(3,builder.build());

            }

            @Override
            public void onFailure(Call<ServerData> call, Throwable t) {

                NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                Intent to_reminders_activity = new Intent(context, Reminder.class);
                to_reminders_activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                PendingIntent pendingIntent = PendingIntent.getActivity(context,12,to_reminders_activity,PendingIntent.FLAG_UPDATE_CURRENT);
                String CHANNEL_ID = "medicine";
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.care_icon)
                        .setContentTitle("Error")
                        .setContentText("Unable to make contact with CareTaker Device")
                        .setAutoCancel(true);

                nm.notify(3,builder.build());

            }
        });
    }
}
