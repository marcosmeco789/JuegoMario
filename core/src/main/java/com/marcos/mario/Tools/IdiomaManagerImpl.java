package com.marcos.mario.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;
import java.util.Locale;

public class IdiomaManagerImpl implements IdiomaManager {
    private I18NBundle bundle;

    public IdiomaManagerImpl(String languageCode) {
        loadLanguage(languageCode);
    }

    public void loadLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        bundle = I18NBundle.createBundle(Gdx.files.internal("i18n/strings"), locale);
    }

    @Override
    public String getString(String key) {
        return bundle.get(key);
    }

    @Override
    public void cambiarIdioma(String nuevoIdioma) {

    }
}
