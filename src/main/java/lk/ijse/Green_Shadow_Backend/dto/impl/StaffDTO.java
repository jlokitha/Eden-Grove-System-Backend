package lk.ijse.Green_Shadow_Backend.dto.impl;

import lk.ijse.Green_Shadow_Backend.dto.SuperDTO;
import lk.ijse.Green_Shadow_Backend.entity.impl.Equipment;
import lk.ijse.Green_Shadow_Backend.entity.impl.Field;
import lk.ijse.Green_Shadow_Backend.entity.impl.MonitoringLog;
import lk.ijse.Green_Shadow_Backend.entity.impl.Vehicle;
import lk.ijse.Green_Shadow_Backend.enums.Designation;
import lk.ijse.Green_Shadow_Backend.enums.Gender;
import lk.ijse.Green_Shadow_Backend.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StaffDTO implements SuperDTO {
    private String id;
    private String name;
    private LocalDate dob;
    private Gender gender;
    private Designation designation;
    private Role role;
    private String email;
    private String mobile;
    private String address;
    private String postalCode;
    private Timestamp joinedDate;
    private List<Field> fields;
    private List<Vehicle> vehicles;
    private List<Equipment> equipments;
    private List<MonitoringLog> monitoringLogs;
}