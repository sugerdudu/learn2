package com.gz.nacos.client;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.gz.cloud.feign.FollowApi;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import javax.security.auth.login.FailedLoginException;

@Component
public class OAFallBackFactory implements FallbackFactory<FollowApi> {
    public OAFallBackFactory(){
        System.out.println("OAFallBackFactory --- ");
    }
    private String getResult(Throwable ex) {
        //Integer.parseInt("ff");

        if (ex instanceof FlowException) {
            return "限流（FlowException）";
        } else if (ex instanceof ParamFlowException) {
            return "参数流控（ParamFlowException）";
        } else if (ex instanceof AuthorityException) {
            return "授权规则（AuthorityException）";
        } else if (ex instanceof SystemBlockException) {
            return "系统规则（SystemBlockException）";
        } else if (ex instanceof DegradeException) {
            return "降级（DegradeException）";
        } else if (ex instanceof BlockException) {
            return "普通流控（BlockException）";
        } else {
            return "未知（FallBackFactory）";
        }
    }

    @Override
    public FollowApi create(Throwable throwable) {
        System.out.println("FollowApi create");
        return new FollowApi() {
            @Override
            public String echo(String string) {
                return getResult(throwable) +" echo " + string;
            }

            @Override
            public String echo3(String string) {
                return getResult(throwable) +" echo3 " + string;
            }
        };
    }
}
