package edu.ouhk.comps380f.controller;

import edu.ouhk.comps380f.dao.PostUserRepository;
import edu.ouhk.comps380f.model.PostUser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("user")
public class PostUserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    PostUserRepository postUserRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = {"", "list"}, method = RequestMethod.GET)
    public String list(ModelMap model) {
        model.addAttribute("postUsers", postUserRepo.findAll());
        return "listUser";
    }

    public static class Form {

        private String username;
        private String password;
        private String[] roles;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String[] getRoles() {
            return roles;
        }

        public void setRoles(String[] roles) {
            this.roles = roles;
        }

    }

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public ModelAndView create() {
        return new ModelAndView("addUser", "postUser", new Form());
    }

    
//    @RequestMapping(value = "create", method = RequestMethod.POST)
//    public View create(Form form) throws IOException {
//      
//        PostUser user = new PostUser();
//        user.setUsername(form.getUsername());
//        user.setPassword(passwordEncoder.encode(form.getPassword()));
//        for (String role : form.getRoles()) {
//            user.addRole(role);
//        }
//        postUserRepo.create(user);
//        logger.info("User " + form.getUsername() + " created.");
//        return new RedirectView("/user/list", true);
//    }
    @RequestMapping(value = "create", method = RequestMethod.POST)
  public ModelAndView create(Form form) throws IOException {
    PostUser user = new PostUser();
    List<PostUser> listUser = new ArrayList<>();
    listUser = postUserRepo.findAll();
    for (PostUser user1 : listUser) {
      if (user1.getUsername().equals(form.getUsername())) {
        ModelAndView modelAndView =  new ModelAndView("addUser");
        String dulpicated = "User has existed!";
        modelAndView.addObject("message", dulpicated);
        modelAndView.addObject("postUser", new Form());
        return modelAndView;
      }
    }
    
    user.setUsername(form.getUsername());
    user.setPassword(passwordEncoder.encode(form.getPassword()));
        for (String role : form.getRoles()) {
          user.addRole(role);
        }
        postUserRepo.create(user);
        logger.info("User " + form.getUsername() + " created.");
    return new ModelAndView("/list");
  }
    
    @RequestMapping(value = "edit/{username}", method = RequestMethod.GET)
    public View editPost(@PathVariable("username") String username) {
        postUserRepo.editByUsername(username, "ROLE_ADMIN");
        logger.info("User " + username + " role edit.");
        return new RedirectView("/user/list", true);
    }
    
    @RequestMapping(value = "edit_user/{username}", method = RequestMethod.GET)
    public View editUser(@PathVariable("username") String username) {
        postUserRepo.editByUsername(username, "ROLE_USER");
        logger.info("User " + username + " role edit.");
        return new RedirectView("/user/list", true);
    }

    @RequestMapping(value = "delete/{username}", method = RequestMethod.GET)
    public View deletePost(@PathVariable("username") String username) {
        postUserRepo.deleteByUsername(username);
        logger.info("User " + username + " deleted.");
        return new RedirectView("/user/list", true);
    }

}
