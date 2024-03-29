import com.flying.utils.Utils;
import com.github.flyinghe.tools.CommonUtils;
import org.junit.Test;

import java.util.Date;

/**
 * Created by FlyingHe on 2019/4/10.
 */
public class CommonTest {
    @Test
    public void test() {
        System.out.println(Utils.getClassSimpleNameFromFullName("com.flying.support.BaseMapper"));
    }

    @Test
    public void testMysql() {
        System.out.println("select cast(so.name as varchar(500)) as TABLE_NAME, " +
                "cast(sep.value as varchar(500)) as COMMENTS from sysobjects so " +
                "left JOIN sys.extended_properties sep on sep.major_id=so.id and sep.minor_id=0 " +
                "where (xtype='U' or xtype='v')");
    }

    @Test
    public void test1() {
        String[] strings = new String[]{"hdic"};
        String entityName = "HdicHdicChecklistItem";

        String result = entityName;
        String entityNamePrefix = strings[0].substring(0, 1).toUpperCase() + strings[0].substring(1);
        if (entityName.startsWith(entityNamePrefix)) {
            result = entityName.replaceFirst(entityNamePrefix, "");
        }
        System.out.println(result);
    }
}
