package lk.ijse.Green_Shadow_Backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/healthtest")
public class HealthTestController {
    /**
     * Health check endpoint to verify that the system is running successfully.
     *
     * @return a string message indicating the health status of the system
     */
    @GetMapping
    public String healthTest(){
        return "Green Shadow System is running successfully";
    }
}
