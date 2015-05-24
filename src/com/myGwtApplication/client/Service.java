package com.myGwtApplication.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.ArrayList;

@RemoteServiceRelativePath("MyGwtApplicationService")
public interface Service extends RemoteService {

    ArrayList<Subject> getAllSubjects();
    Integer addSubject(Subject subject);
    Integer removeSubject(int index);
}
