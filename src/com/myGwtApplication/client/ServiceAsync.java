package com.myGwtApplication.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;

public interface ServiceAsync {

    void getAllSubjects(AsyncCallback<ArrayList<Subject>> async);
    void addSubject(Subject subject, AsyncCallback<Integer> async);
    void removeSubject(int index, AsyncCallback<Integer> async);
    void updateSubject(int index, Subject subject, AsyncCallback<Void> async);
}
