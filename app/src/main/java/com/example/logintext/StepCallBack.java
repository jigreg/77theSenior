package com.example.logintext;

public interface StepCallBack {
    void onStepCallback(int step); // 서비스에서 스탭 변화시 액티비티로 전달하는 콜백 함수
    void onUnbindService(); // 서비스 언바인드 시 액티비티로 전달하는 콜백 함수
}
