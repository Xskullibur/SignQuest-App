package sg.edu.nyp.signquest.game.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import sg.edu.nyp.signquest.R;

public class CircleProgressBar extends View {


    /**
     * ProgressBar's line thickness
     */
    private float strokeWidth = 4;
    private float progress = 0;
    private int min = 0;
    private int max = 100;
    /**
     * Start the progress at 12 o'clock
     */
    private int startAngle = -90;
    private int color = Color.DKGRAY;
    private int textColor = Color.DKGRAY;
    private int textSize = 87;
    private RectF rectF;
    private Paint backgroundPaint;
    private Paint foregroundPaint;
    private Paint textPaint;

    private Rect bounds;
    private String defText = "B";
    private String text = defText;

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleProgressBar(Context context){
        super(context);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CircleProgressBar,
                0, 0);
        //Reading values from the XML layout
        try {
            strokeWidth = typedArray.getDimension(R.styleable.CircleProgressBar_progressBarThickness, strokeWidth);
            progress = typedArray.getFloat(R.styleable.CircleProgressBar_progress, progress);
            color = typedArray.getInt(R.styleable.CircleProgressBar_progressbarColor, color);
            min = typedArray.getInt(R.styleable.CircleProgressBar_min, min);
            max = typedArray.getInt(R.styleable.CircleProgressBar_max, max);
            textColor = typedArray.getInt(R.styleable.CircleProgressBar_textColor, textColor);

            String text = typedArray.getString(R.styleable.CircleProgressBar_text);
            this.text = text == null ? defText : text;

            textSize = typedArray.getInt(R.styleable.CircleProgressBar_textSize, textSize);
        } finally {
            typedArray.recycle();
        }

        init();
    }

    public void init() {
        rectF = new RectF();
        initBackgroundPaint();
        initForegroundPaint();
        initTextPaint();
    }

    private void initTextPaint() {
        textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(textSize);

        //Calculate text bounds
        bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
    }

    private void initForegroundPaint() {
        foregroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        foregroundPaint.setColor(color);
        foregroundPaint.setStyle(Paint.Style.STROKE);
        foregroundPaint.setStrokeWidth(strokeWidth);
    }

    private void initBackgroundPaint() {
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(adjustAlpha(color, 0.3f));
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(strokeWidth);
    }

    private int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int min = Math.min(width, height);
        setMeasuredDimension(min, min);
        rectF.set(0 + strokeWidth / 2, 0 + strokeWidth / 2, min - strokeWidth / 2, min - strokeWidth / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawOval(rectF, backgroundPaint);
        float angle = 360 * progress / max;
        canvas.drawArc(rectF, startAngle, angle, false, foregroundPaint);

        float textWidth = bounds.width();
        float textHeight = bounds.height();

        canvas.drawText(text,  (getWidth()/2.0f) - (textWidth / 2),
                (getHeight()/2.0f) + (textHeight / 2), textPaint);


    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();// Notify the view to redraw it self (the onDraw method is called)
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public float getProgress() {
        return progress;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        int currentColor = this.color;
        if(currentColor != this.color){
            this.color = currentColor;
            init();
        }else{
            this.color = currentColor;
        }

    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


}