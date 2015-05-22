package com.myGwtApplication.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.myGwtApplication.client.MyGwtApplicationService;

public class MyGwtApplicationServiceImpl extends RemoteServiceServlet implements MyGwtApplicationService {
    public String getMessage(String msg) {
        return "Client said: \"" + msg + "\"<br>Server answered: \"Hi!\"";
    }
}