package io.github.aquerr.eaglefactions.common.storage.sql;

import io.github.aquerr.eaglefactions.api.EagleFactions;
import io.github.aquerr.eaglefactions.api.config.StorageConfig;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class SQLAbstractProvider
{
	private final EagleFactions plugin;

	private final String databaseUrl;
	private final String databaseName;
	private final String username;
	private final String password;

	public SQLAbstractProvider(final EagleFactions plugin)
	{
		this.plugin = plugin;

		final StorageConfig storageConfig = plugin.getConfiguration().getStorageConfig();
		this.databaseUrl = storageConfig.getDatabaseUrl();
		this.databaseName = storageConfig.getDatabaseName();
		this.username = storageConfig.getStorageUsername();
		this.password = storageConfig.getStoragePassword();
	}

	protected EagleFactions getPlugin()
	{
		return this.plugin;
	}

	protected String getDatabaseUrl()
	{
		return this.databaseUrl;
	}

	protected String getDatabaseName()
	{
		return this.databaseName;
	}

	protected String getUsername()
	{
		return this.username;
	}

	protected String getPassword()
	{
		return this.password;
	}

	protected abstract Connection getConnection() throws SQLException;
}
