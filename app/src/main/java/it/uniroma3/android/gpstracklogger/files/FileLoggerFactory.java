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

package it.uniroma3.android.gpstracklogger.files;

import java.io.File;

import it.uniroma3.android.gpstracklogger.application.AppSettings;
import it.uniroma3.android.gpstracklogger.model.Track;

/**
 * Created by Fabio on 04/05/2015.
 */
public class FileLoggerFactory {
    private static int countFile = 0;
    private static String directory = AppSettings.getDirectory();

    public static GpxFileLogger getLogger(Track track) {
        File gpxFileFolder = new File(directory);
        if (!gpxFileFolder.exists()) {
            gpxFileFolder.mkdirs();
        }
        String fileName = track.getName().replace(":","-");
        File gpxFile = new File(gpxFileFolder, fileName + ".gpx");
        if (gpxFile.exists()) {
            countFile++;
            String rename = "(" + String.valueOf(countFile) + ")";
            gpxFile = new File(gpxFileFolder, fileName + rename + ".gpx");
        }
        else {
            countFile = 0;
        }
        GpxFileLogger logger = new GpxFileLogger(gpxFile, track);
        return logger;
    }

    public static GPXFileLoader getLoader(String path) {
        String name = path.substring(path.lastIndexOf("/"));
        String dir = path.substring(0,path.lastIndexOf("/"));
        File gpx = new File(dir, name);
        return new GPXFileLoader(gpx);
    }

    public static void loadGpxFile(String gpxFileName) {
        GPXFileLoader loader = getLoader(gpxFileName);
        loader.loadGpxFile();
    }

    public static void write(Track track) {
        GpxFileLogger logger = getLogger(track);
        logger.write();
    }
}
