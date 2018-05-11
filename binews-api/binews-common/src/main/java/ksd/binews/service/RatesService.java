package ksd.binews.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ksd.binews.entity.Rates;
import ksd.binews.mapper.RatesMapper;

@Service
public class RatesService {

	@Autowired
	private RatesMapper ratesMapper;
	
	public List<Rates> findAll(){
		return ratesMapper.selectAll();
	}
}
