package in.vasanth.service;

import java.util.List;

import in.vasanth.binding.CommentForm;
import in.vasanth.binding.CreateBlog;
import in.vasanth.entity.BlogsPage;
import in.vasanth.entity.Comments;

public interface BlogService {
	
	public String saveBlog(CreateBlog bdata);
	
	public List<BlogsPage> dashboardData();
	
	public  BlogsPage editBlog(Integer blogId);
	
	public boolean softDelete(Integer blogId);
	
	public List<BlogsPage> getBlogs();
	
	public BlogsPage detailBlog(Integer blogId);
	
	public boolean  saveComment(CommentForm cform,Integer blogId);
	
	public List<Comments> loadComments();
	
	public boolean deleteComment(Integer id);
	
	public List<Comments> showComments(Integer blogId);

}
