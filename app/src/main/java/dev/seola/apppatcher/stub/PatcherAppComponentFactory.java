package dev.seola.apppatcher.stub;

import android.app.AppComponentFactory;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import com.yc.pm.PackageManagerStub;
import com.yc.pm.WebViewUpdateServiceStub;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PatcherAppComponentFactory extends AppComponentFactory {
    static {
        PackageManagerStub.replaceService();
        WebViewUpdateServiceStub.replaceService();

        try {
            Utils.setStaticField("android.os.Build", "MANUFACTURER", "Samsung");
            Utils.setStaticField("android.os.Build", "MODEL", "SM-X906N");
            Utils.setStaticField("android.os.Build", "PRODUCT", "basquiat");
        } catch (Exception e) {
            Log.d("SEOLA-APPPATCHER", "ERROR!!" + e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Application instantiateApplication(ClassLoader cl, String className)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        try {
            System.loadLibrary("frida-gadget");
        } catch (UnsatisfiedLinkError e) {
            // gadget 없으면 무시
        }
        return super.instantiateApplication(cl, className);
    }

    @Override
    public ClassLoader instantiateClassLoader(ClassLoader cl, ApplicationInfo aInfo) {
        try (InputStream s = cl.getResource("dev.seola.apppatcher.sig.orig").openStream()) {
            try (BufferedReader b = new BufferedReader(new InputStreamReader(s))) {
                Utils.SIGNATURE_HEX = b.readLine();
                Log.d("SEOLA-APPPATCHER", Utils.SIGNATURE_HEX);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return super.instantiateClassLoader(cl, aInfo);
    }
}
