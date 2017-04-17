package edu.ouhk.comps380f.controller;

import edu.ouhk.comps380f.dao.PostRepository;
import edu.ouhk.comps380f.model.Attachment;
import edu.ouhk.comps380f.model.ReplyPost;
import edu.ouhk.comps380f.model.Post;
import edu.ouhk.comps380f.view.DownloadingView;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
import edu.ouhk.comps380f.controller.PollController;
import edu.ouhk.comps380f.dao.AttachmentRepository;
import edu.ouhk.comps380f.dao.ReplyPostRepository;

@Controller
@RequestMapping("post")
public class PostController {
  
    @Autowired
    PostRepository postRepo;
    
    @Autowired
    ReplyPostRepository replyPostRepo;
    
    @Autowired
    AttachmentRepository attachmentRepo; 
  
    private volatile int TICKET_ID_SEQUENCE = 1;

    public static Map<Long, ReplyPost> replyPostDatabase = new LinkedHashMap<>();

    @RequestMapping(value = {"", "list"}, method = RequestMethod.GET)
    public String list(ModelMap model) {
        model.addAttribute("postDatabase", postRepo.findAll());
     //   model.addAttribute("replyPostDatabase",replyPostDatabase );
        model.addAttribute("pollPostDatabase", PollController.pollPostDatabase);
        return "list";
    }
    

    @RequestMapping(value = "view/{postId}", method = RequestMethod.GET)
    public ModelAndView view(@PathVariable("postId") int postId) {
        Post post = this.postRepo.findById(postId);
        List<Attachment> attachmentlist = new ArrayList<Attachment>();
        
        for (Attachment a:attachmentRepo.findAll()){
            if (a.getId()==postId && a.getRid()==0){
                 attachmentlist.add(a);
            }
            
        } 
        List<ReplyPost> replylist= new ArrayList<ReplyPost>();
                      replylist=replyPostRepo.findParts(postId);
                      
        for (Attachment a:attachmentRepo.findAll()){
           
                for (int i=0;i<replylist.size();i++){
                    
                    if (a.getRid()==replylist.get(i).getId()){
                        replylist.get(i).addAttachment(a);
                        //replylist.get(i).getId();     replyid
                        //a                             attachment
                        //reply.set(i, element)
                        
                    }
                }
                    
                  
            
            
        }
        
        if (post == null) {
            return new ModelAndView(new RedirectView("/post/list", true));
        }
        ModelAndView modelAndView = new ModelAndView("view");
        modelAndView.addObject("postId", postId);
        modelAndView.addObject("post", post);
        modelAndView.addObject("numberComment", replyPostRepo.findParts(postId).size());
        //List<ReplyPost> re=replyPostRepo.findParts(postId);
        modelAndView.addObject("selectedReply",replylist);
        modelAndView.addObject("attachmentlist", attachmentlist);
        return modelAndView;
    }

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public ModelAndView create() {
        return new ModelAndView("add", "postForm", new Form());
    }
    
    @RequestMapping(value = "listlecture", method = RequestMethod.GET)
    public String listlecture(ModelMap model) {
        String type="lecture";
        model.addAttribute("postDatabase", postRepo.findByCategories(type));
        return "listlecture";
    }
    @RequestMapping(value = "listlab", method = RequestMethod.GET)
    public String listlab(ModelMap model) {
        String type="lab";
        model.addAttribute("postDatabase", postRepo.findByCategories(type));
        return "listlab";
    }
    @RequestMapping(value = "listother", method = RequestMethod.GET)
    public String listother(ModelMap model) {
        String type="other";
        model.addAttribute("postDatabase", postRepo.findByCategories(type));
        return "listother";
    }

    public static class Form {
        private String subject;
        private String body;
        private List<MultipartFile> attachments;
        private List replyId;
        private String categories;

        public String getCategories() {
            return categories;
        }

        public void setCategories(String categories) {
            this.categories = categories;
        }

        public List getReplyId() {
            return replyId;
        }

        public void setReplyId(List replyId) {
            this.replyId = replyId;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public List<MultipartFile> getAttachments() {
            return attachments;
        }

        public void setAttachments(List<MultipartFile> attachments) {
            this.attachments = attachments;
        }
    }
 
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public View create(Form form, Principal principal) throws IOException {
Post post = new Post();
        //List<String> a= null;
        String name ="";
        if (post.getSubject() == null || post.getSubject().length() <= 0
                || post.getBody() == null || post.getBody().length() <= 0
                || post.getCategories() == null || post.getCategories().length() <= 0) {
            if(postRepo.findAll().isEmpty()){
              post.setId(1);
            }else{
                post.setId(postRepo.maxId()+1);
            }
            post.setCustomerName(principal.getName());
            post.setSubject(form.getSubject());
            post.setBody(form.getBody());
            post.setCategories(form.getCategories());
            postRepo.create(post);
            int post_id = postRepo.maxId();
          
              for (MultipartFile filePart : form.getAttachments()) {
                System.out.println("here:" +filePart);
                Attachment attachment = new Attachment();
                attachment.setId(post_id);
                attachment.setRid(0);
                attachment.setName(filePart.getOriginalFilename());  
                name = attachment.getName();
               attachment.setMimeContentType(filePart.getContentType());
                attachment.setContents(filePart.getBytes());
                if (attachment.getName() != null && attachment.getName().length() > 0
                        && attachment.getContents() != null && attachment.getContents().length > 0) {
                    post.addAttachment(attachment);
                }
                if (!name.equals("")) {
                    attachmentRepo.createPostAttachment(post.getAttachment(name));
                }
                
            }
        }
       // this.postDatabase.put(post.getId(), post);
           
        
        //attachmentRepo.create(post.getAttachment(a));
        
        return new RedirectView("/post/view/" + postRepo.maxId(), true);
    }

    private synchronized long getNextPostId() {
        return this.TICKET_ID_SEQUENCE++;
    }
    
    
    @RequestMapping(
            value = "/{postId}/attachment/{attachment:.+}",
            method = RequestMethod.GET
    )
    public View download(@PathVariable("postId") int postId,
            @PathVariable("attachment") String name) {
        Post post = this.postRepo.findById(postId);
        Attachment attachment= attachmentRepo.findByName(name);
        
        if (post != null) {
            
            if (attachment != null) {
                return new DownloadingView(attachment.getName(),
                        attachment.getMimeContentType(), attachment.getContents());
            }
        }
        return new RedirectView("/post/list", true);
    }

    @RequestMapping(
            value = "/{postId}/delete/{attachment:.+}",
            method = RequestMethod.GET
    )
    public View deleteAttachment(@PathVariable("postId") int postId,
            @PathVariable("attachment") String name) {
        Post post = this.postRepo.findById(postId);
        if (post != null) {
            if (post.hasAttachment(name)) {
                post.deleteAttachment(name);
            }
        }
        return new RedirectView("/post/edit/" + postId, true);
    }

    @RequestMapping(value = "edit/{postId}", method = RequestMethod.GET)
    public ModelAndView showEdit(@PathVariable("postId") int postId,
            Principal principal, HttpServletRequest request) {
        Post post = this.postRepo.findById(postId);
        if (post == null || 
                (!request.isUserInRole("ROLE_ADMIN")
                && !principal.getName().equals(post.getCustomerName()))) {
            return new ModelAndView(new RedirectView("/post/list", true));
        }

        ModelAndView modelAndView = new ModelAndView("edit");
        modelAndView.addObject("postId", Long.toString(postId));
        modelAndView.addObject("post", post);
        
        ModelAndView modelAndView2 = new ModelAndView("list");
        modelAndView2.addObject("postId2", Long.toString(postId));
        modelAndView2.addObject("post2", post);

        Form postForm = new Form();
        postForm.setSubject(post.getSubject());
        postForm.setBody(post.getBody());
        modelAndView.addObject("postForm", postForm);

        return modelAndView;
    }

    @RequestMapping(value = "edit/{postId}", method = RequestMethod.POST)
    public View edit(@PathVariable("postId") int postId, Form form,
            Principal principal, HttpServletRequest request)
            throws IOException {
        Post post = this.postRepo.findById(postId);
        if (post == null || 
                (!request.isUserInRole("ROLE_ADMIN")
                && !principal.getName().equals(post.getCustomerName()))) {
            return new RedirectView("/post/list", true);
        }
        post.setSubject(form.getSubject());
        post.setBody(form.getBody());

        for (MultipartFile filePart : form.getAttachments()) {
            Attachment attachment = new Attachment();
            attachment.setName(filePart.getOriginalFilename());
            attachment.setMimeContentType(filePart.getContentType());
            attachment.setContents(filePart.getBytes());
            if (attachment.getName() != null && attachment.getName().length() > 0
                    && attachment.getContents() != null && attachment.getContents().length > 0) {
                post.addAttachment(attachment);
            }
        }
        
        this.postRepo.update(post);
        return new RedirectView("/post/view/" + post.getId(), true);
    }

    @RequestMapping(value = "delete/{postId}", method = RequestMethod.GET)
    public View deletePost(@PathVariable("postId") int postId) {
        if (this.postRepo.findById(postId) != null) {
            this.postRepo.deleteById(postId);
        }
        return new RedirectView("/post/list", true);
    }

}
