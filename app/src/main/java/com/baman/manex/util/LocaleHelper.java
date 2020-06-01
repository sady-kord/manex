package com.baman.manex.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

public class LocaleHelper {

    private static String LocalizationEN = "en";

    private static String LocalizationFA = "fa";

    public static void onAttach(Context context) {
        LocaleHelper.setLocal(context);
    }

    public static void onAttach(Context context, String defaultLanguage) {
        LocaleHelper.setLocal(context, defaultLanguage);
    }

    @SuppressWarnings("deprecation")
    public static void setSystemLocaleLegacy(Configuration config, Locale locale) {
        config.locale = locale;
        config.setLayoutDirection(locale);
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static void setSystemLocale(Configuration config, Locale locale) {
        config.setLocale(locale);
    }

    public static void setLocal(Context context) {
        setLocal(context, getLanguageSetting(context));
    }

    public static String getLanguageSetting(Context context) {
        /*switch (MyPreferences.readInteger(context, MyPreferences.LANGUAGE, 2)) {
            case 1:
                return LocalizationEN;
            case 2:
                return LocalizationFA;

            default:
                return LocalizationFA;
        }*/

        return LocalizationFA;

    }

    public static void setLocal(Context context, String language) {

        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLayoutDirection(locale);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setSystemLocale(config, locale);
        } else {
            setSystemLocaleLegacy(config, locale);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, locale);
        } else {
            updateResourcesLegacy(context, locale);
        }


    }

//
    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, Locale locale){
        Locale.setDefault(locale);

        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        return context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    private static Context updateResourcesLegacy(Context context, Locale locale){
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale);
        }

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }


    public static String getLocal() {
        Locale localHelper = Locale.getDefault();
        return localHelper.getLanguage();
    }

    public static Boolean isRTL() {
        return getLocal().equals(LocalizationFA);
    }

}
