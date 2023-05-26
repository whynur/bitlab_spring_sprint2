package com.example.bitlab_spring_sprint2.controller;

import com.example.bitlab_spring_sprint2.model.ApplicationRequest;
import com.example.bitlab_spring_sprint2.model.Courses;
import com.example.bitlab_spring_sprint2.model.Operators;
import com.example.bitlab_spring_sprint2.repository.AppRepository;
import com.example.bitlab_spring_sprint2.repository.CourseRepository;
import com.example.bitlab_spring_sprint2.repository.OperatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private OperatorRepository operatorRepository;

    @GetMapping(value = "/")
    public String homePage(Model model){
        List<ApplicationRequest> list = appRepository.findAll();
        model.addAttribute("lists",list);
        return "home";
    }

    @GetMapping(value = "/addapplication")
    public String addApplication(Model model){
        List<Courses> courses = courseRepository.findAll();
        model.addAttribute("courses",courses);
        return "addapplication";
    }

    @PostMapping(value = "/addapplication")
    public String addApplication(@RequestParam(name = "fullName") String name,
                                 @RequestParam(name = "phone") String phone,
                                 @RequestParam(name = "comment") String comment,
                                 @RequestParam(name = "course_id") Long id){

        Courses course = courseRepository.findById(id).orElse(null);

        if(course!=null){
            ApplicationRequest app = new ApplicationRequest();
            app.setUserName(name);
            app.setPhone(phone);
            app.setCommentary(comment);
            app.setHandled(false);
            app.setCourse(course);

            appRepository.save(app);
        }
        return "redirect:/";
    }

    @GetMapping(value = "/details/{id}")
    public String details(@PathVariable(name = "id") Long id,
                          Model model){

        ApplicationRequest app = appRepository.findById(id).orElse(null);
        model.addAttribute("app",app);

        List<Courses> courses = courseRepository.findAll();
        model.addAttribute("courses",courses);

        List<Operators> operators = operatorRepository.findAll();
        model.addAttribute("operators",operators);

        List<Operators> operReq = app.getOperators();
        model.addAttribute("operReq",operReq);
        return "details";
    }

    @PostMapping(value = "/updateapplication")
    public String updateApplication(@RequestParam(name = "id") Long id,
                                    @RequestParam(name = "fullName") String name,
                                    @RequestParam(name = "phone") String phone,
                                    @RequestParam(name = "comment") String comment,
                                    @RequestParam(name = "course_id") Long course_id){

        Courses course = courseRepository.findById(course_id).orElse(null);
        ApplicationRequest app = appRepository.findById(id).orElse(null);
        if(course!=null){
            if(app!=null){
                app.setUserName(name);
                app.setPhone(phone);
                app.setCommentary(comment);
                app.setHandled(true);
                app.setCourse(course);

                appRepository.save(app);
                return "redirect:/details/"+id;
            }
        }
        return "redirect:/";
    }

    @PostMapping(value = "/deleteapplication")
    public String deleteApplication(@RequestParam(name = "id") Long id){
        appRepository.deleteById(id);
        return "redirect:/";
    }

    @GetMapping(value = "/newapp")
    public String newApplication(Model model){
        List<ApplicationRequest> list = appRepository.findAll();
        model.addAttribute("lists",list);
        return "newapp";
    }

    @GetMapping(value = "/editedapp")
    public String editedApplication(Model model){
        List<ApplicationRequest> list = appRepository.findAll();
        model.addAttribute("lists",list);
        return "editedapp";
    }

    @PostMapping(value = "/assignoperator")
    public String assignOperator(@RequestParam(name = "id") Long id,
                                 @RequestParam(name = "operatorId") Long[] operatorId){

        ApplicationRequest applicationRequest = appRepository.findById(id).orElse(null);
        List<Operators> operators = applicationRequest.getOperators();
        if(operators==null){
            operators = new ArrayList<>();
        }

        for(Long op : operatorId){
            Operators operator = operatorRepository.findById(op).orElse(null);
            operators.add(operator);
        }
        applicationRequest.setOperators(operators);
        applicationRequest.setHandled(true);
        appRepository.save(applicationRequest);
        return "redirect:/details/"+id;
    }

    @PostMapping(value = "/unassignoperator")
    public String unAssignOperator(@RequestParam(name = "id") Long id,
                                 @RequestParam(name = "oper_id") Long operatorId){

        ApplicationRequest applicationRequest = appRepository.findById(id).orElse(null);
        List<Operators> operators = applicationRequest.getOperators();
        if(operators==null){
            operators = new ArrayList<>();
        }

        Operators operator = operatorRepository.findById(operatorId).orElse(null);
        operators.remove(operator);
        applicationRequest.setOperators(operators);
        applicationRequest.setHandled(true);
        appRepository.save(applicationRequest);
        return "redirect:/details/"+id;
    }
}
