package com.myGwtApplication.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;

public interface ServiceAsync {

    void getAllSubjects(AsyncCallback<ArrayList<Subject>> async);

}
