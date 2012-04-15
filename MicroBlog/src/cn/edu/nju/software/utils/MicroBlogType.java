package cn.edu.nju.software.utils;

public enum MicroBlogType {
	Tencent("腾讯微博"), Sina("新浪微博");

	private String value;

	private MicroBlogType(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

}
