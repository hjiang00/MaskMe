package edu.ucsb.ece.ece150.maskme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;

public class MaskedImageView extends android.support.v7.widget.AppCompatImageView {

    private enum MaskType {
        NOMASK, FIRST, SECOND
    }

    private SparseArray<Face> faces = null;
    private MaskType maskType = MaskType.NOMASK;
    Paint mPaint = new Paint();
    private Bitmap mBitmap;

    public MaskedImageView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mBitmap = ((BitmapDrawable) getDrawable()).getBitmap();
        if(mBitmap == null){
            return;
        }
        double viewWidth = canvas.getWidth();
        double viewHeight = canvas.getHeight();
        double imageWidth = mBitmap.getWidth();
        double imageHeight = mBitmap.getHeight();
        double scale = Math.min(viewWidth / imageWidth, viewHeight / imageHeight);

        drawBitmap(canvas, scale);

        switch (maskType){
            case FIRST:
                drawFirstMaskOnCanvas(canvas, scale);
                break;
            case SECOND:
                drawSecondMaskOnCanvas(canvas, scale);
                break;
        }
    }

    protected void drawFirstMask(SparseArray<Face> faces){
        this.faces = faces;
        this.maskType = MaskType.FIRST;
        this.invalidate();
    }

    protected void drawSecondMask(SparseArray<Face> faces){
        this.faces = faces;
        this.maskType = MaskType.SECOND;
        this.invalidate();
    }

    private void drawBitmap(Canvas canvas, double scale) {
        double imageWidth = mBitmap.getWidth();
        double imageHeight = mBitmap.getHeight();

        Rect destBounds = new Rect(0, 0, (int)(imageWidth * scale), (int)(imageHeight * scale));
        canvas.drawBitmap(mBitmap, null, destBounds, null);
    }

    private void drawFirstMaskOnCanvas(Canvas canvas, double scale) {

        // TODO: Draw first type of mask on the static photo
        // 1. set properties of mPaint
        // 2. get positions of faces and draw masks on faces.
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.monkey);
//        int scaleWidth = (int) (bitmap.getWidth() * scale);
//        int scaleHeight = (int) (bitmap.getHeight() * scale);
//        Bitmap resizedbitmap = Bitmap.createScaledBitmap(bitmap, scaleWidth, scaleHeight, false);
        for (int i = 0; i < faces.size(); i++){
            Face face = faces.valueAt(i);
            int scaleWidth = (int) (face.getWidth() / 20);
//            int scaleHeight = (int) (face.getHeight() / 20);
//            Log.e("scaleWidth", Integer.toString(scaleWidth));
            Bitmap resizedbitmap = Bitmap.createScaledBitmap(bitmap, scaleWidth, scaleWidth, false);
            int j = 0;
            for(Landmark landmark : face.getLandmarks()){
                if (j == 7) {
                    int cx = (int) (landmark.getPosition().x * scale);
                    int cy = (int) (landmark.getPosition().y * scale);
                    int tx = cx - (resizedbitmap.getWidth() * 1/2);
                    int ty = cy - (resizedbitmap.getHeight() * 1/2);
                    canvas.drawBitmap(resizedbitmap, tx, ty, mPaint);
                }
                j = j + 1;
            }

        }


    }

    private void drawSecondMaskOnCanvas( Canvas canvas, double scale ) {
        // TODO: Draw second type of mask on the static photo
        // 1. set properties of mPaint
        // 2. get positions of faces and draw masks on faces.

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.star);
//        int scaleWidth = (int) (bitmap.getWidth() * scale);
//        int scaleHeight = (int) (bitmap.getHeight() * scale);
//        Bitmap resizedbitmap = Bitmap.createScaledBitmap(bitmap, scaleWidth, scaleHeight, false);
        for (int i = 0; i < faces.size(); i++){
            Face face = faces.valueAt(i);
            int scaleWidth = (int) (face.getWidth() / 20);
            Bitmap resizedbitmap = Bitmap.createScaledBitmap(bitmap, scaleWidth, scaleWidth, false);
            int j = 0;
            for(Landmark landmark : face.getLandmarks()){
                if (j == 4 || j == 3) {
                    int cx = (int) (landmark.getPosition().x * scale);
                    int cy = (int) (landmark.getPosition().y * scale);
                    int tx = cx - (resizedbitmap.getWidth() * 1/2);
                    int ty = cy - (resizedbitmap.getHeight() * 1/2);
                    canvas.drawBitmap(resizedbitmap, tx, ty, mPaint);
                }
                j = j + 1;
            }

        }
    }


    public void noFaces() {
        faces = null;
    }

    public void reset() {
        faces = null;
        setImageBitmap(null);
    }
}
