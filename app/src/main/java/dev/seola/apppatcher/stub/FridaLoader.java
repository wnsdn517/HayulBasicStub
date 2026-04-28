package dev.seola.apppatcher.stub;

import android.util.Log;

public class FridaLoader {
    private static final String TAG = "SEOLA-FRIDA";

    private static final String[] GADGET_PATHS = {
        "/data/local/tmp/re.frida.Gadget.so",
        "/data/local/tmp/frida-gadget.so",
        "/data/local/tmp/libfrida-gadget.so",
    };

    public static void load() {
        for (String path : GADGET_PATHS) {
            try {
                System.load(path);
                Log.d(TAG, "Frida gadget loaded: " + path);
                return;
            } catch (UnsatisfiedLinkError e) {
                Log.d(TAG, "Not found: " + path);
            }
        }
        Log.w(TAG, "Frida gadget not found in any known path");
    }
}
