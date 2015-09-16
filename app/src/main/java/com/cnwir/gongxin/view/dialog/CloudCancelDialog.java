package com.cnwir.gongxin.view.dialog;

import com.cnwir.gongxin.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

public class CloudCancelDialog extends DialogFragment implements
		OnClickListener {

	private TextView delete_tv;

	private TextView cancel_tv;

	private CloudCancelListener mListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setLayout(300, 100);
		View view = inflater.inflate(R.layout.cloud_cancel_dialog, container);

		delete_tv = (TextView) view.findViewById(R.id.cloud_app_delete);
		delete_tv.setOnClickListener(this);
		cancel_tv = (TextView) view.findViewById(R.id.cancel_delete);
		cancel_tv.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.cloud_app_delete:
			mListener.delete();
			break;

		case R.id.cancel_delete:
			mListener.cancel();
			break;

		default:
			break;
		}

	}

	public void setCancelListerer(CloudCancelListener mListener) {

		this.mListener = mListener;
	}

	public interface CloudCancelListener {

		public void delete();

		public void cancel();
	}

}
