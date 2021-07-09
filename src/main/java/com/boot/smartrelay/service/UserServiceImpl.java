package com.boot.smartrelay.service;

import com.boot.smartrelay.Utils;
import com.boot.smartrelay.beans.Device;
import com.boot.smartrelay.beans.ResponseBox;
import com.boot.smartrelay.beans.User;
import com.boot.smartrelay.repository.AdminDeviceRepository;
import com.boot.smartrelay.repository.DeviceRepository;
import com.boot.smartrelay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    final UserRepository userRepository;

    final DeviceRepository deviceRepository;

    final AdminDeviceRepository adminDeviceRepository;


    @Override
    public User findUserById(String userId) {
        return userRepository.findUserById(userId);
    }

    @Override
    public ResponseBox insertLargeSector(String userId, String place) {
        return userRepository.insertLargerSector(userId, place);
    }

    @Override
    public List<Device> selectDeviceByUserIdAndLargeSector(String userId, String largeSector) {
        List<Device> devices = deviceRepository.selectDeviceByUserIdAndLargeSector(userId, largeSector);
        return devices;
    }

    @Override
    public ResponseBox insertDevice(String userId, String largeSector, String smallSector, String deviceId) {

        List<Device> devices = deviceRepository.selectDeviceByUserIdAndLargeSector(userId, largeSector);
        //2.기존에 등록된 장소이름과 중복확인
        for(Device device : devices){
            if(smallSector.equals(device.getSmallSector())){
                return Utils.makeResponseBox(false, "중복되는 장소 이름이 있습니다.");
            }
        }

        Device device = new Device();
        device.setDeviceId(deviceId);
        device.setLargeSector(largeSector);
        device.setSmallSector(smallSector);
        device.setUserId(userId);
        ResponseBox responseBox = deviceRepository.insertDevice(device);
        return responseBox;
    }

    @Override
    public ResponseBox removeLargeSector(String userId, String largeSector){
        User user = findUserById(userId);
        ResponseBox responseBox = deviceRepository.removeLargeSector(userId, largeSector);
        if(responseBox.isStatus()){
            List<String> as_is_list = user.getLargeSector();
            List<String> to_be_list = new ArrayList<>();
            if(as_is_list == null || as_is_list.size() == 0){
                return Utils.makeResponseBox(false, "삭제할 라지섹터가 존재하지 않습니다.");
            }
            as_is_list.stream().forEach(s->{
                if(!s.equals(largeSector)){
                    to_be_list.add(s);
                }
            });
            responseBox = userRepository.updateUserLargeSectorList(userId, to_be_list);
        }
        return responseBox;
    }

    @Override
    public ResponseBox removeSmallSector(String userId, String largeSector, String smallSector) {
        return deviceRepository.removeSmallSector(userId,largeSector, smallSector);
    }

    @Override
    public List<Device> selectDeviceByUserId(String userId) {
        return deviceRepository.selectDeviceByUserId(userId);
    }

	@Override
	public ResponseBox modifyLargeSector(String userId, String largeSector, String newLargeSector) {
		User user = findUserById(userId);
		List<String> checkUserList = user.getLargeSector();
		for(String lssec : checkUserList){
		    if(lssec.equals(newLargeSector)){
                return Utils.makeResponseBox(false, "이미 있는 장소명입니다.");
            }
        }

		ResponseBox responseBox = deviceRepository.modifyLargeSector(userId, largeSector, newLargeSector);
		if(responseBox.isStatus()) {
			   List<String> as_is_list = user.getLargeSector();
	           List<String> to_be_list = new ArrayList<>();
	           as_is_list.stream().forEach(lssec->{
	        	   if(!lssec.equals(largeSector)) {
	        		   to_be_list.add(lssec);
	        	   }else {
	        		   to_be_list.add(newLargeSector);
	        	   }
	           });
	           responseBox = userRepository.updateUserLargeSectorList(userId, to_be_list);
		}
		
		return responseBox;
	}

	@Override
	public ResponseBox modifySmallSector(String userId, String largeSector, String smallSector, String newSmallSector) {
		// TODO Auto-generated method stub

        List<Device> devices = deviceRepository.selectDeviceByUserIdAndLargeSector(userId, largeSector);
        for(Device device : devices){
            if(device.getSmallSector().equals(newSmallSector)){
                return Utils.makeResponseBox(false, "이미 있는 장소명입니다.");
            }
        }

		return deviceRepository.modifySmallSector(userId, largeSector, smallSector, newSmallSector);
	}
    
    
}
