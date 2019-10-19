package com.ryatec.syspalma;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static android.content.ContentValues.TAG;

public class FireBaseInstaceService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        Toast.makeText(this, ""+token.toString(), Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Refreshed token: " + token.toString());
    }
}
