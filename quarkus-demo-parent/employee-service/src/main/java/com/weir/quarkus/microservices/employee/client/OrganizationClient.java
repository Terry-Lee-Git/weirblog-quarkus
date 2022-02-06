package com.weir.quarkus.microservices.employee.client;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;

import com.weir.quarkus.microservices.employee.model.Organization;


@Path("/org")
public interface OrganizationClient {

	@GET
	@Path("/{id}")
	// https://www.youtube.com/watch?v=NpTamexmW_k  学习视频
	// 抛出异常之前执行的最长时间
	@Timeout(5000)
	// 尝试重试 3次，重试之间的延迟计时器为一秒
	@Retry(maxRetries = 4, delay = 1000)
	// 尝试失败降级处理
	@Fallback(RatingServiceFallback.class)
	// 当最后 4 次调用中有 2 次失败时，断路器将打开，并保持打开状态 5 秒
	@CircuitBreaker(requestVolumeThreshold = 4)
	public Organization get(@PathParam("id") Long id);
}
