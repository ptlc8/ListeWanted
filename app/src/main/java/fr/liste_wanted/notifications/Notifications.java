package fr.liste_wanted.notifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.SystemClock;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import fr.liste_wanted.R;
import fr.liste_wanted.data.Event;
import fr.liste_wanted.ui.events.EventActivity;

public class Notifications {

    public static final String eventNotificationChannelId = "fr.liste_wanted.notifications.event";

    public static void sendNotification(Context context, Notification notification, int notificationId) {
        NotificationManagerCompat.from(context).notify(notificationId, notification);
    }

    public static void scheduleNotification(Context context, Notification notification, int notificationId, int requestCode, long delayInMs) {
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, notificationId);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context. ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, delayInMs+SystemClock.elapsedRealtime(), pendingIntent);
    }

    public static void createNotificationChannel(Context context, String channelId, CharSequence name, String description) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setLightColor(Color.YELLOW);
            channel.enableVibration(true);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static Notification createEventNotification(Context context, Event event) {
        Notifications.createNotificationChannel(context, Notifications.eventNotificationChannelId, context.getString(R.string.event_channel_name), context.getString(R.string.event_channel_description));
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Notifications.eventNotificationChannelId)
                .setSmallIcon(R.drawable.ic_logo)
                .setColor(context.getColor(R.color.main))
                .setContentTitle(event.getName())
                .setContentText(event.getDescription())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(event.getDescription()));

        Intent showEventIntent = EventActivity.getShowEventIntent(context, event);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(showEventIntent);
        PendingIntent eventIntent = stackBuilder.getPendingIntent(event.getId()*2, 0);//PendingIntent.getActivity(context, event.getId()*2, showEventIntent, 0);
        builder.setContentIntent(eventIntent)
                .setAutoCancel(true);

        return builder.build();
    }

}
