package com.my.oracleproject.controllers;

import com.my.oracleproject.models.*;
import com.my.oracleproject.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.*;
import java.util.*;


@Controller
public class UserController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;
    private final MeetingRepository meetingRepository;

    @Autowired
    public UserController(UserRepository userRepository, RoleRepository roleRepository, CompanyRepository companyRepository, MeetingRepository meetingRepository) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.companyRepository = companyRepository;
        this.meetingRepository = meetingRepository;
    }

    @GetMapping("/enter")
    public String enter(){
        return "enter";
    }
    @PostMapping("/enter")
    public String cheack(HttpServletResponse response, @RequestParam String login, @RequestParam String password){
        User user = userRepository.findUserByLoginAndPassword(login, password);
        if(user == null)
            return "redirect:/enter";
        else {
            Cookie cookie_user = new Cookie("username", login);
            Cookie cookie_role = new Cookie("role", user.getRole().getName());
            cookie_user.setMaxAge(-1);
            cookie_role.setMaxAge(-1);

            //add cookie to response
            response.addCookie(cookie_user);
            response.addCookie(cookie_role);
            return "redirect:/";
        }
    }
    @GetMapping("/exit")
    public String exit(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("username")) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
                else if (cookie.getName().equals("role")) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
        return "redirect:/";
    }
    @GetMapping("/registr")
    public String registr(Model model){
        model.addAttribute("companies", companyRepository.findAll());
        return "registr";
    }
    @PostMapping("/registr")
    public String check_registr(Model model, @RequestParam String name, @RequestParam String login,
                                @RequestParam String password, @RequestParam String id_company) {
        User user = userRepository.findUserByLogin(login);
        Company company = new Company();
        String[] info = id_company.split("_");
        company.setId(Long.valueOf(info[0]));
        company.setName(info[1]);
        company.setAddress(info[2]);
        if (user != null || password == null) {
            model.addAttribute("companies", companyRepository.findAll());
            return "registr";
        } else{
            if(roleRepository.findById(1L).isPresent()){
                user = new User(name, login, password, roleRepository.findById(1L).get(), company);
                userRepository.save(user);
            }return "redirect:/enter";
        }
    }
    @GetMapping("/meeting/{id}/user")
    public String show(@PathVariable("id") long id, Model model){
        model.addAttribute("users", userRepository.userOfMeeting(id));
        model.addAttribute("id_meeting", id);
        return "meeting-users-details";
    }
    @GetMapping("/meeting/{id_meeting}/users/add")
    public String addUser(@PathVariable("id_meeting") long id, Model model){
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("id_meeting", id);
        return "meeting-users-add";
    }
    @PostMapping("/meeting/{id_meeting}/users/add")
    public String cheak_addUser(@PathVariable("id_meeting") long id, @RequestParam String users){
        String[] us = users.split(";");

        List<Long> index = new ArrayList<>();
        Arrays.stream(us).map(el -> el.split(" ")[0])
               .mapToLong(Long::parseLong)
               .forEach(index::add);

        List<User> checkUsers = userRepository.userOfMeeting(id);
        for (User el: checkUsers){
            index.remove(el.getId());
        }
        for (long i: index){
            if (userRepository.findById(i).isPresent() &&
                    meetingRepository.findById(id).isPresent()){
                User user = userRepository.findById(i).get();
                Meeting meeting = meetingRepository.findById(id).get();
                Set<Meeting> meetings = user.getLikedMeeting();
                if(meetings == null)
                    meetings = new HashSet<>();
                meetings.add(meeting);
                user.setLikedMeeting(meetings);
                userRepository.save(user);
                Set<User> meet_users = meeting.getLikesUser();
                if(meet_users == null)
                    meet_users = new HashSet<>();
                meet_users.add(user);
                meeting.setLikesUser(meet_users);
                meetingRepository.save(meeting);
            }
        }
        return "redirect:/meeting/" + id + "/user";
    }
    @PostMapping("/meeting/{id_meeting}/user/{id_user}/delete")
    public String deleteUser(@PathVariable("id_meeting") long id_meeting, @PathVariable("id_user") long id_user){
        if(userRepository.findById(id_user).isPresent() &&
                meetingRepository.findById(id_meeting).isPresent()){
            User user = userRepository.findById(id_user).get();
            Meeting meeting = meetingRepository.findById(id_meeting).get();
            Set<Meeting> meetings = user.getLikedMeeting();
            meetings.remove(meeting);
            user.setLikedMeeting(meetings);
            userRepository.save(user);
            Set<User> users = meeting.getLikesUser();
            users.remove(user);
            meeting.setLikesUser(users);
            meetingRepository.save(meeting);
        }
        return "redirect:/meeting/" + id_meeting + "/user";
    }

}
