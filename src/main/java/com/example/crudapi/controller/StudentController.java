package com.example.crudapi.controller;

import com.example.crudapi.exceptionHandling.AppError;
import com.example.crudapi.exceptionHandling.RestResponse;
import com.example.crudapi.model.Students;
import com.example.crudapi.service.CourseDataClient;
import com.example.crudapi.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1")
public class StudentController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private CourseDataClient client;

    @GetMapping("/students")
    public ResponseEntity<RestResponse<List<Students>>> getStudents(){
        RestResponse<List<Students>> restResponse = new RestResponse<>();
        restResponse.setData(studentService.getAllStudents());

        return new ResponseEntity(restResponse, HttpStatus.OK);
    }

    @GetMapping("student/{id}")
    public ResponseEntity<Students> getStudentByAddress(@PathVariable String id){
        RestResponse<Students> restResponse = new RestResponse<>();
        restResponse.setData(studentService.getStudentById(id));

        return new ResponseEntity(restResponse, HttpStatus.OK);
    }

    @PutMapping("/enroll/{studentId}/{courseId}")
    public ResponseEntity<Students> enrollStudentInCourse(@PathVariable("studentId") String studentId, @PathVariable("courseId") String courseId){
        RestResponse restResponse = new RestResponse();
        restResponse.setData(studentService.enrollStudent( studentId, courseId));
        return new ResponseEntity(restResponse, HttpStatus.OK);
    }

    @PostMapping("/student")
    public ResponseEntity<Students> createStudent(@Valid @RequestBody Students studentData){
        return new ResponseEntity<>(studentService.createStudent(studentData), HttpStatus.CREATED);
    }

    @PutMapping("/student/{id}")
    public ResponseEntity<Students> updateStudent(@PathVariable("id") String id, @Valid @RequestBody Students student){
        RestResponse<Students> restResponse = new RestResponse<>();
        restResponse.setData(studentService.updateStudent(id, student));
        return new ResponseEntity(restResponse, HttpStatus.OK);
    }

    @DeleteMapping("/student/{id}")
    public ResponseEntity deleteStudent(@PathVariable("id") String id){
        RestResponse<Object> restResponse = new RestResponse<>();
        restResponse.setData(studentService.deleteStudent(id));
        return new ResponseEntity(restResponse.getData(), HttpStatus.NO_CONTENT);
    }

    @PostMapping("/migrate_file")
    public ResponseEntity<Students> saveDataToDB(@RequestParam("file") MultipartFile file) throws Exception{

        return new ResponseEntity(studentService.upload(file), HttpStatus.OK);
    }

    @GetMapping("/students/courses")
    public ResponseEntity<Object> getCourses(){
        RestResponse<List<Object>> restResponse = new RestResponse<>();
        restResponse.setData(client.getCourses());

        return new ResponseEntity(restResponse, HttpStatus.OK);
    }
}
