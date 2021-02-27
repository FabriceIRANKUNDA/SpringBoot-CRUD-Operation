package com.example.crudapi.service;

import com.example.crudapi.exceptionHandling.StudentException;
import com.example.crudapi.model.Address;
import com.example.crudapi.model.Students;
import com.example.crudapi.repository.StudentRepository;
import com.example.crudapi.utils.Constants;
import com.example.crudapi.utils.UploadUtil;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@Service
public class StudentService {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UploadUtil uploadUtil;

    public Students enrollStudent( String studentId, String courseId) {
        Optional<Students> studentData = studentRepository.findById(studentId);
        if(studentData.isEmpty()){
            return studentData.orElseThrow(() -> new StudentException(String.format("Student with id : %s  not found", studentId), "", HttpStatus.NOT_FOUND));
        }

        String res = restTemplate.exchange(Constants.url + courseId, HttpMethod.GET, null, String.class).getBody();

        JsonParser springParser = JsonParserFactory.getJsonParser();
        Map<String, Object> map = springParser.parseMap(res);

        Students _studentData = studentData.get();
        if(_studentData.getCourses() == null){
            List<Object> course = new ArrayList<>();
            course.add(map);
            _studentData.setCourses(course);
            return studentRepository.save(_studentData);
        }
        _studentData.getCourses().add(map);

         return studentRepository.save(_studentData);
    }


    public List<Students> getAllStudents(){
        return studentRepository.findAll();
    }

    public Students getStudentById(String id){
        Optional<Students> student = studentRepository.findById(id);
        return student.orElseThrow(() -> new StudentException(String.format("Student with id : %s  not found", id), "", HttpStatus.NOT_FOUND));
    }

    public Students createStudent(Students studentData){
        return studentRepository.save(studentData);
    }

    public Students updateStudent(String id, Students student) {
        Optional<Students> studentData = studentRepository.findById(id);
        if(studentData.isPresent()){
           Students _studentData = studentData.get();
           _studentData.setFirstName(student.getFirstName());
           _studentData.setLastName(student.getLastName());
           _studentData.setGender(student.getGender());
           _studentData.setLocation(student.getLocation());

           return studentRepository.save(_studentData);
        }
        return studentData.orElseThrow(() -> new StudentException(String.format("Student with id : %s  not found", id), "ST122", HttpStatus.NOT_FOUND));

    }

    public Object deleteStudent(String id) {
        Optional<Students> student = studentRepository.findById(id);
        if(student.isPresent()){
            studentRepository.deleteById(id);
            return HttpStatus.NO_CONTENT;
        }
        return student.orElseThrow(() -> new StudentException(String.format("Student with id : %s  not found", id), "ST122", HttpStatus.NOT_FOUND));

    }


    public List<Students> upload(MultipartFile file) throws Exception {

        Path tempDir = Files.createTempDirectory("");

        File tempFile = tempDir.resolve(file.getOriginalFilename()).toFile();

        file.transferTo(tempFile);

        Workbook workbook = WorkbookFactory.create(tempFile);

        Sheet sheet = workbook.getSheetAt(0);

        Supplier<Stream<Row>> rowStreamSupplier = uploadUtil.getRowStreamSupplier(sheet);

        Row headerRow = rowStreamSupplier.get().findFirst().get();

        List<String> headerCells = uploadUtil.getStream(headerRow)
                .map(Cell::getStringCellValue)
                .collect(Collectors.toList());

        //int colCount = headerCells.size();
        List<Map<String, String>>  locations = new ArrayList<Map<String,String>>();

                List<Map<String, String>> data = rowStreamSupplier.get()
                .skip(1)
                .map(row -> {

                    List<String> cellList = uploadUtil.getStream(row)
                            .map(Cell::getStringCellValue)
                            .collect(Collectors.toList());

                    List<String> address = headerCells.subList(4,7);
                    List<String> locationValue = cellList.subList(4, 7);
                    Map<String, String> mp = uploadUtil.cellIteratorSupplier(3)
                            .get()
                            .collect(toMap(index ->  address.get(index), index -> locationValue.get(index)));
                    locations.add(mp);

                    return uploadUtil.cellIteratorSupplier(4)
                            .get()
                            .collect(toMap(index ->  headerCells.get(index), index -> cellList.get(index)));
                })
                .collect(Collectors.toList());

                List<Students> students= new ArrayList<>();

        uploadUtil.cellIteratorSupplier(sheet.getPhysicalNumberOfRows() - 1).get().forEach(index -> {
            Map<String, String> studData = data.get(index);
            Map<String, String> studentLocation = locations.get(index);
            Address location = new Address(studentLocation.get("city"), studentLocation.get("state"), studentLocation.get("road"));

            Students student = new Students();
            student.setLocation(location);
            student.setFirstName(studData.get("firstName"));
            student.setGender(studData.get("gender"));
            student.setLastName(studData.get("lastName"));
            student.setEmail(studData.get("email"));
            students.add(student);
        });

        return studentRepository.saveAll(students);
    }

}
