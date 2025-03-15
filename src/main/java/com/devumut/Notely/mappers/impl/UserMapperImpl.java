package com.devumut.Notely.mappers.impl;

import com.devumut.Notely.domain.dto.UserDto;
import com.devumut.Notely.domain.entities.UserEntity;
import com.devumut.Notely.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class UserMapperImpl implements Mapper<UserEntity, UserDto> {

    private final ModelMapper mapper;

    @Autowired
    public UserMapperImpl(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public UserDto mapTo(UserEntity userEntity) {
        return mapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserEntity mapFrom(UserDto userDto) {
        return mapper.map(userDto, UserEntity.class);
    }
}
