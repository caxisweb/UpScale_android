package sa.upscale.coworking.CustomFont;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by DELL on 01-02-2016.
 */
public class Mybutton extends Button {

    public Mybutton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Mybutton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Mybutton(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "HelveticaNeueLTArabic-Light_0.ttf");
        setTypeface(tf, 1);

    }

}
