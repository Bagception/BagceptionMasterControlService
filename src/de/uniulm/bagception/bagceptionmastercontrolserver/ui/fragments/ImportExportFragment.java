package de.uniulm.bagception.bagceptionmastercontrolserver.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.lamerman.FileDialog;

import de.uniulm.bagception.bagceptionmastercontrolserver.R;
import de.uniulm.bagception.bagceptionmastercontrolserver.database.DatabaseHelper;
import de.uniulm.bagception.mcs.services.MasterControlServer;

public class ImportExportFragment extends Fragment{
	


	private Button importDB;
	private Button exportDB;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.importexportfragmentlayout, null, false);
		
		
		importDB = (Button) v.findViewById(R.id.importDB);
		exportDB = (Button) v.findViewById(R.id.exportDB);
		importDB.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startDialog(REQUEST_FILE_IMPORT);
			}
		});
		exportDB.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startDialog(REQUEST_FILE_EXPORT);
			}
		});
		return v;
	}

	private void startDialog(int requesttype){
		Intent intent = new Intent(getActivity().getApplicationContext(), FileDialog.class);
        intent.putExtra(FileDialog.START_PATH, DatabaseHelper.DB_BACKUP_PATH);
        
        //can user select directories or not
    	intent.putExtra(FileDialog.CAN_SELECT_DIR, false);
//        if (requesttype == REQUEST_FILE_IMPORT){
//        	intent.putExtra(FileDialog.FORMAT_FILTER, new String[] { "db" });
//        }
        //
        
        startActivityForResult(intent, requesttype);
	}
	public static final int REQUEST_FILE_IMPORT=1;
	public static final int REQUEST_FILE_EXPORT=2;
	
	@Override
	public synchronized void onActivityResult(final int requestCode,
            int resultCode, final Intent data) {

            if (resultCode == Activity.RESULT_OK) {
            		String path = data.getStringExtra(FileDialog.RESULT_PATH);
                    if (requestCode == REQUEST_FILE_EXPORT) {
        				if (MasterControlServer.debuginstance.getDB().exportDatabase(path)){
        					Toast.makeText(getActivity(), "Export erfolgreich: "+path, Toast.LENGTH_SHORT).show();
	    				}else{
	    					Toast.makeText(getActivity(), "Export fehlgeschlagen: "+path, Toast.LENGTH_SHORT).show();
	    				}
                    }else if (requestCode == REQUEST_FILE_IMPORT) {
                    	if (MasterControlServer.debuginstance.getDB().importDatabase(path)){
	    					Toast.makeText(getActivity(), "Import erfolgreich: "+path, Toast.LENGTH_SHORT).show();
	    				}else{
	    					Toast.makeText(getActivity(), "Import fehlgeschlagen: "+path, Toast.LENGTH_SHORT).show();
	    				}
                    }
            } else if (resultCode == Activity.RESULT_CANCELED) {
				Toast.makeText(getActivity(), "Aktion abgebrochen",Toast.LENGTH_SHORT).show();
            }

    }
}
