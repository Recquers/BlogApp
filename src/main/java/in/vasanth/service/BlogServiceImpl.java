package in.vasanth.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.vasanth.appconstants.AppConstants;
import in.vasanth.binding.CommentForm;
import in.vasanth.binding.CreateBlog;
import in.vasanth.entity.BlogsPage;
import in.vasanth.entity.Comments;
import in.vasanth.entity.Users;
import in.vasanth.repository.BlogRepository;
import in.vasanth.repository.CommentRepository;
import in.vasanth.repository.UserRepository;

@Service
public class BlogServiceImpl implements BlogService {
	@Autowired
	private CommentRepository commentRepo;

	@Autowired
	private BlogRepository brepo;
	@Autowired
	private HttpSession session;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private CommentRepository crepo;

	@Override
	public String saveBlog(CreateBlog bdata) {

		Integer userId = (Integer) session.getAttribute(AppConstants.USER_ID);

		BlogsPage blog = new BlogsPage();

		BeanUtils.copyProperties(bdata, blog);

		Optional<Users> findById = userRepo.findById(userId);
		if (findById.isPresent()) {

			Users users = findById.get();
			blog.setUser(users);

			brepo.save(blog);

			return "success";

		}

		return "failed";
	}

	@Override
	public List<BlogsPage> dashboardData() {
		Integer userId = (Integer) session.getAttribute(AppConstants.USER_ID);

		Optional<Users> findById = userRepo.findById(userId);
		if (findById.isPresent()) {
			Users users = findById.get();
			List<BlogsPage> blogs = users.getBlogs();
			return blogs.stream().filter(e -> e.getStatus() == 1).toList();

		}
		return Collections.emptyList();
	}

	@Override
	public BlogsPage editBlog(Integer blogId) {
		Integer userId = (Integer) session.getAttribute(AppConstants.USER_ID);

		Optional<Users> findById = userRepo.findById(userId);
		if (findById.isPresent()) {
			Users users = findById.get();
			List<BlogsPage> blogs = users.getBlogs();

			Optional<BlogsPage> findAny = blogs.stream().filter(e -> e.getBlogId().equals(blogId)).findAny();
			if (findAny.isPresent()) {

				return findAny.get();
			}
		}
		return null;
	}

	@Override
	public boolean softDelete(Integer blogId) {
		Integer userId = (Integer) session.getAttribute(AppConstants.USER_ID);

		Optional<Users> findById = userRepo.findById(userId);
		if (findById.isPresent()) {
			Users users = findById.get();
			List<BlogsPage> blogs = users.getBlogs();
			Optional<BlogsPage> findAny = blogs.stream().filter(e -> e.getBlogId().equals(blogId)).findAny();
			if (findAny.isPresent()) {
				BlogsPage blog = findAny.get();
				blog.setStatus(0);
				brepo.save(blog);

				return true;
			}
		}
		return false;
	}

	@Override
	public List<BlogsPage> getBlogs() {

		List<BlogsPage> findAll = brepo.findAll();
		return findAll.stream().filter(e -> e.getStatus() == 1).toList();
		
	}

	@Override
	public BlogsPage detailBlog(Integer blogId) {
		Optional<BlogsPage> findById = brepo.findById(blogId);
		if (findById.isPresent()) {

			return findById.get();
		}
		return null;
	}

	@Override
	public boolean saveComment(CommentForm cform, Integer blogId) {
		Comments comment = new Comments();
		BeanUtils.copyProperties(cform, comment);
		Optional<BlogsPage> findById = brepo.findById(blogId);

		if (findById.isPresent()) {
			BlogsPage blogsPage = findById.get();
			comment.setBlogs(blogsPage);
			crepo.save(comment);
		}

		return true;
	}

	@Override
	public List<Comments> loadComments() {
		Integer userId = (Integer) session.getAttribute(AppConstants.USER_ID);
		Optional<Users> findUser = userRepo.findById(userId);
		if (findUser.isPresent()) {
			Users users = findUser.get();
			List<BlogsPage> userBlog = users.getBlogs();

			List<Integer> blogId = userBlog.stream().map(e -> e.getBlogId()).toList();
			if (blogId != null) {

				List<Comments> findAll = commentRepo.findAll();

				return findAll.stream().filter(e -> e.getBlogs().getBlogId() > blogId.get(0))
						.toList();
			}

		}

		return Collections.emptyList();
	}

	@Override
	public boolean deleteComment(Integer id) {
		crepo.deleteById(id);
		return true;
	}

	@Override
	public List<Comments> showComments(Integer blogId) {

		List<Comments> comments = commentRepo.findAll();

		return comments.stream().filter(e -> e.getBlogs().getBlogId().equals(blogId))
				.toList();

		
	}

}
