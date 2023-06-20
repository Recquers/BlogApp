package in.vasanth.binding;

import lombok.Data;

@Data
public class CommentForm {
	private String name;
	private String email;
	
	private String comment;
	
	private Integer blogId;
	
}
