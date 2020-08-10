package com.wangcong.library.controller;

import com.wangcong.library.db.*;
import com.wangcong.library.entity.BorrowBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class BorrowController {
        private final UserRepository userRepository;
        private final BookRepository bookRepository;
        private final BorrowRepository borrowRepository;

        @Autowired
        public BorrowController(UserRepository userRepository,BookRepository bookRepository,BorrowRepository borrowRepository) {
            this.userRepository = userRepository;
            this.bookRepository = bookRepository;
            this.borrowRepository = borrowRepository;
        }

    /**
     * 借阅书籍列表(含是否超时)
     * @param tel 电话
     * @return {code:0/1,msg:没有借书/成功,data:/借书列表(overDate属性：1为超时，0为未超时)}
     */
    @RequestMapping(value = "/timeout",params={"tel"}, method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> register(String tel) {

        Map<String, Object> respon = new HashMap<>();
        User user = userRepository.findByTel(tel);
        Long uid = user.getUid();
        List<BorrowBean> borrowBeanList = new ArrayList<BorrowBean>();
        List<Borrow> borrowList = borrowRepository.findAllByUid(uid);
        if(borrowList.size() >= 1){
            for(int i = 0; i < borrowList.size(); i++){
                BorrowBean borrowBean = new BorrowBean();
                Borrow borrow = borrowList.get(i);
                borrowBean.setBorrow(borrow);
                Date endDate = borrow.getEndDate();
                Date date = new Date();
                if(date.after(endDate)){
                    borrowBean.setOverDate(0);//未超时
                }else{
                    borrowBean.setOverDate(1);//已超时
                }
                borrowBeanList.add(borrowBean);
            }
            respon.put("code",1);
            respon.put("msg","请求成功");
            respon.put("data",borrowBeanList);
        }else{
            respon.put("code",0);
            respon.put("msg","该用户没有借书");
        }
        return respon;
    }



}

