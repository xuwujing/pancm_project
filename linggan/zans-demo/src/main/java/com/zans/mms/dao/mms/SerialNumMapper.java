package com.zans.mms.dao.mms;

import org.springframework.stereotype.Repository;

@Repository
public interface SerialNumMapper {
    String getTicketIncrement();

}
