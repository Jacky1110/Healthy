package com.jotangi.healthy;

import static com.jotangi.healthy.ui.account.AccountLoginFragment.getpaymenturl;
import static com.jotangi.healthy.ui.account.AccountLoginFragment.member_information;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.jotangi.healthy.HpayMall.ApiUrl;
import com.jotangi.healthy.HpayMall.CustomDaialog;
import com.jotangi.healthy.HpayMall.DymaticTabFragment;
import com.jotangi.healthy.HpayMall.InformationSeFragment;
import com.jotangi.healthy.HpayMall.Mall.CDetailFragment;
import com.jotangi.healthy.HpayMall.Mall.PayDataFragment;
import com.jotangi.healthy.HpayMall.Mall.SCartFragmemt;
import com.jotangi.healthy.HpayMall.Mall.ShopTermsFragment;
import com.jotangi.healthy.HpayMall.MemberBean;
import com.jotangi.healthy.googlevision.CameraXPreviewFragment;
import com.jotangi.healthy.ui.MyBaseFragment;
import com.jotangi.healthy.ui.ProjBaseFragment;
import com.jotangi.healthy.ui.account.AccountCouponDetailFragment;
import com.jotangi.healthy.ui.account.AccountCouponIndexFragment;
import com.jotangi.healthy.ui.account.AccountCouponMainFragment;
import com.jotangi.healthy.ui.account.AccountDataFragment;
import com.jotangi.healthy.ui.account.AccountForgetPasswordFragment;
import com.jotangi.healthy.ui.account.AccountInviteFriendFragment;
import com.jotangi.healthy.ui.account.AccountLoginFragment;
import com.jotangi.healthy.ui.account.AccountMainFragment;
import com.jotangi.healthy.ui.account.AccountMallRecordFragment;
import com.jotangi.healthy.ui.account.AccountPointFragment;
import com.jotangi.healthy.ui.account.AccountQAFragment;
import com.jotangi.healthy.ui.account.AccountRecommendFragment;
import com.jotangi.healthy.ui.account.AccountRegisterFragment;
import com.jotangi.healthy.ui.account.AccountRuleFragment;
import com.jotangi.healthy.ui.account.AccountTradeMainFragment;
import com.jotangi.healthy.ui.account.AccountTradeRecordFragment;
import com.jotangi.healthy.ui.account.MallOrderDetailFragment;
import com.jotangi.healthy.ui.account.MallPayFragment;
import com.jotangi.healthy.ui.home.HomeMainFragment;
import com.jotangi.healthy.ui.home.HomeWebPayFragment;
import com.jotangi.healthy.utils.CropHeadImageActivity;
import com.jotangi.healthy.utils.ProjSharePreference;
import com.jotangi.jotangi2021.AndroidModule.Api.ApiConUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * ♡♡♡♡♡♡♡♡♡♡♡Kelly 's Note♡♡♡♡♡♡´ސު｀
 * ====================================================
 * <p>
 * 基本上fragment 都繼承了projconstaintfragment的那個來去切換
 * Sean寫的函數可能要去看，都是用switch 巴拉巴拉切換
 */
public class MainActivity extends AppCompatActivity implements MyBaseFragment.FragmentListener {
    protected final String TAG = this.getClass().getSimpleName();
    private static final int PERMISSION_REQUESTS = 1;

    private MyBaseFragment currFragment;
    private int mainFuncNo;

    private ImageButton backButton, btncart;
    private TextView toolbarTitleView;
    private RadioGroup bottomBar;
    private RadioButton rbHome;
    private RadioButton rbMall;
    private RadioButton rbAccount;
    private android.app.AlertDialog dialog = null;

    private int REQUEST_CODE = -1;
    private Boolean isLogin = false;
    //private ActivityMainBinding binding;
    private String appUrl = "com.jotangi.healthy";

    private final CompoundButton.OnCheckedChangeListener rbHomeCheckChangedListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Log.d(TAG, "rbHome.onCheckedChanged(), " + (isChecked ? "checked" : "unchecked"));
            if (isChecked) {
                switchToHomeMainFragment(ProjBaseFragment.FUNC_MAIN_TO_HOME, null);
            }
        }
    };

    private final CompoundButton.OnCheckedChangeListener rbMallCheckChangedListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Log.d(TAG, "rbMall.onCheckedChanged(), " + (isChecked ? "checked" : "unchecked"));
            if (isChecked) {
                //切換到商城
                switchToMall(ProjBaseFragment.FUNC_MAIN_TO_MALL, null);
            }
        }
    };
    private final CompoundButton.OnCheckedChangeListener rbAccountCheckChangedListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Log.d(TAG, "rbAccount.onCheckedChanged(), " + (isChecked ? "checked" : "unchecked"));
            if (isChecked) {
//                if (isLogin) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_host_fragment_activity_main, AccountMainFragment.newInstance());
                    transaction.addToBackStack(AccountMainFragment.class.getSimpleName());
                    transaction.commit();
//                } else {
//                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                    transaction.replace(R.id.nav_host_fragment_activity_main, AccountLoginFragment.newInstance());
//                    transaction.commit();
//                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

//        checkInAppUpdate();

        //binding = ActivityMainBinding.inflate(getLayoutInflater());
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        //setContentView(binding.getRoot());
        setContentView(R.layout.activity_main);

        if (!allPermissionsGranted()) {
            getRuntimePermissions();
        }

        isLogin = ProjSharePreference.getLoginState(this);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        Log.d(TAG, "width=" + dm.widthPixels);
        Log.d(TAG, "height=" + dm.heightPixels);
        Log.d(TAG, "xdpi=" + dm.xdpi);
        Log.d(TAG, "ydpi=" + dm.ydpi);
        Log.d(TAG, "density=" + dm.density);
        Log.d(TAG, "densitydpi=" + dm.densityDpi);
        Log.d(TAG, "scaledensity=" + dm.scaledDensity);

        toolbarTitleView = findViewById(R.id.tv_toolbar_title);
        btncart = findViewById(R.id.btn_car);
        btncart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (isLogin) {
                if (MemberBean.member_id != null && MemberBean.member_id.length() == 10) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_host_fragment_activity_main, SCartFragmemt.Companion.newInstance());
                    transaction.commit();
                } else {
                    Toast.makeText(
                            MainActivity.this,
                            "請登入帳號",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
        backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        rbHome = findViewById(R.id.bn_home);
        rbMall = findViewById(R.id.bn_mall);
        rbAccount = findViewById(R.id.bn_account);

        addBottomBarListeners();

        bottomBar = findViewById(R.id.rg_bottombar);
        bottomBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d(TAG, "bottombar::onCheckedChanged(), checkedID=" + checkedId);
            }
        });

        displayIntentData();

    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == REQUEST_CODE) {
//            if (resultCode != RESULT_OK) {
//                showPopDialog();
//            }
//        }
//    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent");
        setIntent(intent);
        displayIntentData();
    }

    private void checkLoginStateToEnableBottomBar() {
//        boolean enabled = ProjSharePreference.getLoginState(MainActivity.this);
//        bottomBar.setEnabled(enabled);
        bottomBar.setEnabled(true);
        findViewById(R.id.bn_account).setEnabled(true);
        findViewById(R.id.bn_mall).setEnabled(true);
        findViewById(R.id.bn_home).setEnabled(true);
    }

    private void savaTermsStatus(boolean status, String code) {
        SharedPreferences pref = getSharedPreferences("UserTerms", MODE_PRIVATE);
        pref.edit()
                .putBoolean("Terms", status)
                .putString("code", code)
                .commit();
    }

    private void displayIntentData() {
//        boolean islogin = ProjSharePreference.getLoginState(this);

        SharedPreferences isGetLogin = getSharedPreferences("loginInfo", MODE_PRIVATE);
        boolean loginresult = isGetLogin.getBoolean("isLogin", false);
        if (loginresult) {
            MemberBean.member_id = isGetLogin.getString("account", "");
            MemberBean.member_pwd = isGetLogin.getString("password", "");
            member_information();
            getpaymenturl();
        }

        switchToHomeMainFragment(ProjBaseFragment.FUNC_MAIN_TO_HOME, null);
        updateBottomBarByFragmentTag(HomeMainFragment.class.getSimpleName());

        Log.d(TAG, "islogin:" + (isLogin ? "true" : "false"));
        if (isLogin && loginresult) {
            Intent intent = getIntent();
            Log.d(TAG, "displayIntentData(), intent=" + intent.toString());
            String action = intent.getAction();
            Log.d(TAG, "displayIntentData(), action=" + action);
            Uri data = intent.getData();
            Log.d(TAG, "displayIntentData(), data=" + data);
            String strdata = intent.getDataString();
            Log.d(TAG, "displayIntentData(), strdata=" + strdata);
            if (data != null) {
                String scheme = data.getScheme(); // "http"
                Log.d(TAG, "displayIntentData(), scheme=" + scheme);
                String host = data.getHost(); // "twitter.com"
                Log.d(TAG, "displayIntentData(), host=" + host);
                List<String> params = data.getPathSegments();
                if (params != null) {
                    for (int i = 0; i < params.size(); i++) {
                        Log.d(TAG, "displayIntentData(), p" + i + "=" + params.get(i));
                    }
                }

                String projScheme = getString(R.string.proj_scheme);
                String projHost = getString(R.string.proj_scheme_host);
                String schemeHome = getString(R.string.proj_scheme_home_main);
                String schemeTrade = getString(R.string.proj_scheme_account_trade);

                if (projScheme.equals(scheme) && projHost.equals(host)) {
                    if (params.size() > 0) {
                        if (schemeTrade.equals(params.get(0))) {
                            Log.d(TAG, "displayIntentData(), islogin=true, switch to trade");
                            switchToAccountTradeMainFragment(ProjBaseFragment.FUNC_MAIN_TO_ACCOUNT_TRADE, null);
                            updateBottomBarByFragmentTag(AccountTradeMainFragment.class.getSimpleName());
                            return;
                        } else if (schemeHome.equals(params.get(0))) {
                            //switchToHomeMainFragment(ProjBaseFragment.FUNC_MAIN_TO_HOME, null);
                            //return;
                        }
                    }
                }
            }

            Log.d(TAG, "displayIntentData(), islogin=true, switch to home");
            member_information();
            checkCart();

//            String id = MemberBean.member_id;
//            String pwd = MemberBean.member_pwd;
//            if (id != null && pwd != null) {
//                switchToHomeMainFragment(ProjBaseFragment.FUNC_MAIN_TO_HOME, null);
//                updateBottomBarByFragmentTag(HomeMainFragment.class.getSimpleName());
//            } else {
//                Log.d(TAG, "displayIntentData(), islogin=false, switch to login");
//                switchToAccountLoginFragment(ProjBaseFragment.FUNC_MAIN_TO_ACCOUNT, null);
//                updateBottomBarByFragmentTag(AccountLoginFragment.class.getSimpleName());
//            }
        }
//        else {
//            Log.d(TAG, "displayIntentData(), islogin=false, switch to login");
//            switchToAccountLoginFragment(ProjBaseFragment.FUNC_MAIN_TO_ACCOUNT, null);
//            updateBottomBarByFragmentTag(AccountLoginFragment.class.getSimpleName());
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        getAppVersion();
        checkLoginStateToEnableBottomBar();
    }

    private void getAppVersion() {
        ApiConUtils.getAppVersion(
                ApiUrl.API_URL,
                ApiUrl.app_version,
                new ApiConUtils.OnConnect() {
                    @Override
                    public void onSuccess(String jsonString) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            public void run() {
                                try {
                                    JSONArray jsonArray = new JSONArray(jsonString);

                                    if (jsonArray.length() > 0) {
                                        JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                                        String appServerVer = jsonObject.getString("android_version");
                                        SharedPreferences pref = getSharedPreferences(
                                                AppConfig.KEY_APP_INFO,
                                                MODE_PRIVATE
                                        );

                                        pref.edit()
                                                .putString(
                                                        AppConfig.KEY_APP_VERSION,
                                                        appServerVer)
                                                .commit();

                                        checkInAppUpdate();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(String message) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("登入", "成功" + message);
                                CustomDaialog.showNormal(
                                        MainActivity.this,
                                        "",
                                        "登入失敗",
                                        "確定",
                                        new CustomDaialog.OnBtnClickListener() {
                                            @Override
                                            public void onCheck() {
                                            }

                                            @Override
                                            public void onCancel() {
                                                CustomDaialog.closeDialog();
                                            }
                                        });
                                return;
                            }
                        });
                    }
                });
    }

    private void checkInAppUpdate() {
        try {
            PackageInfo packageInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            String curVersionStr = packageInfo.versionName;
            float curVersionFloat = Float.parseFloat(curVersionStr);

            SharedPreferences pref = getSharedPreferences(
                    AppConfig.KEY_APP_INFO,
                    MODE_PRIVATE
            );

            String serverVersionStr = pref.getString(AppConfig.KEY_APP_VERSION, "");
            if (!serverVersionStr.isEmpty()) {
                float serverVersionFloat = Float.parseFloat(serverVersionStr);
                if (serverVersionFloat > curVersionFloat) {
                    showPopDialog();
                }
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showPopDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提醒")
                .setMessage("偵測到有新版本！未更新將無法繼續使用並會結束本 App！")
                .setPositiveButton(
                        "結束 App",
                        (dialog, which) ->
                                finish()
                )
                .setNegativeButton(
                        "繼續更新",
                        (dialog, which) ->
                                startUpdate()
                )
                .show();
    }

    private void startUpdate() {
        Intent mIntent = new Intent(Intent.ACTION_VIEW);

        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.setData(Uri.parse("market://details?id=" + appUrl));
        this.startActivity(mIntent);
    }

    private void updateBackButton() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int fco = getSupportFragmentManager().getBackStackEntryCount();
//        if (fco == 0) { return; }
                String tag = getSupportFragmentManager().getBackStackEntryAt(fco - 1).getName();
                if (fco > 1 && !Objects.equals(tag, AccountLoginFragment.class.getSimpleName())) {
                    backButton.setVisibility(View.INVISIBLE);
                } else if (tag.equals("")) {
                    backButton.setVisibility(View.INVISIBLE);
                } else {
                    backButton.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void updateCar() {
        MainActivity.this.runOnUiThread(() -> {
            try {
                Log.d("圖標", "MemberBean.btnCart " + MemberBean.btnCart);
                if (MemberBean.btnCart) {
                    btncart.setBackgroundResource(R.drawable.ic_shopping_cart0);
                } else {
                    btncart.setBackgroundResource(R.drawable.ic_shopping_cart);
                }
            } catch (Exception e) {

            }

        });

    }

    @Override
    public void onBackPressed() {
        int fco1 = getSupportFragmentManager().getBackStackEntryCount();
        String tag2 = getSupportFragmentManager().getBackStackEntryAt(fco1 - 1).getName();

        if (fco1 == 1 || Objects.equals(tag2, AccountLoginFragment.class.getSimpleName())) {
            showDialog("提示", "確認要離開健康配嗎?", (dialog, which) -> dialog.dismiss());
//                        Toast.makeText(StoreManager.this, message, Toast.LENGTH_SHORT).show();


//            new AlertDialog.Builder(this).setTitle("提示")
//                    .setIconAttribute(android.R.attr.alertDialogIcon)
//                    .setMessage("確認要離開健康配嗎? ")
//                    .setPositiveButton("確定", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            finishAffinity();
//                        }
//                    })
//                    .setNegativeButton("取消", null)
//                    .create().show();
            return;
        }

        super.onBackPressed();
        int fco = getSupportFragmentManager().getBackStackEntryCount();
        String tag = getSupportFragmentManager().getBackStackEntryAt(fco - 1).getName();

        if (fco == 0) {
            finishAffinity();
            return;
        } else if (tag.equals("")) {
            updateBackButton();

        } else {
            updateBackButton();
        }

        Log.d(TAG, "onBackPressed(), stack count=" + fco + "\n tag=\n" + tag);
        updateBottomBarByFragmentTag(tag);
    }

    private void showDialog(String title, String message, DialogInterface.OnClickListener listener) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = new android.app.AlertDialog.Builder(this).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setButton(android.app.AlertDialog.BUTTON_NEUTRAL, "確認", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onClick(dialog, which);
                finishAffinity();
            }
        });
        dialog.show();
    }

    private final String[] FRAGMENT_MALL_GROUP_ARRAY = {
//            TabViewFragment
            //DymaticTabFragment
            InformationSeFragment
                    .class.getSimpleName(),
            CDetailFragment.class.getSimpleName()

    };
    private final String[] FRAGMENT_HOME_GROUP_ARRAY = {
            HomeMainFragment.class.getSimpleName(),
            CameraXPreviewFragment.class.getSimpleName(),
            HomeWebPayFragment.class.getSimpleName()
    };
    private final String[] FRAGMENT_ACCOUNT_GROUP_ARRAY = {
            AccountDataFragment.class.getSimpleName(),
            AccountForgetPasswordFragment.class.getSimpleName(),
            AccountInviteFriendFragment.class.getSimpleName(),
            AccountMainFragment.class.getSimpleName(),
            AccountLoginFragment.class.getSimpleName(),
            AccountPointFragment.class.getSimpleName(),
            AccountQAFragment.class.getSimpleName(),
            AccountRecommendFragment.class.getSimpleName(),
            AccountRegisterFragment.class.getSimpleName(),
            AccountRuleFragment.class.getSimpleName(),
            AccountTradeMainFragment.class.getSimpleName(),
            AccountTradeRecordFragment.class.getSimpleName()
    };

    private void updateBottomBarByFragmentTag(String tag) {
        int rgcheckedid = bottomBar.getCheckedRadioButtonId();
        if (Arrays.asList(FRAGMENT_HOME_GROUP_ARRAY).contains(tag)) {
            if (rgcheckedid != R.id.bn_home) {
                removeBottomBarListeners();
                bottomBar.check(R.id.bn_home);
                addBottomBarListeners();
            }
        } else if (Arrays.asList(FRAGMENT_ACCOUNT_GROUP_ARRAY).contains(tag)) {
            if (rgcheckedid != R.id.bn_account) {
                removeBottomBarListeners();
                bottomBar.check(R.id.bn_account);
                addBottomBarListeners();
            }
        } else if (Arrays.asList(FRAGMENT_MALL_GROUP_ARRAY).contains(tag)) {
            if (rgcheckedid != R.id.bn_mall) {
                removeBottomBarListeners();
                bottomBar.check(R.id.bn_mall);
                addBottomBarListeners();
            }
        }
    }

    private void removeBottomBarListeners() {
        rbHome.setOnCheckedChangeListener(null);
        rbMall.setOnCheckedChangeListener(null);
        rbAccount.setOnCheckedChangeListener(null);
    }

    private void addBottomBarListeners() {
        rbHome.setOnCheckedChangeListener(rbHomeCheckChangedListener);
        rbMall.setOnCheckedChangeListener(rbMallCheckChangedListener);
        rbAccount.setOnCheckedChangeListener(rbAccountCheckChangedListener);
    }

    private void updateActivityTitle(Integer rid) {
        toolbarTitleView.setText(rid);
    }

    @Override
    public void onAction(int funcno, Object data) {
        switch (funcno) {
            case ProjBaseFragment.FUNC_FRAGMENT_RESUME:

                updateActivityTitle(Integer.valueOf(data.toString()));
                updateBackButton();

                break;
            case ProjBaseFragment.FUNC_FRAGMENT_change:

                updateCar();

                break;
            case ProjBaseFragment.FUNC_HOME_TO_SCAN:
                switchToHomeScanFragment(funcno, data);
                break;
            case ProjBaseFragment.FUNC_SCAN_TO_WEBPAY:
                switchToHomeWebPayFragment(funcno, data);
                break;
            case ProjBaseFragment.FUNC_LOGIN_TO_FORGET_PASSWORD:
                switchToAccountForgetFragment(funcno, data);
                break;
            case ProjBaseFragment.FUNC_LOGIN_TO_REGISTER:
                switchToAccountRegisterFragment(funcno, data);
                break;
            case ProjBaseFragment.FUNC_LOGIN_TO_RULE:
                switchToAccountRuleFragment(funcno, data);
                break;
            case ProjBaseFragment.FUNC_LOGIN_TO_ACCOUNT_MAIN: {
                checkLoginStateToEnableBottomBar();
                switchToAccountMainFragment(funcno, data);
                break;
            }
            case ProjBaseFragment.FUNC_MAIN_TO_MALL: {
                switchToMall(funcno, data);
                break;
            }
            case ProjBaseFragment.FUNC_MallDetal: {
                switchToMallDetail(funcno, data);
                break;
            }
            case ProjBaseFragment.FUNC_REGISTER_TO_RULE:
                switchToAccountRuleFragment(funcno, data);
                break;
            case ProjBaseFragment.FUNC_ACCOUNT_MAIN_TO_LOGIN: {
                checkLoginStateToEnableBottomBar();
                switchToAccountLoginFragment(funcno, data);
                break;
            }
            case ProjBaseFragment.FUNC_ACCOUNT_MAIN_TO_DATA:
                switchToAccountDataFragment(funcno, data);
                break;
            case ProjBaseFragment.FUNC_ACCOUNT_MAIN_TO_TRADE_MAIN:
                switchToAccountTradeMainFragment(funcno, data);
                break;
            case ProjBaseFragment.FUNC_ACCOUNT_MAIN_TO_POINT:
                switchToAccountPointFragment(funcno, data);
                break;
            case ProjBaseFragment.FUNC_ACCOUNT_MAIN_TO_USERRULE:
                switchToAccountRuleFragment(funcno, data);
                break;
            case ProjBaseFragment.FUNC_ACCOUNT_MAIN_TO_COUPON_INDEX:
                switchToCouponIndex(funcno, data);
                break;
            case ProjBaseFragment.FUNC_ACCOUNT_COUPON_TO_COUPON_MAIN:
                switchToCouponMain(funcno, data);
                break;
            case ProjBaseFragment.FUNC_ACCOUNT_MAIN_TO_RECOMMEND:
                switchToAccountRecommendFragment(funcno, data);
                break;
            case ProjBaseFragment.FUNC_ACCOUNT_MAIN_TO_QA:
                switchToAccountQAFragment(funcno, data);
                break;
            case ProjBaseFragment.FUNC_ACCOUNT_MAIN_USER_HEAD_CLICKED:
                getUserHeadImage();
                break;
            case ProjBaseFragment.FUNC_ACCOUNT_RECOMMEND_TO_FRIENDS:
                switchToAccountInviteFriendsFragment(funcno, data);
                break;
            case ProjBaseFragment.FUNC_ACCOUNT_RECOMMEND_TO_RULE:
                switchToAccountRuleFragment(funcno, data);
                break;
            case ProjBaseFragment.FUNC_ACCOUNT_TRADE_TO_RECORD:
                switchToAccountTradeRecordFragment(funcno, data);
                break;

            case ProjBaseFragment.FUNC_ACCOUNT_TRADE_TO_ORDER:
                switchToAccountTradeOrderFragment(funcno, data);
                break;
            case ProjBaseFragment.FUNC_ACCOUNT_COUPON_TO_COUPON_DETAIL:
                switchToCouponDetailFragment(funcno, data);
                break;
            case ProjBaseFragment.fraProductDetail:
                fraProDe(funcno, data);
                break;
            case ProjBaseFragment.fraPayData:
                fraPayData(funcno, data);
                break;
            case ProjBaseFragment.fraShoppingCart:
                fraSpCar(funcno, data);
                break;
            case ProjBaseFragment.fraDymaticTab:
                fraDyTab(funcno, data);
                break;
            case ProjBaseFragment.fraInfoToCustom:
                fraInfo(funcno, data);
                break;
            case ProjBaseFragment.fraMallDetail:
                fraMallOderDetail(funcno, data);
                break;
            case ProjBaseFragment.fraMallRecord:
                fraMallRecord(funcno, data);
                break;
            case ProjBaseFragment.fraPay:
                fraMallPay(funcno, data);
                break;

        }
    }


    private void switchToMall(int funcno, Object data) {
        //切換到商城
        DymaticTabFragment fragment = DymaticTabFragment.Companion.newInstance();
        //DymaticTabFragment fragment = DymaticTabFragment.Companion.newInstance();
        switchFragment(fragment, funcno);
    }

    private void switchToMallDetail(int funcno, Object data) {
        CDetailFragment fragment = CDetailFragment.Companion.newInstance();
        switchFragment(fragment, funcno);
    }

    private void switchToAccountMainFragment(int funcno, Object data) {
        AccountMainFragment fragment = AccountMainFragment.newInstance();
        switchFragment(fragment, funcno);
    }

    private void switchToAccountLoginFragment(int funcno, Object data) {
        AccountLoginFragment fragment = AccountLoginFragment.newInstance();
        switchFragment(fragment, funcno);
    }

    private void switchToAccountForgetFragment(int funcno, Object data) {
        AccountForgetPasswordFragment fragment = AccountForgetPasswordFragment.newInstance();
        switchFragment(fragment, funcno);
    }

    private void switchToAccountRegisterFragment(int funcno, Object data) {
        AccountRegisterFragment fragment = AccountRegisterFragment.newInstance();
        switchFragment(fragment, funcno);
    }

    private void switchToCouponIndex(int funcno, Object data) {
        AccountCouponIndexFragment fragment = AccountCouponIndexFragment.newInstance();
        switchFragment(fragment, funcno);
    }

    private void switchToCouponMain(int funcno, Object data) {
        AccountCouponMainFragment fragment = AccountCouponMainFragment.newInstance();
        switchFragment(fragment, funcno);
    }

    private void switchToAccountRuleFragment(int funcno, Object data) {
        AccountRuleFragment fragment = AccountRuleFragment.newInstance();
        switchFragment(fragment, funcno);
    }

    private void switchToAccountDataFragment(int funcno, Object data) {
        AccountDataFragment fragment = AccountDataFragment.newInstance();
        switchFragment(fragment, funcno);
    }

    private void switchToAccountTradeMainFragment(int funcno, Object data) {
        AccountTradeMainFragment fragment = AccountTradeMainFragment.newInstance();
        switchFragment(fragment, funcno);
    }

    private void switchToAccountTradeOrderFragment(int funcno, Object data) {
        AccountMallRecordFragment fragment = AccountMallRecordFragment.Companion.newInstance();
        switchFragment(fragment, funcno);
    }

    private void switchToAccountTradeRecordFragment(int funcno, Object data) {
        AccountTradeRecordFragment fragment = AccountTradeRecordFragment.newInstance();
        switchFragment(fragment, funcno);
    }

    private void switchToAccountPointFragment(int funcno, Object data) {
        AccountPointFragment fragment = AccountPointFragment.newInstance();
        switchFragment(fragment, funcno);
    }

    private void switchToAccountQAFragment(int funcno, Object data) {
        InformationQAFragment fragment = InformationQAFragment.newInstance();
        switchFragment(fragment, funcno);
    }

    private void switchToAccountInviteFriendsFragment(int funcno, Object data) {
        AccountInviteFriendFragment fragment = AccountInviteFriendFragment.newInstance();
        switchFragment(fragment, funcno);
    }

    private void switchToHomeMainFragment(int funcno, Object data) {

        HomeMainFragment fragment = HomeMainFragment.newInstance();
        switchFragment(fragment, funcno);
        checkCart();
    }

    private void switchToHomeScanFragment(int funcno, Object data) {
        CameraXPreviewFragment fragment = CameraXPreviewFragment.newInstance();
        switchFragment(fragment, funcno);
    }

    private void switchToHomeWebPayFragment(int funcno, Object data) {
        HomeWebPayFragment fragment = HomeWebPayFragment.newInstance();
        switchFragment(fragment, funcno);
    }

    private void switchToAccountRecommendFragment(int funcno, Object data) {
        AccountRecommendFragment fragment = AccountRecommendFragment.newInstance();
        switchFragment(fragment, funcno);
    }

    private void switchToCouponDetailFragment(int funcno, Object data) {
        AccountCouponDetailFragment fragment = AccountCouponDetailFragment.newInstance();
        switchFragment(fragment, funcno);
    }

    //product detail fragment
    private void fraProDe(int funcno, Object data) {
        CDetailFragment fragment = CDetailFragment.Companion.newInstance();
        switchFragment(fragment, funcno);
    }

    private void fraPayData(int funcno, Object data) {
        PayDataFragment fragment = PayDataFragment.Companion.newInstance();
        switchFragment(fragment, funcno);
    }

    private void fraSpCar(int funcno, Object data) {
        SCartFragmemt fragment = SCartFragmemt.Companion.newInstance();
        switchFragment(fragment, funcno);
    }

    private void fraSTerms(int funcno, Object data) {
        ShopTermsFragment fragment = ShopTermsFragment.Companion.newInstance();
        switchFragment(fragment, funcno);
    }

    private void fraDyTab(int funcno, Object data) {
        DymaticTabFragment fragment = DymaticTabFragment.Companion.newInstance();
        switchFragment(fragment, funcno);
    }

    private void fraInfo(int funcno, Object data) {
        InformationSeFragment fragment = InformationSeFragment.Companion.newInstance();
        switchFragment(fragment, funcno);
    }

    private void fraMallOderDetail(int funcno, Object data) {
        MallOrderDetailFragment fragment = MallOrderDetailFragment.Companion.newInstance();
        switchFragment(fragment, funcno);
    }

    private void fraMallRecord(int funcno, Object data) {
        AccountMallRecordFragment fragment = AccountMallRecordFragment.Companion.newInstance();
        switchFragment(fragment, funcno);
    }

    private void fraMallPay(int funcno, Object data) {
        MallPayFragment fragment = MallPayFragment.Companion.newInstance();
        switchFragment(fragment, funcno);
    }

    private void switchFragment(MyBaseFragment fragment, int funcno) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_activity_main, fragment);
        transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();
        mainFuncNo = funcno;
        currFragment = fragment;

    }

    private void addFragment(MyBaseFragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.nav_host_fragment_activity_main, fragment);
        transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();
        currFragment = fragment;
    }

    private String[] getRequiredPermissions() {
        try {
            PackageInfo info =
                    this.getPackageManager()
                            .getPackageInfo(this.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] ps = info.requestedPermissions;
            if (ps != null && ps.length > 0) {
                return ps;
            } else {
                return new String[0];
            }
        } catch (Exception e) {
            return new String[0];
        }
    }

    private boolean allPermissionsGranted() {
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                return false;
            }
        }
        return true;
    }

    private void getRuntimePermissions() {
        List<String> allNeededPermissions = new ArrayList<>();
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                allNeededPermissions.add(permission);
            }
        }

        if (!allNeededPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this, allNeededPermissions.toArray(new String[0]), PERMISSION_REQUESTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        Log.i(TAG, "Permission granted!");
        if (allPermissionsGranted()) {
            //bindAllCameraUseCases();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean isPermissionGranted(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission granted: " + permission);
            return true;
        }
        Log.i(TAG, "Permission NOT granted: " + permission);
        return false;
    }

    ActivityResultLauncher<Intent> getUserHeadLuncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.d(TAG, "onActivityResult");
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String path = data.getStringExtra("path");
                            Bitmap bitmap = BitmapFactory.decodeFile(path);
                            if (bitmap != null) {

                            }
                        }
                    }
                }
            });

    private void getUserHeadImage() {
        Intent intent = new Intent(MainActivity.this, CropHeadImageActivity.class);
        getUserHeadLuncher.launch(intent);
    }

    private void checkCart() {
        //Check if the shopping cart has value
        String id = MemberBean.member_id;
        String pwd = MemberBean.member_pwd;
        if (id != null && pwd != null) {
            ApiConUtils.shoppingcart_count(ApiUrl.API_URL, ApiUrl.shoppingcart_count, id, pwd, new ApiConUtils.OnConnect() {
                @Override
                public void onSuccess(String jsonString) {

                    Log.e("fuck", "shoppingcart_count " + jsonString);
                    try {
                        JSONObject jsonObject = new JSONObject(jsonString);
                        String status = jsonObject.getString("status");
                        String code = jsonObject.getString("code");

                        String responseMessage = jsonObject.getString("responseMessage");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (code.equals("0x0200") && !responseMessage.equals("0")) {

                                    btncart.setBackgroundResource(R.drawable.ic_shopping_cart0);

                                } else {

                                    btncart.setBackgroundResource(R.drawable.ic_shopping_cart);

                                }
                            }
                        });
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            public void run() {

                            }
                        });

                    } catch (Exception e) {
                    }


                }

                @Override
                public void onFailure(String message) {

                }
            });
        }
    }
}