package in.vasanth.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class BlogsPage {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer blogId;
	private String title;
	private String shrtDscrptn;
	@Lob
	private String content;
	@CreationTimestamp
	private LocalDate createdDate;
	@UpdateTimestamp
	private LocalDate updatedDate;
	private Integer status=1;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private Users user;
	@OneToMany
	(mappedBy="blogs",cascade=CascadeType.ALL)
	private List<Comments> comments;
	

}
