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
    private String fieldName;
    private Double fieldSize;
    private Point fieldLocation;
    @Lob
    private String fieldImage1;
    @Lob
    private String fieldImage2;
    @OneToMany(mappedBy = "field")
    private List<Crop> crops;
    @ManyToMany(mappedBy = "fields")
    private List<Staff> allocatedStaff;
    @OneToMany(mappedBy = "field")
    private List<Equipment> equipments;
    @ManyToMany(mappedBy = "fields")
    private List<MonitoringLog> monitoringLogs;
}
