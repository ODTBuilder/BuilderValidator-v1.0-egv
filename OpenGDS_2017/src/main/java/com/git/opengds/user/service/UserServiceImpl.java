package com.git.opengds.user.service;

import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.git.opengds.user.domain.UserVO;
import com.git.opengds.user.persistent.UserDAO;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("userService")
public class UserServiceImpl extends EgovAbstractServiceImpl implements UserService {
//	@Inject
	@Resource(name="userDAO")
	private UserDAO userDAO;
	
	@Override
	public UserVO loginUserByInfo(HashMap<String, Object> infoMap){
		UserVO userVO = new UserVO();
		
		try {
			userVO = userDAO.loginUserByInfo(infoMap);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		 
		return userVO;
	}
}
