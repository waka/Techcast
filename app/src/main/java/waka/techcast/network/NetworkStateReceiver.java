package waka.techcast.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import javax.inject.Inject;

import waka.techcast.internal.di.Injector;

public class NetworkStateReceiver extends BroadcastReceiver {
    @Inject
    Client client;

    public NetworkStateReceiver() {
        super();
        Injector.get().inject(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        boolean isConnected = info != null && info.isConnectedOrConnecting();
        client.changeNetworkState(isConnected);
    }
}
