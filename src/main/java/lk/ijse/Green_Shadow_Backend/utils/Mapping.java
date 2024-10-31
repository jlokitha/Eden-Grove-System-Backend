package lk.ijse.Green_Shadow_Backend.utils;

import lk.ijse.Green_Shadow_Backend.dto.impl.RegisterStaffDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.StaffDTO;
import lk.ijse.Green_Shadow_Backend.entity.impl.Staff;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Mapping {
    private final ModelMapper mapper;
    public Mapping(ModelMapper mapper) {
        this.mapper = mapper;
        this.mapper.addMappings(new PropertyMap<RegisterStaffDTO, Staff>() {
            @Override
            protected void configure() {
                skip(destination.getName());
                skip(destination.getGender());
                skip(destination.getDesignation());
                skip(destination.getRole());
                skip(destination.getAddress());
            }
        });
    }
    // Method to map the source object to the destination object
    public StaffDTO convertToDTO(Staff staff) {
        return mapper.map(staff, StaffDTO.class);
    }
    public Staff convertToEntity(StaffDTO dto) {
        return mapper.map(dto, Staff.class);
    }
    public Staff convertToEntity(RegisterStaffDTO dto) {
        return mapper.map(dto, Staff.class);
    }
    public List<StaffDTO> convertToDTO(List<Staff> staffs) {
        return mapper.map(staffs, new TypeToken<List<StaffDTO>>() {}.getType());
    }
}