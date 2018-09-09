package com.lyl.husky.console.restful;

import com.google.common.base.Optional;
import com.lyl.husky.console.service.JobAPIService;
import com.lyl.husky.console.service.impl.JobAPIServiceImpl;
import com.lyl.husky.lifecycle.domain.JobBriefInfo;
import com.lyl.husky.lifecycle.domain.ShardingInfo;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

/**
 * 作业维度操作的RESTful API
 */
@Path("/jobs")
public final class JobOperationRestfulApi {

    private JobAPIService jobAPIService = new JobAPIServiceImpl();

    /**
     * 获取作业总数.
     *
     * @return 作业总数
     */
    @GET
    @Path("/count")
    public int getJobsTotalCount(){
        return jobAPIService.getJobStatisticsAPI().getJobsTotalCount();
    }

    /**
     * 获取作业详情.
     *
     * @return 作业详情集合
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<JobBriefInfo> getAllJobsBriefInfo() {
        return jobAPIService.getJobStatisticsAPI().getAllJobsBriefInfo();
    }

    /**
     * 触发作业.
     *
     * @param jobName 作业名称
     */
    @POST
    @Path("/{jobName}/trigger")
    public void triggerJob(@PathParam("jobName") final String jobName) {
        jobAPIService.getJobOperatorAPI().trigger(Optional.of(jobName), Optional.<String>absent());
    }

    /**
     * 禁用作业.
     *
     * @param jobName 作业名称
     */
    @POST
    @Path("/{jobName}/disable")
    @Consumes(MediaType.APPLICATION_JSON)
    public void disableJob(@PathParam("jobName") final String jobName) {
        jobAPIService.getJobOperatorAPI().disable(Optional.of(jobName), Optional.<String>absent());
    }

    /**
     * 启用作业.
     *
     * @param jobName 作业名称
     */
    @DELETE
    @Path("/{jobName}/disable")
    @Consumes(MediaType.APPLICATION_JSON)
    public void enableJob(@PathParam("jobName") final String jobName) {
        jobAPIService.getJobOperatorAPI().enable(Optional.of(jobName), Optional.<String>absent());
    }

    /**
     * 终止作业.
     *
     * @param jobName 作业名称
     */
    @POST
    @Path("/{jobName}/shutdown")
    @Consumes(MediaType.APPLICATION_JSON)
    public void shutdownJob(@PathParam("jobName") final String jobName) {
        jobAPIService.getJobOperatorAPI().shutdown(Optional.of(jobName), Optional.<String>absent());
    }

    /**
     * 获取分片信息.
     *
     * @param jobName 作业名称
     * @return 分片信息集合
     */
    @GET
    @Path("/{jobName}/sharding")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<ShardingInfo> getShardingInfo(@PathParam("jobName") final String jobName) {
        return jobAPIService.getShardingStatisticsAPI().getShardingInfo(jobName);
    }

    @POST
    @Path("/{jobName}/sharding/{item}/disable")
    @Consumes(MediaType.APPLICATION_JSON)
    public void disableSharding(@PathParam("jobName") final String jobName, @PathParam("item") final String item) {
        jobAPIService.getShardingOperateAPI().disable(jobName, item);
    }

    @DELETE
    @Path("/{jobName}/sharding/{item}/disable")
    @Consumes(MediaType.APPLICATION_JSON)
    public void enableSharding(@PathParam("jobName") final String jobName, @PathParam("item") final String item) {
        jobAPIService.getShardingOperateAPI().enable(jobName, item);
    }


}
