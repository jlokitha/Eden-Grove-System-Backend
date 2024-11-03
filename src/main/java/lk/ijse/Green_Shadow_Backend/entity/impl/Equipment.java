package lk.ijse.Green_Shadow_Backend.entity.impl;

import jakarta.persistence.*;
import lk.ijse.Green_Shadow_Backend.entity.SuperEntity;
import lk.ijse.Green_Shadow_Backend.enums.EquipmentType;
import lk.ijse.Green_Shadow_Backend.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "equipment")
@Entity
public class Equipment implements SuperEntity {
    @Id
    private String equipmentId;
    @Column(length = 100, nullable = false)
    private String name;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EquipmentType type;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_staff_id", referencedColumnName = "id")
    private Staff staff;
    @ManyToOne
    @JoinColumn(name = "assigned_field_id")
    private Field field;
}
