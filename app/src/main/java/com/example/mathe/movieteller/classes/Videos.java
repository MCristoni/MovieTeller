package com.example.mathe.movieteller.classes;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;


public class Videos extends ExpandableGroup<ResultsTrailer> {

    public Videos(String title, List<ResultsTrailer> items) {
        super(title, items);
    }
}
