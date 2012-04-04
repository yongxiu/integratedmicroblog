package cn.edu.nju.software.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.edu.nju.software.service.MicroBlogService;
import cn.edu.nju.software.utils.MicroBlogType;

public class MicroBlogServiceFactory {

	private static Map<MicroBlogType, MicroBlogService> SERVICE_MAP = new HashMap<MicroBlogType, MicroBlogService>();

	static {
		SERVICE_MAP.put(MicroBlogType.Tencent, new TencentMicroBlogService());
		SERVICE_MAP.put(MicroBlogType.Sina, new SinaMicroBlogService());
	}

	public static Iterator<MicroBlogService> getAllMicroBlogService() {
		return SERVICE_MAP.values().iterator();
	}

	public static MicroBlogService getMicroBlogService(MicroBlogType type) {
		return SERVICE_MAP.get(type);
	}

}
