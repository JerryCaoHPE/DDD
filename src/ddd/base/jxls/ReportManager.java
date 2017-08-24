package ddd.base.jxls;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Leonid Vysochyn
 */
public interface ReportManager {
    public List exec(String sql) throws SQLException;
}
