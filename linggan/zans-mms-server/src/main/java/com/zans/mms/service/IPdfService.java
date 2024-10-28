package com.zans.mms.service;

import com.zans.mms.vo.ticket.TicketsDispatchPdfVO;

public interface IPdfService {

    String generatePdfByTemplate(String templatePath,Object dataObj);

    String generateDispatchPdf(TicketsDispatchPdfVO pdfVO);

    String generateAcceptPdf(TicketsDispatchPdfVO pdfVO);
}
