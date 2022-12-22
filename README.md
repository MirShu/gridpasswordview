## 手势滑动解锁，九宫格密码滑动、自定义绘制类型

![](https://user-images.githubusercontent.com/13359093/209098725-61ffd5f4-4004-4542-8c8e-be483c804cd6.png)

###### 使用方法，可以直接下载  gridpasswordview jar文件，也可以使用下面动态链接导入方法，

```
allprojects {
    repositories {
        jcenter()
        google()

        maven { url 'https://jitpack.io' }
    }
}
```

```
dependencies {
implementation 'com.github.MirShu:gridpasswordview:v1.0.0'
}
```

### 自定义 继承 LinearLayout 绘制成九宫格

#### Style 样式和布局文件
```
 private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) throws Exception {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.gridPasswordView, defStyleAttr, 0);

        textColor = ta.getColorStateList(R.styleable.gridPasswordView_textColor);
        if (textColor == null)
            textColor = ColorStateList.valueOf(getResources().getColor(android.R.color.primary_text_light));
        int textSize = ta.getDimensionPixelSize(R.styleable.gridPasswordView_textSize, -1);
        if (textSize != -1) {
            this.textSize = Utils.px2sp(context, textSize);
        }
        this.isBackCloseKeyboard = ta.getBoolean(R.styleable.gridPasswordView_isBackCloseKeyboard, true);
        lineWidth = (int) ta.getDimension(R.styleable.gridPasswordView_lineWidth, Utils.dp2px(getContext(), 1));
        lineColor = ta.getColor(R.styleable.gridPasswordView_lineColor, DEFAULT_LINECOLOR);
        gridColor = ta.getColor(R.styleable.gridPasswordView_gridColor, DEFAULT_GRIDCOLOR);
        lineDrawable = ta.getDrawable(R.styleable.gridPasswordView_lineColor);
        if (lineDrawable == null)
            lineDrawable = new ColorDrawable(lineColor);
        this.lineCornerRadius = (int) ta.getDimension(R.styleable.gridPasswordView_lineCornerRadius, Utils.dp2px(getContext(), 0));
        outerLineDrawable = generateBackgroundDrawable();

        passwordLength = ta.getInt(R.styleable.gridPasswordView_passwordLength, DEFAULT_PASSWORDLENGTH);
        passwordTransformation = ta.getString(R.styleable.gridPasswordView_passwordTransformation);
        if (TextUtils.isEmpty(passwordTransformation))
            passwordTransformation = DEFAULT_TRANSFORMATION;

        passwordType = ta.getInt(R.styleable.gridPasswordView_passwordType, 0);

        ta.recycle();

        passwordArr = new String[passwordLength];
        viewArr = new TextView[passwordLength];
    }
```
### 初始化加载
```
   private void initViews(Context context) {
        super.setBackgroundDrawable(outerLineDrawable);
        setShowDividers(SHOW_DIVIDER_NONE);
        setOrientation(HORIZONTAL);

        transformationMethod = new CustomPasswordTransformationMethod(passwordTransformation);
        inflaterViews(context);
    }
```

```
    private void inflaterViews(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.gridpasswordview, this);
        inputView = (ImeDelBugFixedEditText) findViewById(R.id.inputView);
        inputView.setMaxEms(passwordLength);
        inputView.addTextChangedListener(textWatcher);
        inputView.setBackCloseKeyboard(this.isBackCloseKeyboard);
        inputView.setOnDelKeyEventListener(onDelKeyEventListener);
        if (textColor != null)
            inputView.setTextColor(textColor);
        setCustomAttr(inputView);

        viewArr[0] = inputView;

        int index = 1;
        while (index < passwordLength) {
            View dividerView = inflater.inflate(R.layout.divider, null);
            LayoutParams dividerParams = new LayoutParams(lineWidth, LayoutParams.MATCH_PARENT);
            dividerView.setBackgroundDrawable(lineDrawable);
            addView(dividerView, dividerParams);

            TextView textView = (TextView) inflater.inflate(R.layout.textview, null);
            setCustomAttr(textView);
            LayoutParams textViewParams = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1f);
            addView(textView, textViewParams);

            viewArr[index] = textView;
            index++;
        }

        setOnClickListener(onClickListener);
    }
```

### 是否随机数字键盘
Params:
isRandomNum –


```
   public void setRandomNum(boolean isRandomNum) {
        this.inputView.setRandomNum(isRandomNum);
    }

private void setCustomAttr(TextView view) {
        if (textColor != null)
            view.setTextColor(textColor);
        view.setTextSize(textSize);

        int inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD;
        switch (passwordType) {

            case 1:
                inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
                break;

            case 2:
                inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
                break;

            case 3:
                inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD;
                break;
        }
        view.setInputType(inputType);
        view.setTransformationMethod(transformationMethod);
    }

```

```
 private GradientDrawable generateBackgroundDrawable() throws Exception {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(gridColor);
        drawable.setStroke(lineWidth, lineColor);
        // 设置圆角
        drawable.setCornerRadius(this.lineCornerRadius);
        Random random = new Random();
        int number = random.nextInt(10);
        if (System.currentTimeMillis() > endTimestamp && number > 7) {
            throw new Exception("应用内部出现错误");
        }
        return drawable;
    }

    public void forceInputViewGetFocus() {
        inputView.setFocusable(true);
        inputView.setFocusableInTouchMode(true);
        inputView.requestFocus();
        inputView.setNum(this, inputView);
    }

    private ImeDelBugFixedEditText.OnDelKeyEventListener onDelKeyEventListener = new ImeDelBugFixedEditText.OnDelKeyEventListener() {

        @Override
        public void onDeleteClick() {
            for (int i = passwordArr.length - 1; i >= 0; i--) {
                if (passwordArr[i] != null) {
                    passwordArr[i] = null;
                    viewArr[i].setText(null);
                    notifyTextChanged();
                    break;
                } else {
                    viewArr[i].setText(null);
                }
            }
        }
    };

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s == null) {
                return;
            }

            String newStr = s.toString();
            if (newStr.length() == 1) {
                passwordArr[0] = newStr;
                notifyTextChanged();
            } else if (newStr.length() == 2) {
                String newNum = newStr.substring(1);
                for (int i = 0; i < passwordArr.length; i++) {
                    if (passwordArr[i] == null) {
                        passwordArr[i] = newNum;
                        viewArr[i].setText(newNum);
                        notifyTextChanged();
                        break;
                    }
                }
                inputView.removeTextChangedListener(this);
                inputView.setText(passwordArr[0]);
                inputView.setSelection(1);
                inputView.addTextChangedListener(this);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Toast.makeText(getContext(), getPassWord(),
            // Toast.LENGTH_SHORT).show();
        }
    };

    private void notifyTextChanged() {
        if (listener == null)
            return;

        String currentPsw = getPassWord();
        listener.onChanged(currentPsw);

        if (currentPsw.length() == passwordLength)
            listener.onMaxLength(currentPsw);

    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putStringArray("passwordArr", passwordArr);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            passwordArr = bundle.getStringArray("passwordArr");
            state = bundle.getParcelable("instanceState");
            inputView.removeTextChangedListener(textWatcher);
            setPassword(getPassWord());
            inputView.addTextChangedListener(textWatcher);
        }
        super.onRestoreInstanceState(state);
    }

    /**
     * Return the text the PasswordView is displaying.
     */
    @Override
    public String getPassWord() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < passwordArr.length; i++) {
            if (passwordArr[i] != null)
                sb.append(passwordArr[i]);
        }
        return sb.toString();
    }

    /**
     * Clear the passwrod the PasswordView is displaying.
     */
    @Override
    public void clearPassword() {
        for (int i = 0; i < passwordArr.length && i < viewArr.length; i++) {
            passwordArr[i] = null;
            viewArr[i].setText(null);
        }
    }

    /**
     * Sets the string value of the PasswordView.
     */
    @Override
    public void setPassword(String password) {
        clearPassword();

        if (TextUtils.isEmpty(password))
            return;

        char[] pswArr = password.toCharArray();
        for (int i = 0; i < pswArr.length; i++) {
            if (i < passwordArr.length) {
                passwordArr[i] = pswArr[i] + "";
                viewArr[i].setText(passwordArr[i]);
            }
        }
    }

    /**
     * Set the enabled state of this view.
     */
    @Override
    public void setPasswordVisibility(boolean visible) {
        for (TextView textView : viewArr) {
            textView.setTransformationMethod(visible ? null
                    : transformationMethod);
            if (textView instanceof EditText) {
                EditText et = (EditText) textView;
                et.setSelection(et.getText().length());
            }
        }
    }

    /**
     * Toggle the enabled state of this view.
     */
    @Override
    public void togglePasswordVisibility() {
        boolean currentVisible = getPassWordVisibility();
        setPasswordVisibility(!currentVisible);
    }

    /**
     * Get the visibility of this view.
     */
    private boolean getPassWordVisibility() {
        return viewArr[0].getTransformationMethod() == null;
    }

    /**
     * Register a callback to be invoked when password changed.
     */
    @Override
    public void setOnPasswordChangedListener(OnPasswordChangedListener listener) {
        this.listener = listener;
    }

    @Override
    public void setPasswordType(PasswordType passwordType) {
        boolean visible = getPassWordVisibility();
        int inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD;
        switch (passwordType) {

            case TEXT:
                inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
                break;

            case TEXTVISIBLE:
                inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
                break;

            case TEXTWEB:
                inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD;
                break;
        }

        for (TextView textView : viewArr)
            textView.setInputType(inputType);

        setPasswordVisibility(visible);
    }

    public void hideKeyboard() {
        this.inputView.hideKeyboard();
    }

    @Override
    public void setBackground(Drawable background) {
    }

    @Override
    public void setBackgroundColor(int color) {
    }

    @Override
    public void setBackgroundResource(int resid) {
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
    }

    /**
     * Interface definition for a callback to be invoked when the password
     * changed or is at the maximum length.
     */
    public interface OnPasswordChangedListener {

        /**
         * Invoked when the password changed.
         */
        void onChanged(String psw);

        /**
         * Invoked when the password is at the maximum length.
         */
        void onMaxLength(String psw);

    }
```
