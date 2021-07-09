package com.boot.smartrelay.repository;

import com.boot.smartrelay.beans.DeviceStatus;
import org.springframework.data.repository.CrudRepository;

public interface DeviceStatusRedisRepository extends CrudRepository<DeviceStatus, String> {

}
