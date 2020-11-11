package com.ceglarski.vacation.order;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VacationOrder {

    public static final String ID_FIELD = "id";
    public static final String STATUS_FIELD = "status";
    public static final String NAME_FIELD = "name";
    public static final String DATE_AVAILABLE_FIELD = "dateAvailable";
    public static final String EMPLOYEE_FIELD = "employee";
    public static final String LEADER_FIELD = "leader";
    public static final String MANAGER_FIELD = "manager";

    private Integer id;
    private String status;
    private String name;
    private boolean dateAvailable;
    private String employee;
    private String leader;
    private String manager;
}
