package it.uniroma3.android.gpstracklogger;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import it.uniroma3.android.gpstracklogger.application.AppSettings;
import it.uniroma3.android.gpstracklogger.application.Session;
import it.uniroma3.android.gpstracklogger.files.FileLoggerFactory;

public class LoadFileActivity extends Activity {
    private File mPath = new File(AppSettings.getDirectory());
    private static final String FTYPE = ".gpx";
    private String chosenFile;
    private List<String> list;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadFileList();
        setContentView(R.layout.activity_loadfile);
        listView = (ListView) findViewById(R.id.list);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                chosenFile = (String) listView.getItemAtPosition(position);
                FileLoggerFactory.loadGpxFile(chosenFile);
                ViewPropertyAnimator viewPropertyAnimator = view.animate().setDuration(500).alpha(0);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    viewPropertyAnimator.setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            list.remove(chosenFile);
                            adapter.notifyDataSetChanged();
                            view.setAlpha(1);
                        }
                    });
                } else {
                    viewPropertyAnimator.withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            list.remove(chosenFile);
                            adapter.notifyDataSetChanged();
                            view.setAlpha(1);
                        }
                    });
                }
            }
        });
    }

    private void loadFileList() {
        try {
            mPath.mkdirs();
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
        list = new ArrayList<>();
        if(mPath.exists()) {
            FilenameFilter filter = new FilenameFilter() {

                @Override
                public boolean accept(File dir, String filename) {
                    File sel = new File(dir, filename);
                    return filename.contains(FTYPE) || sel.isDirectory();
                }

            };
            String[] mFileList = mPath.list(filter);
            for (String file : mFileList) {
                if (Session.getController().getImportedTrack(file) == null) {
                    list.add(file);
                }
            }
        }
    }


}
