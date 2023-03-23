package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.jpa.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // '인증' 처리
        UserEntity userEntity = userRepository.findByEmail(username); // 여기서 username은 email이다. 보통 username으로 쓴다.

        if (userEntity == null) {
            throw new UsernameNotFoundException(username + " : not found");
        }

        // 파라미터 값과 DB에 있는 데이터 값을 알아서 비교해준다.
        return new User(userEntity.getEmail(), userEntity.getEncrypedPwd(), // db에 있는 encrypedPwd 값을 넘긴다.
                true, true, true, true, new ArrayList<>()); // true가 무조건 4개가 들어간다.
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT); // 데이터 값을 넣는 것. 엄격한 기준으로 넣는다.

        UserEntity userEntity = mapper.map(userDto, UserEntity.class); // 복사됨.
        userEntity.setEncrypedPwd(passwordEncoder.encode(userDto.getPwd()));

        userRepository.save(userEntity); // 최종적으로 userRepository에 저장

        UserDto returnUserDto = mapper.map(userEntity, UserDto.class);

        return returnUserDto;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        return null;
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return null;
    }

}
