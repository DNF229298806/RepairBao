package cn.edu.zwu.repairbao.MyUI;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author Richard_Y_Wang
 * @version $Rev$
 * @des 2018/4/3
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class DeleteEditText extends android.support.v7.widget.AppCompatEditText {
    private Drawable mRightDrawable;
    boolean isHasFocus;

    //构造方法一
    public DeleteEditText(Context context) {
        super(context);
        init();
    }

    //构造方法二
    public DeleteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    //构造方法三
    public DeleteEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //此方法获取控件左上右下四个方位插入的图片
        Drawable drawables[] = this.getCompoundDrawables();
        //得到右边的图片
        mRightDrawable = drawables[2];
        //设置文本监听器和焦点监听器
        this.addTextChangedListener(new TextWatcherImpl());
        this.setOnFocusChangeListener(new OnFocusChangeImpl());
        //初始设置所有右边图片不可见
        setClearDrawableVisible(false);
    }

    private class TextWatcherImpl implements TextWatcher {

        //重写三个方法   输入前 输入中 输入后
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            boolean isNoNull = getText().toString().length() >= 1;
            setClearDrawableVisible(isNoNull);
        }
    }

    private class OnFocusChangeImpl implements OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            isHasFocus = hasFocus;
            //如果目前获取到了焦点
            if (isHasFocus) {
                //如果当前的输入框中存在任意字符(包括空格以及特殊符号)
                boolean isNoNull = getText().toString().length() >= 1;
                setClearDrawableVisible(isNoNull);
            } else {
                setClearDrawableVisible(false);
            }
        }
    }

    private void setClearDrawableVisible(boolean isNoNull) {
        Drawable rightDrawable;
        //如果输入框中有字就显示右侧图片
        if (isNoNull) {
            rightDrawable = mRightDrawable;
        } else {
            rightDrawable = null;
        }
        //设置代码控制该控件left,top,right,and bottom处的图标
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], rightDrawable, getCompoundDrawables()[3]);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                //删除图片右侧到EditText控件最左侧距离
                int length1 = getWidth() - getPaddingRight();
                //删除图片左侧到EditText控件最左侧距离
                int length2 = getWidth() - getTotalPaddingRight();
                boolean isClean = (event.getX() > length2) && (event.getX() < length1);
                if (isClean) {
                    setText("");
                }
                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}
