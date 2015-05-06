package it.uniroma3.android.gpstracklogger;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import de.greenrobot.event.EventBus;
import it.uniroma3.android.gpstracklogger.events.Events;
import it.uniroma3.android.gpstracklogger.service.GPSLoggingService;
import java.util.Date;


public class GPSMainActivity extends Activity {
    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpsmain);
        RegisterEventBus();
        Button start = (Button) findViewById(R.id.startButton);
        Button stop = (Button) findViewById(R.id.stopButton);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startClick();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopClick();
            }
        });
    }

    private void RegisterEventBus() {
        EventBus.getDefault().register(this);
    }

    private void UnregisterEventBus(){
        try {
            EventBus.getDefault().unregister(this);
        } catch (Throwable t){
            //this may crash if registration did not go through. just be safe
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gpsmain, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startClick() {
        String notice;
        if (serviceIntent == null) {
            serviceIntent = new Intent(this, GPSLoggingService.class);
            startService(serviceIntent);
            notice = "Service started";
        }
        else {
            notice = "Service already running...";
        }
        setTextViewValue(R.id.notice, notice);
        setTextViewValue(R.id.provider, "GPS");
        setTextViewValue(R.id.available, "TRUE");
        setTextViewValue(R.id.enabled, "TRUE");
    }

    private void stopClick() {
        stopLogging();
        stopService(serviceIntent);
        setTextViewValue(R.id.notice, "Service stopped");
        setTextViewValue(R.id.provider, "");
        setTextViewValue(R.id.enabled, "");
        setTextViewValue(R.id.available, "");
        setTextViewValue(R.id.timestamp, "");
        setTextViewValue(R.id.latitude, "");
        setTextViewValue(R.id.longitude, "");
        setTextViewValue(R.id.speed, "");
        setTextViewValue(R.id.altitude, "");
        serviceIntent = null;
    }

    private void stopLogging() {
        EventBus.getDefault().post(new Events.Stop());
    }


    public void onEventMainThread(Events.LocationUpdate locationEvent){
        displayLocationInfo(locationEvent.location);
    }

    public void onEventMainThread(Events.Message message) {
        setTextViewValue(message.id, message.info);
    }

    private void displayLocationInfo(Location location) {
        Date timestamp = new Date(location.getTime());
        setTextViewValue(R.id.timestamp, timestamp.toString());
        double latitude = location.getLatitude();
        setTextViewValue(R.id.latitude, String.valueOf(latitude));
        double longitude = location.getLongitude();
        setTextViewValue(R.id.longitude, String.valueOf(longitude));
        if (location.hasAltitude()) {
            double altitude = location.getAltitude();
            setTextViewValue(R.id.altitude, String.valueOf(altitude));
        }
        if (location.hasSpeed()) {
            float speed = location.getSpeed();
            setTextViewValue(R.id.speed, String.valueOf(speed));
        }
    }

    private void setTextViewValue(int textViewId, String value) {
        TextView textView = (TextView) findViewById(textViewId);
        textView.setText(value);
    }

    @Override
    protected void onDestroy() {
        UnregisterEventBus();
        super.onDestroy();
    }
}