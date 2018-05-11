package ksd.binews.mapper.price;

import java.util.List;

import ksd.binews.dto.PriceDto;
import ksd.binews.entity.price.PriceEntity;

public interface PriceMapper {		
	
	List<PriceEntity> listPrice(PriceDto entity);	
}
