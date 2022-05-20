package com.example.tannae.activity.account;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tannae.R;
import com.example.tannae.activity.user_service.UserServiceListActivity;
import com.example.tannae.network.Network;
import com.example.tannae.sub.Toaster;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountEditActivity extends AppCompatActivity {
    private EditText etID, etPW,etCheckPW, etEmail, etPhone;
    private TextView tvCheckID, tvCheckPW;
    private Button btnCheckID, btnEdit;
    private Toolbar toolbar;

    private boolean availableID = false, checkedID = false, availablePW = false, availablePWC = false, genderType = true, availableEmail = false, availablePhone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit);
        setViews();
        setEventListeners();
    }
    private void setViews(){
        etID = findViewById(R.id.et_id_account_edit);
        etPW = findViewById(R.id.et_pw_account_edit);
        etCheckPW = findViewById(R.id.et_checkpw_account_edit);
        etEmail = findViewById(R.id.et_email_account_edit);
        etPhone = findViewById(R.id.et_phone_account_edit);
        tvCheckID = findViewById(R.id.tv_checkid_account_edit);
        tvCheckPW = findViewById(R.id.tv_retrypw_account_edit);
        btnCheckID = findViewById(R.id.btn_checkid_account_edit);
        btnEdit = findViewById(R.id.btn_edit_account_edit);
        toolbar = findViewById(R.id.topAppBar_accountedit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void setEventListeners(){
        etID.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String id = etID.getText().toString();
                if (id.length() == 0) {
                    tvCheckID.setTextColor(0xAA000000);
                    tvCheckID.setText("영문 혹은 숫자를 사용하여 6자리 이상 작성하세요.");
                    availableID = false;
                } else if (id.length() >= 6 && (id.matches(".*[a-zA-Z].*") || id.matches(".*[0-9].*"))
                        && !id.matches(".*[가-힣].*") && !id.matches(".*[\\W].*")) {
                    tvCheckID.setTextColor(0xAA0000FF);
                    tvCheckID.setText("사용 가능한 ID 형식입니다.");
                    availableID = true;
                } else {
                    tvCheckID.setTextColor(0xAAFF0000);
                    tvCheckID.setText("사용 불가능한 ID 형식입니다.");
                    availableID = false;
                }
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void afterTextChanged(Editable s) { }
        });
        btnCheckID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if ID type is available
                if (!availableID) {
                    Toaster.show(getApplicationContext(), "지원되지 않는 ID 형식입니다. \n다른 ID를 사용해주세요.");
                    //Toast.makeText(getApplicationContext(), "지원되지 않는 ID 형식입니다. \n다른 ID를 사용해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Request if ID is not user [RETROFIT]
                Network.service.checkID(etID.getText().toString()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String message = response.body();
                        System.out.println(message);

                        if (message.equals("OK")) {
                            checkedID = true;
                            Toaster.show(getApplicationContext(), "사용 가능한 ID 입니다.");
                            //Toast.makeText(getApplicationContext(), "사용 가능한 ID 입니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            checkedID = false;
                            Toaster.show(getApplicationContext(), message);
                            //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toaster.show(getApplicationContext(), "Error");
                        //Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        Log.e("Error", t.getMessage());
                    }
                });
            }
        });

        // Check if PW type is available
        etPW.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pw = etPW.getText().toString();
                if (etPW.getText().toString().length() == 0) {
                    tvCheckPW.setTextColor(0xAA000000);
                    tvCheckPW.setText("영문, 숫자를 조합하여 8자리 이상으로 작성하세요.");
                    availablePW = false;
                } else if (pw.length() >= 8 && pw.matches(".*[a-zA-Z].*") && pw.matches(".*[0-9].*")
                        && !pw.matches(".*[가-힣].*") && !pw.matches(".*[\\W].*")) {
                    tvCheckPW.setTextColor(0xAA0000FF);
                    tvCheckPW.setText("사용 가능한 PW 형식입니다.");
                    availablePW = true;
                } else {
                    tvCheckPW.setTextColor(0xAAFF0000);
                    tvCheckPW.setText("사용 불가능한 PW 형식입니다..");
                    availablePW = false;
                }
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void afterTextChanged(Editable s) { }
        });

        // Check if PWR is identical with PW
        etCheckPW.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pwr = etCheckPW.getText().toString();
                if (!availablePW) {
                    tvCheckPW.setTextColor(0xAAFF0000);
                    tvCheckPW.setText("사용 불가능한 PW 형식입니다.");
                    availablePWC = false;
                } else {
                    if(etPW.getText().toString().equals(pwr)) {
                        tvCheckPW.setTextColor(0xAA0000FF);
                        tvCheckPW.setText("비밀번호가 일치합니다.");
                        availablePWC = true;
                    } else {
                        tvCheckPW.setTextColor(0xAAFF0000);
                        tvCheckPW.setText("비밀번호가 일치하지 않습니다.");
                        availablePWC = false;
                    }
                }
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void afterTextChanged(Editable s) { }
        });



        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* try {
                    // Check if entered info's are available
                    if (!availableID || !availablePW)
                        Toaster.show(getApplicationContext(), "허용되지 않은 ID or PW 형식입니다.");
                        //Toast.makeText(getApplicationContext(), "허용되지 않은 ID or PW 형식입니다.", Toast.LENGTH_SHORT).show();
                    else if (!checkedID)
                        Toaster.show(getApplicationContext(), "ID 중복을 확인하세요");
                        //Toast.makeText(getApplicationContext(), "ID 중복을 확인하세요.", Toast.LENGTH_SHORT).show();
                    else if (!availablePWC)
                        Toaster.show(getApplicationContext(), "PW가 일치하지 않습니다.");
                        //Toast.makeText(getApplicationContext(), "PW가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches())
                        Toaster.show(getApplicationContext(), "Email 을 정확하게 작성하세요.");
                        //Toast.makeText(getApplicationContext(), "Email 을 정확하게 작성하세요.", Toast.LENGTH_SHORT).show();
                    else if (!Patterns.PHONE.matcher(etPhone.getText().toString()).matches())
                        Toaster.show(getApplicationContext(), "전화번호를 정확하게 작성하세요.");
                        //Toast.makeText(getApplicationContext(), "전화번호를 정확하게 작성하세요.", Toast.LENGTH_SHORT).show();
                        // If available request sign up [RETROFIT]
                    else {
                        ////////////////////////////////////////////// 수정한 정보 서버DB에 보내주는 곳 채우기 Create User JSON
                        ////////////////////////////////////////////// 수정한 정보대로 내부 DB도 수정해주어야 함

                        JSONObject reqObj = new JSONObject();
                        reqObj.put("id", etID.getText().toString());
                        reqObj.put("pw", etPW.getText().toString());
                        reqObj.put("gender", genderType);
                        reqObj.put("phone", etPhone.getText().toString());
                        reqObj.put("email", etEmail.getText().toString());

                        // Request sign up [RETROFIT]
                        Network.service.signup(reqObj).enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                Toaster.show(getApplicationContext(), "회원정보 수정이 완료되었습니다.");

                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {
                                Toaster.show(getApplicationContext(), "Error");
                                //Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                                Log.e("Error", t.getMessage());
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } */
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
