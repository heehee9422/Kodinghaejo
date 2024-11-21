package com.kodinghaejo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.kodinghaejo.entity.CommonCodeEntity;
import com.kodinghaejo.entity.repository.CommonCodeRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BaseServiceImpl implements BaseService {
	private final CommonCodeRepository commonCodeRepository;

	//공통코드 가져오기
	@Override
	public Map<String, Object> loadUsedCommonCode() {
		List<CommonCodeEntity> entities = commonCodeRepository.findByIsUse("Y");

		Map<String, Object> data = new HashMap<>();
		List<Map<String, Object>> lvlList = new ArrayList<>();
		List<Map<String, Object>> tecList = new ArrayList<>();
		List<Map<String, Object>> jobList = new ArrayList<>();
		List<Map<String, Object>> lngList = new ArrayList<>();
		List<Map<String, Object>> catList = new ArrayList<>();

		for (CommonCodeEntity code : entities) {
			Map<String, Object> subdata = new HashMap<>();
			subdata.put("code", code.getCode());
			subdata.put("type", code.getType());
			subdata.put("val", code.getVal());
			subdata.put("note", code.getNote());
			subdata.put("isUse", code.getIsUse());

			switch (code.getType()) {
				case "LVL":
					lvlList.add(subdata);
					break;
				case "TEC":
					tecList.add(subdata);
					break;
				case "JOB":
					jobList.add(subdata);
					break;
				case "LNG":
					lngList.add(subdata);
					break;
				case "CAT":
					catList.add(subdata);
					break;
			}
		}

		data.put("lvl", lvlList);
		data.put("tec", tecList);
		data.put("job", jobList);
		data.put("lng", lngList);
		data.put("cat", catList);

		return data;
	}

}
