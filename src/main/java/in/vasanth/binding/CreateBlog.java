package in.vasanth.binding;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CreateBlog {
	private Integer blogId;
	private String title;
	private String shrtDscrptn;
	private LocalDate createdDate;
	
	private String content;

}
