package sa.upscale.coworking.CustomFont;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by DELL on 01-02-2016.
 */
public class MyeditText extends EditText {

    public MyeditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyeditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyeditText(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "HelveticaNeueLTArabic-Roman_0.ttf");
        setTypeface(tf, 1);

    }

}
