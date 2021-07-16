package com.boot.smartrelay.repository;

import com.boot.smartrelay.beans.DeviceStatus;
import com.boot.smartrelay.beans.PacketList;
import org.springframework.data.repository.CrudRepository;

public interface DeviceStatusRedisRepository extends CrudRepository<PacketList, String> {

}
