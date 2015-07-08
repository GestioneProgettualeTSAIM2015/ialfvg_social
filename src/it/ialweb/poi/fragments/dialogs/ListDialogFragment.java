package it.ialweb.poi.fragments.dialogs;

import it.ialweb.poi.R;
import it.ialweb.poi.core.ListAdapter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ListView;

public class ListDialogFragment extends DialogFragment
{
	public final static String TAG = "ListDialogFragment";
	
	private static final String DIALOG_TYPE = "DIALOG_TYPE";


	private ListView mList;
	
	public static ListDialogFragment newInstance(String dialogType) {
		ListDialogFragment dialog = new ListDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putString(DIALOG_TYPE, dialogType);
		dialog.setArguments(bundle);
		return dialog;
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_list, null);
		
		mList = (ListView) view.findViewById(R.id.list);
		String type = getArguments().getString(DIALOG_TYPE);
		if (type != null)
			populateList(type);

        builder.setView(view).setPositiveButton(android.R.string.ok, null);
        
        return builder.create();
    }

	private void populateList(String type) {	
		ListAdapter adapter = new ListAdapter(getActivity(), false, type);
		mList.setAdapter(adapter);
		adapter.loadObjects();
	}
}
