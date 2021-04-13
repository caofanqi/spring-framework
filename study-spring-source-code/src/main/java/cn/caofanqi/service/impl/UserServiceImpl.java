package cn.caofanqi.service.impl;

import cn.caofanqi.pojo.User;
import cn.caofanqi.repository.UserRepository;
import cn.caofanqi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Value("${xxx:123}")
	private int xxx;

	@Override
	public void save(User user) {
		userRepository.save(user);
		System.out.println("save user: " + user);
	}

}
