package lk.ijse.Green_Shadow_Backend.entity.impl;

import jakarta.persistence.*;
import lk.ijse.Green_Shadow_Backend.entity.SuperEntity;
import lk.ijse.Green_Shadow_Backend.enums.AvailabilityStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.geo.Point;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "field")
@Entity
public class Field implements SuperEntity {
    @Id
    private String fCode;
    @Column(length = 100, nullable = false, unique = true)
    private String fieldName;
    @Column(nullable = false)
    private Double fieldSize;
    @Column(nullable = false)
    private Point fieldLocation;
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String fieldImage1;
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String fieldImage2;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AvailabilityStatus status;
    @ToString.Exclude
    @OneToMany(mappedBy = "field")
    private List<Crop> crops;
    @ToString.Exclude
    @ManyToMany
    @JoinTable(
            name = "field_staff",
            joinColumns = @JoinColumn(name = "field_id"),
            inverseJoinColumns = @JoinColumn(name = "staff_id")
    )
    private List<Staff> staffs;
    @OneToMany(mappedBy = "field")
    private List<Equipment> equipments;
    @ToString.Exclude
    @OneToMany(mappedBy = "field")
    private List<MonitoringLog> monitoringLogs;
}
