package com.hackathon.Lops.controllers;

import com.hackathon.Lops.beans.SchedulerCombinationDto;
import com.hackathon.Lops.services.SchedulerService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scheduler")
@CrossOrigin("*")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SchedulerController {
    @Autowired
    SchedulerService schedulerService;
    @PostMapping
    public ResponseEntity<String> createCombination(@RequestBody SchedulerCombinationDto schedulerCombinationDto){
        schedulerService.createNewCombination(schedulerCombinationDto);
        return ResponseEntity.ok().body("done");
    }

}
