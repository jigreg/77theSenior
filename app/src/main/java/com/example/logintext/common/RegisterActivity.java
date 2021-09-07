package com.example.logintext.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.example.logintext.R;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;



public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private static final String KEY_VERIFICATION_ID = "key_verification_id";

    // 비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");

    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    // 이메일과 비밀번호
    private EditText mEmailText, mPasswordText, mPasswordcheckText, et_name, et_nickname, phonenum, tellText;
    private Button mregisterBtn, tell, tellCheck, personal_btn, service_btn, location_btn;
    private ImageButton back;

    private CheckBox ch1, ch2, ch3, ch4, allCheck;

    private RadioGroup sex;
    private RadioButton mrbtn, frbtn;

    private Spinner yspinner, mspinner, dspinner;

    private int gen_check = 0;

    private String name = "";
    private String nickname = "";
    private String email = "";
    private String password = "";
    private String pwdcheck = "";
    private String gender = "";
    private String type = ((ChooseActivity) ChooseActivity.context_choose).type;

    private Boolean authSuccess = false ;

    public static String mVerificationId;

    public static Context context_register;

    public static FirebaseUser user;
    public static PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign);

        context_register = this;

        back = (ImageButton) findViewById(R.id.back);

        // 파이어베이스 인증 객체 선언
        firebaseAuth = FirebaseAuth.getInstance();

        mEmailText = (EditText) findViewById(R.id.et_email);
        mPasswordText = (EditText) findViewById(R.id.et_password);
        mPasswordcheckText = (EditText) findViewById(R.id.et_passwordCheck);
        et_name = (EditText) findViewById(R.id.et_name);
        et_nickname = (EditText) findViewById(R.id.et_nickname);

        mregisterBtn = (Button) findViewById(R.id.btn_signUp);

        yspinner = (Spinner) findViewById(R.id.spinner_year);
        mspinner = (Spinner) findViewById(R.id.spinner_month);
        dspinner = (Spinner) findViewById(R.id.spinner_date);

        ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(this, R.array.date_year, android.R.layout.simple_spinner_dropdown_item);
        yspinner.setAdapter(yearAdapter);

        ArrayAdapter monthAdapter = ArrayAdapter.createFromResource(this, R.array.date_month, android.R.layout.simple_spinner_dropdown_item);
        mspinner.setAdapter(monthAdapter);

        ArrayAdapter dateAdapter = ArrayAdapter.createFromResource(this, R.array.date_date, android.R.layout.simple_spinner_dropdown_item);
        dspinner.setAdapter(dateAdapter);

        sex = (RadioGroup) findViewById(R.id.sex);
        mrbtn = (RadioButton) findViewById(R.id.male);
        frbtn = (RadioButton) findViewById(R.id.female);

        phonenum = (EditText) findViewById(R.id.phonenum);

        tell = (Button) findViewById(R.id.tell);
        tellCheck = (Button) findViewById(R.id.tellCheck);
        tellText = (EditText) findViewById(R.id.tellText);

        ch1 = (CheckBox)findViewById(R.id.checkbox1);
        ch2 = (CheckBox)findViewById(R.id.checkbox2);
        ch3 = (CheckBox) findViewById(R.id.checkbox3);
        ch4 = (CheckBox) findViewById(R.id.checkbox4);
        allCheck = (CheckBox) findViewById(R.id.checkbox5);

        personal_btn = (Button) findViewById(R.id.privacy);
        service_btn = (Button) findViewById(R.id.service);
        location_btn = (Button) findViewById(R.id.location);


        //성별 리스너
        sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.male) {
                    gender = "m";
                    gen_check++;
                } else {
                    gender = "f";
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        allCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allCheck.isChecked()) {
                    ch1.setChecked(true);
                    ch2.setChecked(true);
                    ch3.setChecked(true);
                    ch4.setChecked(true);
                }
            }
        });

        service_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                LinearLayout linear = (LinearLayout) inflater.inflate(R.layout.service_info, null);

                linear.setBackgroundColor(Color.parseColor("#99000000"));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT
                        , LinearLayout.LayoutParams.FILL_PARENT);
                addContentView(linear, params);

                linear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ViewManager) linear.getParent()).removeView(linear);
                    }
                });
            }
        });

        personal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                LinearLayout linear = (LinearLayout) inflater.inflate(R.layout.personal_info, null);

                linear.setBackgroundColor(Color.parseColor("#99000000"));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT
                        , LinearLayout.LayoutParams.FILL_PARENT);
                addContentView(linear, params);

                linear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ViewManager) linear.getParent()).removeView(linear);
                    }
                });
            }
        });

        location_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                LinearLayout linear = (LinearLayout) inflater.inflate(R.layout.location_info, null);

                linear.setBackgroundColor(Color.parseColor("#99000000"));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT
                        , LinearLayout.LayoutParams.FILL_PARENT);
                addContentView(linear, params);

                linear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ViewManager) linear.getParent()).removeView(linear);
                    }
                });
            }
        });

        //firebase에 데이터를 저장
        mregisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = "";
                nickname = "";
                email = "";
                password = "";
                pwdcheck = "";

                //가입 정보 가져오기
                name = et_name.getText().toString();
                nickname = et_nickname.getText().toString();
                email = mEmailText.getText().toString();
                password = mPasswordText.getText().toString();
                pwdcheck = mPasswordcheckText.getText().toString();

                if (isValidName() && isValidNickName() && isValidSex() && isValidEmail() && isValidPasswd() && isValidPasswdCheck()) {

                    if (ch1.isChecked() && ch2.isChecked()) {

                        if (authSuccess) {

                            if (password.equals(pwdcheck)) {
                                Log.d(TAG, "등록 버튼 " + email + " , " + password);
                                final ProgressDialog mDialog = new ProgressDialog(RegisterActivity.this);
                                mDialog.setMessage("가입중입니다...");
                                mDialog.show();

                                //파이어베이스에 신규계정 등록하기
                                firebaseAuth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    mDialog.dismiss();

                                                    user = firebaseAuth.getCurrentUser();
                                                    String email = user.getEmail();
                                                    String uid = user.getUid();
                                                    String name = et_name.getText().toString().trim();
                                                    String nickname = et_nickname.getText().toString().trim();
                                                    String birth = yspinner.getSelectedItem().toString() + mspinner.getSelectedItem().toString() + dspinner.getSelectedItem().toString();
                                                    String pnum = phonenum.getText().toString();

                                                    SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월dd일");
                                                    Calendar time = Calendar.getInstance();
                                                    String format_time = format.format(time.getTime());

                                                    //해쉬맵 테이블을 파이어베이스 데이터베이스에 저장
                                                    HashMap<Object, Object> hashMap = new HashMap<>();

                                                    hashMap.put("uid", uid);
                                                    hashMap.put("email", email);
                                                    hashMap.put("name", name);
                                                    hashMap.put("nickname", nickname);
                                                    hashMap.put("birth", birth);
                                                    hashMap.put("gender", gender);
                                                    hashMap.put("phonenum", pnum);
                                                    hashMap.put("type", type);
                                                    hashMap.put("joined", format_time);
                                                    hashMap.put("today_walking", 0);
                                                    hashMap.put("today_training", 0);

                                                    if (type.equals("user")) {
                                                        hashMap.put("myProtector", "none");
                                                        hashMap.put("gps", "none");
                                                        hashMap.put("walk", "0");
                                                    } else if (type.equals("protector")) {
                                                        hashMap.put("myUser", "none");
                                                        hashMap.put("safeZone", "none");
                                                    }

                                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                    DatabaseReference reference = database.getReference("Users");
                                                    reference.child(type).child(uid).setValue(hashMap);

                                                    //가입이 이루어졌을시 가입 화면을 빠져나감.
                                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                    Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();

                                                } else {
                                                    mDialog.dismiss();
                                                    Toast.makeText(RegisterActivity.this, "회원가입 실패.", Toast.LENGTH_SHORT).show();
                                                    return;  //해당 메소드 진행을 멈추고 빠져나감.
                                                }

                                            }
                                        });

                                //비밀번호 오류시
                            } else {
                                Toast.makeText(RegisterActivity.this, "비밀번호가 틀렸습니다. 다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "전화번호 인증을 해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "약관 동의를 해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        mAuth = FirebaseAuth.getInstance();

        // 전화인증 콜백 초기화
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Toast.makeText(RegisterActivity.this, "인증번호 발송 실패", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "onVerificationFailed", e);

                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.

                // Save verification ID and resending token so we can use them later
                if (verificationId == null && savedInstanceState != null)
                    onRestoreInstanceState(savedInstanceState);
                mVerificationId = verificationId;
                mResendToken = token;
                Toast.makeText(RegisterActivity.this, "인증번호 발송", Toast.LENGTH_SHORT).show();
                tellText.requestFocus();
            }
        };
        // [END phone_auth_callbacks]

        tell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tellCheck.setVisibility(View.VISIBLE);
                tellText.setVisibility(View.VISIBLE);
                startPhoneNumberVerification("+82"+phonenum.getText().toString().substring(1));

            }
        });

        tellCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    verifyPhoneNumberWithCode(mVerificationId, tellText.getText().toString());
                }catch (Exception e){
                    Toast.makeText(RegisterActivity.this, "인증번호 입력 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // 이름 유효성 검사
    private boolean isValidName() {
        if (name.isEmpty()) {
            // 이름 공백
            Toast.makeText(getApplicationContext(),"이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // 닉네임 유효성 검사
    private boolean isValidNickName() {
        if (nickname.isEmpty()) {
            // 닉네임 공백
            Toast.makeText(getApplicationContext(),"닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // 성별 유효성 검사
    private boolean isValidSex() {
        if (gen_check == 0) {
            // 성별 미선택
            Toast.makeText(getApplicationContext(),"성별을 선택해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // 이메일 유효성 검사
    private boolean isValidEmail() {
        if (email.isEmpty()) {
            // 이메일 공백
            Toast.makeText(getApplicationContext(),"이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // 이메일 형식 불일치
            Toast.makeText(getApplicationContext(),"이메일 형식 오류", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    // 비밀번호 유효성 검사
    private boolean isValidPasswd() {
        if (password.isEmpty()) {
            // 비밀번호 공백
            Toast.makeText(getApplicationContext(),"비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            // 비밀번호 형식 불일치
            Toast.makeText(getApplicationContext(),"비밀번호 형식 오류", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    // 비밀번호 확인 유효성 검사
    private boolean isValidPasswdCheck() {
        if (pwdcheck.isEmpty()) {
            // 이름 공백
            Toast.makeText(getApplicationContext(),"비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]


    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        // [END start_phone_auth]
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
        // [END verify_with_code]
    }

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(RegisterActivity.this,"인증 성공",Toast.LENGTH_SHORT).show();
                            FirebaseUser user = task.getResult().getUser();
                            authSuccess = true;
                            tell.setClickable(false);
                            tellCheck.setClickable(false);
                            tellText.setClickable(false);
                            tellText.setFocusable(false);
                            // Update UI
                        } else {
                            // Sign in failed, display a message and update the UI
                            Toast.makeText(RegisterActivity.this,"인증코드 에러",Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }


    // [END sign_in_with_phone]

    private void updateUI(FirebaseUser user) {

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_VERIFICATION_ID, mVerificationId);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationId = savedInstanceState.getString(KEY_VERIFICATION_ID);
    }
}