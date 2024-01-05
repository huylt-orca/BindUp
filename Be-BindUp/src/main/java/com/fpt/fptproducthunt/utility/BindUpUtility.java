package com.fpt.fptproducthunt.utility;

import com.fpt.fptproducthunt.common.entity.Project;
import com.fpt.fptproducthunt.common.metadata.ProjectStatus;
import com.fpt.fptproducthunt.project.dto.CreatedProjectDTO;
import com.fpt.fptproducthunt.user.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class BindUpUtility {
    private static ModelMapper modelMapper = new ModelMapper();
    public static String getUniqueId() {
        return String.format("_%s%s", UUID.randomUUID().toString().substring(0, 5), System.currentTimeMillis() / 1000).substring(0, 7);
    }
    public static String getPassword() {
        return UUID.randomUUID().toString().substring(0, 10);
    }

    public static Date getCurrentDate() {
        return Date.valueOf(LocalDate.now());
    }
    public static Timestamp getCurrentTimestamp() {
        return Timestamp.valueOf(LocalDateTime.now());
    }

//    public static <S, T> List<T> mapList(Set<S> source, Class<T> targetClass) {
//        List<S> sourceList = new ArrayList<>(source);
//        return sourceList
//                .stream()
//                .map(element -> modelMapper.map(element, targetClass))
//                .collect(Collectors.toList());
//    }

    public static <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
//        List<S> sourceList = new ArrayList<>(source);
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }
}
