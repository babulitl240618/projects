package com.mayojava.chathead;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by mayowa on 14/01/2017.
 */

public class ChatHeadService extends Service {
    private WindowManager mWindowManager;
    private View mChatView;

    public ChatHeadService() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //inflate the chat head layout
        mChatView = LayoutInflater.from(this)
                .inflate(R.layout.layout_chat_head, null);

        displayChatHeadOnScreenTopLeftCorner();

        setChatHeadCloseButton();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mChatView != null) {
            mWindowManager.removeView(mChatView);
        }
    }

    private void displayChatHeadOnScreenTopLeftCorner() {
        //Add the view to the window
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );


        //specify the chat head position. Initially view will be added to top-left corner

        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = 100;

        //Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mChatView, params);

        //add touch listener to chat head
        dragAndMoveChatHeadOnTouch(params);
    }

    private void setChatHeadCloseButton() {
        ImageView closeButton = (ImageView) mChatView.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopSelf();
            }
        });
    }

    private void dragAndMoveChatHeadOnTouch(final WindowManager.LayoutParams params) {
        final ImageView chatHeadImage = (ImageView) mChatView.findViewById(R.id.chat_head_profile_iv);

        chatHeadImage.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            private int lastAction;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;

                        initialTouchX = motionEvent.getRawX();
                        initialTouchY = motionEvent.getRawY();

                        lastAction = motionEvent.getAction();
                        return true;

                    case MotionEvent.ACTION_UP:
                        if (lastAction == MotionEvent.ACTION_DOWN) {
                            //Open the chat conversation
                            Intent intent = new Intent(ChatHeadService.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            //close service and remove chat head
                            stopSelf();
                        }

                        lastAction = motionEvent.getAction();
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        //calculate the X and Y coordinates of the view
                        params.x = initialX + (int) (motionEvent.getRawX() - initialTouchX);
                        params.y = initialY + (int) (motionEvent.getRawY() - initialTouchY);

                        mWindowManager.updateViewLayout(mChatView, params);
                        lastAction = motionEvent.getAction();
                        return true;
                }

                return false;
            }
        });
    }
}
