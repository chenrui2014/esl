package com.boe.esl.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.boe.esl.model.BaseModel;
import com.boe.esl.vo.BaseVO;

public interface BaseService<T extends BaseModel<ID>, ID extends Serializable, V extends BaseVO> extends ConvertService<T, V> {

	/**
    * 新增或更新
    */
   T save(T t);
   
   /**
    * 新增或更新
    * 注意数量不要太大，特别是数据迁移时不要使用该方法
    */
   Iterable<T> save(Iterable<T> entities);
   
   /**
    * 根据ID删除
    */
   void del(ID id);
   
   /**
    * 根据实体删除
    */
   void del(T t);
   
   /**
    * 根据ID查找对象
    */
   T findById(ID id);
   
   List<T> findAll();
   
   /**
    * @Title findAll
    * @Description 分页排序获取数据, 禁止使用该接口进行count操作
    * @param pageable pageable = new PageRequest(0, 10, new Sort(Sort.Direction.DESC,"id"));
    * @return
    * @throws 
    * @create 2018年9月10日 上午10:03:04
    * @lastUpdate 2018年9月10日 上午10:03:04
    */
   Page<T> findAll(Pageable pageable);
   
   /**
    * @Title list
    * @Description 多条件查询 注：多个条件间是and关系 & 参数是属性对应的类型 使用时注意避免结果集过大
    * @param params {"username:like":"test"} 键的格式为字段名:过滤方式,过滤方式见{@code QueryTypeEnum}
    * @return List<T>
    * @throws 
    * @create 2018年9月10日 上午10:06:11
    * @lastUpdate 2018年9月10日 上午10:06:11
    */
   List<T> list(Map<String, Object> params);
   
   /**
    * @Title list
    * @Description 分页多条件查询 注：多个条件间是and关系 & 参数是属性对应的类型
    * @param params {"username:like":"test"} 键的格式为字段名:过滤方式,过滤方式见{@code QueryTypeEnum}
    * @param pageable 分页信息 new PageRequest(page, size,new Sort(Direction.DESC, "updateTime"))
    * @return Page<T>
    * @throws 
    * @create 2018年9月10日 上午9:53:58
    * @lastUpdate 2018年9月10日 上午9:53:58
    */
   Page<T> list(Map<String, Object> params,Pageable pageable);
}
