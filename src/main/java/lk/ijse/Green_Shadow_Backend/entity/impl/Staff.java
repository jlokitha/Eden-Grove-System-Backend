package lk.ijse.Green_Shadow_Backend.entity.impl;

import jakarta.persistence.*;
import lk.ijse.Green_Shadow_Backend.entity.SuperEntity;
import lk.ijse.Green_Shadow_Backend.enums.Designation;
import lk.ijse.Green_Shadow_Backend.enums.Gender;
import lk.ijse.Green_Shadow_Backend.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private LocalDate dob;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private Designation designation;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String mobile;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String postalCode;
    @CreationTimestamp
    private Timestamp joinedDate;
    @ManyToMany
    @JoinTable(
            name = "field_staff",
            joinColumns = @JoinColumn(name = "staff_id"),
            inverseJoinColumns = @JoinColumn(name = "field_id")
    )
    private List<Field> fields;
    @OneToMany(mappedBy = "staff")
    private List<Vehicle> vehicles;
    @OneToMany(mappedBy = "staff")
    private List<Equipment> equipments;
    @ManyToMany(mappedBy = "staffs")
    private List<MonitoringLog> monitoringLogs;
}
