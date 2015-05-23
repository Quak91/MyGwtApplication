package com.myGwtApplication.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import java.util.ArrayList;

public class MyGwtApplication implements EntryPoint {

    private FlexTable flexTable;
    private Button btnCreate;

    public void onModuleLoad() {

        flexTable = new FlexTable();
        btnCreate = new Button("Dodaj przedmiot");

        flexTable.setText(0, 0, "Przedmiot");
        for(int i=1;i<=10;i++)
            flexTable.setText(0, i, i+"");
        flexTable.setText(0,11, "Edytuj");
        flexTable.setText(0,12, "Usuń");

        RootPanel.get("placeholder").add(flexTable);
        RootPanel.get("placeholder").add(btnCreate);

        btnCreate.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                new NewItemDialogBox().center();
            }
        });

        /*
        final Button button = new Button("Click me");
        final Label label = new Label();
        button.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (label.getText().equals("")) {
                    MyGwtApplicationService.App.getInstance().getMessage("Hello, World!", new MyAsyncCallback(label));
                } else {
                    label.setText("");
                }
            }
        });
        RootPanel.get("slot1").add(button);
        RootPanel.get("slot2").add(label);*/
    }

    //DialogBox do tworzenia nowego przedmiotu
    private static class NewItemDialogBox extends DialogBox {
        public NewItemDialogBox() {
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
                    NewItemDialogBox.this.hide();
                }
            });

            //tworzenie nowego przedmiotu
            btnOk.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    Subject subject;
                    boolean isValid = true;
                    if(textBoxItemName.getValue() != "") {
                        //TODO sprawdzić czy przedmiot o takiej nazwie już istnieje
                        ArrayList<Integer> grades = new ArrayList<Integer>(10);
                        try {
                            for (int i = 0; i < 10; i++) {
                                grades.add(i, gradesIntBoxes.get(i).getValueOrThrow());
                                Integer grade = gradesIntBoxes.get(i).getValue();
                                if(grade != null && (grade < 1 || grade > 6)) { // walidacja ocen (skala 1-6)
                                    isValid = false;
                                }
                            }
                            subject = new Subject(textBoxItemName.getValue(), grades);
                        } catch (Exception e) {
                            isValid = false;
                        }
                        if(!isValid) {
                            // błąd - nieprawidłowe oceny
                            NewItemDialogBox.this.setText("Błąd! Nieprawidłowe oceny!");
                        } else {
                            //TODO wysłać na serwer
                        }
                    } else {
                        // błąd - brak nazwy przedmiotu
                        NewItemDialogBox.this.setText("Błąd! Brak nazwy przedmiotu!");
                    }
                }
            });

            NewItemDialogBox.this.setWidget(vPanel);
        }
    }

    private static class MyAsyncCallback implements AsyncCallback<String> {
        private Label label;

        public MyAsyncCallback(Label label) {
            this.label = label;
        }

        public void onSuccess(String result) {
            label.getElement().setInnerHTML(result);
        }

        public void onFailure(Throwable throwable) {
            label.setText("Failed to receive answer from server!");
        }
    }
}
