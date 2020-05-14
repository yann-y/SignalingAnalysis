package mapper;

import org.apache.ibatis.annotations.Select;
import pojo.Locate;

import java.util.List;

public interface LocateMapper {


    @Select("select * from locate")
    List<Locate> getLocate();


}
