package com.mangonon.johnry.popularmovieapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.mangonon.johnry.popularmovieapp.utils.NetworkUtils;


public class SortDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.sort_dialog_title)
                .setItems(R.array.sort_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NetworkUtils.SortType sortType;
                        switch (which) {
                            case 1: sortType = NetworkUtils.SortType.TOP_RATED;
                                break;
                            case 2: sortType = NetworkUtils.SortType.FAVOURITE;
                                break;
                            default: sortType = NetworkUtils.SortType.POPULAR;
                                break;
                        }
                        SortDialogListener listener = (MainActivity) getActivity();
                        listener.onSortSelected(sortType);
                    }
                });
        return builder.create();
    }

    public interface SortDialogListener {
        void onSortSelected(NetworkUtils.SortType sortType);
    }
}
