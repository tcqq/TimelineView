package com.tcqq.timelineview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Vipul Asri on 05-12-2015.
 */
public class TimelineView extends View {

    public static final String TAG = TimelineView.class.getSimpleName();

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LineOrientation.HORIZONTAL, LineOrientation.VERTICAL})
    public @interface LineOrientation {
        int HORIZONTAL = 0;
        int VERTICAL = 1;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LineType.NORMAL, LineType.START, LineType.END, LineType.ONLYONE})
    private @interface LineType {
        int NORMAL = 0;
        int START = 1;
        int END = 2;
        int ONLYONE = 3;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LineStyle.NORMAL, LineStyle.DASHED})
    public @interface LineStyle {
        int NORMAL = 0;
        int DASHED = 1;
    }

    private Drawable mMarker;
    private int mMarkerSize;
    private int mMarkerPaddingTop;
    private int mMarkerPaddingBottom;
    private boolean mMarkerInCenter;
    private Paint mLinePaint = new Paint();
    private boolean mDrawStartLine = true;
    private boolean mDrawEndLine = true;
    private float mStartLineStartX, mStartLineStartY, mStartLineStopX, mStartLineStopY;
    private float mEndLineStartX, mEndLineStartY, mEndLineStopX, mEndLineStopY;
    private int mStartLineColor;
    private int mEndLineColor;
    private int mMarkerColor;
    private int mLineWidth;
    private int mLineOrientation;
    private int mLineStyle;
    private int mLineStyleDashLength;
    private int mLineStyleDashGap;
    private int mLinePadding;

    private Rect mBounds;

    public TimelineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TimelineView);
        mMarker = typedArray.getDrawable(R.styleable.TimelineView_timeline_marker);
        mMarkerSize = typedArray.getDimensionPixelSize(R.styleable.TimelineView_timeline_marker_size, Utils.dp2px(20F));
        mMarkerPaddingTop = typedArray.getDimensionPixelSize(R.styleable.TimelineView_timeline_marker_padding_top, 0);
        mMarkerPaddingBottom = typedArray.getDimensionPixelSize(R.styleable.TimelineView_timeline_marker_padding_bottom, 0);
        mMarkerInCenter = typedArray.getBoolean(R.styleable.TimelineView_timeline_marker_in_center, true);
        mStartLineColor = typedArray.getColor(R.styleable.TimelineView_timeline_start_line_color, getResources().getColor(android.R.color.darker_gray));
        mEndLineColor = typedArray.getColor(R.styleable.TimelineView_timeline_end_line_color, getResources().getColor(android.R.color.darker_gray));
        mMarkerColor = typedArray.getColor(R.styleable.TimelineView_timeline_marker_color, getThemeColor(R.attr.colorAccent, getContext()));
        mLineWidth = typedArray.getDimensionPixelSize(R.styleable.TimelineView_timeline_line_width, Utils.dp2px(2F));
        mLineOrientation = typedArray.getInt(R.styleable.TimelineView_timeline_line_orientation, LineOrientation.VERTICAL);
        mLinePadding = typedArray.getDimensionPixelSize(R.styleable.TimelineView_timeline_line_padding, 0);
        mLineStyle = typedArray.getInt(R.styleable.TimelineView_timeline_line_style, LineStyle.NORMAL);
        mLineStyleDashLength = typedArray.getDimensionPixelSize(R.styleable.TimelineView_timeline_line_style_dash_length, Utils.dp2px(8F));
        mLineStyleDashGap = typedArray.getDimensionPixelSize(R.styleable.TimelineView_timeline_line_style_dash_gap, Utils.dp2px(4F));
        typedArray.recycle();

/*        if (isInEditMode()) {
            mDrawStartLine = true;
            mDrawEndLine = true;
        }*/

        if (mMarker == null) {
            mMarker = getResources().getDrawable(R.drawable.marker);
        }

        initTimeline();
        initLinePaint();

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //Width measurements of the width and height and the inside view of child controls
        int w = mMarkerSize + getPaddingLeft() + getPaddingRight();
        int h = mMarkerSize + getPaddingTop() + getPaddingBottom();

        // Width and height to determine the final view through a systematic approach to decision-making
        int widthSize = resolveSizeAndState(w, widthMeasureSpec, 0);
        int heightSize = resolveSizeAndState(h, heightMeasureSpec, 0);

        setMeasuredDimension(widthSize, heightSize);
        initTimeline();
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        initTimeline();
    }

    private void initTimeline() {

        int pLeft = getPaddingLeft();
        int pRight = getPaddingRight();
        int pTop = getPaddingTop();
        int pBottom = getPaddingBottom();

        int width = getWidth();// Width of current custom view
        int height = getHeight();

        int cWidth = width - pLeft - pRight;// Circle width
        int cHeight = height - pTop - pBottom;

        int markSize = Math.min(mMarkerSize, Math.min(cWidth, cHeight));

        mMarker.setColorFilter(mMarkerColor, PorterDuff.Mode.SRC_ATOP);

        if (mMarkerInCenter) { //Marker in center is true

            if (mMarker != null) {
                mMarker.setBounds((width / 2) - (markSize / 2), (height / 2) - (markSize / 2) + mMarkerPaddingTop - mMarkerPaddingBottom, (width / 2) + (markSize / 2), (height / 2) + (markSize / 2) + mMarkerPaddingTop - mMarkerPaddingBottom);
                mBounds = mMarker.getBounds();
            }

        } else { //Marker in center is false

            if (mMarker != null) {
                mMarker.setBounds(pLeft, pTop + mMarkerPaddingTop - mMarkerPaddingBottom, pLeft + markSize, pTop + markSize + mMarkerPaddingTop - mMarkerPaddingBottom);
                mBounds = mMarker.getBounds();
            }
        }

        if (mLineOrientation == LineOrientation.HORIZONTAL) {

            if (mDrawStartLine) {
                mStartLineStartX = pLeft;
                mStartLineStartY = mBounds.centerY();
                mStartLineStopX = mBounds.left - mLinePadding;
                mStartLineStopY = mBounds.centerY();
            }

            if (mDrawEndLine) {
                mEndLineStartX = mBounds.right + mLinePadding;
                mEndLineStartY = mBounds.centerY();
                mEndLineStopX = getWidth();
                mEndLineStopY = mBounds.centerY();
            }
        } else {

            if (mDrawStartLine) {
                mStartLineStartX = mBounds.centerX();

                if (mLineStyle == LineStyle.DASHED) {
                    mStartLineStartY = pTop - mLineStyleDashLength;
                } else {
                    mStartLineStartY = pTop;
                }

                mStartLineStopX = mBounds.centerX();
                mStartLineStopY = mBounds.top - mLinePadding;
            }

            if (mDrawEndLine) {
                mEndLineStartX = mBounds.centerX();
                mEndLineStartY = mBounds.bottom + mLinePadding;
                mEndLineStopX = mBounds.centerX();
                mEndLineStopY = getHeight();
            }
        }

        invalidate();
    }

    private void initLinePaint() {
        mLinePaint.setAlpha(0);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(mStartLineColor);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(mLineWidth);

        if (mLineStyle == LineStyle.DASHED)
            mLinePaint.setPathEffect(new DashPathEffect(new float[]{(float) mLineStyleDashLength, (float) mLineStyleDashGap}, 0.0f));
        else
            mLinePaint.setPathEffect(new PathEffect());

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mMarker != null) {
            mMarker.draw(canvas);
        }

        if (mDrawStartLine) {
            mLinePaint.setColor(mStartLineColor);
            invalidate();
            canvas.drawLine(mStartLineStartX, mStartLineStartY, mStartLineStopX, mStartLineStopY, mLinePaint);
        }

        if (mDrawEndLine) {
            mLinePaint.setColor(mEndLineColor);
            invalidate();
            canvas.drawLine(mEndLineStartX, mEndLineStartY, mEndLineStopX, mEndLineStopY, mLinePaint);
        }
    }

    /**
     * Sets marker.
     *
     * @param marker will set marker drawable to timeline
     */
    public void setMarker(Drawable marker) {
        mMarker = marker;
        initTimeline();
    }

    public Drawable getMarker() {
        return mMarker;
    }

    /**
     * Sets marker.
     *
     * @param marker will set marker drawable to timeline
     * @param color  with a color
     */
    public void setMarker(Drawable marker, int color) {
        mMarker = marker;
        mMarker.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        initTimeline();
    }

    /**
     * Sets marker color.
     *
     * @param color the color
     */
    public void setMarkerColor(int color) {
        mMarker.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        initTimeline();
    }

    /**
     * Sets start line.
     *
     * @param color    the color of the start line
     * @param viewType the view type
     */
    public void setStartLineColor(int color, int viewType) {
        mStartLineColor = color;
        initLine(viewType);
    }

    public int getStartLineColor() {
        return mStartLineColor;
    }

    /**
     * Sets end line.
     *
     * @param color    the color of the end line
     * @param viewType the view type
     */
    public void setEndLineColor(int color, int viewType) {
        mEndLineColor = color;
        initLine(viewType);
    }

    public int getEndLineColor() {
        return mEndLineColor;
    }

    /**
     * Sets marker size.
     *
     * @param markerSize the marker size
     */
    public void setMarkerSize(int markerSize) {
        mMarkerSize = markerSize;
        initTimeline();
    }

    public int getMarkerSize() {
        return mMarkerSize;
    }

    public void setMarkerPaddingTop(int markerPaddingTop) {
        mMarkerPaddingTop = markerPaddingTop;
        initTimeline();
    }

    public int getMarkerPaddingTop() {
        return mMarkerPaddingTop;
    }

    public void setMarkerPaddingBottom(int markerPaddingBottom) {
        mMarkerPaddingBottom = markerPaddingBottom;
        initTimeline();
    }

    public int getMarkerPaddingBottom() {
        return mMarkerPaddingBottom;
    }

    public boolean isMarkerInCenter() {
        return mMarkerInCenter;
    }

    public void setMarkerInCenter(boolean markerInCenter) {
        this.mMarkerInCenter = markerInCenter;
        initTimeline();
    }

    /**
     * Sets line width.
     *
     * @param lineWidth the line width
     */
    public void setLineWidth(int lineWidth) {
        mLineWidth = lineWidth;
        initTimeline();
    }

    public int getLineWidth() {
        return mLineWidth;
    }

    /**
     * Sets line padding
     *
     * @param padding the line padding
     */
    public void setLinePadding(int padding) {
        mLinePadding = padding;
        initTimeline();
    }

    public int getLineOrientation() {
        return mLineOrientation;
    }

    public void setLineOrientation(int lineOrientation) {
        this.mLineOrientation = lineOrientation;
    }

    public int getLineStyle() {
        return mLineStyle;
    }

    public void setLineStyle(int lineStyle) {
        this.mLineStyle = lineStyle;
        initLinePaint();
    }

    public int getLineStyleDashLength() {
        return mLineStyleDashLength;
    }

    public void setLineStyleDashLength(int lineStyleDashLength) {
        this.mLineStyleDashLength = lineStyleDashLength;
        initLinePaint();
    }

    public int getLineStyleDashGap() {
        return mLineStyleDashGap;
    }

    public void setLineStyleDashGap(int lineStyleDashGap) {
        this.mLineStyleDashGap = lineStyleDashGap;
        initLinePaint();
    }

    public int getLinePadding() {
        return mLinePadding;
    }

    private void showStartLine(boolean show) {
        mDrawStartLine = show;
        initTimeline();
    }

    private void showEndLine(boolean show) {
        mDrawEndLine = show;
        initTimeline();
    }

    /**
     * Init line.
     *
     * @param viewType the view type
     */
    public void initLine(int viewType) {
        if (viewType == LineType.START) {
            showStartLine(false);
            showEndLine(true);
        } else if (viewType == LineType.END) {
            showStartLine(true);
            showEndLine(false);
        } else if (viewType == LineType.ONLYONE) {
            showStartLine(false);
            showEndLine(false);
        } else {
            showStartLine(true);
            showEndLine(true);
        }

        initTimeline();
    }

    /**
     * Gets timeline view type.
     *
     * @param position  the position of current item view
     * @param totalSize the total size of the items
     * @return the timeline view type
     */
    public static int getTimeLineViewType(int position, int totalSize) {
        if (totalSize == 1) {
            return LineType.ONLYONE;
        } else if (position == 0) {
            return LineType.START;
        } else if (position == totalSize - 1) {
            return LineType.END;
        } else {
            return LineType.NORMAL;
        }
    }

    @ColorInt
    private int getThemeColor(@AttrRes int attrResId, @NonNull Context context) {
        TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(attrResId, value, true);
        return value.data;
    }
}
