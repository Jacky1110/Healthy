package com.jotangi.healthy.ui.home;

import static com.jotangi.healthy.ui.account.AccountLoginFragment.getpaymenturl;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.jotangi.healthy.HpayMall.ApiUrl;
import com.jotangi.healthy.HpayMall.MemberBean;
import com.jotangi.healthy.R;
import com.jotangi.healthy.ui.ProjConstraintFragment;
import com.jotangi.jotangi2021.AndroidModule.Api.ApiConUtils;

import org.json.JSONException;

import java.net.URISyntaxException;

/**
 * ♡♡♡♡♡♡♡♡♡♡♡Kelly 's Note♡♡♡♡♡♡´ސު｀
 * ====================================================
 */
public class HomeWebPayFragment extends ProjConstraintFragment {
    WebView webView;

    public static HomeWebPayFragment newInstance() {
        HomeWebPayFragment fragment = new HomeWebPayFragment();
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        activityTitleRid = R.string.buy_car1;

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        rootView = (ConstraintLayout) inflater.inflate(R.layout.fragment_home_pay_webview, container, false);
        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();
        webView = rootView.findViewById(R.id.webview);
        if (MemberBean.payment_url != null) {
            loadWebView();
            Log.d(TAG, "aaaaaa: " + "bbbbbbb");
        } else {
//            loadWebViewOut();
            getpaymenturl();
            Log.d(TAG, "aaaaaa: " + "aaaaaa");
        }


    }

    private void loadWebViewOut() {

    }

    private void loadWebView() {
        if (MemberBean.payUrl.contains("medicalec")) {

            String WEB_SCAN_TO_PAY_URL = ApiUrl.homePay + "?sid=" + MemberBean.barcode_id + "&mid=" + MemberBean.mid;
            initWebview();
            webView.loadUrl(WEB_SCAN_TO_PAY_URL);
            MemberBean.channel_id = 3;
            Log.d(TAG, "payment_url: " + MemberBean.payment_url);
            Log.d(TAG, "payUrl: " + MemberBean.payUrl);
            Log.d(TAG, "WEB?????" + WEB_SCAN_TO_PAY_URL);

        } else {
            String WEB_SCAN_TO_PAY_URL = MemberBean.payUrl + "sid=" + MemberBean.barcode_id;
            initWebview();
//            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(WEB_SCAN_TO_PAY_URL));
//            browserIntent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//            startActivity(browserIntent);
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(requireContext(), Uri.parse(WEB_SCAN_TO_PAY_URL));
//            webView.loadUrl(WEB_SCAN_TO_PAY_URL);
            Log.d(TAG, "payUrl: " + MemberBean.payUrl);
            Log.d(TAG, "WEB?????" + WEB_SCAN_TO_PAY_URL);
        }
    }

    public void getpaymenturl() {
        String account = MemberBean.member_id;
        String pwd = MemberBean.member_pwd;
        ApiConUtils.payment_url(ApiUrl.API_URL, ApiUrl.get_payment_url, account, pwd, new ApiConUtils.OnConnect() {
            @Override
            public void onSuccess(String jsonString) throws JSONException {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(" MemberBean.payment_url", " 三小?" + jsonString);
                        MemberBean.payment_url = jsonString;
                        Log.d(TAG, "onSuccess: ");
                        loadWebView();
                    }
                });
            }

            @Override
            public void onFailure(String message) {
                Log.d("member_usercode", " fail" + message);

            }
        });
    }

    private void initWebview() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setLoadWithOverviewMode(true);

        webView.getSettings().setSaveFormData(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);

        webView.setWebChromeClient(new WebChromeClient() {
            /*
             *
             *  ♡♡♡♡♡♡♡♡♡♡♡Kelly 's Note♡♡♡♡♡♡´ސު｀
             *
             *  這是用來回調gps的實際上並不需要
             * */
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                // callback.invoke(String origin, boolean allow, boolean remember);
                callback.invoke(origin, true, false);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

            }

            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Log.e("Lewis", " granting");
                    request.grant(request.getResources());
                    Log.e("Lewis", " granted");
                } else {
                    Log.e("Lewis", "no permission");
                }

            }// onPermissionRequest

            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                //return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
                WebView newWebView = new WebView(view.getContext());
                newWebView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        //startActivity(browserIntent);
                        //return true;
                        return processIntentScheme(view, url);
                    }

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                        //return super.shouldOverrideUrlLoading(view, request);
                        String url = request.getUrl().toString();
                        return processIntentScheme(view, url);
                    }
                });
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(newWebView);
                resultMsg.sendToTarget();
                return true;
            }
        });


    }

    private boolean processIntentScheme(WebView view, String url) {
        Log.d(TAG, "processIntentScheme(), url= " + url);
        if (url != null) {
            if (url.startsWith("http://") || url.startsWith("https://")) {
                view.loadUrl(url);
            } else if (url.startsWith("line://")) {
                view.getContext().startActivity(
                        new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            } else if (url.startsWith("intent://")) {
                try {
                    Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                    Intent existPackage = view.getContext().getPackageManager().getLaunchIntentForPackage(intent.getPackage());
                    if (existPackage != null) {
                        startActivity(intent);
                    } else {
                        Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                        marketIntent.setData(Uri.parse("market://details?id=" + intent.getPackage()));
                        startActivity(marketIntent);
                    }
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (url.startsWith("market://")) {
                try {
                    Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                    if (intent != null) {
                        startActivity(intent);
                    }
                    return true;
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            } else { // unhandled url scheme
                view.getContext().startActivity(
                        new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
            return true;
        } else {
            return false;
        }
    }

    //fixme 2021  10  28  kelly add
    private void geturlContext() {
    }
}