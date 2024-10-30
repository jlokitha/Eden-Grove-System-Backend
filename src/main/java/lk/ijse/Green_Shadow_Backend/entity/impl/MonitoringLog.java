package lk.ijse.Green_Shadow_Backend.entity.impl;

import jakarta.persistence.*;
import lk.ijse.Green_Shadow_Backend.entity.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "monitoring_log")
@Entity
public class MonitoringLog implements SuperEntity {
    @Id
    private String logCode;
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date logDate;
    @Column(nullable = false)
    private String observation;
    @Column(nullable = false)
    @Lob
    private String observedImage;
    @ManyToMany
    @JoinTable(
            name = "log_field",
            joinColumns = @JoinColumn(name = "log_id"),
            inverseJoinColumns = @JoinColumn(name = "field_id")
    )
    private List<Field> fields;
    @ManyToMany
    @JoinTable(
            name = "log_crop",
            joinColumns = @JoinColumn(name = "log_id"),
            inverseJoinColumns = @JoinColumn(name = "crop_id")
    )
    private List<Crop> crops;
    @ManyToMany
    @JoinTable(
            name = "log_staff",
            joinColumns = @JoinColumn(name = "log_id"),
            inverseJoinColumns = @JoinColumn(name = "staff_id")
    )
    private List<Staff> staffs;
}