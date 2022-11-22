package com.jotangi.healthy.ui.account;

import static com.jotangi.healthy.ui.account.AccountLoginFragment.member_information;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.jotangi.baseutils.DialogUtils;
import com.jotangi.healthy.HpayMall.ApiUrl;
import com.jotangi.healthy.HpayMall.CustomDaialog;
import com.jotangi.healthy.HpayMall.MemberBean;
import com.jotangi.healthy.R;
import com.jotangi.healthy.ui.ProjConstraintFragment;
import com.jotangi.jotangi2021.AndroidModule.Api.ApiConUtils;

public class AccountDataFragment extends ProjConstraintFragment {
    TextInputEditText edName, edBirthday, edHomenumber, edMail, edPhone;
    RadioButton r1, r2;
    ConstraintLayout btnModify;

    public static AccountDataFragment newInstance() {
        AccountDataFragment fragment = new AccountDataFragment();
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        rootView = (ConstraintLayout) inflater.inflate(R.layout.fragment_account_data, container, false);

        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();

        edName = rootView.findViewById(R.id.ed_name);
        edBirthday = rootView.findViewById(R.id.ed_birthday);
        edHomenumber = rootView.findViewById(R.id.ed_home_number);
        edMail = rootView.findViewById(R.id.ed_email);
        edPhone = rootView.findViewById(R.id.ed_phone);
        r1 = rootView.findViewById(R.id.radio0);
        r2 = rootView.findViewById(R.id.radio1);
        edName.setText(MemberBean.member_name);
        edHomenumber.setText(MemberBean.member_phone);
        edPhone.setText(MemberBean.member_id);
        if(MemberBean.member_birthday.isEmpty()||MemberBean.member_birthday.equals("null"))
        {
            edBirthday.setText(null);
            //edHomenumber.setText(null);
        }else {
            edBirthday.setText(MemberBean.member_birthday);
        }

        edMail.setText(MemberBean.member_email);

        switch (MemberBean.member_gender) {
            case "0":
                r1.setChecked(false);
                r2.setChecked(true);
                break;
            case "1":
                r1.setChecked(true);
                r2.setChecked(false);
                break;

        }
        btnModify = rootView.findViewById(R.id.btn_register);
        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useredit();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        activityTitleRid = R.string.account_listitem_data;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    private void useredit() {
        String account = MemberBean.member_id;
        String pwd = MemberBean.member_pwd;
        String name = edName.getText().toString();
        String gender = "0";
        if (r1.isChecked()) {
            gender = "1";
        } else if (r2.isChecked()) {
            gender = "0";
        }
        String email = edMail.getText().toString().trim();
        String birthday = edBirthday.getText().toString().trim();
        String phone = edHomenumber.getText().toString();
        if(birthday.isEmpty()||birthday.equals("null"))
        {
            requireActivity().runOnUiThread(()->{
                showDialog("", "請輸入生日", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            });
            return;
        }
        ApiConUtils.member_edit(ApiUrl.API_URL, ApiUrl.user_edit, account, pwd, name, gender, email, birthday, phone, new ApiConUtils.OnConnect() {
            @Override
            public void onSuccess(String jsonString) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("eee",jsonString);
                        if (jsonString.equals("0x0200")) {
                            member_information();
                            Log.d(TAG, " success" + jsonString);
                            CustomDaialog.showNormal(requireActivity(), "", "資料修改成功", "確定", new CustomDaialog.OnBtnClickListener() {
                                @Override
                                public void onCheck() {

                                }

                                @Override
                                public void onCancel() {

                                        fragmentListener.onAction(FUNC_LOGIN_TO_ACCOUNT_MAIN, null);
                                    CustomDaialog.closeDialog();

                                }
                            });
                            return;
//                            DialogUtils.showMyDialog(rootView, 0, R.string.member_data_true, "", R.string.button_confirm, new DialogUtils.OneButtonClickListener() {
//                                @Override
//                                public void onButton1Clicked() {
//                                    fragmentListener.onAction(FUNC_ACCOUNT_MAIN_TO_LOGIN, null);
//
//                                }
//                            });
                        } else {
                            Log.d(TAG, " fail" + jsonString);
                            CustomDaialog.showNormal(requireActivity(), "", "資料修改失敗", "確定", new CustomDaialog.OnBtnClickListener() {
                                @Override
                                public void onCheck() {

                                }

                                @Override
                                public void onCancel() {

//                                        fragmentListener.onAction(FUNC_ACCOUNT_MAIN_TO_LOGIN, null);
                                    CustomDaialog.closeDialog();

                                }
                            });
                            return;
//                            DialogUtils.showMyDialog(rootView, 0, R.string.member_data_false, "", R.string.button_confirm, new DialogUtils.OneButtonClickListener() {
//                                @Override
//                                public void onButton1Clicked() {
//
//                                }
//                            });
                        }
                    }
                });

            }

            @Override
            public void onFailure(String message) {
                Log.d("member_usercode", " fail" + message);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CustomDaialog.showNormal(requireActivity(), "", "連線失敗", "確定", new CustomDaialog.OnBtnClickListener() {
                            @Override
                            public void onCheck() {

                            }

                            @Override
                            public void onCancel() {

//                                        fragmentListener.onAction(FUNC_ACCOUNT_MAIN_TO_LOGIN, null);
                                CustomDaialog.closeDialog();

                            }
                        });
                        return;
//                        DialogUtils.showMyDialog(rootView, 0, R.string.member_register_false, "", R.string.button_confirm, new DialogUtils.OneButtonClickListener() {
//                            @Override
//                            public void onButton1Clicked() {
//
//                            }
//                        });
                    }
                });

            }
        });
    }
    private AlertDialog dialog = null;

    private void showDialog(String title, String message, DialogInterface.OnClickListener listener) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = new AlertDialog.Builder(requireContext()).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "確認", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onClick(dialog, which);
            }
        });
        dialog.show();
    }
}