package lk.ijse.Green_Shadow_Backend.entity.impl;


import jakarta.persistence.*;
import lk.ijse.Green_Shadow_Backend.entity.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "crop")
@Entity
public class Crop implements SuperEntity {
    @Id
    private String cropCode;
    private String commonName;
    private String scientificName;
    private String category;
    private String season;
    @Lob
    private String cropImage;
    @ManyToOne
    @JoinColumn(name = "field_id")
    private Field field;
    @ManyToMany(mappedBy = "crops")
    private List<MonitoringLog> monitoringLogs;
}
