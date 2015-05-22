package com.myGwtApplication.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("MyGwtApplicationService")
public interface MyGwtApplicationService extends RemoteService {
    String getMessage(String msg);

    public static class App {
        private static MyGwtApplicationServiceAsync ourInstance = GWT.create(MyGwtApplicationService.class);

        public static synchronized MyGwtApplicationServiceAsync getInstance() {
            return ourInstance;
        }
    }
}
