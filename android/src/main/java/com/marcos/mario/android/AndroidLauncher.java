package com.marcos.mario.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.marcos.mario.Main;
import com.marcos.mario.Tools.IdiomaManager;

/** Launches the Android application. */
public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
        configuration.useImmersiveMode = true; // Recommended, but not required.

        // Crear instancia del manejador de idioma y pasarlo a Main
        IdiomaManager idiomaManager = new AndroidIdiomaManager(this);
        initialize(new Main(idiomaManager), configuration);
    }
}
