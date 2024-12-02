package lk.ijse.Green_Shadow_Backend.entity.impl;

import jakarta.persistence.*;
import lk.ijse.Green_Shadow_Backend.entity.SuperEntity;
import lk.ijse.Green_Shadow_Backend.enums.Designation;
import lk.ijse.Green_Shadow_Backend.enums.Gender;
import lk.ijse.Green_Shadow_Backend.enums.StaffStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "staff")
@Entity
public class Staff implements SuperEntity {
    @Id
    private String id;
    @Column(length = 150, nullable = false)
    private String name;
    @Column(nullable = false)
    private LocalDate dob;
    @Column(length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(length = 30, nullable = false)
    @Enumerated(EnumType.STRING)
    private Designation designation;
    @Column(length = 30, nullable = false)
    private String role;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(length = 15, nullable = false)
    private String mobile;
    @Column(nullable = false)
    private String address;
    @Column(length = 10, nullable = false)
    private String postalCode;
    @CreationTimestamp
    private Timestamp joinedDate;
    @Column(length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private StaffStatus status;
    @ToString.Exclude
    @ManyToMany(mappedBy = "staffs", fetch = FetchType.LAZY)
    private List<Field> fields;
    @ToString.Exclude
    @OneToMany(mappedBy = "staff", fetch = FetchType.LAZY)
    private List<Vehicle> vehicles;
    @ToString.Exclude
    @ManyToMany(mappedBy = "staffs", fetch = FetchType.LAZY)
    private List<MonitoringLog> monitoringLogs;
}
