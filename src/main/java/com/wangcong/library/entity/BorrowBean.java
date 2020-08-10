package com.wangcong.library.entity;

import com.wangcong.library.db.Borrow;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BorrowBean {
    private Borrow borrow;
    private int overDate;

}
