package com.lcgsen.doview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lcgsen.master.R;
import com.lcgsen.utils.DragFloatActionButton;
import com.lcgsen.utils.ViewHelper;
import com.lcgsen.utils.custom.EllipsizingTextView;

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

                addTextView(view, 0, 0, Color.argb(60, 200, 200, 200));
                addTextView(view, 0, 0, Color.argb(70, 200, 200, 200));
                addTextView(view, 0, 0, Color.argb(80, 200, 200, 200));
                addTextView(view, 0, 0, Color.argb(90, 200, 200, 200));
                addTextView(view, 0, 0, Color.argb(100, 200, 200, 200));
                addTextView(view, 0, 0, Color.argb(110, 200, 200, 200));
                addTextView(view, 0, 0, Color.argb(120, 200, 200, 200));
                addTextView(view, 0, 0, Color.argb(100, 200, 200, 200));
                addTextView(view, 0, 0, Color.argb(110, 200, 200, 200));
                addTextView(view, 0, 0, Color.argb(120, 200, 200, 200));
                addTextView(view, 0, 0, Color.argb(130, 200, 200, 200));
                addTextView(view, 0, 0, Color.argb(140, 200, 200, 200));
                addTextView(view, 0, 0, Color.argb(150, 200, 200, 200));
                addTextView(view, 0, 0, Color.argb(160, 200, 200, 200));

            } else {
                tv = (TextView) view.findViewById(R.id.fragment_test_tv);
                String name = nameObj.toString();
                tv.setText(name);
            }
        }
    }

    private LinearLayout addTextView(final View view, int height, int width, int color) {

        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.home_line);

        EllipsizingTextView textView = new EllipsizingTextView(view.getContext());
        textView.setMaxLines(5);
        textView.setText("\t海军蓝\t#000080\t0,0,128\n" +
                "　\tRoyalBlue\t皇家蓝\t#4169E1\t65,105,225\n" +
                "　\tCornflowerBlue\t矢车菊的蓝色\t#6495ED\t100,149,237\n" +
                "　\tLightSteelBlue\t淡钢蓝\t#B0C4DE\t176,196,222\n" +
                "　\tLightSlateGray\t浅石板灰\t#778899\t119,136,153\n" +
                "　\tSlateGray\t石板灰\t#708090\t112,128,144\n" +
                "　\tDoderBlue\t道奇蓝\t#1E90FF\t30,144,255\n" +
                "　\tAliceBlue\t爱丽丝蓝\t#F0F8FF\t240,248,255\n" +
                "　\tSteelBlue\t钢蓝\t#4682B4\t70,130,180\n" +
                "　\tLightSkyBlue\t淡蓝色\t#87CEFA\t135,206,250\n" +
                "　\tSkyBlue\t天蓝色\t#87CEEB\t135,206,235\n" +
                "　\tDeepSkyBlue\t深天蓝\t#00BFFF\t0,191,255\n" +
                "　\tLightBLue\t淡蓝\t#ADD8E6\t173,216,230\n" +
                "　\tPowDerBlue\t火药蓝\t#B0E0E6");
        // textView.setBackgroundColor(color);
        textView.setPadding(10, 10, 10, 10);
        // recyclerView.addView(view.findViewById(R.id.fragment_test_tv));
        linearLayout.addView(textView);

        ViewHelper.setMargins(textView, 0, 0, 0, ViewHelper.getScreenHeight(view.getContext()) / 9 / 3);

        //设置recyclerView高度
        ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
        textView.setLayoutParams(layoutParams);

        return linearLayout;
    }


}
