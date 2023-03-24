package com.fastcampus.schedule.user.service;

import com.fastcampus.schedule.exception.ScheduleException;
import com.fastcampus.schedule.user.constant.Role;
import com.fastcampus.schedule.user.domain.User;
import com.fastcampus.schedule.user.domain.UserDto;
import com.fastcampus.schedule.user.domain.UserRequest;
import com.fastcampus.schedule.user.domain.UserResponse;
import com.fastcampus.schedule.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import com.fastcampus.schedule.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import static com.fastcampus.schedule.exception.constant.ErrorCode.USER_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;

	// 권한 수정
	public UserResponse editUserRole(Long userId, UserRequest request) {
		User user = checkExist(userId);
		if (!user.getRole().equals(request.getRole())) {
			user.setRole(request.getRole());
		}
		return UserResponse.fromEntity(user);
	}

    //회원 가입

    public void signUp(User user) throws Exception{
        //회원가입하려눈 userName으로 회원가입된 user가 있는지
//        if(userRepository.findByUserName(user.getUserName()) != null){
//            throw new Exception("유저네임이 존재합니다");
//        }
        userRepository.save(user);
    }

    // 권한 수정
    public UserResponse editUserRole(Long userId, UserRoleRequest request) {
       User user = checkExist(userId);
        if(!user.getRole().equals(request.getRole())){
            user.setRole(request.getRole());
        }
        return UserResponse.fromEntity(user);
    }

    // 유저 정보 수정
    public UserResponse editUserInfo(Long userId, UserInfoRequest request) {
        User user = checkExist(userId);
        if(!user.getUserName().equals(request.getUserName())){
            user.setUserName(request.getUserName());
        }
        if(!user.getEmail().equals(request.getEmail())){
            user.setEmail(request.getEmail());
        }
        return UserResponse.fromEntity(user);
    }

    // 전체 조회
    public Page<UserResponse> getUserList(Role role, Pageable pageable) {
        if (role != null) {
            userRepository.findAllUsersByRole(role, pageable)
                    .map(UserResponse::fromEntity);
        }
        return (Page<UserResponse>) userRepository.findAll(pageable).stream().map(UserResponse::fromEntity);
    }

    // 한명 조회
    public UserResponse getUser(Long userId) {
        return userRepository.findById(userId)
                .map(UserResponse::fromEntity)
                .orElseThrow(()
                        -> new ScheduleException(USER_NOT_FOUND, USER_NOT_FOUND.getMessage()));
    }

    // 유저 존재 여부 체크 공통 메서드
    public User checkExist(Long userId){
        User user = userRepository.findById(userId).orElseThrow(()
                -> new ScheduleException(USER_NOT_FOUND, USER_NOT_FOUND.getMessage()));
        return user;
    }

}