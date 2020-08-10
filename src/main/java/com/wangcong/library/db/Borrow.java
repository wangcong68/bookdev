package com.wangcong.library.db;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "borrow")
public class Borrow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "borrow_id")
    private Long borrowId;

    @Column(name = "uid")
    private Long uid;

    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "borrow_date")
    private Date borrowDate;

    @Column(name = "end_date")
    private Date endDate;
}
