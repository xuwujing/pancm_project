package com.zans.mms.service;

public interface ISerialNumService {

    String generateTicketSerialNum();


    String fillCurrentId(String currentId);

	String generatePoManagerSerialNum();
}
