package com.marcos.mario.android;


import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import com.marcos.mario.Tools.IdiomaManager;

import java.util.Locale;

public class AndroidIdiomaManager implements IdiomaManager {
    private Context context;
    private Resources resources;

    public AndroidIdiomaManager(Context context) {
        this.context = context;
        this.resources = context.getResources();
    }

    @Override
    public String getString(String key) {
        int resId = context.getResources().getIdentifier(key, "string", context.getPackageName());
        return resId != 0 ? context.getString(resId) : key;
    }

    public void cambiarIdioma(String idioma) {
        Locale nuevoLocale = new Locale(idioma);
        Locale.setDefault(nuevoLocale);

        Configuration config = new Configuration();
        config.setLocale(nuevoLocale);
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }
}
