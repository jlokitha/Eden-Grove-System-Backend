package lk.ijse.Green_Shadow_Backend.entity.impl;

import jakarta.persistence.*;
import lk.ijse.Green_Shadow_Backend.entity.SuperEntity;
import lk.ijse.Green_Shadow_Backend.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "vehicle")
@Entity
public class Vehicle implements SuperEntity {
    @Id
    private String vehicleCode;
    @Column(length = 20, nullable = false, unique = true)
    private String licensePlateNo;
    @Column(length = 50, nullable = false)
    private String category;
    @Column(length = 30, nullable = false)
    private String fuelType;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(length = 200, nullable = false)
    private String remark;
    @ManyToOne
    @JoinColumn(name = "assigned_staff_id")
    private Staff staff;
}
