package xebia.ismail.firstblood.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.ResponseBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xebia.ismail.firstblood.R;
import xebia.ismail.firstblood.controller.BaseApiService;
import xebia.ismail.firstblood.controller.SharedPrefManager;
import xebia.ismail.firstblood.controller.UtilsApi;

/**
 * Created by Admin on 12/29/2017.
 */

public class LoginActivity extends AppCompatActivity {

    ImageView a,logo;
    ImageView b;
    TextView intro,introdes;
    LinearLayout login_btn;

    @BindView(R.id.ed_email)
    EditText edmail;
    @BindView(R.id.ed_pass)
    EditText edpass;

    ProgressDialog loading;

    Context mContext;
    BaseApiService mApiService;

    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        mContext = this;
        mApiService = UtilsApi.getAPIService(); // meng-init yang ada di package apihelper
        sharedPrefManager = new SharedPrefManager(this);

        if (sharedPrefManager.getSPSudahLogin()){
            startActivity(new Intent(LoginActivity.this, DashboardActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }

        //Belum di butterknife
        a = (ImageView)findViewById(R.id.acca);
        logo = (ImageView)findViewById(R.id.logo);
        b = (ImageView)findViewById(R.id.accb);
        intro = (TextView)findViewById(R.id.intro);
        login_btn = (LinearLayout)findViewById(R.id.login_btn);
        introdes = (TextView)findViewById(R.id.intro_des);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                requestLogin();
            }
        });
        StartAnimations();
    }

    private void requestLogin(){
        mApiService.loginRequest(edmail.getText().toString(), edpass.getText().toString())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")){

                                    // Jika login berhasil maka data nama yang ada di response API
                                    // akan diparsing ke activity selanjutnya.

                                    Toast.makeText(mContext, "BERHASIL LOGIN", Toast.LENGTH_SHORT).show();
                                    String nama = jsonRESULTS.getJSONObject("user").getString("nama");

                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_NAMA, nama);
                                    // Shared Pref ini berfungsi untuk menjadi trigger session login

                                    sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, true);
                                    startActivity(new Intent(mContext, DashboardActivity.class)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                    finish();

                                } else {
                                    // Jika login gagal
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        loading.dismiss();
                    }
                });
    }

    private void loadingDialog() {

        final View dialogView = View.inflate(this,R.layout.dialog_loading,null);

        final Dialog dialog = new Dialog(this,R.style.LoadingDialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);

        TextView imageView = (TextView) dialog.findViewById(R.id.closeDialogImg);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                revealShow(dialogView, false, dialog);
            }
        });

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                revealShow(dialogView, true, null);
            }
        });

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK){

                    revealShow(dialogView, false, dialog);
                    return true;
                }

                return false;
            }
        });



        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.show();

    }

    private void revealShow(View dialogView, boolean b, final Dialog dialog) {

        final View view = dialogView.findViewById(R.id.dialog);

        int w = view.getWidth();
        int h = view.getHeight();

        int endRadius = (int) Math.hypot(w, h);

        int cx = (int) (login_btn.getX() + (login_btn.getWidth()/2));
        int cy = (int) (login_btn.getY())+ login_btn.getHeight() + 630;


        if(b){
            Animator revealAnimator = ViewAnimationUtils.createCircularReveal(view, cx,cy, 0, endRadius);

            view.setVisibility(View.VISIBLE);
            revealAnimator.setDuration(1000);
            revealAnimator.setInterpolator(new LinearOutSlowInInterpolator());
            revealAnimator.start();

        } else {

            Animator anim =
                    ViewAnimationUtils.createCircularReveal(view, cx, cy, endRadius, 0);

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    dialog.dismiss();
                    view.setVisibility(View.INVISIBLE);

                }
            });
            anim.setDuration(400);
            anim.setInterpolator(new FastOutSlowInInterpolator());
            anim.start();
        }

    }

    private void StartAnimations() {
        Animation popeup1 = AnimationUtils.loadAnimation(this, R.anim.upup);
        Animation popeup2 = AnimationUtils.loadAnimation(this, R.anim.upup);
        Animation popeup3 = AnimationUtils.loadAnimation(this, R.anim.up);
        Animation popeup4 = AnimationUtils.loadAnimation(this, R.anim.up);
        Animation popeup5 = AnimationUtils.loadAnimation(this, R.anim.popeup);


        popeup1.setStartOffset(300);
        popeup2.setStartOffset(850);
        popeup3.setStartOffset(1000);
        popeup4.setStartOffset(1100);
        popeup5.setStartOffset(1200);

        a.startAnimation(popeup1);
        b.startAnimation(popeup2);
        logo.startAnimation(popeup5);
        intro.startAnimation(popeup3);
        introdes.startAnimation(popeup4);
    }
}
