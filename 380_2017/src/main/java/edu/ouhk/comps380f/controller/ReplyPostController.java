
package edu.ouhk.comps380f.controller;

import edu.ouhk.comps380f.model.Attachment;
import edu.ouhk.comps380f.model.ReplyPost;
import edu.ouhk.comps380f.model.Post;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
import edu.ouhk.comps380f.dao.AttachmentRepository;
import edu.ouhk.comps380f.dao.ReplyPostRepository;
import edu.ouhk.comps380f.dao.PostRepository;
import edu.ouhk.comps380f.view.DownloadingView;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
@RequestMapping("reply")


public class ReplyPostController {
     @Autowired
     ReplyPostRepository ReplyPostRepo;
     
    @Autowired
    PostRepository postRepo;
    
    @Autowired
    AttachmentRepository attachmentRepo; 
     
     private volatile long REPLYTICKET_ID_SEQUENCE = 1;
   
       
     @RequestMapping(value = "{postId}", method = RequestMethod.GET)
    public ModelAndView replyView(@PathVariable("postId") int postId) {
        //Post post = this.postDatabase.get(postId);

        ModelAndView modelAndView = new ModelAndView("view");
        modelAndView.addObject("postId", postId);
       return new ModelAndView("reply", "replyForm",new ReplyForm());
    }
    
       public static class ReplyForm {

        private int refpostId;
        private String replybody;

        private List<MultipartFile> replyattachments;

        public int getRefpostId() {
            return refpostId;
        }

        public void setRefpostId(int refpostId) {
            this.refpostId = refpostId;
        }
      
      
        public String getReplybody() {
            return replybody;
        }

        public void setReplybody(String replybody) {
            this.replybody = replybody;
        }

        public List<MultipartFile> getReplyattachments() {
            return replyattachments;
        }

        public void setReplyattachments(List<MultipartFile> replyattachments) {
            this.replyattachments = replyattachments;
        }
    }
       
        @RequestMapping(value = "{postId}", method = RequestMethod.POST)
    public View reply(Principal principal,ReplyForm form,@PathVariable("postId") int postId) throws IOException {
        String attachmentname ="";
        ReplyPost replyPost = new ReplyPost();
        if(!ReplyPostRepo.findAll().isEmpty()){
                replyPost.setId(ReplyPostRepo.maxId()+1);
            }else{
                replyPost.setId(1);
            }
        
        replyPost.setReplyName(principal.getName());
        replyPost.setRefPostid(postId);
        replyPost.setReplybody(form.getReplybody());
        int reply_id = replyPost.getId();
        int referencePostId = replyPost.getRefPostid();
        Attachment attachment = new Attachment();
        for (MultipartFile filePart : form.getReplyattachments()) {
            
            attachment.setRid(reply_id);
            attachment.setId(referencePostId);
            attachment.setName(filePart.getOriginalFilename());
            attachment.setMimeContentType(filePart.getContentType());
            attachment.setContents(filePart.getBytes());
            attachmentname = attachment.getName();
            if (attachment.getName() != null && attachment.getName().length() > 0
                    && attachment.getContents() != null && attachment.getContents().length > 0) {
                replyPost.addAttachment(attachment);
            }
            if (!attachmentname.equals("")) {
                attachmentRepo.createReplyAttachment(replyPost.getAttachment(attachmentname));}
        }
        System.out.println("post id:"+attachment.getId() + "reply_id" + attachment.getRid());
        //PostController.replyPostDatabase.put(replyPost.getId(), replyPost);
        ReplyPostRepo.create(replyPost);
        //Post post = PostController.postRepo.get(postId);
        
        //post.setReplyId(replyPost.getId());
        
        //PostController.postRepo.replace(postId,post);
        
        return new RedirectView("/post/view/" + postId, true);
    }
    
     @RequestMapping(
            value = "/{replyId}/attachment/{attachment:.+}",
            method = RequestMethod.GET
    )
    public View download(@PathVariable("replyId") int replyId,
            @PathVariable("attachment") String name) {
        ReplyPost replyPost = this.ReplyPostRepo.findById(replyId);
        Attachment attachment= attachmentRepo.findByName(name);
        if (replyPost != null) {
            
            if (attachment != null) {
                return new DownloadingView(attachment.getName(),
                        attachment.getMimeContentType(), attachment.getContents());
            }
        }
        return new RedirectView("/post/list", true);
    }
    
    private synchronized long getNextReplyPostId() {
        return this.REPLYTICKET_ID_SEQUENCE++;
    }
    
     @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    public View deletePost(@PathVariable("id") int reId) {
        if (this.ReplyPostRepo.findById(reId) != null) {
            this.ReplyPostRepo.deleteById(reId);
        }
        return new RedirectView("/post/list", true);
    }
}
