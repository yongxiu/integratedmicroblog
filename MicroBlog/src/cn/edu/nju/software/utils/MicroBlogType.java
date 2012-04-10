package cn.edu.nju.software.utils;

public enum MicroBlogType {
	Tencent("tencent"), Sina("sina");
	private String value;

	private MicroBlogType(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
