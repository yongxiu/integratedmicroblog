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
	
	public static String GetMicroBlogName(MicroBlogType type) {
		if (type.equals(MicroBlogType.Tencent)) {
			return "腾讯微博";
		} else {
			return "新浪微博";
		}
	}
}
