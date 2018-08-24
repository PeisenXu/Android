package com.lcgsen.doview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lcgsen.master.R;
import com.lcgsen.utils.DragFloatActionButton;
import com.lcgsen.utils.ScreenUtils;

public class FragmentUtils extends Fragment {

    private TextView tv;
    private DragFloatActionButton floatingView;
    private LinearLayout linearLayout;

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
                floatingView = (DragFloatActionButton) view.findViewById(R.id.fab);
                floatingView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(view.getContext(), "FAB clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                linearLayout = (LinearLayout) view.findViewById(R.id.home_line);
                addRecyclerView(view, 0, 0, 0xFFF0F8FF);
                addRecyclerView(view, 0, 0, 0xFFFFFACD);
                addRecyclerView(view, 0, 0, 0xFF8B8B83);
                addRecyclerView(view, 0, 0, 0xFFCDC8B1);
                addRecyclerView(view, 0, 0, 0xFFF0F8FF);
                addRecyclerView(view, 0, 0, 0xFFFFFACD);
                addRecyclerView(view, 0, 0, 0xFF8B8B83);
                addRecyclerView(view, 0, 0, 0xFFCDC8B1);
            } else {
                tv = (TextView) view.findViewById(R.id.fragment_test_tv);
                String name = nameObj.toString();
                tv.setText(name);
            }
        }
    }

    private LinearLayout addRecyclerView(final View view, int height, int width, int color) {
        RecyclerView recyclerView = new RecyclerView(view.getContext());
        if (color == 0) {
            color = android.graphics.Color.RED;
        }
        recyclerView.setBackgroundColor(color);
        recyclerView.setPadding(5, 5, 5, 5);
        linearLayout.addView(recyclerView);

        //设置recyclerView高度
        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);

        if (height == 0) {
            height = ScreenUtils.getScreenHeight(view.getContext()) / 9;
        }
        if (width != 0) {
            layoutParams.width = width;
        }

        layoutParams.height = height;

        recyclerView.setLayoutParams(layoutParams);

        return linearLayout;
    }

}
