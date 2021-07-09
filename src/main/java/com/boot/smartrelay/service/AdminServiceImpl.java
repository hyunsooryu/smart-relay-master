package com.boot.smartrelay.service;

import com.boot.smartrelay.beans.ResponseBox;
import com.boot.smartrelay.beans.User;
import com.boot.smartrelay.repository.AdminDeviceRepository;
import com.boot.smartrelay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    final AdminDeviceRepository adminDeviceRepository;

    final UserRepository userRepository;

    /**
     * 어드민
     * 유저 신규 생성 및 등록
     * @param user
     * @return
     */
    @Override
    public ResponseBox createUser(User user) {
    	
        ResponseBox responseBox = userRepository.createUser(user);
         
        return responseBox;
    }


    /**
     * 어드민
     * 유저와 디바이스 맵핑
     * @param user
     * @return
     */
    @Override
    public ResponseBox addDeviceToUser(String userId, List<String> deviceId) {
        ResponseBox responseBox = adminDeviceRepository.updateAdminUserDeviceIdLists(userId, deviceId);
        return responseBox;
    }

    /**
     * 어드민
     * 유저에게 할당 가능 한 디바이스 리스트 수정
     * @param userId
     * @param deviceId
     * @return
     */
    @Override
    public ResponseBox deleteDeviceFromUser(String userId, List<String> deviceId) {
        ResponseBox responseBox = adminDeviceRepository.modifyAdminUserDeviceIdLists(userId, deviceId);
        return responseBox;
    }

    @Override
    public List<String> getValidDeviceIdLists(User user) {
        return adminDeviceRepository.getValidDeviceIdLists(user);
    }


	@Override
	public boolean isExistDeviceIds(List<String> deviceIds) {
		// TODO Auto-generated method stub
		return adminDeviceRepository.isExistDeviceIds(deviceIds);
	}


	@Override
	public ResponseBox saveSmartRelayDeviceIds(List<String> deviceIds) {
		// TODO Auto-generated method stub
		return adminDeviceRepository.saveDeviceIds(deviceIds);
	}
	
	
    
    
    
}
