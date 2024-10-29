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
    private String licensePlateNo;
    private String category;
    private String fuelType;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String remark;
    @ManyToOne
    @JoinColumn(name = "assigned_staff_id")
    private Staff staff;
}
