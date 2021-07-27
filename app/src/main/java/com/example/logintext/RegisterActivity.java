package com.example.logintext;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
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

    private Button mregisterBtn;
    ImageButton before;

    // 비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");
    public static Context context_register;

    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;

    // 이메일과 비밀번호
    private EditText mEmailText, mPasswordText, mPasswordcheckText, mname, nname, phonenum;

    private Spinner yspinner, mspinner, dspinner;

    private RadioGroup ggroup;
    private RadioButton mrbtn, frbtn;

    private String email = "";
    private String password = "";
    private String pwdcheck = "";
    private String gender = "";
    private String type = ((ChooseActivity) ChooseActivity.context_choose).type;

    static FirebaseUser user;

    static String mVerificationId;
    static PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    Boolean as = false, ts ;

    private FirebaseAuth mAuth;
    Button tell, tellcheck3;
    EditText telltext3;
    private static final String KEY_VERIFICATION_ID = "key_verification_id";

    CheckBox ch1, ch2, ch3, ch4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign);
        context_register = this;

        before = (ImageButton) findViewById(R.id.before1);

        // 파이어베이스 인증 객체 선언
        firebaseAuth = FirebaseAuth.getInstance();

        mEmailText = (EditText) findViewById(R.id.et_email);
        mPasswordText = (EditText) findViewById(R.id.et_password);
        mPasswordcheckText = (EditText) findViewById(R.id.et_password2);

        mregisterBtn = (Button) findViewById(R.id.btn_signUp);

        mname = (EditText) findViewById(R.id.et_name);
        nname = (EditText) findViewById(R.id.et_nname);

        yspinner = (Spinner) findViewById(R.id.spinner_year);
        mspinner = (Spinner) findViewById(R.id.spinner_month);
        dspinner = (Spinner) findViewById(R.id.spinner_date);
        ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(this, R.array.date_year, android.R.layout.simple_spinner_dropdown_item);
        yspinner.setAdapter(yearAdapter);

        ArrayAdapter monthAdapter = ArrayAdapter.createFromResource(this, R.array.date_month, android.R.layout.simple_spinner_dropdown_item);
        mspinner.setAdapter(monthAdapter);

        ArrayAdapter dateAdapter = ArrayAdapter.createFromResource(this, R.array.date_date, android.R.layout.simple_spinner_dropdown_item);
        dspinner.setAdapter(dateAdapter);


        ggroup = (RadioGroup) findViewById(R.id.ggroup);
        mrbtn = (RadioButton) findViewById(R.id.male);
        frbtn = (RadioButton) findViewById(R.id.female);

        phonenum = (EditText) findViewById(R.id.phonenum);

        tell = (Button) findViewById(R.id.tell);
        tellcheck3 = (Button) findViewById(R.id.tellCheck3);
        telltext3 = (EditText) findViewById(R.id.tellText3);

        ch1 = (CheckBox)findViewById(R.id.checkbox1);
        ch2 = (CheckBox)findViewById(R.id.checkbox2);
        ch3 = (CheckBox) findViewById(R.id.checkbox3);
        ch4 = (CheckBox) findViewById(R.id.checkbox4);

        //성별 리스너
        ggroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.male) {
                    gender = "m";
                } else {
                    gender = "f";
                }
            }
        });

        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        ch4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ch4.isChecked()) {
                    ch1.setChecked(true);
                    ch2.setChecked(true);
                    ch3.setChecked(true);
                }
            }
        });
        //firebase에 데이터를 저장
        mregisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = "";
                password = "";
                pwdcheck = "";

                //가입 정보 가져오기
                email = mEmailText.getText().toString();
                password = mPasswordText.getText().toString();
                pwdcheck = mPasswordcheckText.getText().toString();

                 if (mname.getText().toString() == "") {
                    Toast.makeText(RegisterActivity.this, "이름을 입력해주세요",
                            Toast.LENGTH_SHORT).show();
                    ts = false;
                } else if (nname.getText().toString() == "") {
                    Toast.makeText(RegisterActivity.this, "닉네임을 입력해주세요",
                            Toast.LENGTH_SHORT).show();
                    ts = false;
                } else if (email.isEmpty()){
                    mEmailText.requestFocus();
                    Toast.makeText(RegisterActivity.this, "이메일을 입력해주세요",
                            Toast.LENGTH_SHORT).show();
                    ts = false;
                } else if (password.isEmpty()){
                    mPasswordText.requestFocus();
                    Toast.makeText(RegisterActivity.this, "비밀번호를 입력해주세요",
                            Toast.LENGTH_SHORT).show();
                    ts = false;
                } else if (pwdcheck.isEmpty()){
                    mPasswordcheckText.requestFocus();
                    Toast.makeText(RegisterActivity.this, "비밀번호 확인을 입력해주세요",
                            Toast.LENGTH_SHORT).show();
                    ts = false;
                } else{
                    ts = true;
                }
                if(ts) {
                    if (as) {
                        if (ch1.isChecked() && ch2.isChecked()) {

                            if (isValidEmail() && isValidPasswd()) {

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
                                                        String name = mname.getText().toString().trim();
                                                        String nickname = nname.getText().toString().trim();
                                                        String birth = yspinner.getSelectedItem().toString() + mspinner.getSelectedItem().toString() + dspinner.getSelectedItem().toString();
                                                        String pnum = phonenum.getText().toString();

                                                        SimpleDateFormat format = new SimpleDateFormat ( "yyyy년 MM월dd일");
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

                                                        //가입이 이루어져을시 가입 화면을 빠져나감.
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
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "약관 동의를 해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "전화번호 인증을 해주세요.", Toast.LENGTH_SHORT).show();
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
                telltext3.requestFocus();
            }
        };
        // [END phone_auth_callbacks]

        tell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tellcheck3.setVisibility(View.VISIBLE);
                telltext3.setVisibility(View.VISIBLE);
                startPhoneNumberVerification("+82"+phonenum.getText().toString().substring(1));

            }
        });

        tellcheck3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    verifyPhoneNumberWithCode(mVerificationId, telltext3.getText().toString());
                }catch (Exception e){
                    Toast.makeText(RegisterActivity.this, "인증번호 입력 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                            as = true;
                            tell.setClickable(false);
                            tellcheck3.setClickable(false);
                            telltext3.setClickable(false);
                            telltext3.setFocusable(false);
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