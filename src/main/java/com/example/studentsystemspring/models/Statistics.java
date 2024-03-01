package com.example.studentsystemspring.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Statistics {
    final double average;
    final double median;
    final double highest;
    final double lowest;

    @Override
    public String toString() {
        return ("Average: " + average + "\n")
        + ("Median: " + median + "\n")
        + ("Highest: " + highest + "\n")
        + ("Lowest: " + lowest + "\n");
    }
}
