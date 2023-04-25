package com.my.oracleproject.controllers;

import com.my.oracleproject.models.*;
import com.my.oracleproject.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.*;
import java.sql.Timestamp;
import java.util.*;


@Controller
public class MeetingController {
    private final StatusRepository statusRepository;
    private final CountryRepository countryRepository;
    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final SpeakerRepository speakerRepository;
    @Autowired
    public MeetingController(StatusRepository statusRepository, CountryRepository countryRepository, MeetingRepository meetingRepository, UserRepository userRepository, SpeakerRepository speakerRepository) {
        this.statusRepository = statusRepository;
        this.countryRepository = countryRepository;
        this.meetingRepository = meetingRepository;
        this.userRepository = userRepository;
        this.speakerRepository = speakerRepository;
    }


    @GetMapping("/meeting")
    public String meetingMain(HttpServletRequest request, Model model){
        String login = "";
        String role = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("username")) {
                    login = cookie.getValue();

                }
                else if (cookie.getName().equals("role")) {
                    role = cookie.getValue();
                }
            }
        }
        if(role.equals("reader"))
            model.addAttribute("meetings", meetingRepository.findByUserLogin(login));
        else
            model.addAttribute("meetings", meetingRepository.findAll());
        model.addAttribute("role", role);
        if(!login.equals("")) {
            return "meeting-main";
        }
        return "redirect:/";
    }
    @GetMapping("/meeting/add")
    public String meetingAdd(Model model){
        model.addAttribute("countries", countryRepository.findAll());
        model.addAttribute("statuses", statusRepository.findAll());
        model.addAttribute("meeting", new Meeting());
        model.addAttribute("speakers", speakerRepository.findAll());
        return "meeting-add";

    }
    @PostMapping("/meeting/add")
    public String create(@RequestParam String title, @RequestParam String date, @RequestParam String address,
                         @RequestParam String id_country, @RequestParam String id_status, @RequestParam String id_speaker){
        String[] info_country = id_country.split(" ");
        String[] info_status = id_status.split(" ");
        Country country = countryRepository.findById(Long.valueOf(info_country[0])).orElse(null);
        Status status = statusRepository.findById(Long.valueOf(info_status[0])).orElse(null);
        String corectDate = date.split("T")[0] + " " + date.split("T")[1] + ":00";
        Meeting meeting = new Meeting(title, Timestamp.valueOf(corectDate), address, country, status);
        meeting = meetingRepository.save(meeting);
        String[] us = id_speaker.split(";");
        List<Long> index = new ArrayList<>();
        Arrays.stream(us).map(el -> el.split(" ")[0])
                .mapToLong(Long:: parseLong)
                .forEach(index::add);
        for (long i: index){
            if(speakerRepository.findById(i).isPresent()){
                Speaker speaker = speakerRepository.findById(i).get();
                Set<Meeting> meetings = speaker.getLikedMeeting();
                if(meetings == null)
                    meetings = new HashSet<>();
                meetings.add(meeting);
                speaker.setLikedMeeting(meetings);
                speakerRepository.save(speaker);
                Set<Speaker> speakers = meeting.getLikesSpeaker();
                if(speakers == null)
                   speakers = new HashSet<>();
                speakers.add(speaker);
                meeting.setLikesSpeaker(speakers);
                meetingRepository.save(meeting);
            }
        }
        return "redirect:/meeting";
    }
    @GetMapping("/meeting/{id}")
    public String show(HttpServletRequest request, @PathVariable("id") long id, Model model){
        Meeting meeting = meetingRepository.findById(id).orElse(null);
        if(meeting == null){
            return "redirect:/meeting";
        }
        String role = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("role")) {
                    role = cookie.getValue();
                }
            }
        }
        model.addAttribute("role", role);
        model.addAttribute("meeting", meeting);
        model.addAttribute("speakers", speakerRepository.speakerOfMeeting(id));
        return "meeting-details";
    }
    @GetMapping("/meeting/{id}/edit")
    public String blogEdit(@PathVariable("id") long id, Model model){
        Meeting meeting = meetingRepository.findById(id).orElse(null);
        if(meeting == null){
            return "redirect:/meeting";
        }
        model.addAttribute("countries", countryRepository.findAll());
        model.addAttribute("statuses", statusRepository.findAll());
        model.addAttribute("meeting", meeting);
        List<Speaker> speakers =  speakerRepository.speakerOfMeeting(id);
        StringBuilder res_speakers = new StringBuilder();
        for (Speaker speaker: speakers){
            res_speakers.append(speaker.getId()).append(" ").append(speaker.getName()).append(";");
        }
        if(res_speakers.isEmpty())
            model.addAttribute("speaker", "");
        else
            model.addAttribute("speaker",
                res_speakers.substring(0, res_speakers.length() - 1));

        model.addAttribute("speakers", speakerRepository.findAll());
        return "meeting-edit";
    }
    @PostMapping("/meeting/{id}/edit")
    public String update(@PathVariable("id") long id, @RequestParam String title, @RequestParam String date, @RequestParam String address,
                         @RequestParam String id_country, @RequestParam String id_status, @RequestParam String id_speaker){
        Meeting meeting = meetingRepository.findById(id).orElse(null);
        if(meeting == null){
            return "redirect:/meeting";
        }
        String[] info_country = id_country.split(" ");
        String[] info_status = id_status.split(" ");
        Country country = countryRepository.findById(Long.valueOf(info_country[0])).orElse(null);
        Status status = statusRepository.findById(Long.valueOf(info_status[0])).orElse(null);
        String corectDate = date.split("T")[0] + " " + date.split("T")[1] + ":00";
        meeting.setTitle(title);
        meeting.setDate(Timestamp.valueOf(corectDate));
        meeting.setAddress(address);
        meeting.setCountry(country);
        meeting.setStatus(status);
        meeting = meetingRepository.save(meeting);

        String[] us = id_speaker.split(";");
        List<Long> index = new ArrayList<>();
        Arrays.stream(us).map(el -> el.split(" ")[0])
                .mapToLong(Long:: parseLong)
                .forEach(index::add);
        List<Speaker> checkSpeakers = speakerRepository.speakerOfMeeting(id);
        for (Speaker el: checkSpeakers){
            index.remove(el.getId());
        }

        for (Speaker el: checkSpeakers){
            if(speakerRepository.findById(el.getId()).isPresent()) {
                Speaker speaker = speakerRepository.findById(el.getId()).get();
                Set<Meeting> meetings = speaker.getLikedMeeting();
                meetings.remove(meeting);
                speaker.setLikedMeeting(meetings);
                speakerRepository.save(speaker);
                Set<Speaker> speakers = meeting.getLikesSpeaker();
                speakers.remove(speaker);
                meeting.setLikesSpeaker(speakers);
                meetingRepository.save(meeting);
            }
        }
        for (long i: index){
            if(speakerRepository.findById(i).isPresent()){
                Speaker speaker = speakerRepository.findById(i).get();
                Set<Meeting> meetings = speaker.getLikedMeeting();
                if(meetings == null)
                    meetings = new HashSet<>();
                meetings.add(meeting);
                speaker.setLikedMeeting(meetings);
                speakerRepository.save(speaker);
                Set<Speaker> speakers = meeting.getLikesSpeaker();
                if(speakers == null)
                    speakers = new HashSet<>();
                speakers.add(speaker);
                meeting.setLikesSpeaker(speakers);
                meetingRepository.save(meeting);
            }

        }

        return "redirect:/meeting";
    }
    @PostMapping("/meeting/{id}/remove")
    public String remove(@PathVariable("id") long id){
        Meeting meeting = meetingRepository.findById(id).orElse(null);
        if(meeting == null){
            return "redirect:/meeting";
        }
        meetingRepository.delete(meeting);

        return "redirect:/meeting";
    }
    @GetMapping("/meeting/speakers")
    public String allSpeakers(Model model){
        model.addAttribute("speakers", speakerRepository.findAll());
        return "meeting-speakers";
    }
    @GetMapping("/meeting/speakers/add")
    public String addSpeaker(Model model){
        model.addAttribute("users", userRepository.findAll());
        return "meeting-speakers-add";
    }
    @PostMapping("/meeting/speakers/add")
    public String cheak_addUser(@RequestParam String speakers){
        String[] us = speakers.split(";");

        List<String> index = new ArrayList<>();
        Arrays.stream(us).map(el -> el.split(" ")[1])
                .forEach(index::add);

        List<Speaker> check = speakerRepository.findAll();
        for (Speaker el: check){
            index.remove(el.getName());
        }
        Speaker speaker;
        for (String i: index){
            speaker = new Speaker(i);
            speakerRepository.save(speaker);
        }
        return "redirect:/meeting/speakers";
    }
    @PostMapping("/meeting/speakers/{id}/delete")
    public String removeSpeaker(@PathVariable("id") long id){
        if(speakerRepository.findById(id).isPresent())
            speakerRepository.delete(speakerRepository.findById(id).get());
        return "redirect:/meeting/speakers";
    }
}
