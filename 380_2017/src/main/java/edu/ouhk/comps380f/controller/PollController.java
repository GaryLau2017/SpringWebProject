package edu.ouhk.comps380f.controller;

import edu.ouhk.comps380f.model.Poll;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("poll")
public class PollController {

    private volatile long POLL_ID_SEQUENCE = 1;

    private Map<String, Integer> map = new HashMap<String, Integer>();
    public static Map<Long, Poll> pollPostDatabase = new LinkedHashMap<>();

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String createPoll(ModelMap model) {

        return "createpoll";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public View pollcreate(HttpServletRequest req, Principal principal) throws IOException {
        map.clear();
        int length = Integer.valueOf(req.getParameter("count"));
        Poll poll = new Poll();
        for (int i = 0; i < length; i++) {
            map.put(req.getParameter("item" + i), 0);
        }

        poll.setId(this.getNextPostId());

        poll.setPollSubject(req.getParameter("subject"));
        poll.setMap(map);

        this.pollPostDatabase.put(poll.getId(), poll);

        // System.out.println("aaaaaaaaaaaaaaaaaa"+poll.getId()+poll.getPollSubject());
        return new RedirectView("/poll/view/" + poll.getId(), true);
    }

    private synchronized long getNextPostId() {
        return this.POLL_ID_SEQUENCE++;
    }

    @RequestMapping(value = "view/{pollId}", method = RequestMethod.GET)
    public ModelAndView pollView(Principal principal,@PathVariable("pollId") long pollId) {
        Poll poll = this.pollPostDatabase.get(pollId);
        String username = principal.getName();
        for(String name: poll.getUserPolled()){
            if(name.equals(username)){
                 return new ModelAndView(new RedirectView("/poll/viewresult/" + poll.getId(), true)); 
            }
        }      
        ModelAndView modelAndView = new ModelAndView("poll");
        modelAndView.addObject("pollId", Long.toString(pollId));
        modelAndView.addObject("poll", poll);
        return modelAndView;
    }

    @RequestMapping(value = "view/dopoll", method = RequestMethod.POST)
    public View dopoll(HttpServletRequest req, Principal principal) throws IOException {

        String selectedItem = req.getParameter("item");
        long pollId = Long.valueOf(req.getParameter("pollId"));
        Poll poll = this.pollPostDatabase.get(pollId);
        String username = principal.getName();
        System.out.println("usename:"+username);
        for (String name : poll.getUserPolled()) {
            if (name.equals(username)) {              
                return new RedirectView("/poll/pollerror/", true);
            }
        }
        List<String> preList = new ArrayList<>();
        preList = poll.getUserPolled();
        preList.add(username);
        //tricky place, need to system print
        //System.out.println("list "+ poll.getUserPolled());
        poll.setUserPolled(preList);
       
        poll.getMap().put(selectedItem, poll.getMap().get(selectedItem) + 1);
        this.pollPostDatabase.put(poll.getId(), poll);

        return new RedirectView("/poll/viewresult/" + poll.getId(), true);
    }

    @RequestMapping(value = "viewresult/{pollId}", method = RequestMethod.GET)
    public ModelAndView pollResultView(Principal principal,@PathVariable("pollId") long pollId) {
        Poll poll = this.pollPostDatabase.get(pollId);
       
        ModelAndView modelAndView = new ModelAndView("pollresult");
        modelAndView.addObject("pollId", Long.toString(pollId));
        modelAndView.addObject("poll", poll);
        int total=0;
        Object o;
        for (int i=0;i<poll.getMap().size();i++){         
          total=total+(int)poll.getMap().values().toArray()[i];
          System.out.println("total :"+total );
        }
        
        modelAndView.addObject("total", total);
        
        return modelAndView;
        
    }
    
     @RequestMapping(value = "pollerror", method = RequestMethod.GET)
     public String pollError(ModelMap model) {

        return "pollerror";
    }    

}
