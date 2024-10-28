package com.zans.mms.dao;

import org.springframework.stereotype.Repository;

@Repository
public interface SerialNumMapper {
    String getTicketIncrement();

	String getPoManagerIncrement();
}
