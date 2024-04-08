package cloud.deuterium.wb;

import cloud.deuterium.wb.books.entity.Book;
import cloud.deuterium.wb.users.entity.WookieUser;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Milan Stojkovic 26-Mar-2024
 */
public class TestUtil {

    public static WookieUser createUser() {
        return WookieUser.builder()
                .email("lohgarra@example.com")
                .pseudonym("lohgarra")
                .password("$2a$10$7iDHirkJJZw9LBm3KXvKO.pOBsCdgEWdM0yAXD.85Di9MH3R0iKuG") // 12345678
                .books(new ArrayList<>())
                .build();
    }

    public static Book createBookOne(){
        Book book = new Book();
        book.setTitle("Title One");
        book.setCover("Cover One");
        book.setDescription("Description One");
        book.setPrice(BigDecimal.TEN);
        return book;
    }

    public static Book createBookTwo(){
        Book book = new Book();
        book.setTitle("Title Two");
        book.setCover("Cover Two");
        book.setDescription("Description Two");
        book.setPrice(BigDecimal.TWO);
        return book;
    }

    public static Book createBookThree(){
        Book book = new Book();
        book.setTitle("Title Three");
        book.setCover("Cover Three");
        book.setDescription("Description Three");
        book.setPrice(BigDecimal.TEN);
        return book;
    }
}
