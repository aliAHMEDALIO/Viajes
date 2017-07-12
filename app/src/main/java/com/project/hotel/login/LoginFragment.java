/*

    Copyright 2017, The Android Open Source Project

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

    IF there any question ,Please contact me at :
    m.elbehiry44@gmail.com

*/

package com.project.hotel.login;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.project.hotel.R;
import com.project.hotel.dialog.ProgressDialogFragment;
import com.project.hotel.main.MainActivity;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Created by Elbehiry on 2/23/17.
 */

public class LoginFragment extends Fragment implements LoginContractor.View {
    private Button Login;
    private LoginContractor.Presenter mPresenter;
    private static final String PROGRESS_DIALOG = "ProgressDialog";
    private boolean isParent = true;
    private boolean change = false;



    public LoginFragment(){}


    public static LoginFragment getInstance(){
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login,container,false);

        Login = (Button) rootView.findViewById(R.id.google_login);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.login();
            }
        });



        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void showErrorMessage(String message) {

        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showUserCreateErrorMessage() {
        Toast.makeText(getActivity(), "Error In Create User", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showProgress() {
        ProgressDialogFragment f = ProgressDialogFragment.getInstance();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(f, PROGRESS_DIALOG)
                .commitAllowingStateLoss();
    }

    @Override
    public void dismissProgress() {
        try {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            if (manager == null) return;
            ProgressDialogFragment f = (ProgressDialogFragment) manager.findFragmentByTag(PROGRESS_DIALOG);
            if (f != null) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(f).commitAllowingStateLoss();
            }
        } catch (Exception ex) {

        }
    }

    @Override
    public void showMain() {
        getActivity().startActivity(new Intent(getActivity(), MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        getActivity().finish();
    }

    @Override
    public boolean isActive(boolean active) {
        return isAdded();
    }



    @Override
    public void setPresenter(LoginContractor.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }



}
