package com.ryatec.syspalma;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ryatec.syspalma.R;

public class FireBaseNoticifacaoService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String from = remoteMessage.getFrom();
        //Toast.makeText(this, "Mensagem de: "+from, Toast.LENGTH_SHORT).show();
        if(remoteMessage.getNotification() != null){
            //Toast.makeText(this, "Notificação: "+remoteMessage.getNotification().getBody().toString(), Toast.LENGTH_SHORT).show();
            mostrarNotificacao(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
        if(remoteMessage.getData().size() > 0){
            //Toast.makeText(this, "Dados: "+remoteMessage.getData().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void mostrarNotificacao(String titulo, String msg){

        Intent intent = new Intent(this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificaoBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.agricola)
                .setContentTitle(titulo)
                .setContentText(msg)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificaoBuilder.build());
    }
}
