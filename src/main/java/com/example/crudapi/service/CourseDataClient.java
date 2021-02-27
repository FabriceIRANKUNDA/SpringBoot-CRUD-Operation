package com.example.crudapi.service;
import com.example.crudapi.utils.Constants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "courses", url = Constants.url)
public interface CourseDataClient {
    @RequestMapping(method = RequestMethod.GET, value = "")
    List<Object> getCourses();
}
