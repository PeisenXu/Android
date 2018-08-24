package com.lcgsen.doview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lcgsen.master.R;
import com.lcgsen.utils.DragFloatActionButton;

public class FragmentUtils extends Fragment {

    private TextView tv;
    private DragFloatActionButton floatingView;

    public static FragmentUtils newInstance(String name) {

        Bundle args = new Bundle();
        args.putString("name", name);
        FragmentUtils fragment = new FragmentUtils();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        Object nameObj = bundle.get("name");
        View view;
        if (bundle != null && nameObj != null) {
            if ("首页".equalsIgnoreCase(nameObj.toString())) {
                view = inflater.inflate(R.layout.fragment_one, container, false);
            } else {
                view = inflater.inflate(R.layout.fragment, container, false);
            }
        } else {
            view = inflater.inflate(R.layout.fragment, container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        Object nameObj = bundle.get("name");
        if (bundle != null && nameObj != null) {
            if ("首页".equalsIgnoreCase(nameObj.toString())) {
                floatingView = (DragFloatActionButton)(FloatingActionButton) view.findViewById(R.id.fab);
                floatingView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(view.getContext(),"FAB clicked",Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                tv = (TextView) view.findViewById(R.id.fragment_test_tv);
                String name = nameObj.toString();
                tv.setText(name);
            }
        }
    }

}
