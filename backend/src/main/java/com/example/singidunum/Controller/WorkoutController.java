package com.example.singidunum.Controller;

import com.example.singidunum.Model.Workout;
import com.example.singidunum.Repository.WorkoutRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class WorkoutController {
    @Autowired
    private WorkoutRepository workoutRepository;

    @GetMapping("/getAllWorkouts")
    //@RolesAllowed("admin")
    public ResponseEntity<List<Workout>> findAll() {
        return ResponseEntity.ok(workoutRepository.findAll());
    }
    @GetMapping("/getWorkout/{name}")
    //@RolesAllowed("admin")
    public ResponseEntity<Workout> findById(@PathVariable(value = "name") String id){
        Optional<Workout> userData = Optional.ofNullable(workoutRepository.findByName(id));
        return userData.map(workout -> new ResponseEntity<>(workout, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/addWorkout")
    //@RolesAllowed({"user", "admin"})
    public ResponseEntity<Workout> save(@RequestBody Workout workout){
        return ResponseEntity.ok(workoutRepository.save(workout));
    }
    @DeleteMapping("/deleteWorkout/{name}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("name") String id) {
        try {
            workoutRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAllNames")
    public ResponseEntity<List<String>> findAllNames(){
        return ResponseEntity.ok(workoutRepository.findDistinctNames());
    }    

    @PutMapping("/updateWorkout/{name}")
    public ResponseEntity<Workout> updateUsers(@PathVariable("name") String id, @RequestBody Workout workout) {
        Optional<Workout> userData = Optional.ofNullable(workoutRepository.findByName(id));
        if (userData.isPresent()) {
            Workout user = userData.get();
            user.setName(workout.getName());
            user.setId(workout.getId());
            user.setMoves(workout.getMoves());
            user.setRestMin(workout.getRestMin());
            user.setRestSec(workout.getRestSec());
            user.setPrepareSec(workout.getPrepareSec());
            user.setPreparetMin(workout.getPreparetMin());
            user.setRoundNumber(workout.getRoundNumber());
            user.setRoundSec(workout.getRoundSec());
            user.setRoundMin(workout.getRoundMin());
            user.setMovesTime(workout.getMovesTime());
            return new ResponseEntity<>(workoutRepository.save(user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
