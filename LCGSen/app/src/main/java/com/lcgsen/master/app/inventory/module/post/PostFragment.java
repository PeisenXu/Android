package com.lcgsen.master.app.inventory.module.post;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import com.xujiaji.happybubble.BubbleDialog;
import com.lcgsen.master.app.inventory.R;
import com.lcgsen.master.app.inventory.base.App;
import com.lcgsen.master.app.inventory.base.BaseFragment;
import com.lcgsen.master.app.inventory.helper.BubbleCreator;
import com.lcgsen.master.app.inventory.helper.InputHelper;
import com.lcgsen.master.app.inventory.helper.ToastHelper;
import com.lcgsen.master.app.inventory.helper.ToolbarHelper;
import com.lcgsen.master.app.inventory.module.main.MainActivity;
import com.lcgsen.master.app.inventory.module.main.MainContract;
import com.lcgsen.master.app.inventory.repository.bean.TodoTypeBean;
import com.lcgsen.master.app.inventory.util.SoftKeyUtil;

import java.text.SimpleDateFormat;
import java.util.Locale;

import io.doist.datetimepicker.date.DatePicker;

/**
 * author: xujiaji
 * created on: 2018/10/10 22:02
 * description:
 */
public class PostFragment extends BaseFragment<PostPresenter> implements PostContract.View, View.OnClickListener {

    private EditText mEtInput;
    private ImageView mBtnChooseCalendar;
    private ImageView mBtnTypeList;
    private ImageView mBtnPriority;
    private ImageView mBtnContent;
    private ImageView mBtnOk;
    private DatePicker mDatePicker;
    private ProgressBar mProgressBar;
    private BubbleDialog mBubbleDialog;
    private BubbleDialog mEditContentBubbleDialog;
    private BubbleDialog mChoosePriorityDialog;
    private TodoTypeBean.TodoListBean.TodoBean mEditTodoBean;

    private @ColorInt int grey = ContextCompat.getColor(App.getInstance(), R.color.grey_500);
    private @ColorInt int green = ContextCompat.getColor(App.getInstance(), R.color.green_500);

    private String mDate;
    private int mCategory = -1;
    private int mPriority = MainContract.PRIORITY_NOTURGENT_NOTIMPORTANT;
    private String mContent;

    @Override
    public int layoutId() {
        return R.layout.fragment_post;
    }

    @Override
    public void onInitCircle() {
        super.onInitCircle();
        ToolbarHelper.initPaddingTopDiffBar(getRootView().findViewById(R.id.layoutTop));

        mBtnChooseCalendar = getRootView().findViewById(R.id.btnChooseCalendar);
        mBtnTypeList       = getRootView().findViewById(R.id.btnTypeList);
        mProgressBar       = getRootView().findViewById(R.id.progressBar);
        mBtnPriority       = getRootView().findViewById(R.id.btnPriority);
        mBtnContent        = getRootView().findViewById(R.id.btnContent);
        mDatePicker        = getRootView().findViewById(R.id.datePicker);
        mEtInput           = getRootView().findViewById(R.id.etInput);
        mBtnOk             = getRootView().findViewById(R.id.btnOk);

        mDatePicker.setMinDate(System.currentTimeMillis());
        mDatePicker.setMaxDate(System.currentTimeMillis() + 356L * 24L * 60L * 60L * 1000L);

        onVisible();
    }

    @Override
    public void onArgumentsHandle(@NonNull Bundle bundle) {
        super.onArgumentsHandle(bundle);
        handleArgument((TodoTypeBean.TodoListBean.TodoBean) bundle.getParcelable("todo_bean"));
    }

    public void handleArgument(TodoTypeBean.TodoListBean.TodoBean todoBean) {
        mEditTodoBean = todoBean;
    }

    @Override
    public void onVisible() {
        super.onVisible();
        mDate = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(System.currentTimeMillis());
        mCategory = -1;
        mPriority = MainContract.PRIORITY_NOTURGENT_NOTIMPORTANT;
        mContent = null;

        mBtnChooseCalendar.setColorFilter(grey);
        mBtnTypeList.setColorFilter(grey);
        mBtnPriority.setColorFilter(grey);
        mBtnContent.setColorFilter(grey);

        mEtInput.requestFocus();
        SoftKeyUtil.show(getActivity(), mEtInput);

        mBubbleDialog = null;
        mEditContentBubbleDialog = null;
        mChoosePriorityDialog = null;
        displayEdit();
    }

    private void displayEdit() {
        if (mEditTodoBean == null) return;
        mContent = mEditTodoBean.getContent();
        mEtInput.setText(mEditTodoBean.getTitle());
        mCategory = mEditTodoBean.getType();
        mPriority = mEditTodoBean.getPriority();
        mDate = mEditTodoBean.getDateStr();
    }

    @Override
    public void onInvisible() {
        super.onInvisible();
        mEditTodoBean = null;
    }

    @Override
    public void onListenerCircle() {
        super.onListenerCircle();
        getRootView().findViewById(R.id.btnBack)      .setOnClickListener(this);
        getRootView().findViewById(R.id.rootContainer).setOnClickListener(this);

        final View v = getRootView().findViewById(R.id.rootContainer);
        mEtInput.post(new Runnable() {
            @Override
            public void run() {
                SoftKeyUtil.doMoveLayout( v,  v, new View[]{mDatePicker}, 0);
            }
        });

        mBtnChooseCalendar.setOnClickListener(this);
        mBtnContent.setOnClickListener(this);
        mBtnTypeList.setOnClickListener(this);
        mBtnPriority.setOnClickListener(this);
        mBtnOk.setOnClickListener(this);

        mDatePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mBtnChooseCalendar.setColorFilter(green);
                mDate = String.format("%s-%s-%s", year, monthOfYear + 1, dayOfMonth);
                hideChooseCalender();
            }
        });

        mEtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (InputHelper.isEmpty(mEtInput)) {
                    mBtnOk.setColorFilter(grey);
                } else {
                    mBtnOk.setColorFilter(green);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnChooseCalendar:
                showChooseCalender();
                break;
            case R.id.btnTypeList:
                showChooseTodoCategory();
                break;
            case R.id.btnPriority:
                showChoosePriority();
                break;
            case R.id.btnContent:
                showEditContent();
                break;
            case R.id.btnBack:
            case R.id.rootContainer:
                hidePage();
                break;
            case R.id.btnOk:
                addTodo();
                break;
        }
    }

    private void addTodo() {
        final String title = InputHelper.toString(mEtInput);
        if (TextUtils.isEmpty(title)) {
            ToastHelper.info(getString(R.string.please_input_todo));
            return;
        }
        if (mCategory == -1) {
            showChooseTodoCategory();
            ToastHelper.info(getString(R.string.please_choose_category));
            mCategory = MainContract.CATEGORY_USE_ONE;
            mBtnTypeList.setColorFilter(green);
            return;
        }
        TodoTypeBean.TodoListBean.TodoBean todoBean = new TodoTypeBean.TodoListBean.TodoBean();
        todoBean.setTitle(title);
        todoBean.setType(mCategory);
        todoBean.setContent(mContent);
        todoBean.setDateStr(mDate);
        todoBean.setPriority(mPriority);
        if (mEditTodoBean != null) {
            todoBean.setId(mEditTodoBean.getId());
            todoBean.setStatus(mEditTodoBean.getStatus());
            presenter.requestUpdateTodo(todoBean);
        } else {
            presenter.requestAddTodo(todoBean);
        }
    }

    @Override
    public void hidePage() {
        mEtInput.setText("");
        SoftKeyUtil.hide(getActivity());
        getActivity().onBackPressed();
    }

    @Override
    public void showChooseCalender() {
        SoftKeyUtil.hide(getActivity(), mEtInput);
        mDatePicker.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDatePicker.setVisibility(View.VISIBLE);
            }
        }, 200);
    }

    @Override
    public void hideChooseCalender() {
        mDatePicker.setVisibility(View.GONE);
    }

    @Override
    public void showChooseTodoCategory() {
        if (mBubbleDialog == null) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_choose_category, null);
            mBubbleDialog = new BubbleDialog(getActivity())
                    .setPosition(BubbleDialog.Position.TOP)
                    .setBubbleLayout(BubbleCreator.get(getActivity()))
                    .addContentView(view);
            RadioGroup group = view.findViewById(R.id.rgGroup);
            if (mEditTodoBean != null) {
                switch (mEditTodoBean.getType()) {
                    case MainContract.CATEGORY_USE_ONE:
                        group.check(R.id.rbUseOne);
                        break;
                    case MainContract.CATEGORY_WORK:
                        group.check(R.id.rbWork);
                        break;
                    case MainContract.CATEGORY_LEARN:
                        group.check(R.id.rbLearn);
                        break;
                    case MainContract.CATEGORY_LIFE:
                        group.check(R.id.rbLife);
                        break;
                }
            } else {
                group.check(R.id.rbUseOne);
            }
            group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    hideChooseTodoCategory();
                    mBtnTypeList.setColorFilter(green);
                    switch (checkedId) {
                        case R.id.rbUseOne:
                            mCategory = MainContract.CATEGORY_USE_ONE;
                            break;
                        case R.id.rbWork:
                            mCategory = MainContract.CATEGORY_WORK;
                            break;
                        case R.id.rbLearn:
                            mCategory = MainContract.CATEGORY_LEARN;
                            break;
                        case R.id.rbLife:
                            mCategory = MainContract.CATEGORY_LIFE;
                            break;
                    }
                }
            });
        }

        mBubbleDialog.setClickedView(mBtnTypeList);
        mBubbleDialog.show();

        if (mBubbleDialog.getWindow() != null)
            mBubbleDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public void hideChooseTodoCategory() {
        mBubbleDialog.dismiss();
    }

    @Override
    public void showChoosePriority() {
        if (mChoosePriorityDialog == null) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_choose_priority, null);
            mChoosePriorityDialog = new BubbleDialog(getActivity())
                    .setPosition(BubbleDialog.Position.TOP)
                    .addContentView(view)
                    .setBubbleLayout(BubbleCreator.get(getActivity()));
            RadioGroup group = view.findViewById(R.id.rgGroup);
            if (mEditTodoBean != null) {
                switch (mEditTodoBean.getPriority()) {
                    case MainContract.PRIORITY_URGENT_IMPORTANT:
                        group.check(R.id.rbPriorityUrgentImportant);
                        break;
                    case MainContract.PRIORITY_IMPORTANT_NOTURGENT:
                        group.check(R.id.rbPriorityImportantNotUrgent);
                        break;
                    case MainContract.PRIORITY_URGENT_NOTIMPORTANT:
                        group.check(R.id.rbPriorityUrgentNotImportant);
                        break;
                        default:
                }
            }

            group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.rbPriorityUrgentImportant:
                            mPriority = MainContract.PRIORITY_URGENT_IMPORTANT;
                            mBtnPriority.setColorFilter(ContextCompat.getColor(App.getInstance(), R.color.red_800));
                            break;
                        case R.id.rbPriorityImportantNotUrgent:
                            mPriority = MainContract.PRIORITY_IMPORTANT_NOTURGENT;
                            mBtnPriority.setColorFilter(ContextCompat.getColor(App.getInstance(), R.color.orange_800));
                            break;
                        case R.id.rbPriorityUrgentNotImportant:
                            mPriority = MainContract.PRIORITY_URGENT_NOTIMPORTANT;
                            mBtnPriority.setColorFilter(ContextCompat.getColor(App.getInstance(), R.color.yellow_800));
                            break;
                        case R.id.rbPriorityNotUrgentNotImportant:
                            mPriority = MainContract.PRIORITY_NOTURGENT_NOTIMPORTANT;
                            mBtnPriority.setColorFilter(ContextCompat.getColor(App.getInstance(), R.color.grey_500));
                            break;
                    }
                    hideChoosePriority();
                }
            });
        }
        mChoosePriorityDialog.setClickedView(mBtnPriority);
        mChoosePriorityDialog.show();

        if (mChoosePriorityDialog.getWindow() != null)
            mChoosePriorityDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public void hideChoosePriority() {
        mChoosePriorityDialog.dismiss();
    }

    @Override
    public void showEditContent() {
        if (mEditContentBubbleDialog == null) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_add_content, null);
            mEditContentBubbleDialog = new BubbleDialog(getActivity())
                    .addContentView(view)
                    .setBubbleLayout(BubbleCreator.get(getActivity()))
                    .setPosition(BubbleDialog.Position.TOP);

            final EditText editText = view.findViewById(R.id.etEditContent);
            if (mEditTodoBean != null) {
                editText.setText(mEditTodoBean.getContent());
            }
            view.findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContent = InputHelper.toString(editText);
                    if (TextUtils.isEmpty(mContent)) {
                        mBtnContent.setColorFilter(grey);
                    } else {
                        mBtnContent.setColorFilter(green);
                    }
                    hideEditContent();
                }
            });
        }

        mEditContentBubbleDialog.setClickedView(mBtnContent);
        mEditContentBubbleDialog.show();
        if (mEditContentBubbleDialog.getWindow() != null)
            mEditContentBubbleDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public void hideEditContent() {
        mEditContentBubbleDialog.dismiss();
    }

    @Override
    public void displayAddTodoIng() {
        mBtnOk.setEnabled(false);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayAddTodoFinished() {
        mBtnOk.setEnabled(true);
        mEtInput.setText("");
        mProgressBar.setVisibility(View.GONE);
        hidePage();
        if (getActivity() != null)
            ((MainActivity) getActivity()).onRefresh(mCategory);
    }
}
