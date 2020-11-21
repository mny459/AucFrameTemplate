package com.blankj.template.pkg;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.common.CommonTitleActivity;


public class TemplateActivity extends CommonTitleActivity {


    @Override
    public CharSequence bindTitle() {
        return "template";
    }

    @Override
    public int bindLayout() {
        return R.layout.template_activity;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView) {

    }

    @Override
    public void doBusiness() {

    }
}
