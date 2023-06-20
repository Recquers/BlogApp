package in.vasanth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.vasanth.entity.BlogsPage;

public interface BlogRepository extends JpaRepository<BlogsPage,Integer> {

}
