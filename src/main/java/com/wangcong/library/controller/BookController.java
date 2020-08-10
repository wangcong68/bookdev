package com.wangcong.library.controller;

import com.wangcong.library.db.*;
import com.wangcong.library.db.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class BookController {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BorrowRepository borrowRepository;
    private final CollectionRepository collectionRepository;

    @Autowired
    public BookController(UserRepository userRepository,BookRepository bookRepository,BorrowRepository borrowRepository,CollectionRepository collectionRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.borrowRepository = borrowRepository;
        this.collectionRepository = collectionRepository;
    }

    /**
     * 添加图书
     * @param isbn isbn号
     * @param name 书名
     * @param photoUrl 封皮
     * @param num 个数
     * @param description 描述
     * @param type 类型
     * @param startDate 出版时间
     * @param author 作者
     * @return {code:1/0,msg:成功/失败}
     */
    @RequestMapping(value = "/addbook",params={"isbn","name","photoUrl","num","description","type","startDate","author"}, method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> addBook(String isbn,
                                 String name,
                                 String photoUrl,
                                 Long num,
                                 String description,
                                 String type,
                                 String startDate,
                                 String author) {

        Map<String, Object> respon = new HashMap<>();
        Book book = new Book();
        book.setAuthor(author);
        book.setDescription(description);
        book.setIsbn(isbn);
        book.setName(name);
        book.setPhotoUrl(photoUrl);
        book.setType(type);
        book.setStartDate(startDate);
        book.setNum(num);
        if(bookRepository.save(book) != null){
            respon.put("code",1);
            respon.put("msg","图书添加成功");
        }else{
            respon.put("code",0);
            respon.put("msg","图书添加失败");
        }
        return respon;
    }

    /**
     * 搜索图书 0作者 1书名
     * @param param 作者名或书名
     * @param type 0或1
     * @return {code:0/1,msg:失败/,data:/数据}
     */
    @RequestMapping(value = "/searchbook",params={"param","type"}, method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> addBook(String param,
                                int type) {

        Map<String, Object> respon = new HashMap<>();
        List<Book> bookList;
        if(type == 0){ //按作者搜索
            bookList  = bookRepository.findAllByAuthor(param);
            if(bookList.size() == 0){
                respon.put("code",0);
                respon.put("msg","没有数据");
            }else{
                respon.put("code",1);
                respon.put("data",bookList);
            }

        }else if(type == 1){//按书名搜索
            bookList  = bookRepository.findByNameIsLike("%"+param+"%");
            if(bookList.size() == 0){
                respon.put("code",0);
                respon.put("msg","没有数据");
            }else{
                respon.put("code",1);
                respon.put("data",bookList);
            }
        }
        return respon;
    }

    /**
     * 借书
     * @param isbn 书号
     * @param tel 电话号码
     * @return {code:0/1/2,msg:成功/库存不足/用户不存在}
     */
    @RequestMapping(value = "/borrow",params={"isbn","tel"}, method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> borrowBook(String isbn,String tel) {
        Map<String, Object> respon = new HashMap<>();
        List<Book> bookList = bookRepository.findAllByIsbn(isbn);
        if(bookList.size() > 0){
            Book book = bookList.get(0);
            Long number = book.getNum();
            if(number >= 1){
                number = number - 1;
                book.setNum(number);
                bookRepository.save(book);
                Borrow borrow = new Borrow();
                Long bookId = book.getBookId();
                User user = userRepository.findByTel(tel);
                if(user != null){
                    borrow.setUid(user.getUid());
                }else{
                    respon.put("code",2);
                    respon.put("msg","用户不存在");
                    return respon;
                }
                borrow.setBookId(bookId);
                Date date = new Date();
                borrow.setBorrowDate(date);
                Calendar calendar =Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(calendar.MONTH,1);
                Date endDate = calendar.getTime();
                borrow.setEndDate(endDate);
                borrowRepository.save(borrow);
                respon.put("code",1);
                respon.put("msg","借书成功");
            }else{
                respon.put("code",0);
                respon.put("msg","书库存不足");
            }
        }
        return respon;
    }

    /**
     * 推荐列表
     * @return {code:0/1,msg:没有书籍/成功,data:/库中所有图书}
     */
    @RequestMapping(value = "/recommendbooks", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> borrowBook() {
        Map<String, Object> respon = new HashMap<>();
        List<Book> bookList = bookRepository.findAll();
        if(bookList.size() >= 1){
            respon.put("code",1);
            respon.put("msg","请求成功");
            respon.put("data",bookList);
        }else{
            respon.put("code",0);
            respon.put("msg","数据库中没有课推荐的书籍");
        }
        return respon;
    }

    /**
     * 收藏
     * @param tel 电话
     * @param isbn isbn号
     * @return code{0/1,msg:用户不存在/收藏成功}
     */
    @RequestMapping(value = "/collect", params={"tel","isbn"},method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> collect(String tel,String isbn) {
        Map<String, Object> map = new HashMap<String, Object>();
        User user = userRepository.findByTel(tel);
        Book book = bookRepository.findByIsbn(isbn);
        Long bookId = book.getBookId();
        if(user != null){
            Long uid = user.getUid();
            Collection collection = new Collection();
            collection.setUid(uid);
            collection.setBookId(bookId);
            collectionRepository.save(collection);
            map.put("code",1);
            map.put("msg","收藏成功");
        }else{
            map.put("code",0);
            map.put("msg","该用户不存在");
        }
        return map;
    }

    /**
     * 我的收藏
     * @param tel 电话
     * @return {code:0/1,msg:没有收藏/,data:/收藏书籍数据}
     */
    @RequestMapping(value = "/mycollection", params={"tel"},method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> collect(String tel) {
        Map<String, Object> map = new HashMap<String, Object>();
        User user = userRepository.findByTel(tel);
        Long uid = user.getUid();
        List<Collection> collectionList = collectionRepository.findAllByUid(uid);
        List<Book> bookList = new ArrayList<Book>();
        if(collectionList.size() >= 1){
            for(int i = 0; i < collectionList.size(); i++){
                Collection collection = collectionList.get(i);
                Long bookId = collection.getBookId();
                Book book0 = bookRepository.findByBookId(bookId);
                bookList.add(book0);
            }
            map.put("code",1);
            map.put("data",bookList);
        }else{
            map.put("code",0);
            map.put("msg","该用户没有收藏任何书籍");
        }

        return map;
    }
}

