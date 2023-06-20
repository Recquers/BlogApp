package in.vasanth.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import in.vasanth.appconstants.AppConstants;
import in.vasanth.binding.CommentForm;
import in.vasanth.binding.CreateBlog;
import in.vasanth.entity.BlogsPage;
import in.vasanth.entity.Comments;
import in.vasanth.service.BlogService;

@Controller
public class BlogController {
	
	
	@Autowired
	private BlogService bservice;
	
	

	
	@GetMapping("/dashboard")
	public String dashboardPage(Model model) {
		List<BlogsPage> dashboardData = bservice.dashboardData();
		model.addAttribute("blogs", dashboardData);
		return AppConstants.DASHBOARD;
	}
	@GetMapping("/blog")
	public String blogPage(Model model) {
		model.addAttribute("blog", new CreateBlog());
		return AppConstants.BLOG_PAGE;
	}
	@PostMapping("/saveBlog")
	public String saveContent(@ModelAttribute("blog") CreateBlog form,Model model) {
		
		String status = bservice.saveBlog(form);
		if(status.equals("success")) {
			List<BlogsPage> dashboardData = bservice.dashboardData();
			model.addAttribute("blogs", dashboardData);
			return AppConstants.DASHBOARD;
			
		}
		else {
			model.addAttribute(AppConstants.SUCC_MSG, status);
			return AppConstants.BLOG_PAGE;
		}
	}
	
	@GetMapping("/edit")
	public String editBlog(@RequestParam("blogId") Integer blogId,Model model) {
		BlogsPage editBlog = bservice.editBlog(blogId);
		model.addAttribute("blog", editBlog);
		return AppConstants.BLOG_PAGE;
	}
	@GetMapping("/delete")
	public String deleteBlog(@RequestParam("blogId") Integer blogId,Model model) {
		boolean status = bservice.softDelete(blogId);
		if(status) {
		model.addAttribute(AppConstants.SUCC_MSG,"This Blog is deleted");			
		}
		else {
		model.addAttribute(AppConstants.SUCC_MSG, "Error occured try again");
		}
		return AppConstants.DASHBOARD;
	}
	@GetMapping("/detailBlog")
	public String loadBlogs(@RequestParam("blogId") Integer blogId,Model model) {
		
		model.addAttribute("id", blogId);
		
		model.addAttribute("blogger", bservice.detailBlog(blogId));
		
		model.addAttribute(AppConstants.MODEL_KEY,bservice.showComments(blogId));
		
		model.addAttribute("comment",new CommentForm());
		
		return AppConstants.BLOG;
	}
	@PostMapping("/saveComment")
	public String saveComments(@RequestParam("blogId") Integer blogId,@ModelAttribute("comment") @RequestBody CommentForm cform,Model model) {
		model.addAttribute("id", blogId);
		BlogsPage detailBlog = bservice.detailBlog(blogId);
		model.addAttribute("blogger", detailBlog);
		
		model.addAttribute(AppConstants.MODEL_KEY,bservice.showComments(blogId));
		bservice.saveComment(cform,blogId);
		model.addAttribute(AppConstants.MODEL_KEY,bservice.showComments(blogId));
		
		return AppConstants.BLOG;
		
	}
	@GetMapping("/seeComments")
	public String blogsComment(Model model) {
		
		
		model.addAttribute("comments",  bservice.loadComments());
		return AppConstants.COMMENTS;
	}
	
	@GetMapping("/deleteComment")
	public String deleteComment(@RequestParam("commentId") Integer commentId,Model model) {
		boolean status = bservice.deleteComment(commentId);
		if(status) {
			model.addAttribute(AppConstants.SUCC_MSG,"comment deleted successfully");
		}
		else {
			model.addAttribute(AppConstants.SUCC_MSG,"error occured please try after sometime...");
		}
		List<Comments> loadComments = bservice.loadComments();
		model.addAttribute("comments", loadComments);
		return AppConstants.COMMENTS;
	}
	
	

}
