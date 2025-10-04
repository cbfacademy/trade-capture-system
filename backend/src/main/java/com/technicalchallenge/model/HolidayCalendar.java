package com.technicalchallenge.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "holiday_calendar")
public class HolidayCalendar {// Entity to represent a Holiday Calendar
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String holidayCalendar;
}
