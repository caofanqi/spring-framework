package cn.caofanqi.controller;

import cn.caofanqi.pojo.Person;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/persons")
class PersonController {

	@GetMapping(value= "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Person getPerson(@PathVariable Long id) {
		System.out.println("id: " + id);
		return new Person(id, "zhangsan", 23);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void add(@RequestBody Person person) {
		System.out.println("add: " + person);
	}


}