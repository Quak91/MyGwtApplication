package com.myGwtApplication.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MyGwtApplicationServiceAsync {
    void getMessage(String msg, AsyncCallback<String> async);
}
