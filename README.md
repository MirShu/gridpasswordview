手势滑动解锁，九宫格密码滑动。
自定义绘制类型
        this.isBackCloseKeyboard = ta.getBoolean(R.styleable.gridPasswordView_isBackCloseKeyboard, true);
        lineWidth = (int) ta.getDimension(R.styleable.gridPasswordView_lineWidth, Utils.dp2px(getContext(), 1));
        lineColor = ta.getColor(R.styleable.gridPasswordView_lineColor, DEFAULT_LINECOLOR);
        gridColor = ta.getColor(R.styleable.gridPasswordView_gridColor, DEFAULT_GRIDCOLOR);
        lineDrawable = ta.getDrawable(R.styleable.gridPasswordView_lineColor);
        if (lineDrawable == null)
            lineDrawable = new ColorDrawable(lineColor);
        this.lineCornerRadius = (int) ta.getDimension(R.styleable.gridPasswordView_lineCornerRadius, Utils.dp2px(getContext(), 0));
        outerLineDrawable = generateBackgroundDrawable();
