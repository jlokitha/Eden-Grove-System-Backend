package lk.ijse.Green_Shadow_Backend.customeObj;

import lombok.Builder;

@Builder
public record DashboardCounts(Integer fieldCount, Integer cropCount, Integer vehicleCount, Integer staffCount) {
}
