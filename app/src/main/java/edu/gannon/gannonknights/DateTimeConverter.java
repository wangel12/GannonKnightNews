package edu.gannon.gannonknights;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DateTimeConverter {
    //Get TIME DATE FORMAT
    public String DateTimeConverter(String _date) {
        String date = _date;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:MM:SS");
        Date testDate = null;
        try {
        testDate = sdf.parse(date);
        }catch(Exception ex){
          ex.printStackTrace();
        }

        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a, F MMM");
        String newFormat = formatter.format(testDate);
        //PostDate.setText(newFormat);
        //System.out.println(".....Date..." + newFormat);
        return newFormat;

    }
}
