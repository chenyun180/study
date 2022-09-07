package com.cloud.test.client;

import com.cloud.common.constants.CloudServiceList;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = CloudServiceList.CLOUD_SERVICE_ADMIN)
public interface AdminClient {


}
