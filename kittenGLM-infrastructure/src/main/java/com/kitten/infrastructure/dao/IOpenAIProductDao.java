package com.kitten.infrastructure.dao;

import com.kitten.infrastructure.po.OpenAIProductPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IOpenAIProductDao {


    List<OpenAIProductPO> queryProductList();
}
