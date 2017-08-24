package ddd.base.persistence;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

public class DDDDataSource implements DataSource{
	
	private DataSource dataSource;
	private ThreadLocal<Connection> currentConnection = new ThreadLocal<Connection>();
	
	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return dataSource.getLogWriter();
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return dataSource.getLoginTimeout();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return dataSource.getParentLogger();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		dataSource.setLogWriter(out);
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		dataSource.setLoginTimeout(seconds);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return dataSource.isWrapperFor(iface);
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return dataSource.unwrap(iface);
	}

	@Override
	public Connection getConnection() throws SQLException {
		if(currentConnection.get()==null||currentConnection.get().isClosed()){
			currentConnection.set(dataSource.getConnection());
		}
		return currentConnection.get();
	}

	@Override
	public Connection getConnection(String username, String password)
			throws SQLException {
		if(currentConnection.get()==null||currentConnection.get().isClosed()){
			currentConnection.set(dataSource.getConnection(username, password));
		}
		return currentConnection.get();
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
