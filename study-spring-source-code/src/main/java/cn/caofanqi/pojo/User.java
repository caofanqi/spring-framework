package cn.caofanqi.pojo;

public class User {

	public static final String MAN = "MAN";

	public static final String WOMAN = "WOMAN";

	private int id;

	private String name;

	private Integer age;

	private String sex;

	public User() {
	}

	public User(String username, Integer age) {
		this.name = username;
		this.age = age;
	}

	public User(String name, Integer age, String sex) {
		this.name = name;
		this.age = age;
		this.sex = sex;
	}

	public User(int id, String name, Integer age, String sex) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.sex = sex;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", name='" + name + '\'' +
				", age=" + age +
				", sex='" + sex + '\'' +
				'}';
	}
}
