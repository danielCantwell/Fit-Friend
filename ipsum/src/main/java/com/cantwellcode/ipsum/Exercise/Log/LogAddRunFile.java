package com.cantwellcode.ipsum.Exercise.Log;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.cantwellcode.ipsum.R;
import com.cantwellcode.ipsum.Startup.MainActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.MetadataBuffer;

/**
 * Created by Daniel on 5/30/2014.
 */
public class LogAddRunFile extends Fragment{

    public static Fragment newInstance() {
        Fragment f = new LogAddRunFile();
        return f;
    }

    private ListView mFileList;
    private ResultsAdapter mResultsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.file_results_list, null);

        MainActivity mainActivity = (MainActivity) getActivity();
        GoogleApiClient googleApiClient = mainActivity.getGoogleApiClient();

        mFileList = (ListView) root.findViewById(R.id.resultsList);
        mResultsAdapter = new ResultsAdapter(getActivity());

        mFileList.setAdapter(mResultsAdapter);

        DriveFolder folder = Drive.DriveApi.getRootFolder(googleApiClient);
        folder.listChildren(googleApiClient)
                .setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
            @Override
            public void onResult(DriveApi.MetadataBufferResult metadataBufferResult) {
                if (!metadataBufferResult.getStatus().isSuccess()) {
                    Toast.makeText(getActivity(), "problem retrieving files", Toast.LENGTH_SHORT).show();
                    return;
                }
                MetadataBuffer metadata = metadataBufferResult.getMetadataBuffer();
                mResultsAdapter.clear();
                mResultsAdapter.append(metadata);
                Toast.makeText(getActivity(), "successfully retrieved files", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }
}
