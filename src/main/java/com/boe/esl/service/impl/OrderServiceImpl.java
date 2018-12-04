package com.boe.esl.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boe.esl.ESLConstant;
import com.boe.esl.dao.BaseDao;
import com.boe.esl.dao.GoodsDao;
import com.boe.esl.dao.OrderDao;
import com.boe.esl.model.Goods;
import com.boe.esl.model.Order;
import com.boe.esl.service.OrderService;
import com.boe.esl.vo.OrderVO;

@Transactional
@Service
public class OrderServiceImpl extends BaseServiceImpl<Order, Long, OrderVO> implements OrderService {

	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private GoodsDao goodsDao;

	@Override
	public OrderVO convertEntity(Order order) {
		OrderVO orderVO = new OrderVO();
		orderVO.setId(order.getId());
		orderVO.setCode(order.getCode());
		orderVO.setCount(order.getCount());
		orderVO.setStatus(order.getStatus());
		
		Goods goods = order.getGoods();
		if(goods != null) {
			orderVO.setGoodsId(goods.getId());
			orderVO.setGoodsName(goods.getName());
		}
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate = formatter.format(order.getOrderTime());
		orderVO.setOrderTime(formattedDate);
		
		return orderVO;
	}

	@Override
	public Order convertVO(OrderVO orderVO) {
		Order order = new Order();
		order.setId(orderVO.getId());
		order.setCode(orderVO.getCode());
		order.setCount(orderVO.getCount());
		order.setStatus(orderVO.getStatus());
		
		Timestamp ts = new Timestamp(System.currentTimeMillis()); 
		try {   
            ts = Timestamp.valueOf(orderVO.getOrderTime());     
        } catch (Exception e) {   
            e.printStackTrace();   
        } 
		order.setOrderTime(ts);
		
		Goods goods = goodsDao.findById(orderVO.getGoodsId()).orElse(null);
		order.setGoods(goods);
		return order;
	}

	@Override
	public List<OrderVO> convertEntityList(List<Order> orderList) {
		List<OrderVO> orderVOList = new ArrayList<OrderVO>();
		for (Order order : orderList) {
			OrderVO orderVO = convertEntity(order);
			orderVOList.add(orderVO);
		}
		return orderVOList;
	}

	@Override
	public List<Order> convertVOList(List<OrderVO> entityVOList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseDao<Order, Long> getDAO() {
		return this.orderDao;
	}

	@Override
	public List<Order> getUnSendOrder() {

		Sort sort = new Sort(Direction.DESC, "orderTime");
		
		return this.orderDao.findByStatus(sort,ESLConstant.ORDER_STATUS_UNSENT);
	}

}
