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
                            //próba wysłania nowego przedmiotu na serwer
                            service.addSubject(subject, new AddSubjectCallback());
                        }
                    }
                }
            });

            CreateNewSubjectDialogBox.this.setWidget(vPanel);
        }
    }

    //DialogBox do edytowania przedmiotu
    private class EditSubjectDialogBox extends DialogBox {
        public EditSubjectDialogBox(final int rowIndex, final Subject subject) {
            setText("Edytuj przedmiot");
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
            textBoxItemName.setValue(subject.getName());
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
                if(subject.getGrades()[i] != 0) {
                    gradesIntBoxes.get(i).setValue(subject.getGrades()[i]);
                }
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
                    hide();
                }
            });

            btnOk.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    //TODO walidacja + edycja po stronie serwera
                    hide();
                }
            });

            setWidget(vPanel);
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
                    // usuwanie rekordu
                    flexTable.setWidget(i+1, 12, new Button("Usuń", new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            int rowIndex = flexTable.getCellForEvent(event).getRowIndex() - 1;
                            service.removeSubject(rowIndex, new RemoveSubjectCallback());
                        }
                    }));
                    // edytowanie rekordu
                    flexTable.setWidget(i+1, 11, new Button("Edytuj", new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            int rowIndex = flexTable.getCellForEvent(event).getRowIndex();
                            String subjectName = flexTable.getText(rowIndex, 0);
                            int[] grades = new int[10];
                            for (int i=0; i<10; i++) {
                                if(flexTable.getText(rowIndex, i+1) != "") {
                                    grades[i] = Integer.parseInt(flexTable.getText(rowIndex, i+1));
                                }
                            }
                            new EditSubjectDialogBox(rowIndex - 1, new Subject(subjectName, grades)).center();
                        }
                    }));
                }
            }
        }
        @Override
        public void onFailure(Throwable caught) {

        }
    }

    //wysyłanie nowego rekordu na serwer
    private class AddSubjectCallback implements AsyncCallback<Integer> {

        @Override
        public void onFailure(Throwable caught) {

        }

        @Override
        public void onSuccess(Integer result) {
            //result 0 <- OK
            //result 1 <- przedmiot o takiej nazwie już istnieje
            if(result == 0) {
                GWT.log("LOG: new subject added to database");
                //po dodaniu rekordu do bazy pobieram całą tabelę z serwera
                service.getAllSubjects(new GetAllSubjectsCallback());
            } else {
                Window.alert("Błąd! Przedmiot o takiej nazwie już istnieje!");
                GWT.log("LOG: subject already exists!");
            }
        }
    }

    //usuwnie rekordu z bazy
    private class RemoveSubjectCallback implements AsyncCallback<Integer> {

        @Override
        public void onFailure(Throwable caught) {

        }

        @Override
        public void onSuccess(Integer index) {
            //po usunięciu z bazy usuwam z flextable
            GWT.log("LOG: deleted index: "+index);
            flexTable.removeRow(index + 1);
        }
    }
}
