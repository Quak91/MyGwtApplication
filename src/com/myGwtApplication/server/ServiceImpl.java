package com.myGwtApplication.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.myGwtApplication.client.Service;
import com.myGwtApplication.client.Subject;
import java.util.ArrayList;

public class ServiceImpl extends RemoteServiceServlet implements Service {

    private ArrayList<Subject> subjects;

    public ServiceImpl() {
        super();
        subjects = new ArrayList<Subject>();

        subjects.add(new Subject("Matematyka", new int[]{3,2,4,1,5,6,2,1,3,3}));
        subjects.add(new Subject("Geografia", new int[]{1,3,4,6,2,0,1,5,0,3}));
        subjects.add(new Subject("Język angielski", new int[]{5,2,3,0,2,4,1,5,3,3}));
    }

    @Override
    public ArrayList<Subject> getAllSubjects() {
        return subjects;
    }

    @Override
    public Integer addSubject(Subject subject) {
        for(int i=0; i<subjects.size(); i++) {
            if(subjects.get(i).getName().equals(subject.getName())) {
                return 1; //przedmiot o takiej nazwie już istnieje
            }
        }
        subjects.add(subject);
        return 0;
    }
}