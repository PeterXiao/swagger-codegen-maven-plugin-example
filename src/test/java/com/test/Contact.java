/**
 *
 */
package com.test;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author LiY
 * @time 下午5:51:48
 */
public class Contact {
	@JsonProperty
	public String name;
	@JsonProperty
	public int age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
}
