package com.mandiri.most.desktop.common.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.EventHandler;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.TreeSet;

import static java.time.temporal.ChronoUnit.DAYS;

public class MultiDatePicker
{

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final ObservableSet<LocalDate> selectedDates;
    private final DatePicker datePicker;
    private ObservableList<LocalDate> datesList;

    public MultiDatePicker()
    {
        this.selectedDates = FXCollections.observableSet(new TreeSet<>());
        this.datePicker = new DatePicker();
        setUpDatePicker();
    }

    public ObservableSet<LocalDate> getSelectedDates()
    {
        return this.selectedDates;
    }

    public DatePicker getDatePicker()
    {
        return this.datePicker;
    }

    private void setUpDatePicker()
    {
        this.datePicker.setConverter(new StringConverter<LocalDate>()
        {
            @Override
            public String toString(LocalDate date)
            {
                return (date == null) ? "" : DATE_FORMAT.format(date);
            }

            @Override
            public LocalDate fromString(String string)
            {
                return ((string == null) || string.isEmpty()) ? null : LocalDate.parse(string, DATE_FORMAT);
            }
        });

        EventHandler<MouseEvent> mouseClickedEventHandler = (MouseEvent clickEvent) ->
        {
            if (clickEvent.getButton() == MouseButton.PRIMARY)
            {
                if (!this.selectedDates.contains(this.datePicker.getValue()))
                {
                    this.selectedDates.add(datePicker.getValue());

                } else
                {
                    this.selectedDates.remove(this.datePicker.getValue());

                    this.datePicker.setValue(getClosestDateInTree(new TreeSet<>(this.selectedDates), this.datePicker.getValue()));

                }

            }
            this.datePicker.show();
            clickEvent.consume();
        };

        this.datePicker.setDayCellFactory((DatePicker param) -> new DateCell()
        {
            @Override
            public void updateItem(LocalDate item, boolean empty)
            {
                super.updateItem(item, empty);

                //...
                if (item != null && !empty)
                {
                    //...
                    addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickedEventHandler);
                } else
                {
                    //...
                    removeEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickedEventHandler);
                }

                if (selectedDates.contains(item))
                {

                    setStyle("-fx-background-color: rgba(3, 169, 244, 0.7);");

                } else
                {
                    setStyle(null);

                }
            }
        });

    }

    private static LocalDate getClosestDateInTree(TreeSet<LocalDate> dates, LocalDate date)
    {
        Long lower = null;
        Long higher = null;

        if (dates.isEmpty())
        {
            return null;
        }

        if (dates.size() == 1)
        {
            return dates.first();
        }

        if (dates.lower(date) != null)
        {
            lower = Math.abs(DAYS.between(date, dates.lower(date)));
        }
        if (dates.higher(date) != null)
        {
            higher = Math.abs(DAYS.between(date, dates.higher(date)));
        }

        if (lower == null)
        {
            return dates.higher(date);
        } else if (higher == null)
        {
            return dates.lower(date);
        } else if (lower <= higher)
        {
            return dates.lower(date);
        } else if (lower > higher)
        {
            return dates.higher(date);
        } else
        {
            return null;
        }
    }

}