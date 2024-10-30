package lk.ijse.Green_Shadow_Backend.entity.impl;

import jakarta.persistence.*;
import lk.ijse.Green_Shadow_Backend.entity.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "field")
@Entity
public class Field implements SuperEntity {
    @Id
    private String fCode;
    @Column(nullable = false)
    private String fieldName;
    @Column(nullable = false)
    private Double fieldSize;
    @Column(nullable = false)
    private Point fieldLocation;
    @Column(nullable = false)
    @Lob
    private String fieldImage1;
    @Column(nullable = false)
    @Lob
    private String fieldImage2;
    @OneToMany(mappedBy = "field")
    private List<Crop> crops;
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "field_staff",
            joinColumns = @JoinColumn(name = "field_id"),
            inverseJoinColumns = @JoinColumn(name = "staff_id")
    )
    private List<Staff> staffs;
    @OneToMany(mappedBy = "field", orphanRemoval = true)
    private List<Equipment> equipments;
    @ManyToMany(mappedBy = "fields")
    private List<MonitoringLog> monitoringLogs;
}