package waka.techcast.internal.di;

import android.app.Application;

public class Injector {
    private static TechcastComponent component;

    public static void init(final Application application) {
        component = Dagger_TechcastComponent.builder()
                .androidModule(new AndroidModule(application))
                .build();
    }

    public static TechcastComponent get() {
        return component;
    }
}
