package com.kodinghaejo.dto;

import java.time.LocalDateTime;

public interface ReplyInterface {

	Long getIdx();
	String getRePrnt();
	Long getPrntIdx();
	String getEmail();
	String getWriter();
	String getContent();
	LocalDateTime getRegdate();
	String getIsUse();

}