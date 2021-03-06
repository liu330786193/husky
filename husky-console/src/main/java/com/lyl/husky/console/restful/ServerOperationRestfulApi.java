package com.lyl.husky.console.restful;

import com.google.common.base.Optional;
import com.lyl.husky.console.service.JobAPIService;
import com.lyl.husky.console.service.impl.JobAPIServiceImpl;
import com.lyl.husky.lifecycle.domain.JobBriefInfo;
import com.lyl.husky.lifecycle.domain.ServerBriefInfo;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

/**
 * 服务器维度操作的RESTful API.
 */
@Path("/servers")
public class ServerOperationRestfulApi {

    private JobAPIService jobAPIService = new JobAPIServiceImpl();

    /**
     * 获取服务器总数
     */
    @GET
    @Path("/count")
    public int getServersTotalCount(){
        return jobAPIService.getServerStatisticsAPI().getServersTotalCount();
    }

    /**
     * 获取服务器详情.
     *
     * @return 服务器详情集合
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<ServerBriefInfo> getAllServersBriefInfo() {
        return jobAPIService.getServerStatisticsAPI().getAllServersBriefInfo();
    }

    /**
     * 禁用作业.
     *
     * @param serverIp 服务器IP地址
     */
    @POST
    @Path("/{serverIp}/disable")
    public void disableServer(@PathParam("serverIp") final String serverIp) {
        jobAPIService.getJobOperatorAPI().disable(Optional.<String>absent(), Optional.of(serverIp));
    }

    /**
     * 启用作业.
     *
     * @param serverIp 服务器IP地址
     */
    @DELETE
    @Path("/{serverIp}/disable")
    public void enableServer(@PathParam("serverIp") final String serverIp) {
        jobAPIService.getJobOperatorAPI().enable(Optional.<String>absent(), Optional.of(serverIp));
    }

    /**
     * 终止作业.
     *
     * @param serverIp 服务器IP地址
     */
    @POST
    @Path("/{serverIp}/shutdown")
    public void shutdownServer(@PathParam("serverIp") final String serverIp) {
        jobAPIService.getJobOperatorAPI().shutdown(Optional.<String>absent(), Optional.of(serverIp));
    }

    /**
     * 清理作业.
     *
     * @param serverIp 服务器IP地址
     */
    @DELETE
    @Path("/{serverIp}")
    public void removeServer(@PathParam("serverIp") final String serverIp) {
        jobAPIService.getJobOperatorAPI().remove(Optional.<String>absent(), Optional.of(serverIp));
    }

    /**
     * 获取该服务器上注册的作业的简明信息.
     *
     * @param serverIp 服务器IP地址
     * @return 作业简明信息对象集合
     */
    @GET
    @Path("/{serverIp}/jobs")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<JobBriefInfo> getJobs(@PathParam("serverIp") final String serverIp) {
        return jobAPIService.getJobStatisticsAPI().getJobsBriefInfo(serverIp);
    }

    /**
     * 禁用作业.
     *
     * @param serverIp 服务器IP地址
     * @param jobName 作业名称
     */
    @POST
    @Path("/{serverIp}/jobs/{jobName}/disable")
    public void disableServerJob(@PathParam("serverIp") final String serverIp, @PathParam("jobName") final String jobName) {
        jobAPIService.getJobOperatorAPI().disable(Optional.of(jobName), Optional.of(serverIp));
    }

    /**
     * 启用作业.
     *
     * @param serverIp 服务器IP地址
     * @param jobName 作业名称
     */
    @DELETE
    @Path("/{serverIp}/jobs/{jobName}/disable")
    public void enableServerJob(@PathParam("serverIp") final String serverIp, @PathParam("jobName") final String jobName) {
        jobAPIService.getJobOperatorAPI().enable(Optional.of(jobName), Optional.of(serverIp));
    }

    /**
     * 终止作业.
     *
     * @param serverIp 服务器IP地址
     * @param jobName 作业名称
     */
    @POST
    @Path("/{serverIp}/jobs/{jobName}/shutdown")
    public void shutdownServerJob(@PathParam("serverIp") final String serverIp, @PathParam("jobName") final String jobName) {
        jobAPIService.getJobOperatorAPI().shutdown(Optional.of(jobName), Optional.of(serverIp));
    }

    /**
     * 清理作业.
     *
     * @param serverIp 服务器IP地址
     * @param jobName 作业名称
     */
    @DELETE
    @Path("/{serverIp}/jobs/{jobName}")
    public void removeServerJob(@PathParam("serverIp") final String serverIp, @PathParam("jobName") final String jobName) {
        jobAPIService.getJobOperatorAPI().remove(Optional.of(jobName), Optional.of(serverIp));
    }
}
