package lk.ijse.Green_Shadow_Backend.entity.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lk.ijse.Green_Shadow_Backend.entity.SuperEntity;
import lk.ijse.Green_Shadow_Backend.enums.AvailabilityStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "crop")
@Entity
public class Crop implements SuperEntity {
    @Id
    private String cropCode;
    @Column(length = 100, nullable = false)
    private String commonName;
    @Column(length = 150, nullable = false, unique = true)
    private String scientificName;
    @Column(length = 50, nullable = false)
    private String category;
    @Column(length = 30, nullable = false)
    private String season;
    @Column(length = 15, nullable = false)
    @Enumerated(EnumType.STRING)
    private AvailabilityStatus status;
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String cropImage;
    @ToString.Exclude
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "field_id")
    private Field field;
    @ToString.Exclude
    @ManyToMany(mappedBy = "crops")
    private List<MonitoringLog> monitoringLogs;
}
