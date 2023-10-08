import utils.DateConverter;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        DateConverter defCons = new DateConverter();
        System.out.println("Today date AD: " + LocalDateTime.now() + " and BS: "+ defCons.toBS(LocalDateTime.now()));
        System.out.println("Date BS: 2080-06-17 and converted date in AD: "+ defCons.toAD("2080-06-17") );
        DateConverter dc = new DateConverter("yyyy-MM-dd", '-');
        System.out.println("Date BS: 2070-06-17 and converted date in AD: "+ defCons.toAD("2070-06-17") );
    }
}
