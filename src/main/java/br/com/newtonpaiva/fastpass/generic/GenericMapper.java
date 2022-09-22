package br.com.newtonpaiva.fastpass.generic;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GenericMapper {

    @Autowired
    private ModelMapper modelMapper;

    public Object toDTO(Object object, Class<?> tClass) {
        return modelMapper.map(object, tClass);
    }

    public Object toEntity(Object object, Class<?> tClass) {
        return modelMapper.map(object, tClass);
    }

}
