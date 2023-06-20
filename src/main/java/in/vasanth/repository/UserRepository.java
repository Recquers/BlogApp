package in.vasanth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.vasanth.entity.Users;

public interface UserRepository extends JpaRepository<Users	,Integer> {
	
	public Users findByEmail(String email); 
	
	public Users findByEmailAndPassword(String email,String password);

}
