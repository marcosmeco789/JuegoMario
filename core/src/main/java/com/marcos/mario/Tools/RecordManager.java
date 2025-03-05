package com.marcos.mario.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class RecordManager {
    private static final String PREFS_NAME = "gameRecords";
    private Preferences prefs;

    public RecordManager() {
        // Inicializa las preferencias
        prefs = Gdx.app.getPreferences(PREFS_NAME);
    }

    // Método para resetear los records
    public void resetRecords() {
        // Elimina las preferencias de los niveles
        prefs.remove("bestTimeLevel1");
        prefs.remove("bestTimeLevel2");
        prefs.remove("bestTimeLevel3");
        prefs.flush();  // Asegúrate de guardar los cambios

        // Log para depuración
        Gdx.app.log("RecordManager", "Records reseteados: Nivel 1 = " + prefs.getInteger("bestTimeLevel1", 9999) + ", Nivel 2 = " + prefs.getInteger("bestTimeLevel2", 9999));
    }

    // Método para actualizar el mejor tiempo de un nivel
    public void updateBestTime(int mapNumber, int timeTaken) {
        if (mapNumber == 0) {
            return; // Ignorar el tutorial
        }

        String key = "bestTimeLevel" + mapNumber;
        int currentBest = getBestTime(mapNumber);

        // Si no hay récord o el nuevo tiempo es mejor, se actualiza
        if (currentBest == 9999 || timeTaken < currentBest) {
            prefs.putInteger(key, timeTaken);
            prefs.flush();  // Guarda los cambios inmediatamente
        }
    }

    // Método para obtener el mejor tiempo de un nivel
    public int getBestTime(int mapNumber) {
        String key = "bestTimeLevel" + mapNumber;
        return prefs.getInteger(key, 9999);
    }
}
