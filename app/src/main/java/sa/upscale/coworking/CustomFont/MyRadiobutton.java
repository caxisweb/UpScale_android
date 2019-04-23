package sa.upscale.coworking.CustomFont;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;

/**
 * Created by DELL on 23-02-2018.
 */

@SuppressLint("AppCompatCustomView")
public class MyRadiobutton extends RadioButton {

    public MyRadiobutton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyRadiobutton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyRadiobutton(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "HelveticaNeueLTArabic-Light_0.ttf");
        setTypeface(tf, 1);

    }

}
