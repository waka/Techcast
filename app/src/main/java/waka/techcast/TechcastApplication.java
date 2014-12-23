package waka.techcast;

import android.app.Application;

import waka.techcast.internal.di.Injector;

public class TechcastApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Injector.init(this);
    }
}
