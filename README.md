## Our Site
- Notion : [notion](https://www.notion.so/8bed43b5c040436db8bc28af704163f0?v=74646b09f6ff4957b3e0c7c62024d928)
- TeamGantt : [TeamGantt](https://prod.teamgantt.com/gantt/schedule/?ids=2616664#&ids=2616664&user=&custom=&company=&hide_completed=false&date_filter=&color_filter=)

## Daily Issue
### 0726 Hwang Won Tae
    : Ranking Function -> TabHost + ListView (release)

- issue 1. RealTime DB sorting is not supported

- issue 2. Walking data must be changed to "int"

---

### 0727 Yu Jun
    : Calendar Function -> Calendar View (release)

- issue 1. AVD DEAD TEST Unavaliable

- issue 2. Calendar can only memo -> Walking data and Brain training data

---

### 0731 Hwang Won Tae
    : Ranking Function -> Deleted TabHost, Used ListView & Intent

- @ Test completed and available for release

- (Clear) !issue 1. RealTime DB sorting is not supported -> Resolved using algorithm.

- (Non-Clear) !issue 2. Walking data must be changed to "int".

---

### 0803 Yu Jun
    : Calendar Function -> Slidingpannellayout add

- issue 1. WalkData & BrainData should sort by day (0801 : walkdata 700 ,braindata : 609)

- issue 2. AVD still DEAD Cant solve help...

---

### 0804 Back Su Jung
    : BrainMain, CatFindActivity add

- issue 1. Don't have Criteria for Scores

- issue 2. Watermelongame's Difficulty or Necessity

---

### 0805 All
    : Adjust Code Style, Rename Filing & ReGroup Package

- issue 1. Yu Jun & Back Su Jung 's AVD is Dead.. :..(

- issue 2. drawable needs Renaming

---

### 0808 Hwang Won Tae
    : Testing Auto-Login Function

- issue 1. Auto-Login Function to set coverage

- issue 2. Change of plan ( Foreground Function -> Auto-Login )

---

### 0809 Yu Seo Jun
    : AVD bug fix!! Calendar xml, java fix

- issue 1. Need WalkData and BrainData coding in Calendar

- issue 2. Fix walkdata for int and sort by date in WalkActivity

---

### 0811 Yu Seo Jun
    : Solved Calendar Error!! , Add progressbar, distance, calorie in Calendar

- issue 1. Change date savetype in firebase 2021년 08월 11일 -> 2021811

- issue 2. Need Braindata resource & more Walkdata 

---

### 0813 Yu Seo Jun, Hwang Won Tae
    : Solved Calendar Error!! & Overall XML modification

- issue 1. Need to prepare for the contest report ( Do not proceed today )

- issue 2. UI change required & Dementia grade score & Dementia News Crawling

- issue 3. Mrs. Back Penalty stack +1 ( Not attending the meeting )

---

### 0813 Yu Seo Jun, Hwang Won Tae
    : Add Pro_Calendar Pro can access User's Calendar, Imported User's walking to User_Main, Add Auto-Login

- (Clear) issue 1. Need to Pro Calendar data test

---

### 0815 Hwang Won Tae
    : Fix Ranking Function & Modifying some DB entries

- (Clear) issue 1. Fix Ranking Activity's WalkData

---

### 0817 Back Su Jung
    : CatFind ADD, BrainData add

- issue 1. Reapply Watermelon Game with Unitiy

---

### 0818 Hwang Won Tae
    : Adding brain training scores to 'User_main & Pro_main'

- issue 1. Need a standard for brain training scores

---

### 0822 Hwang Won Tae
    : Add ForegroundService, but not interlocked

- issue 1. Failed to read "User_WalkActivity.mStepDetector" in foreground

---

### 0823 Hwang Won Tae
    : Fix RegisterActivity Error, Implementing 'foreground', there are many errors, Touch the notification window to call 'User_WalkActivity'

- (Clear) issue 1. Put 'today_walking', 'today_training' on the hashMap

- issue 1. Failed to read "User_WalkActivity.mStepDetector" in foreground :(

---

### 0824 Yu Seo Jun, Hwang Won Tae
    : ADD NEWs Crawling in MainActivity, Fix link intent error, Clone Foreground & Testing

- (Clear) issue 1. News link intent error

---

### 0827 Hwang Won Tae
    : Change Ranking Activity to TabHost, Annotation all Foreground ( Can't destroy )

- (Non-Clear) issue 1. Foreground error ( Can't Use )

---

### 0831 Hwang Won Tae
    : Add Manifest 'ACCESS_BACKGROUND_LOCATION'(Function not implemented)

- (Clear) issue 1. Solve RankActivity XML error

---

### 0903 Hwang Won Tae
    : Developing internal capabilities in User_Setting

- issue 1. check out in R.string ^^.. ( Need a better way )

---

### 0907 Hwang Won Tae
    : Add Pro_set & Several XML modifications, Adjust RegisterActivity

- issue 1. Need to select dementia grade icon

---

### 0908 Yu Seo Jun
    : Fix FindIdPW xml & FINDIDPW complete & add alertDialog

- issue 1. Wrong Id or PW Toast message error

---

### 0911 Yu Seo Jun
    : ADD Mypage xml & java(add protector info dialog)

- issue 1. Need to Add walk,brain rank info & dementia grade info
- issue 2. Galaxy S21 avd --> need to xml change!!!

---

### 0914 Hwang Won Tae
    : Solve All_Setting_Activity(user, pro) & Several XML modifications

- issue 1. I have an idea about brain training.

    two_btn, a_btn is the practice & b_btn is the test // Use LayoutInflater
    
    Can take the test only once a day

---

### 0916 Hwang Won Tae
    : Add Background Service

- issue 1. Need to solve Foreground Service :(

---

### 0920 Hwang Won Tae
    : Create Walk_Foreground Module & Implement notification tap's XML

- issue 1. Need to merge new Module and User_WalkActivity

---

### 0921 Hwang Won Tae
    : Merge Walk_Foreground Moudule & User_WalkActivity -> User_TestWalkActibity

- issue 1. The number of notification tab steps has not changed.
- issue 2. The foreground doesn't apply at once.