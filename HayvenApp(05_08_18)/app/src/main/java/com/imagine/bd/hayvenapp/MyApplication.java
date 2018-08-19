package com.imagine.bd.hayvenapp;

import android.app.Application;

import com.imagine.bd.hayvenapp.utils.AppConstant;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class MyApplication extends Application {

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(AppConstant.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }
}
