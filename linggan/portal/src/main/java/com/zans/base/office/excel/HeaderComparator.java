package com.zans.base.office.excel;

import java.util.Comparator;

/**
 * @author xv
 * @since 2020/4/30 13:00
 */
public class HeaderComparator implements Comparator<Header> {

    @Override
    public int compare(Header header, Header t1) {
        return header.getCol() - t1.getCol();
    }
}
