package com.intern.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.google.android.gms.common.SignInButton;
import com.intern.myapplication.Model.CheckUserResponse;
import com.intern.myapplication.Model.User;
import com.intern.myapplication.Retrofit.IDrinkShopApi;
import com.intern.myapplication.Utils.Common;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.szagurskii.patternedtextwatcher.PatternedTextWatcher;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    Button btn_continue;
int REQUEST_CODE=1000;
IDrinkShopApi mServices;
    SliderLayout sliderLayout;
    HashMap<String,Integer> url_maps = new HashMap<String, Integer>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn_continue = findViewById(R.id.bcontinue);
        getSupportActionBar().hide();
        mServices = Common.getAPI();
        sliderLayout=findViewById(R.id.slider);
        url_maps.put("img1",R.drawable.imga);
        url_maps.put("img2",R.drawable.imgb);
        url_maps.put("img3",R.drawable.imgc);
        url_maps.put("img4",R.drawable.imgd);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoginPage(LoginType.PHONE);
            }
        });


        //Check login Session

      if (AccountKit.getCurrentAccessToken() != null) {
            final SpotsDialog alertDialog= new SpotsDialog(MainActivity.this,R.style.Custom);
            alertDialog.show();
            alertDialog.setMessage("Please Wait...");

            //Auto login
            AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                @Override
                public void onSuccess(final Account account) {


                    mServices.checkUserExists( account.getPhoneNumber().toString()).enqueue(new Callback<CheckUserResponse>() {
                        @Override
                        public void onResponse(Call<CheckUserResponse> call, Response<CheckUserResponse> response) {

                            CheckUserResponse userResponse=response.body();
                            if(userResponse.isExists())
                            {
                                // if user already exists,just start new activity
                                //fetch information


                                mServices.getUserInformation(account.getPhoneNumber().toString()).enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {
                                        alertDialog.dismiss();
                                        Common.currentuser=response.body();
                                        Toast.makeText(MainActivity.this, "Already Registered", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(MainActivity.this,HomeActivity.class));
                                        finish();

                                    }

                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {
                                        Toast.makeText(MainActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                            else {
                                //else new request;
                                alertDialog.dismiss();
                                showRegisterDilog(account.getPhoneNumber().toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<CheckUserResponse> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "Response Error"+t.getMessage(), Toast.LENGTH_SHORT).show();

                            alertDialog.dismiss();


                        }
                    });

                }

                @Override
                public void onError(AccountKitError accountKitError) {
                    Log.d("Error",accountKitError.getErrorType().getMessage());
                    Toast.makeText(MainActivity.this, ""+accountKitError.getErrorType().getMessage(), Toast.LENGTH_SHORT).show();

                }
            });


        }
    }

    private void startLoginPage(LoginType phone) {

        Intent intent=new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder builder=new  AccountKitConfiguration.AccountKitConfigurationBuilder(phone,AccountKitActivity.ResponseType.TOKEN);
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,builder.build());
        startActivityForResult(intent,REQUEST_CODE);




    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, @Nullable Intent data) {


        if(requestCode==REQUEST_CODE) {
            AccountKitLoginResult result = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (result.getError() != null)
            {
                Toast.makeText(this, ""+result.getError().getErrorType().getMessage(), Toast.LENGTH_SHORT).show();
            }
            else if(result.wasCancelled())
            {
                         Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();

            }
        else
            {
                if(result.getAccessToken()!=null)
                {
                    final SpotsDialog alertDialog= new SpotsDialog(MainActivity.this);
                    alertDialog.show();
                    alertDialog.setMessage("Please Wait...");

                    AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                        @Override
                        public void onSuccess(final Account account) {


                            mServices.checkUserExists( account.getPhoneNumber().toString()).enqueue(new Callback<CheckUserResponse>() {
                                @Override
                                public void onResponse(Call<CheckUserResponse> call, Response<CheckUserResponse> response) {

                                    CheckUserResponse userResponse=response.body();
                                    if(userResponse.isExists())
                                    {
                                        // if user already exists,just start new activity
                                        //fetch information


                                        mServices.getUserInformation(account.getPhoneNumber().toString()).enqueue(new Callback<User>() {
                                            @Override
                                            public void onResponse(Call<User> call, Response<User> response) {
                                                alertDialog.dismiss();
                                                Common.currentuser=response.body();
                                                Toast.makeText(MainActivity.this, "Already Registered", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(MainActivity.this,HomeActivity.class));
                                                finish();

                                            }

                                            @Override
                                            public void onFailure(Call<User> call, Throwable t) {
                                                Toast.makeText(MainActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                    else {
                                        //else new request;
                                        alertDialog.dismiss();
                                        showRegisterDilog(account.getPhoneNumber().toString());
                                    }
                                }

                                @Override
                                public void onFailure(Call<CheckUserResponse> call, Throwable t) {
                                   Toast.makeText(MainActivity.this, "Response Error"+t.getMessage(), Toast.LENGTH_SHORT).show();

                                    alertDialog.dismiss();


                                }
                            });

                        }

                        @Override
                        public void onError(AccountKitError accountKitError) {
                            Log.d("Error",accountKitError.getErrorType().getMessage());
                            Toast.makeText(MainActivity.this, ""+accountKitError.getErrorType().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        }

    }

    private void showRegisterDilog(final String phone) {
        final AlertDialog.Builder alertDialog=new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Register");
        LayoutInflater inflater=this.getLayoutInflater();
        final View register_layout=inflater.inflate(R.layout.register_layout,null);
        alertDialog.setView(register_layout);


        final MaterialEditText username = register_layout.findViewById(R.id.name);
        final MaterialEditText address = register_layout.findViewById(R.id.address);
        final MaterialEditText birthdate = register_layout.findViewById(R.id.birthdate);
        Button button=register_layout.findViewById(R.id.bbcontinue);

        birthdate.addTextChangedListener(new PatternedTextWatcher("####-##-##"));

          // Event

            button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.create().dismiss();

                if(TextUtils.isEmpty(username.getText().toString()))
                {
                    Toast.makeText(MainActivity.this, "Please Enter Name ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(address.getText().toString()))
                {
                    Toast.makeText(MainActivity.this, "Please Enter Address ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(birthdate.getText().toString()))
                {
                    Toast.makeText(MainActivity.this, "Please Enter Birthdate ", Toast.LENGTH_SHORT).show();
                    return;
                }
                final SpotsDialog alertDialog1= new SpotsDialog(MainActivity.this);
                alertDialog1.show();
                alertDialog1.setMessage("Please Wait...");
                mServices.registerNewUser(phone,username.getText().toString()
                        ,  birthdate.getText().toString(),address.getText().toString()
                      ).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {

                        alertDialog1.dismiss();
                        User user=response.body();
                        if(TextUtils.isEmpty(user.getError_msg()))
                        {
                            Toast.makeText(MainActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();

                         Common.currentuser=response.body(); //on succesful registration we have return $user object
                            // ,so we do not need to fetch information just assign jsom result to currentuser.
                            // But on login step,we  need do that.


                            startActivity(new Intent(MainActivity.this,HomeActivity.class));
                             finish();

                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        alertDialog1.dismiss();
                        Toast.makeText(MainActivity.this, "Failed To Register", Toast.LENGTH_SHORT).show();

                    }
                });



            }
        });


        alertDialog.show();


    }

    private void printkeyhash() {
        try{
            PackageInfo info=getPackageManager().getPackageInfo("com.intern.myapplication",PackageManager.GET_SIGNATURES);
            for(Signature signature:info.signatures)
            {
                MessageDigest md=MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KEY", Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        for(String name : url_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);


            sliderLayout.addSlider(textSliderView);
        }

    }
}
