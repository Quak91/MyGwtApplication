package com.myGwtApplication.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

import java.text.ParseException;
import java.util.ArrayList;

public class MyGwtApplication implements EntryPoint {

    private FlexTable flexTable;
    private Button btnCreate;
    private ServiceAsync service = GWT.create(Service.class);

    public void onModuleLoad() {

        flexTable = new FlexTable();
        btnCreate = new Button("Dodaj przedmiot");

        flexTable.setText(0, 0, "Przedmiot");
        for(int i=1;i<=10;i++)
            flexTable.setText(0, i, i+"");
        flexTable.setText(0,11, "Edytuj");
        flexTable.setText(0, 12, "Usuń");

        RootPanel.get("placeholder").add(flexTable);
        RootPanel.get("placeholder").add(btnCreate);

        btnCreate.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                new CreateNewSubjectDialogBox().center();
            }
        });

        service.getAllSubjects(new GetAllSubjectsCallback());
    }

    //DialogBox do tworzenia nowego przedmiotu
    private class CreateNewSubjectDialogBox extends DialogBox {
        public CreateNewSubjectDialogBox() {
            setText("Nowy przedmiot");
            setAnimationEnabled(true);
            setGlassEnabled(true);

            Button btnOk = new Button("OK");
            Button btnCancel = new Button("Anuluj");
            btnOk.setWidth("70");
            btnCancel.setWidth("70");

            VerticalPanel vPanel = new VerticalPanel();
            vPanel.setWidth("400");
            vPanel.setSpacing(15);
            vPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

            HorizontalPanel hPanelItemName = new HorizontalPanel();
            hPanelItemName.setSpacing(15);
            final TextBox textBoxItemName = new TextBox();
            hPanelItemName.add(new Label("Nazwa przedmiotu:"));
            hPanelItemName.add(textBoxItemName);

            HorizontalPanel hPanelGrades = new HorizontalPanel();
            hPanelGrades.setSpacing(5);
            hPanelGrades.add(new Label("Oceny:"));

            //lista intboxów z ocenami
            final ArrayList<IntegerBox> gradesIntBoxes = new ArrayList<IntegerBox>(10);
            for (int i=0;i<10;i++) {
                gradesIntBoxes.add(new IntegerBox());
                gradesIntBoxes.get(i).setWidth("20");
                hPanelGrades.add(gradesIntBoxes.get(i));
            }

            HorizontalPanel hPanelButtons = new HorizontalPanel();
            hPanelButtons.setSpacing(15);
            hPanelButtons.add(btnOk);
            hPanelButtons.add(btnCancel);

            vPanel.add(hPanelItemName);
            vPanel.add(hPanelGrades);
            vPanel.add(hPanelButtons);

            btnCancel.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    CreateNewSubjectDialogBox.this.hide();
                }
            });

            //tworzenie nowego przedmiotu
            btnOk.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    boolean isValid = true;
                    if(textBoxItemName.getValue() == "") {
                        Window.alert("Błąd! Brak nazwy przedmiotu!");
                    } else {
                        //TODO sprawdzić czy przedmiot o takiej nazwie już istnieje
                        int[] grades = new int[10];
                        for(int i=0; i<10; i++) {
                            try {
                                if(gradesIntBoxes.get(i).getValueOrThrow() != null) {
                                    grades[i] = gradesIntBoxes.get(i).getValueOrThrow();
                                    if(grades[i] < 1 || grades[i] > 6)
                                        isValid = false;
                                }
                            } catch (ParseException e) {
                                isValid = false;
                            }
                        }
                        if(!isValid) {
                            Window.alert("Błąd! Nieprawidłowe oceny!");
                        } else {
                            Subject subject = new Subject(textBoxItemName.getValue(), grades);
                            hide();
                            GWT.log("LOG: new subject created");
                            //wysyłanie nowego rekordu na serwer
                            service.addSubject(subject, new AddSubjectCallback());
                        }
                    }
                }
            });

            CreateNewSubjectDialogBox.this.setWidget(vPanel);
        }
    }

    //odbieranie wszystkich rekordów z serwera
    private class GetAllSubjectsCallback implements  AsyncCallback<ArrayList<Subject>> {

        @Override
        public void onSuccess(ArrayList<Subject> result) {
            GWT.log("LOG: received all subjects from server");
            for(int i=0; i<result.size(); i++) {
                flexTable.setText(i+1, 0, result.get(i).getName());
                for(int j=0;j<10;j++) {
                    if(result.get(i).getGrades()[j] != 0) {
                        flexTable.setText(i + 1, j + 1, result.get(i).getGrades()[j] + "");
                    }
                }
            }
        }
        @Override
        public void onFailure(Throwable caught) {

        }
    }

    //wysyłanie nowego rekordu na serwer
    private class AddSubjectCallback implements AsyncCallback<Void> {

        @Override
        public void onFailure(Throwable caught) {

        }

        @Override
        public void onSuccess(Void result) {
            //po dodaniu rekordu do bazy pobieram całą tabelę z serwera
            GWT.log("LOG: new subject added to database");
            service.getAllSubjects(new GetAllSubjectsCallback());
        }
    }
}
