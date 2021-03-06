/*******************************************************************************
 * This file is part of GPSTrackLogger.
 * Copyright (C) 2015  Fabio Cibecchini
 *
 * GPSTrackLogger is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GPSTrackLogger is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GPSTrackLogger.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package it.uniroma3.android.gpstracklogger.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

/**
 * Created by Fabio on 26/05/2015.
 */
public class AppSettings extends Application {
    private static int minTime, minDistance, period, fixedDistance, fixedTime;
    private static SharedPreferences preferences;
    private static boolean latitudeBar, longitudeBar;
    private static String directory;

    public static int getMinTime() {
        return minTime;
    }

    public static void setMinTime(int minTime) {
        AppSettings.minTime = minTime;
    }

    public static int getMinDistance() {
        return minDistance;
    }

    public static void setMinDistance(int minDistance) {
        AppSettings.minDistance = minDistance;
    }

    public static int getPeriod() {
        return period;
    }

    public static void setPeriod(int period) {
        AppSettings.period = period;
    }

    public static boolean isLatitudeBar() {
        return latitudeBar;
    }

    public static void setLatitudeBar(boolean latitudeBar) {
        AppSettings.latitudeBar = latitudeBar;
    }

    public static boolean isLongitudeBar() {
        return longitudeBar;
    }

    public static void setLongitudeBar(boolean longitudeBar) {
        AppSettings.longitudeBar = longitudeBar;
    }

    public static int getFixedDistance() {
        return fixedDistance;
    }

    public static void setFixedDistance(int fixedDistance) {
        AppSettings.fixedDistance = fixedDistance;
    }

    public static int getFixedTime() {
        return fixedTime;
    }

    public static void setFixedTime(int fixedTime) {
        AppSettings.fixedTime = fixedTime;
    }

    public static String getDirectory() {
        return directory;
    }

    public static void setDirectory(String directory) {
        AppSettings.directory = directory;
    }

    public static void loadSettings(Context context) {
        if (preferences == null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        setMinTime(Integer.valueOf(preferences.getString("minTime", "0")));
        setMinDistance(Integer.valueOf(preferences.getString("minDistance", "15")));
        setPeriod(Integer.valueOf(preferences.getString("period", "10")));
        setLatitudeBar(preferences.getBoolean("latitudeBar", true));
        setLongitudeBar(preferences.getBoolean("longitudeBar", true));
        setFixedDistance(Integer.valueOf(preferences.getString("elapsedDist", "0")));
        setFixedTime(Integer.valueOf(preferences.getString("elapsedTime", "0")));
        setDirectory(context.getFilesDir().getAbsolutePath());
    }

}
