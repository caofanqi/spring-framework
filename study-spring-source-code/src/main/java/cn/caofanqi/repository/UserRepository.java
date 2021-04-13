package cn.caofanqi.repository;

import cn.caofanqi.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Transactional
	public void save(User user) {
		jdbcTemplate.update("INSERT INTO `user`(name,age,sex) VALUES(?,?,?) ",
				user.getName(), user.getAge(), user.getSex());

		throw new RuntimeException("xxx");
	}

}
